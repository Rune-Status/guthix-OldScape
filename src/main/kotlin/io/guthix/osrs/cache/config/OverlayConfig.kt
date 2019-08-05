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
import io.guthix.cache.js5.io.uMedium
import io.guthix.cache.js5.io.writeMedium
import java.awt.Color
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.nio.ByteBuffer

@ExperimentalUnsignedTypes
data class OverlayConfig(override val id: Int) : Config(id) {
    var color = Color(0)
    var texture: UByte? = null
    var isHidden = true
    var otherColor: Color? = null

    @ExperimentalUnsignedTypes
    override fun encode(): ByteBuffer {
        val byteStr = ByteArrayOutputStream()
        DataOutputStream(byteStr).use { os ->
            if(color.rgb != 0) {
                os.writeOpcode(1)
                os.writeMedium(color.rgb)
            }
            texture?.let {
                os.writeOpcode(2)
                os.writeByte(texture!!.toInt())
            }
            if(!isHidden) os.writeOpcode(5)
            otherColor?.let {
                os.writeOpcode(7)
                os.writeMedium(otherColor!!.rgb)
            }
            os.writeOpcode(0)
        }
        return ByteBuffer.wrap(byteStr.toByteArray())
    }

    companion object : ConfigCompanion<OverlayConfig>() {
        override val id = 4

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, data: ByteArray): OverlayConfig {
            val buffer = ByteBuffer.wrap(data)
            val overlayConfig = OverlayConfig(id)
            decoder@ while (true) {
                when (val opcode = buffer.uByte.toInt()) {
                    0 -> break@decoder
                    1 -> overlayConfig.color = Color(buffer.uMedium)
                    2 -> overlayConfig.texture = buffer.uByte
                    5 -> overlayConfig.isHidden = false
                    7 -> overlayConfig.otherColor = Color(buffer.uMedium)
                    else -> throw IOException("Did not recognise opcode $opcode.")
                }
            }
            return overlayConfig
        }


    }
}