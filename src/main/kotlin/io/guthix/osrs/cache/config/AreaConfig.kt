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

import io.guthix.cache.js5.io.*
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.nio.ByteBuffer

@ExperimentalUnsignedTypes
data class AreaConfig(override val id: Int) : Config(id) {
    var spriteId: Int? = null
    var field3032: Int? = null
    var name: String? = null
    var field3033: Int? = null
    var field3034: UByte = 0u
    var shortArray: ShortArray? = null
    var aString1970: String? = null
    var flags: UByte = 0u
    var intarray35: IntArray? = null
    var anInt1980: UShort? = null
    var byteArray33: ByteArray? = null
    val stringArray = arrayOfNulls<String>(5)

    @ExperimentalUnsignedTypes
    override fun encode(): ByteBuffer {
        val byteStr = ByteArrayOutputStream()
        DataOutputStream(byteStr).use { os ->
            spriteId?.let {
                os.writeOpcode(1)
                os.writeNullableLargeSmart(spriteId)
            }
            field3032?.let {
                os.writeOpcode(2)
                os.writeNullableLargeSmart(field3032)
            }
            name?.let {
                os.writeOpcode(3)
                os.writeString(name!!)
            }
            field3033?.let {
                os.writeOpcode(4)
                os.writeMedium(field3033!!)
            }
            if(field3034.toInt() != 0) {
                os.writeOpcode(6)
                os.writeByte(field3034.toInt())
            }
            if(flags.toInt() != 0) {
                os.writeOpcode(7)
                os.writeByte(flags.toInt())
            }
            stringArray.forEachIndexed { i, s ->
                s?.let {
                    os.writeOpcode(i + 10)
                    os.writeString(s)
                }
            }
            if(shortArray != null && intarray35 != null && byteArray33 != null) {
                os.writeOpcode(15)
                os.writeByte(shortArray!!.size / 2)
                shortArray!!.forEach { os.writeShort(it.toInt()) }
                os.writeInt(0)
                os.writeByte(intarray35!!.size)
                intarray35!!.forEach { os.writeInt(it) }
                byteArray33!!.forEach { os.writeByte(it.toInt()) }
            }
            aString1970?.let {
                os.writeOpcode(17)
                os.writeString(aString1970!!)
            }
            anInt1980?.let {
                os.writeOpcode(19)
                os.writeShort(anInt1980!!.toInt())
            }
            os.writeOpcode(0)
        }
        return ByteBuffer.wrap(byteStr.toByteArray())
    }

    @ExperimentalUnsignedTypes
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AreaConfig) return false
        if (spriteId != other.spriteId) return false
        if (field3032 != other.field3032) return false
        if (name != other.name) return false
        if (field3033 != other.field3033) return false
        if (field3034 != other.field3034) return false
        if (shortArray != null) {
            if (other.shortArray == null) return false
            if (!shortArray!!.contentEquals(other.shortArray!!)) return false
        } else if (other.shortArray != null) return false
        if (aString1970 != other.aString1970) return false
        if (flags != other.flags) return false
        if (intarray35 != null) {
            if (other.intarray35 == null) return false
            if (!intarray35!!.contentEquals(other.intarray35!!)) return false
        } else if (other.intarray35 != null) return false
        if (anInt1980 != other.anInt1980) return false
        if (byteArray33 != null) {
            if (other.byteArray33 == null) return false
            if (!byteArray33!!.contentEquals(other.byteArray33!!)) return false
        } else if (other.byteArray33 != null) return false
        if (!stringArray.contentEquals(other.stringArray)) return false
        return true
    }

    @ExperimentalUnsignedTypes
    override fun hashCode(): Int {
        var result = spriteId ?: 0
        result = 31 * result + (field3032 ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (field3033 ?: 0)
        result = 31 * result + field3034.hashCode()
        result = 31 * result + (shortArray?.contentHashCode() ?: 0)
        result = 31 * result + (aString1970?.hashCode() ?: 0)
        result = 31 * result + flags.hashCode()
        result = 31 * result + (intarray35?.contentHashCode() ?: 0)
        result = 31 * result + (anInt1980?.hashCode() ?: 0)
        result = 31 * result + (byteArray33?.contentHashCode() ?: 0)
        result = 31 * result + stringArray.contentHashCode()
        return result
    }

    companion object : ConfigCompanion<AreaConfig>() {
        override val id = 35

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, data: ByteArray): AreaConfig {
            val buffer = ByteBuffer.wrap(data)
            val areaConfig = AreaConfig(id)
            decoder@ while (true) {
                when (val opcode = buffer.uByte.toInt()) {
                    0 -> break@decoder
                    1 -> areaConfig.spriteId = buffer.nullableLargeSmart
                    2 -> areaConfig.field3032 = buffer.nullableLargeSmart
                    3 -> areaConfig.name = buffer.string
                    4 -> areaConfig.field3033 = buffer.uMedium
                    5 -> buffer.uMedium
                    6 -> areaConfig.field3034 = buffer.uByte
                    7 -> areaConfig.flags = buffer.uByte
                    8 -> buffer.uByte
                    in 10..14 -> areaConfig.stringArray[opcode - 10] = buffer.string
                    15 -> {
                        val size = buffer.uByte.toInt()
                        areaConfig.shortArray = ShortArray(size * 2) {
                            buffer.short
                        }
                        buffer.int
                        val size2 = buffer.uByte.toInt()
                        areaConfig.intarray35 = IntArray(size2) {
                            buffer.int
                        }
                        areaConfig.byteArray33 = ByteArray(size2) {
                            buffer.get()
                        }
                    }
                    17 -> areaConfig.aString1970 = buffer.string
                    18 -> buffer.largeSmart
                    19 -> areaConfig.anInt1980 = buffer.uShort
                    21 -> buffer.int
                    22 -> buffer.int
                    23 -> repeat(3) { buffer.uByte }
                    24 -> repeat(2) { buffer.short }
                    25 -> buffer.nullableLargeSmart
                    28 -> buffer.uByte
                    29 -> buffer.uByte
                    30 -> buffer.uByte
                    else -> throw IOException("Did not recognise opcode $opcode.")
                }
            }
            return areaConfig
        }
    }
}