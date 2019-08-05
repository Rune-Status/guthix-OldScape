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
import java.io.IOException
import java.nio.ByteBuffer

data class VarClientConfig(override val id: Int) : Config(id) {
    var isPeristent = false

    override fun encode(): ByteBuffer = if(isPeristent) {
        ByteBuffer.allocate(2).apply {
            put(2)
            put(0)
        }
    } else {
        ByteBuffer.allocate(1).apply { put(0) }
    }

    companion object : ConfigCompanion<VarClientConfig>() {
        override val id = 19

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, data: ByteArray): VarClientConfig {
            val buffer = ByteBuffer.wrap(data)
            val varClientConfig = VarClientConfig(id)
            decoder@ while (true) {
                when (val opcode = buffer.uByte.toInt()) {
                    0 -> break@decoder
                    2 -> varClientConfig.isPeristent = true
                    else -> throw IOException("Did not recognise opcode $opcode.")
                }
            }
            return varClientConfig
        }
    }
}