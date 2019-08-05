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

import io.guthix.cache.js5.io.nullableLargeSmart
import io.guthix.cache.js5.io.uByte
import io.guthix.cache.js5.io.uShort
import io.guthix.cache.js5.io.writeNullableLargeSmart
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.nio.ByteBuffer

@ExperimentalUnsignedTypes
data class HitBarConfig(override val id: Int) : Config(id) {
    var field3310: UByte = UByte.MAX_VALUE
    var field3307: UByte = UByte.MAX_VALUE
    var field3312: UShort? = null
    var field3313: UShort = 70u
    var field3315: Int? = null
    var field3316: Int? = null
    var healthScale: UByte = 30u
    var field3318: UByte = 0u


    @ExperimentalUnsignedTypes
    override fun encode(): ByteBuffer {
        val byteStr = ByteArrayOutputStream()
        DataOutputStream(byteStr).use { os ->
            if(field3310 != UByte.MAX_VALUE) {
                os.writeOpcode(2)
                os.writeByte(field3310.toInt())
            }
            if(field3307 != UByte.MAX_VALUE) {
                os.writeOpcode(3)
                os.writeByte(field3307.toInt())
            }
            field3312?.let {
                if(field3312!!.toInt() != 0) throw IOException("Field3312 should be 0.")
                os.writeOpcode(4)
            }
            if(field3313.toInt() != 70) {
                os.writeOpcode(5)
                os.writeShort(field3313.toInt())
            }
            field3315?.let {
                os.writeOpcode(7)
                os.writeNullableLargeSmart(field3315)
            }
            field3316?.let {
                os.writeOpcode(8)
                os.writeNullableLargeSmart(field3316)
            }
            field3312?.let {
                os.writeOpcode(11)
                os.writeShort(field3312!!.toInt())
            }
            if(healthScale.toInt() != 30) {
                os.writeOpcode(14)
                os.writeByte(healthScale.toInt())
            }
            if(field3318.toInt() != 0) {
                os.writeOpcode(15)
                os.writeByte(field3318.toInt())
            }
            os.writeOpcode(0)
        }
        return ByteBuffer.wrap(byteStr.toByteArray())
    }

    companion object : ConfigCompanion<HitBarConfig>() {
        override val id = 33

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, data: ByteArray): HitBarConfig {
            val buffer = ByteBuffer.wrap(data)
            val hitBarConfig = HitBarConfig(id)
            decoder@ while (true) {
                when(val opcode = buffer.uByte.toInt()) {
                    0 -> break@decoder
                    1 -> buffer.uShort
                    2 -> hitBarConfig.field3310 = buffer.uByte
                    3 -> hitBarConfig.field3307 = buffer.uByte
                    4 -> hitBarConfig.field3312 = 0u
                    5 -> hitBarConfig.field3313 = buffer.uShort
                    6 -> buffer.uByte
                    7 -> hitBarConfig.field3315 = buffer.nullableLargeSmart
                    8 -> hitBarConfig.field3316 = buffer.nullableLargeSmart
                    11 -> hitBarConfig.field3312 = buffer.uShort
                    14 -> hitBarConfig.healthScale = buffer.uByte
                    15 -> hitBarConfig.field3318 = buffer.uByte
                    else -> throw IOException("Did not recognise opcode $opcode.")
                }
            }
            return HitBarConfig(id)
        }

    }
}