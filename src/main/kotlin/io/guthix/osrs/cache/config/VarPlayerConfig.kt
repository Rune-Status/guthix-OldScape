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
data class VarPlayerConfig(override val id: Int) : Config(id) {
    var type: UShort = 0u

    @ExperimentalUnsignedTypes
    override fun encode(): ByteBuffer = if(type.toInt() != 0) {
        ByteBuffer.allocate(2).apply {
            put(5)
            putShort(type.toShort())
            put(0)
        }
    } else {
        ByteBuffer.allocate(1).apply { put(0) }
    }

    companion object : ConfigCompanion<VarPlayerConfig>() {
        override val id = 16

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, data: ByteArray): VarPlayerConfig {
            val buffer = ByteBuffer.wrap(data)
            val varPlayerConfig = VarPlayerConfig(id)
            decoder@ while (true) {
                when (val opcode = buffer.uByte.toInt()) {
                    0 -> break@decoder
                    5 -> varPlayerConfig.type = buffer.uShort
                    else -> throw IOException("Did not recognise opcode $opcode.")
                }
            }
            return varPlayerConfig
        }
    }
}