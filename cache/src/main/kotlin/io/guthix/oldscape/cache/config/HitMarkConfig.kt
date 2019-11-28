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

data class HitMarkConfig(
    override val id: Int,
    val fontId: Int? = null,
    val textColor: Int = 16777215,
    val int1: Int? = null,
    val int2: Int? = null,
    val int3: Int? = null,
    val int4: Int? = null,
    val int5: Short = 0,
    val string1: String = "",
    val int6: Int = 70,
    val int7: Short = 0,
    val int8: Int? = null,
    val int9: Short? = null,
    val int10: Short = 0,
    val transformVarbit: Int? = null,
    val transformVarp: Int? = null,
    val transforms: Array<Int?>? = null
) : Config(id) {

    override fun encode(): ByteBuf {
        val data = Unpooled.buffer()
        fontId.let {
            data.writeOpcode(1)
            data.writeNullableLargeSmart(it)
        }
        if(textColor != 16777215) {
            data.writeOpcode(2)
            data.writeMedium(textColor)
        }
        int1.let {
            data.writeOpcode(3)
            data.writeNullableLargeSmart(int1)
        }
        int2.let {
            data.writeOpcode(4)
            data.writeNullableLargeSmart(int2)
        }
        int3.let {
            data.writeOpcode(5)
            data.writeNullableLargeSmart(int3)
        }
        int4.let {
            data.writeOpcode(6)
            data.writeNullableLargeSmart(int4)
        }
        if(int5.toInt() != 0) {
            data.writeOpcode(7)
            data.writeShort(int5.toInt())
        }
        if(string1 != "") {
            data.writeOpcode(8)
            data.writeString0CP1252(string1)
        }
        if(int6 != 70) {
            data.writeOpcode(9)
            data.writeShort(int6)
        }
        if(int7.toInt() != 0) {
            data.writeOpcode(10)
            data.writeShort(int7.toInt())
        }
        int8?.let {
            if(it == 0) {
                data.writeOpcode(11)
            } else {
                data.writeOpcode(14)
                data.writeShort(it)
            }
        }
        int9?.let {
            data.writeOpcode(12)
            data.writeByte(it.toInt())
        }
        if(int10.toInt() != 0) {
            data.writeOpcode(13)
            data.writeShort(int10.toInt())
        }
        transforms?.let { transforms ->
            data.writeOpcode(if(transforms.last() == null) 18 else 17)
            if(transformVarbit == null) data.writeShort(65535) else data.writeShort(transformVarbit!!.toInt())
            if(transformVarp == null) data.writeShort(65535) else data.writeShort(transformVarp!!.toInt())
            transforms.last()?.let { data.writeShort(it) }
            val size = transforms.size - 2
            data.writeByte(size)
            for(i in 0..size) {
                val transform = transforms[i]
                if(transform == null) {
                    data.writeShort(65535)
                } else {
                    data.writeShort(transform)
                }
            }
        }
        data.writeOpcode(0)
        return data
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HitMarkConfig

        if (id != other.id) return false
        if (fontId != other.fontId) return false
        if (textColor != other.textColor) return false
        if (int1 != other.int1) return false
        if (int2 != other.int2) return false
        if (int3 != other.int3) return false
        if (int4 != other.int4) return false
        if (int5 != other.int5) return false
        if (string1 != other.string1) return false
        if (int6 != other.int6) return false
        if (int7 != other.int7) return false
        if (int8 != other.int8) return false
        if (int9 != other.int9) return false
        if (int10 != other.int10) return false
        if (transformVarbit != other.transformVarbit) return false
        if (transformVarp != other.transformVarp) return false
        if (transforms != null) {
            if (other.transforms == null) return false
            if (!transforms.contentEquals(other.transforms)) return false
        } else if (other.transforms != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (fontId ?: 0)
        result = 31 * result + textColor
        result = 31 * result + (int1 ?: 0)
        result = 31 * result + (int2 ?: 0)
        result = 31 * result + (int3 ?: 0)
        result = 31 * result + (int4 ?: 0)
        result = 31 * result + int5
        result = 31 * result + string1.hashCode()
        result = 31 * result + int6
        result = 31 * result + int7
        result = 31 * result + (int8 ?: 0)
        result = 31 * result + (int9 ?: 0)
        result = 31 * result + int10
        result = 31 * result + (transformVarbit ?: 0)
        result = 31 * result + (transformVarp ?: 0)
        result = 31 * result + (transforms?.contentHashCode() ?: 0)
        return result
    }

    companion object : ConfigCompanion<HitMarkConfig>() {
        override val id = 32

        override fun decode(id: Int, data: ByteBuf): HitMarkConfig {
            var fontId: Int? = null
            var textColor = 16777215
            var anInt1: Int? = null
            var anInt2: Int? = null
            var anInt3: Int? = null
            var anInt4: Int? = null
            var anInt5: Short = 0
            var aString = ""
            var anInt6 = 70
            var anInt7: Short = 0
            var anInt8: Int? = null
            var anInt9: Short? = null
            var anInt10: Short = 0
            var transformVarbit: Int? = null
            var transformVarp: Int? = null
            var transforms: Array<Int?>? = null
            decoder@ while (true) {
                when(val opcode = data.readUnsignedByte().toInt()) {
                    0 -> break@decoder
                    1 -> fontId = data.readNullableLargeSmart()
                    2 -> textColor = data.readUnsignedMedium()
                    3 -> anInt1 = data.readNullableLargeSmart()
                    4 -> anInt2 = data.readNullableLargeSmart()
                    5 -> anInt3 = data.readNullableLargeSmart()
                    6 -> anInt4 = data.readNullableLargeSmart()
                    7 -> anInt5 = data.readShort()
                    8 -> aString = data.readString0CP1252()
                    9 -> anInt6 = data.readUnsignedShort()
                    10 -> anInt7 = data.readShort()
                    11 -> anInt8 = 0
                    12 -> anInt9 = data.readUnsignedByte()
                    13 -> anInt10 = data.readShort()
                    14 -> anInt8 = data.readUnsignedShort()
                    17, 18 -> {
                        transformVarbit = data.readUnsignedShort()
                        transformVarbit = if(transformVarbit == 65535) null else transformVarbit
                        transformVarp = data.readUnsignedShort()
                        transformVarp = if(transformVarbit == 65535) null else transformVarp
                        val lastEntry = if(opcode == 18) {
                            val entry = data.readUnsignedShort()
                            if(entry == 65535) null else entry
                        } else null
                        val size = data.readUnsignedByte().toInt()
                        transforms = arrayOfNulls<Int?>(size + 2)
                        for(i in 0..size) {
                            val transform = data.readUnsignedShort()
                            transforms[i] = if(transform == 65535) null else transform
                        }
                        if(opcode == 18) {
                            transforms[size + 1] = lastEntry
                        }
                    }
                    else -> throw IOException("Did not recognise opcode $opcode.")
                }
            }
            return HitMarkConfig(id, fontId, textColor, anInt1, anInt2, anInt3, anInt4, anInt5, aString, anInt6, anInt7,
                anInt8, anInt9, anInt10, transformVarbit, transformVarp, transforms)
        }
    }
}