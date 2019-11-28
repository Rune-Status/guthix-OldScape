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

import io.guthix.buffer.*
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import java.io.IOException

data class AreaConfig(
    override val id: Int,
    val spriteId1: Int? = null,
    val spriteId2: Int? = null,
    val name: String? = null,
    val field3033: Int? = null,
    val textSize: Short = 0,
    val iop: Array<String?> = arrayOfNulls(5),
    val shortArray: ShortArray? = null,
    val intArray: IntArray? = null,
    val byteArray: ByteArray? = null,
    val menuTargetName: String? = null,
    val category: Int? = null,
    val horizontalAlignment: Short? = null,
    val verticalAlignment: Short? = null
) : Config(id) {


    override fun encode(): ByteBuf {
        val data = Unpooled.buffer()
        spriteId1.let {
            data.writeOpcode(1)
            data.writeNullableLargeSmart(it)
        }
        spriteId2.let {
            data.writeOpcode(2)
            data.writeNullableLargeSmart(it)
        }
        name?.let {
            data.writeOpcode(3)
            data.writeStringCP1252(it)
        }
        field3033?.let {
            data.writeOpcode(4)
            data.writeMedium(it)
        }
        if(textSize.toInt() != 0) {
            data.writeOpcode(6)
            data.writeByte(textSize.toInt())
        }
        iop.forEachIndexed { i, menuAction ->
            menuAction?.let {
                data.writeOpcode(i + 10)
                data.writeStringCP1252(menuAction)
            }
        }
        shortArray?.let { shortArray -> intArray?.let { intArray -> byteArray?.let { byteArray ->
            data.writeOpcode(15)
            data.writeByte(shortArray.size / 2)
            shortArray.forEach { data.writeShort(it.toInt()) }
            data.writeInt(0)
            data.writeByte(intArray.size)
            intArray.forEach { data.writeInt(it) }
            byteArray.forEach { data.writeByte(it.toInt()) }
        } } }
        menuTargetName?.let {
            data.writeOpcode(17)
            data.writeStringCP1252(it)
        }
        category?.let {
            data.writeOpcode(19)
            data.writeShort(it)
        }
        data.writeOpcode(0)
        return data
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as AreaConfig
        if (id != other.id) return false
        if (spriteId1 != other.spriteId1) return false
        if (spriteId2 != other.spriteId2) return false
        if (name != other.name) return false
        if (field3033 != other.field3033) return false
        if (textSize != other.textSize) return false
        if (!iop.contentEquals(other.iop)) return false
        if (shortArray != null) {
            if (other.shortArray == null) return false
            if (!shortArray.contentEquals(other.shortArray)) return false
        } else if (other.shortArray != null) return false
        if (intArray != null) {
            if (other.intArray == null) return false
            if (!intArray.contentEquals(other.intArray)) return false
        } else if (other.intArray != null) return false
        if (byteArray != null) {
            if (other.byteArray == null) return false
            if (!byteArray.contentEquals(other.byteArray)) return false
        } else if (other.byteArray != null) return false
        if (menuTargetName != other.menuTargetName) return false
        if (category != other.category) return false
        if (horizontalAlignment != other.horizontalAlignment) return false
        if (verticalAlignment != other.verticalAlignment) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (spriteId1 ?: 0)
        result = 31 * result + (spriteId2 ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (field3033 ?: 0)
        result = 31 * result + textSize
        result = 31 * result + iop.contentHashCode()
        result = 31 * result + (shortArray?.contentHashCode() ?: 0)
        result = 31 * result + (intArray?.contentHashCode() ?: 0)
        result = 31 * result + (byteArray?.contentHashCode() ?: 0)
        result = 31 * result + (menuTargetName?.hashCode() ?: 0)
        result = 31 * result + (category ?: 0)
        result = 31 * result + (horizontalAlignment ?: 0)
        result = 31 * result + (verticalAlignment ?: 0)
        return result
    }

    companion object : ConfigCompanion<AreaConfig>() {
        override val id = 35

        override fun decode(id: Int, data: ByteBuf): AreaConfig {
            var spriteId1: Int? = null
            var spriteId2: Int? = null
            var name: String? = null
            var anInt: Int? = null
            var textSize: Short = 0
            val iop: Array<String?> = arrayOfNulls(5)
            var shortArray: ShortArray? = null
            var intArray: IntArray? = null
            var byteArray: ByteArray? = null
            var menuTargetName: String? = null
            var category: Int? = null
            var horizontalAlignment: Short? = null
            var verticalAlignment: Short? = null
            decoder@ while (true) {
                when (val opcode = data.readUnsignedByte().toInt()) {
                    0 -> break@decoder
                    1 -> spriteId1 = data.readNullableLargeSmart()
                    2 -> spriteId2 = data.readNullableLargeSmart()
                    3 -> name = data.readStringCP1252()
                    4 -> anInt = data.readUnsignedMedium()
                    5 -> data.readUnsignedMedium()
                    6 -> textSize = data.readUnsignedByte()
                    7 -> data.readUnsignedByte() // some type of flag set
                    8 -> data.readUnsignedByte()
                    in 10..14 -> iop[opcode - 10] = data.readStringCP1252()
                    15 -> {
                        val size = data.readUnsignedByte().toInt()
                        shortArray = ShortArray(size * 2) {
                            data.readShort()
                        }
                        data.readInt()
                        val size2 = data.readUnsignedByte().toInt()
                        intArray = IntArray(size2) {
                            data.readInt()
                        }
                        byteArray = ByteArray(size2) {
                            data.readByte()
                        }
                    }
                    17 -> menuTargetName = data.readStringCP1252()
                    18 -> data.readNullableLargeSmart()
                    19 -> category = data.readUnsignedShort()
                    21 -> data.readInt()
                    22 -> data.readInt()
                    23 -> repeat(3) { data.readUnsignedByte() }
                    24 -> repeat(2) { data.readShort() }
                    25 -> data.readNullableLargeSmart()
                    28 -> data.readUnsignedByte()
                    29 -> horizontalAlignment = data.readUnsignedByte()
                    30 -> verticalAlignment = data.readUnsignedByte()
                    else -> throw IOException("Did not recognise opcode $opcode.")
                }
            }
            return AreaConfig(id, spriteId1, spriteId2, name, anInt, textSize, iop, shortArray, intArray, byteArray,
                menuTargetName, category, horizontalAlignment, verticalAlignment
            )
        }
    }
}