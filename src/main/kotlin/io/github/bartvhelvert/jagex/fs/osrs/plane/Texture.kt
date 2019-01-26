/*
GNU LGPL V3
Copyright (C) 2019 Bart van Helvert
B.A.J.v.Helvert@gmail.com

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with this program; if not, write to the Free Software Foundation,
Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package io.github.bartvhelvert.jagex.fs.osrs.plane

import io.github.bartvhelvert.jagex.fs.io.uByte
import io.github.bartvhelvert.jagex.fs.io.uShort
import java.io.IOException
import java.nio.ByteBuffer

class Texture @ExperimentalUnsignedTypes constructor(
    field1527: UShort,
    field1530: Boolean,
    fileIds: UShortArray,
    field1535: UByteArray?,
    field1532: UByteArray?,
    field1536: IntArray,
    field1537: UByte,
    field1538: UByte
) {
    companion object {
        @ExperimentalUnsignedTypes
        fun decode(buffer: ByteBuffer): Texture {
            val field1527 = buffer.uShort
            val field1530 = buffer.uByte.toInt() == 1
            val amount = buffer.uByte.toInt()
            if(amount !in 0..4) throw IOException("Amount of textures should be between 0 and 4 but is $amount")
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
            return Texture(field1527, field1530, fileIds, field1535, field1532, field1536, field1537, field1538)
        }
    }
}