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
package io.guthix.osrs.cache.config

import io.guthix.cache.js5.io.uByte
import io.guthix.cache.js5.io.uShort
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.nio.ByteBuffer

@ExperimentalUnsignedTypes
data class SpotAnimConfig(override val id: Int) : Config(id) {
    var animationId: UShort? = null
    var rotation: UShort = 0u
    var resizeY: UShort = 128u
    var resizeX: UShort = 128u
    var modelId: UShort = 0u
    var ambient: UByte = 0u
    var contrast: UByte = 0u
    var textureReplace: UShortArray? = null
    var textureFind: UShortArray? = null
    var colorFind: UShortArray? = null
    var colorReplace: UShortArray? = null

    @ExperimentalUnsignedTypes
    override fun encode(): ByteBuffer {
        val byteStr = ByteArrayOutputStream()
        DataOutputStream(byteStr).use { os ->
            if(modelId.toInt() != 0) {
                os.writeOpcode(1)
                os.writeShort(modelId.toInt())
            }
            animationId?.let {
                os.writeOpcode(2)
                os.writeShort(animationId!!.toInt())
            }
            if(resizeX.toInt() != 128) {
                os.writeOpcode(4)
                os.writeOpcode(resizeX.toInt())
            }
            if(resizeY.toInt() != 128) {
                os.writeOpcode(5)
                os.writeOpcode(resizeY.toInt())
            }
            if(rotation.toInt() != 0) {
                os.writeOpcode(6)
                os.writeShort(rotation.toInt())
            }
            if(ambient.toInt() != 0) {
                os.writeOpcode(7)
                os.writeByte(ambient.toInt())
            }
            if(contrast.toInt() != 0) {
                os.writeOpcode(8)
                os.writeByte(contrast.toInt())
            }
            if (colorFind != null && colorReplace != null) {
                os.writeOpcode(40)
                os.writeByte(colorFind!!.size)
                for (i in 0 until colorFind!!.size) {
                    os.writeShort(colorFind!![i].toInt())
                    os.writeShort(colorReplace!![i].toInt())
                }
            }
            if (textureFind != null && textureReplace != null) {
                os.writeOpcode(41)
                os.writeByte(textureFind!!.size)
                for (i in 0 until textureReplace!!.size) {
                    os.writeShort(textureFind!![i].toInt())
                    os.writeShort(textureReplace!![i].toInt())
                }
            }
            os.writeOpcode(0)
        }
        return ByteBuffer.wrap(byteStr.toByteArray())
    }

    companion object : ConfigCompanion<SpotAnimConfig>() {
        override val id = 13

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, data: ByteArray): SpotAnimConfig {
            val buffer = ByteBuffer.wrap(data)
            val spotAnimConfig = SpotAnimConfig(id)
            decoder@ while (true) {
                when (val opcode = buffer.uByte.toInt()) {
                    0 -> break@decoder
                    1 -> spotAnimConfig.modelId = buffer.uShort
                    2 -> spotAnimConfig.animationId = buffer.uShort
                    4 -> spotAnimConfig.resizeX = buffer.uShort
                    5 -> spotAnimConfig.resizeY = buffer.uShort
                    6 -> spotAnimConfig.rotation = buffer.uShort
                    7 -> spotAnimConfig.ambient = buffer.uByte
                    8 -> spotAnimConfig.contrast = buffer.uByte
                    40 -> {
                        val size = buffer.uByte.toInt()
                        spotAnimConfig.colorFind = UShortArray(size)
                        spotAnimConfig.colorReplace = UShortArray(size)
                        for (i in 0 until size) {
                            spotAnimConfig.colorFind!![i] = buffer.uShort
                            spotAnimConfig.colorReplace!![i] = buffer.uShort
                        }
                    }
                    41 -> {
                        val size = buffer.uByte.toInt()
                        spotAnimConfig.textureFind = UShortArray(size)
                        spotAnimConfig.textureReplace = UShortArray(size)
                        for (i in 0 until size) {
                            spotAnimConfig.textureFind!![i] = buffer.uShort
                            spotAnimConfig.textureReplace!![i] = buffer.uShort
                        }
                    }
                    else -> throw IOException("Did not recognise opcode $opcode.")
                }
            }
            return spotAnimConfig
        }
    }
}