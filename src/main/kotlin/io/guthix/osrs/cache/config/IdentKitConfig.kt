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
data class IdentKitConfig(override val id: Int) : Config(id) {
    var colorFind: UShortArray? = null
    var colorReplace: UShortArray? = null
    var textureFind: UShortArray? = null
    var textureReplace: UShortArray? = null
    var bodyPartId: UByte? = null
    var modelIds: UShortArray? = null
    val models = intArrayOf(-1, -1, -1, -1, -1)
    var nonSelectable = false

    @ExperimentalUnsignedTypes
    override fun encode(): ByteBuffer {
        val byteStr = ByteArrayOutputStream()
        DataOutputStream(byteStr).use { os ->
            bodyPartId?.let {
                os.writeOpcode(1)
                os.writeByte(bodyPartId!!.toInt())
            }
            modelIds?.let {
                os.writeOpcode(2)
                os.writeByte(modelIds!!.size)
                modelIds!!.forEach { id -> os.writeShort(id.toInt()) }
            }
            if(nonSelectable) os.writeOpcode(3)
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
            models.forEachIndexed { i, id ->
                if(id != -1) {
                    os.writeOpcode(60 + i)
                    os.writeShort(id)
                }
            }
            os.writeOpcode(0)
        }
        return ByteBuffer.wrap(byteStr.toByteArray())
    }

    @ExperimentalUnsignedTypes
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is IdentKitConfig) return false
        if (id != other.id) return false
        if (colorFind != other.colorFind) return false
        if (colorReplace != other.colorReplace) return false
        if (textureFind != other.textureFind) return false
        if (textureReplace != other.textureReplace) return false
        if (bodyPartId != other.bodyPartId) return false
        if (modelIds != other.modelIds) return false
        if (!models.contentEquals(other.models)) return false
        if (nonSelectable != other.nonSelectable) return false
        return true
    }

    @ExperimentalUnsignedTypes
    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (colorFind?.hashCode() ?: 0)
        result = 31 * result + (colorReplace?.hashCode() ?: 0)
        result = 31 * result + (textureFind?.hashCode() ?: 0)
        result = 31 * result + (textureReplace?.hashCode() ?: 0)
        result = 31 * result + (bodyPartId?.hashCode() ?: 0)
        result = 31 * result + (modelIds?.hashCode() ?: 0)
        result = 31 * result + models.contentHashCode()
        result = 31 * result + nonSelectable.hashCode()
        return result
    }

    companion object : ConfigCompanion<IdentKitConfig>() {
        override val id = 3

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, data: ByteArray): IdentKitConfig {
            val buffer = ByteBuffer.wrap(data)
            val identKitConfig = IdentKitConfig(id)
            decoder@ while (true) {
                when (val opcode = buffer.uByte.toInt()) {
                    0 -> break@decoder
                    1 -> identKitConfig.bodyPartId = buffer.uByte
                    2 -> {
                        val length = buffer.uByte
                        identKitConfig.modelIds = UShortArray(length.toInt()) { buffer.uShort }
                    }
                    3 -> identKitConfig.nonSelectable = true
                    40 -> {
                        val colors = buffer.uByte.toInt()
                        identKitConfig.colorFind = UShortArray(colors)
                        identKitConfig.colorReplace = UShortArray(colors)
                        for (i in 0 until colors) {
                            identKitConfig.colorFind!![i] = buffer.uShort
                            identKitConfig.colorReplace!![i] = buffer.uShort
                        }
                    }
                    41 -> {
                        val textures = buffer.uByte.toInt()
                        identKitConfig.textureFind = UShortArray(textures)
                        identKitConfig.textureReplace = UShortArray(textures)
                        for (i in 0 until textures) {
                            identKitConfig.textureFind!![i] = buffer.uShort
                            identKitConfig.textureReplace!![i] = buffer.uShort
                        }
                    }
                    in 60..69 -> {
                        identKitConfig.models[opcode - 60] = buffer.uShort.toInt()
                    }
                    else -> throw IOException("Did not recognise opcode $opcode.")
                }
            }
            return identKitConfig
        }
    }
}