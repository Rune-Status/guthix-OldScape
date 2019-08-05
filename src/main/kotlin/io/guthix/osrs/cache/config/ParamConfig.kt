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
import io.guthix.cache.js5.util.toEncodedChar
import io.guthix.cache.js5.util.toJagexChar
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.nio.ByteBuffer

data class ParamConfig(override val id: Int) : Config(id) {
    var stackType: Char? = null
    var autoDisable: Boolean = true
    var defaultInt: Int? = null
    var defaultString: String? = null

    override fun encode(): ByteBuffer {
        val byteStr = ByteArrayOutputStream()
        DataOutputStream(byteStr).use { os ->
            stackType?.let {
                os.writeOpcode(1)
                os.writeByte(toEncodedChar(stackType!!))
            }
            defaultInt?.let {
                os.writeOpcode(2)
                os.writeInt(defaultInt!!)
            }
            if(!autoDisable) os.writeOpcode(4)
            defaultString?.let {
                os.writeOpcode(5)
                os.writeString(defaultString!!)
            }
            os.writeOpcode(0)
        }
        return ByteBuffer.wrap(byteStr.toByteArray())
    }

    companion object : ConfigCompanion<ParamConfig>() {
        override val id = 11

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, data: ByteArray): ParamConfig {
            val buffer = ByteBuffer.wrap(data)
            val paramConfig = ParamConfig(id)
            decoder@ while (true) {
                when(val opcode = buffer.uByte.toInt()) {
                    0 -> break@decoder
                    1 -> {
                        val charId = buffer.uByte
                        if(charId.toInt() == 0) throw IOException("Char id can't be 0.")
                        paramConfig.stackType = toJagexChar(charId.toInt())
                    }
                    2 -> paramConfig.defaultInt = buffer.int
                    4 -> paramConfig.autoDisable = false
                    5 -> paramConfig.defaultString = buffer.string
                    else -> throw IOException("Did not recognise opcode $opcode.")
                }
            }
            return paramConfig
        }
    }
}