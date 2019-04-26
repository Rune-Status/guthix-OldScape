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
package io.guthix.osrs.cache.script

import io.guthix.cache.fs.io.*
import java.nio.ByteBuffer

data class AssemblyScript(
    val id: Int,
    val instructions: IntArray,
    val intOperands: Map<Int, Int>,
    val stringOperands: Map<Int, String>,
    val localIntCount: Int,
    val localStringCount: Int,
    val intArgumentCount: Int,
    val stringArgumentCount: Int,
    val switches: Array<Map<Int, Int>>
) {
    companion object {
        @ExperimentalUnsignedTypes
        fun decode(id: Int, buffer: ByteBuffer): AssemblyScript {
            val switchDataLength = buffer.getUShort(buffer.limit() - 2).toInt() // ushort
            val opcodeEndPos = buffer.limit() - 2 - switchDataLength - 12
            buffer.position(opcodeEndPos)
            val opcodeCount = buffer.int
            val localIntCount = buffer.uShort.toInt()
            val localStringCount = buffer.uShort.toInt()
            val intArgumentCount = buffer.uShort.toInt()
            val stringArgumentCount = buffer.uShort.toInt()
            val switches = Array<Map<Int, Int>>(buffer.uByte.toInt()) {
                val caseCount = buffer.uShort.toInt()
                val switch = mutableMapOf<Int, Int>()
                repeat(caseCount) {
                    switch[buffer.int] = buffer.int
                }
                switch
            }
            buffer.position(0)
            buffer.nullableString
            val instructions = IntArray(opcodeCount)
            val intOperands = mutableMapOf<Int, Int>()
            val stringOperands = mutableMapOf<Int, String>()
            var i = 0
            while (buffer.position() < opcodeEndPos) {
                val opcode = buffer.uShort.toInt()
                if(opcode == Instruction.SCONST.opcode) {
                    stringOperands[i] = buffer.string
                } else if (opcode < 100 && opcode != Instruction.RETURN.opcode
                    && opcode != Instruction.POP_INT.opcode && opcode != Instruction.POP_STRING.opcode) {
                    intOperands[i] = buffer.int
                } else {
                    intOperands[i] = buffer.uByte.toInt()
                }
                instructions[i++] = opcode
            }
            return AssemblyScript(id, instructions, intOperands, stringOperands, localIntCount, localStringCount,
                intArgumentCount, stringArgumentCount, switches
            )
        }
    }
}