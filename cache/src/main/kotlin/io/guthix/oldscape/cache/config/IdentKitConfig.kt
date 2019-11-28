/**
 * This file is part of Guthix OldScape.
 *
 * Guthix OldScape is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Guthix OldScape is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Foobar. If not, see <https://www.gnu.org/licenses/>.
 */
package io.guthix.oldscape.cache.config

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import java.io.IOException

data class IdentKitConfig(
    override val id: Int,
    val colorFind: IntArray? = null,
    val colorReplace: IntArray? = null,
    val textureFind: IntArray? = null,
    val textureReplace: IntArray? = null,
    val bodyPartId: Short? = null,
    val modelIds: IntArray? = null,
    val models: IntArray = intArrayOf(-1, -1, -1, -1, -1),
    val nonSelectable: Boolean = false
) : Config(id) {

    
    override fun encode(): ByteBuf {
        val data = Unpooled.buffer()
        bodyPartId?.let {
            data.writeOpcode(1)
            data.writeByte(it.toInt())
        }
        modelIds?.let {
            data.writeOpcode(2)
            data.writeByte(it.size)
            it.forEach { id -> data.writeShort(id) }
        }
        if(nonSelectable) data.writeOpcode(3)
        colorFind?.let { colorFind -> colorReplace?.let { colorReplace->
            data.writeOpcode(40)
            data.writeByte(colorFind.size)
            for (i in colorFind.indices) {
                data.writeShort(colorFind[i])
                data.writeShort(colorReplace[i])
            }
        } }
        textureFind?.let { textureFind -> textureReplace?.let { textureReplace->
            data.writeOpcode(41)
            data.writeByte(textureFind.size)
            for (i in textureFind.indices) {
                data.writeShort(textureFind[i])
                data.writeShort(textureReplace[i])
            }
        } }
        models.forEachIndexed { i, id ->
            if(id != -1) {
                data.writeOpcode(60 + i)
                data.writeShort(id)
            }
        }
        data.writeOpcode(0)
        return data
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as IdentKitConfig
        if (id != other.id) return false
        if (colorFind != null) {
            if (other.colorFind == null) return false
            if (!colorFind.contentEquals(other.colorFind)) return false
        } else if (other.colorFind != null) return false
        if (colorReplace != null) {
            if (other.colorReplace == null) return false
            if (!colorReplace.contentEquals(other.colorReplace)) return false
        } else if (other.colorReplace != null) return false
        if (textureFind != null) {
            if (other.textureFind == null) return false
            if (!textureFind.contentEquals(other.textureFind)) return false
        } else if (other.textureFind != null) return false
        if (textureReplace != null) {
            if (other.textureReplace == null) return false
            if (!textureReplace.contentEquals(other.textureReplace)) return false
        } else if (other.textureReplace != null) return false
        if (bodyPartId != other.bodyPartId) return false
        if (modelIds != null) {
            if (other.modelIds == null) return false
            if (!modelIds.contentEquals(other.modelIds)) return false
        } else if (other.modelIds != null) return false
        if (models != other.models) return false
        if (nonSelectable != other.nonSelectable) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (colorFind?.contentHashCode() ?: 0)
        result = 31 * result + (colorReplace?.contentHashCode() ?: 0)
        result = 31 * result + (textureFind?.contentHashCode() ?: 0)
        result = 31 * result + (textureReplace?.contentHashCode() ?: 0)
        result = 31 * result + (bodyPartId ?: 0)
        result = 31 * result + (modelIds?.contentHashCode() ?: 0)
        result = 31 * result + models.hashCode()
        result = 31 * result + nonSelectable.hashCode()
        return result
    }

    companion object : ConfigCompanion<IdentKitConfig>() {
        override val id = 3

        override fun decode(id: Int, data: ByteBuf): IdentKitConfig {
            var colorFind: IntArray? = null
            var colorReplace: IntArray? = null
            var textureFind: IntArray? = null
            var textureReplace: IntArray? = null
            var bodyPartId: Short? = null
            var modelIds: IntArray? = null
            val models = intArrayOf(-1, -1, -1, -1, -1)
            var nonSelectable = false
            decoder@ while (true) {
                when (val opcode = data.readUnsignedByte().toInt()) {
                    0 -> break@decoder
                    1 -> bodyPartId = data.readUnsignedByte()
                    2 -> {
                        val length = data.readUnsignedByte().toInt()
                        modelIds = IntArray(length) { data.readUnsignedShort() }
                    }
                    3 -> nonSelectable = true
                    40 -> {
                        val colorsSize = data.readUnsignedByte().toInt()
                        colorFind = IntArray(colorsSize)
                        colorReplace = IntArray(colorsSize)
                        for (i in 0 until colorsSize) {
                            colorFind[i] = data.readUnsignedShort()
                            colorReplace[i] = data.readUnsignedShort()
                        }
                    }
                    41 -> {
                        val texturesSize = data.readUnsignedByte().toInt()
                        textureFind = IntArray(texturesSize)
                        textureReplace = IntArray(texturesSize)
                        for (i in 0 until texturesSize) {
                            textureFind[i] = data.readUnsignedShort()
                            textureReplace[i] = data.readUnsignedShort()
                        }
                    }
                    in 60..69 -> {
                        models[opcode - 60] = data.readUnsignedShort()
                    }
                    else -> throw IOException("Did not recognise opcode $opcode.")
                }
            }
            return IdentKitConfig(id, colorFind, colorReplace, textureFind, textureReplace, bodyPartId, modelIds,
                models, nonSelectable
            )
        }
    }
}