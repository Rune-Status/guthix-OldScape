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

data class VarPlayerConfig(override val id: Int, val type: Int = 0) : Config(id) {
    override fun encode(): ByteBuf = if(type != 0) {
        Unpooled.buffer(2).apply {
            writeOpcode(5)
            writeShort(type)
            writeOpcode(0)
        }
    } else {
        Unpooled.buffer(1).apply { writeOpcode(0) }
    }

    companion object : ConfigCompanion<VarPlayerConfig>() {
        override val id = 16

        override fun decode(id: Int, data: ByteBuf): VarPlayerConfig {
            var type = 0
            decoder@ while (true) {
                when (val opcode = data.readUnsignedByte().toInt()) {
                    0 -> break@decoder
                    5 -> type = data.readUnsignedShort()
                    else -> throw IOException("Did not recognise opcode $opcode.")
                }
            }
            return VarPlayerConfig(id, type)
        }
    }
}