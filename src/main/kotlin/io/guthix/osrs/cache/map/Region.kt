/*
 * Copyright (C) 2019 Guthix
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package io.guthix.osrs.cache.map

import io.guthix.cache.js5.io.smallUSmart
import io.guthix.cache.js5.io.uByte
import java.nio.ByteBuffer

class Region @ExperimentalUnsignedTypes constructor(
    val tileHeights: Array<Array<IntArray>>,
    val renderRules: Array<Array<UByteArray>>,
    val overlayIds: Array<Array<ByteArray>>,
    val overlayPaths: Array<Array<UByteArray>>,
    val overlayRotations: Array<Array<UByteArray>>,
    val underlayIds: Array<Array<UByteArray>>,
    val objectLocation: List<ObjectLocation>
) {

    companion object {
        const val FLOOR_COUNT = 4

        const val SIZE = 64

        @ExperimentalUnsignedTypes
        fun decode(landData: ByteArray, mapData: ByteArray, baseX: Int, baseY: Int): Region {
            val landBuffer = ByteBuffer.wrap(landData)
            val mapBuffer = ByteBuffer.wrap(mapData)
            val land = Land.decode(landBuffer, baseX, baseY)
            val objectLocations = ObjectLocation.decode(mapBuffer, land.renderRules)
            return Region(
                land.tileHeights, land.renderRules, land.overlayIds, land.overlayPaths, land.overlayRotations,
                land.underlayIds, objectLocations
            )
        }
    }
}

@ExperimentalUnsignedTypes
class Land(
    val tileHeights: Array<Array<IntArray>>,
    val renderRules: Array<Array<UByteArray>>,
    val overlayIds: Array<Array<ByteArray>>,
    val overlayPaths: Array<Array<UByteArray>>,
    val overlayRotations: Array<Array<UByteArray>>,
    val underlayIds: Array<Array<UByteArray>>
) {
    companion object {
        private const val JAGEX_CIRCULAR_ANGLE = 2048
        private const val ANGULAR_RATIO = 360.0 / JAGEX_CIRCULAR_ANGLE
        private val JAGEX_RADIAN = Math.toRadians(ANGULAR_RATIO)
        private val COS = IntArray(JAGEX_CIRCULAR_ANGLE) {
            ((UShort.MAX_VALUE.toInt() + 1) * Math.cos(it.toDouble() * JAGEX_RADIAN).toInt())
        }

        @ExperimentalUnsignedTypes
        fun decode(buffer: ByteBuffer, baseX: Int, baseY: Int): Land {
            val tileHeights = Array(Region.FLOOR_COUNT) { Array(Region.SIZE) { IntArray(
                Region.SIZE
            ) } }
            val renderRules = Array(Region.FLOOR_COUNT) { Array(Region.SIZE) { UByteArray(
                Region.SIZE
            ) } }
            val overlayIds = Array(Region.FLOOR_COUNT) { Array(Region.SIZE) { ByteArray(
                Region.SIZE
            ) } }
            val overlayPaths = Array(Region.FLOOR_COUNT) { Array(Region.SIZE) { UByteArray(
                Region.SIZE
            ) } }
            val overlayRotations = Array(Region.FLOOR_COUNT) { Array(Region.SIZE) { UByteArray(
                Region.SIZE
            ) } }
            val underlayIds = Array(Region.FLOOR_COUNT) { Array(Region.SIZE) { UByteArray(
                Region.SIZE
            ) } }
            for (z in 0 until Region.FLOOR_COUNT) {
                for (x in 0 until Region.SIZE) {
                    for (y in 0 until Region.SIZE) {
                        while (true) {
                            val opcode = buffer.uByte.toInt()
                            when {
                                opcode == 0 -> {
                                    if(z == 0) {
                                        tileHeights[0][x][y] =
                                            calcZ0Height(baseX, baseY, x, y)
                                    } else {
                                        tileHeights[z][x][y] = tileHeights[z - 1][x][y] - 240
                                    }
                                }
                                opcode == 1 -> {
                                    var height = buffer.uByte.toInt()
                                    if (height == 1) height = 0
                                    if(z == 0) {
                                        tileHeights[0][x][y] = -height * 8
                                    } else {
                                        tileHeights[z][x][y] = tileHeights[z - 1][x][y] - height * 8
                                    }
                                }
                                opcode <= 49 -> {
                                    overlayIds[z][x][y] = buffer.get()
                                    overlayPaths[z][x][y] = ((opcode - 2) shr 2).toUByte()
                                    overlayRotations[z][x][y] = ((opcode - 2) and 0x3).toUByte()
                                }
                                opcode <= 89 -> renderRules[z][x][y] = (opcode - 49).toUByte()
                                else -> underlayIds[z][x][y] = (opcode - 81).toUByte()
                            }
                        }
                    }
                }
            }
            return Land(tileHeights, renderRules, overlayIds, overlayPaths, overlayRotations, underlayIds)
        }

        @ExperimentalUnsignedTypes
        private fun calcZ0Height(baseX: Int, baseY: Int, x: Int, y: Int): Int {
            val xc = x + baseX + 932731
            val yc = y + baseY + 556238
            var height = interpolateNoise(45365 + xc, yc + 91923, 4) - 128
                    + (interpolateNoise(10294 + xc, 37821 + yc, 2) - 128 shr 1)
                    + (interpolateNoise(xc, yc, 1) - 128 shr 2)
            height = (height * 0.3).toInt() + 35
            if (height < 10)  height = 10 else if (height > 60) height = 60
            return -height * 8
        }

        @ExperimentalUnsignedTypes
        private fun interpolateNoise(x: Int, y: Int, frequency: Int): Int {
            val intX = x / frequency
            val fracX = x and (frequency - 1)
            val intY = y / frequency
            val fracY = y and (frequency - 1)
            val v1 = smoothNoise2d(intX, intY)
            val v2 = smoothNoise2d(intX + 1, intY)
            val v3 = smoothNoise2d(intX, intY + 1)
            val v4 = smoothNoise2d(1 + intX, 1 + intY)
            val i1 = interpolate(v1, v2, fracX, frequency)
            val i2 = interpolate(v3, v4, fracX, frequency)
            return interpolate(i1, i2, fracY, frequency)
        }

        @ExperimentalUnsignedTypes
        private fun smoothNoise2d(x: Int, y: Int): Int {
            val corners = noise(x - 1, y - 1) + noise(x + 1, y - 1) + noise(x - 1, 1 + y)
                + noise(x + 1, y + 1)
            val sides = noise(x - 1, y) + noise(1 + x, y) + noise(x, y - 1) + noise(x, 1 + y)
            val center = noise(x, y)
            return corners / 16 + sides / 8 + center / 4
        }

        @ExperimentalUnsignedTypes
        private fun noise(x: Int, y: Int): Int {
            var n = x + y * 57
            n = n xor (n shl 13)
            return (n * n * 15731 + 789221) * n + 1376312589 and (Int.MAX_VALUE shr 19) and UByte.MAX_VALUE.toInt()
        }

        private fun interpolate(a: Int, b: Int, x: Int, y: Int): Int {
            val f = (UShort.MAX_VALUE.toInt() + 1) - COS[(JAGEX_CIRCULAR_ANGLE / 2) * x / y] shr 1
            return (f * b shr 16) + (a * ((UShort.MAX_VALUE.toInt() + 1) - f) shr 16)
        }
    }
}

@ExperimentalUnsignedTypes
class ObjectLocation(
    val id: Int,
    val floor: Int,
    val localX: Int,
    val localY: Int,
    val type: Int,
    val orientation: Int
) {
    companion object {
        const val BLOCKED_TILE_MASK: UByte = 0x1u
        const val BRIDGE_TILE_MASK: UByte = 0x2u
        const val ROOF_TILE_MASK: UByte = 0x4u

        @ExperimentalUnsignedTypes
        fun decode(buffer: ByteBuffer, renderRules: Array<Array<UByteArray>>): List<ObjectLocation> {
            var id = -1
            var offset = buffer.smallUSmart.toInt()
            val locations = mutableListOf<ObjectLocation>()
            while (offset != 0) {
                id += offset
                var positionHash = 0
                var positionOffset = buffer.smallUSmart.toInt()
                while (positionOffset != 0) {
                    positionHash += positionOffset - 1
                    val localY = positionHash and 0x3F
                    val localX = (positionHash shr 6) and 0x3F
                    var z = (positionHash shr 12) and 0x3
                    if(renderRules[1][localX][localY] == BRIDGE_TILE_MASK) z--
                    if(z < 0) {
                        buffer.get()
                    } else {
                        val attributes = buffer.uByte.toInt()
                        val orientation = attributes and 0x3
                        val type = attributes shr 2
                        locations.add(ObjectLocation(id, z, localX, localY, type, orientation))
                    }
                    positionOffset = buffer.smallUSmart.toInt()
                }
                offset = buffer.smallUSmart.toInt()
            }
            return locations
        }
    }
}