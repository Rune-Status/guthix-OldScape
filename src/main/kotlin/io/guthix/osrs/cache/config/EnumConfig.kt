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

import io.guthix.cache.js5.io.string
import io.guthix.cache.js5.io.uByte
import io.guthix.cache.js5.io.uShort
import io.guthix.cache.js5.io.writeString
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.nio.ByteBuffer

data class EnumConfig(override val id: Int) : Config(id) {
    var keyType: Char = 0.toChar()
    var valType: Char = 0.toChar()
    var defaultString = "null"
    var defaultInt = 0
    val keyValuePairs = mutableMapOf<Int, Any>()

    override fun encode(): ByteBuffer {
        val byteStr = ByteArrayOutputStream()
        DataOutputStream(byteStr).use { os ->
            if (keyType.toInt() != 0) {
                os.writeOpcode(1)
                os.writeByte(keyType.toInt())
            }
            if (valType.toInt() != 0) {
                os.writeOpcode(2)
                os.writeByte(valType.toInt())
            }
            if(defaultString != "null") {
                os.writeOpcode(3)
                os.writeString(defaultString)
            }
            if(defaultInt != 0) {
                os.writeOpcode(4)
                os.writeInt(defaultInt)
            }
            when {
                keyValuePairs.all { it.value is String } -> {
                    os.writeOpcode(5)
                    keyValuePairs.forEach { (key, value) ->
                        os.writeInt(key)
                        os.writeString(value as String)
                    }
                }
                keyValuePairs.all { it.value is Int } -> {
                    os.writeOpcode(6)
                    keyValuePairs.forEach { (key, value) ->
                        os.writeInt(key)
                        os.writeInt(value as Int)
                    }
                }
                else -> throw IOException("Enum can only contain ints or strings.")
            }
            os.writeOpcode(0)
        }
        return ByteBuffer.wrap(byteStr.toByteArray())
    }

    companion object : ConfigCompanion<EnumConfig>() {
        override val id = 8

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, data: ByteArray): EnumConfig {
            val buffer = ByteBuffer.wrap(data)
            val enumConfig = EnumConfig(id)
            decoder@ while (true) {
                when (val opcode = buffer.uByte.toInt()) {
                    0 -> break@decoder
                    1 -> enumConfig.keyType = buffer.uByte.toShort().toChar()
                    2 -> enumConfig.valType = buffer.uByte.toShort().toChar()
                    3 -> enumConfig.defaultString = buffer.string
                    4 -> enumConfig.defaultInt = buffer.int
                    5 -> {
                        val length = buffer.short.toInt()
                        for (i in 0 until length) {
                            val key = buffer.int
                            enumConfig.keyValuePairs[key] = buffer.string
                        }
                    }
                    6 -> {
                        val length = buffer.uShort.toInt()
                        for (i in 0 until length) {
                            val key = buffer.int
                            enumConfig.keyValuePairs[key] = buffer.int
                        }
                    }
                    else -> throw IOException("Did not recognise opcode $opcode.")
                }
            }
            return enumConfig
        }
    }
}