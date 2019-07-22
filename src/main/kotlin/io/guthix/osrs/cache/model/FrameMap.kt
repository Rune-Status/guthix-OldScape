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
package io.guthix.osrs.cache.model

import java.nio.ByteBuffer
import io.guthix.cache.js5.io.uByte


@ExperimentalUnsignedTypes
class FrameMap(
    val types: UByteArray,
    val frameMaps: Array<UByteArray>
) {
    companion object {
        @ExperimentalUnsignedTypes
        fun decode(buffer: ByteBuffer): FrameMap {
            val length = buffer.uByte.toInt()
            val types = UByteArray(length) {
                buffer.uByte
            }
            val frameMaps = Array(length) {
                UByteArray(buffer.uByte.toInt())
            }
            frameMaps[0].map { buffer.uByte }

            frameMaps.forEach { frameMap ->
                for(i in 0 until frameMap.size) {
                    frameMap[i] = buffer.uByte
                }
            }
            return FrameMap(types, frameMaps)
        }
    }
}