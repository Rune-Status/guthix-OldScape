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
import java.io.IOException
import java.nio.ByteBuffer

@ExperimentalUnsignedTypes
data class VarbitConfig(override val id: Int) : Config(id) {
    var varpId: UShort = 0u
    var lsb: UByte = 0u
    var msb: UByte = 0u

    @ExperimentalUnsignedTypes
    override fun encode(): ByteBuffer  = if(varpId.toInt() != 0 && lsb.toInt() != 0 && msb.toInt() != 0) {
        ByteBuffer.allocate(6).apply {
            put(1)
            putShort(varpId.toShort())
            put(lsb.toByte())
            put(msb.toByte())
            put(0)
        }
    } else {
        ByteBuffer.allocate(1).apply { put(0) }
    }

    companion object : ConfigCompanion<VarbitConfig>() {
        override val id = 14

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, data: ByteArray): VarbitConfig {
            val buffer = ByteBuffer.wrap(data)
            val varbitConfig = VarbitConfig(id)
            decoder@ while (true) {
                when(val opcode = buffer.get().toInt() and 0xFF) {
                    0 -> break@decoder
                    1 -> {
                        varbitConfig.varpId = buffer.uShort
                        varbitConfig.lsb = buffer.uByte
                        varbitConfig.msb = buffer.uByte
                    }
                    else -> throw IOException("Did not recognise opcode $opcode.")
                }
            }
            return varbitConfig
        }
    }
}