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
package io.guthix.osrs.cache.plane

import io.guthix.cache.js5.io.uByte
import io.guthix.cache.js5.io.uMedium
import io.guthix.cache.js5.io.uShort
import java.awt.image.BufferedImage
import java.nio.ByteBuffer

class Sprite(
    val id: Int,
    val width: Int,
    val height: Int,
    val images: Array<BufferedImage>
) {
    companion object {
        private const val FLAG_VERTICAL = 0x01
        private const val FLAG_ALPHA = 0x02

        @ExperimentalUnsignedTypes
        fun decode(id: Int, data: ByteArray): Sprite {
            val buffer = ByteBuffer.wrap(data)
            buffer.position(buffer.limit() - 2)
            val spriteCount = buffer.uShort.toInt()
            val offsetsX = IntArray(spriteCount)
            val offsetsY = IntArray(spriteCount)
            val subWidths = IntArray(spriteCount)
            val subHeights = IntArray(spriteCount)
            buffer.position(buffer.limit() - spriteCount * 8 - 7)
            val width = buffer.uShort.toInt()
            val height = buffer.uShort.toInt()
            val palette = IntArray(buffer.uByte.toInt() + 1)
            for(i in 0 until spriteCount) {
                offsetsX[i] = buffer.uShort.toInt()
            }
            for(i in 0 until spriteCount) {
                offsetsY[i] = buffer.uShort.toInt()
            }
            for(i in 0 until spriteCount) {
                subWidths[i] = buffer.uShort.toInt()
            }
            for(i in 0 until spriteCount) {
                subHeights[i] = buffer.uShort.toInt()
            }

            // read palette
            buffer.position(buffer.limit() - 7 - spriteCount * 8 - (palette.size - 1) * 3)
            for (i in 1 until palette.size) {
                palette[i] = buffer.uMedium
                if (palette[i] == 0) palette[i] = 1
            }

            // read pixels
            buffer.position(0)
            val images = Array(spriteCount) {
                val subWidth = subWidths[it]
                val subHeight = subHeights[it]
                val offsetX = offsetsX[it]
                val offsetY = offsetsY[it]
                val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
                val indices = Array(subWidth) { IntArray(subHeight) }
                val flags = buffer.uByte.toInt()
                if (flags and FLAG_VERTICAL != 0) { // read rgb vertical first
                    for (x in 0 until subWidth) {
                        for (y in 0 until subHeight) {
                            indices[x][y] = buffer.uByte.toInt()
                        }
                    }
                } else { // read rgb horizontal first
                    for (y in 0 until subHeight) {
                        for (x in 0 until subWidth) {
                            indices[x][y] = buffer.uByte.toInt()
                        }
                    }
                }
                if (flags and FLAG_ALPHA != 0) { // set rgb with alpha
                    if (flags and FLAG_VERTICAL != 0) { // read alpha vertical first
                        for (x in 0 until subWidth) {
                            for (y in 0 until subHeight) {
                                val alpha = buffer.uByte.toInt()
                                image.setRGB(x + offsetX, y + offsetY, alpha shl 24 or palette[indices[x][y]])
                            }
                        }
                    } else { // read alpha horizontal first
                        for (y in 0 until subHeight) {
                            for (x in 0 until subWidth) {
                                val alpha = buffer.uByte.toInt()
                                image.setRGB(x + offsetX, y + offsetY, alpha shl 24 or palette[indices[x][y]])
                            }
                        }
                    }
                } else { // set rgb without alpha
                    for (x in 0 until subWidth) {
                        for (y in 0 until subHeight) {
                            val index = indices[x][y]
                            if (index == 0) {
                                image.setRGB(x + offsetX, y + offsetY, 0)
                            } else {
                                image.setRGB(x + offsetX, y + offsetY, -0x1000000 or palette[index])
                            }
                        }
                    }
                }
                image
            }
            return Sprite(id, width, height, images)
        }
    }
}