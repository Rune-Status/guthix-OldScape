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
package io.guthix.osrs.cache.plane

import io.guthix.cache.js5.io.uByte
import io.guthix.cache.js5.io.uShort
import java.io.IOException
import java.nio.ByteBuffer

class Texture @ExperimentalUnsignedTypes constructor(
    val id: Int,
    val field1527: UShort,
    val field1530: Boolean,
    val fileIds: UShortArray,
    val field1535: UByteArray?,
    val field1532: UByteArray?,
    val field1536: IntArray,
    val field1537: UByte,
    val field1538: UByte
) {
    companion object {
        @ExperimentalUnsignedTypes
        fun decode(id: Int, data: ByteArray): Texture {
            val buffer = ByteBuffer.wrap(data)
            val field1527 = buffer.uShort
            val field1530 = buffer.uByte.toInt() == 1
            val amount = buffer.uByte.toInt()
            if(amount !in 0..4) throw IOException("Amount of textures should be between 0 and 4 but is $amount.")
            val fileIds = UShortArray(amount) {
                buffer.uShort
            }
            val (field1535, field1532) = if (amount > 1) {
                Pair(
                    UByteArray(amount - 1) {
                        buffer.uByte
                    },
                    UByteArray(amount - 1) {
                        buffer.uByte
                    }
                )
            } else Pair(null, null)
            val field1536 = IntArray(amount) {
                buffer.int
            }
            val field1537 = buffer.uByte
            val field1538 = buffer.uByte
            return Texture(
                id,
                field1527,
                field1530,
                fileIds,
                field1535,
                field1532,
                field1536,
                field1537,
                field1538
            )
        }
    }
}