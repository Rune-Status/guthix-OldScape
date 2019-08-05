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

import io.guthix.cache.js5.io.*
import mu.KotlinLogging
import java.io.IOException
import java.nio.ByteBuffer
import kotlin.text.StringBuilder

private val logger = KotlinLogging.logger {}

data class MachineScript(
    val id: Int,
    val instructions: Array<InstructionDefinition>,
    val localIntCount: Int,
    val localStringCount: Int,
    val intArgumentCount: Int,
    val stringArgumentCount: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MachineScript) return false

        if (id != other.id) return false
        if (!instructions.contentEquals(other.instructions)) return false
        if (localIntCount != other.localIntCount) return false
        if (localStringCount != other.localStringCount) return false
        if (intArgumentCount != other.intArgumentCount) return false
        if (stringArgumentCount != other.stringArgumentCount) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + instructions.contentHashCode()
        result = 31 * result + localIntCount
        result = 31 * result + localStringCount
        result = 31 * result + intArgumentCount
        result = 31 * result + stringArgumentCount
        return result
    }

    override fun toString(): String  {
        val strBuilder = StringBuilder()
        strBuilder.append("""
            id: $id
            localIntCount: $localIntCount
            localStringCount: $localStringCount
            intArgumentCount: $intArgumentCount
            stringArgumentCount: $stringArgumentCount
        """.trimIndent())
        strBuilder.append("\n")
        instructions.forEach { instruction ->
            strBuilder.append(String.format("%-22s", instruction.name))
            when(instruction) {
                is IntInstruction -> strBuilder.append("${instruction.operand}\n")
                is StringInstruction -> strBuilder.append("${instruction.operand}\n")
                is SwitchInstruction -> {
                    strBuilder.append("${instruction.operand.size}\n")
                    instruction.operand.forEach { key, jumpTo ->
                        strBuilder.append("    $key -> $jumpTo\n")
                    }
                }
            }
        }
        return strBuilder.toString()
    }

    companion object {
        @ExperimentalUnsignedTypes
        fun decode(id: Int, data: ByteArray): MachineScript {
            val buffer = ByteBuffer.wrap(data)
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
            val opcodes = IntArray(opcodeCount)
            val intOperands = mutableMapOf<Int, Int>()
            val stringOperands = mutableMapOf<Int, String>()
            var i = 0
            while (buffer.position() < opcodeEndPos) {
                val opcode = buffer.uShort.toInt()
                if(opcode == InstructionDefinition.SCONST.opcode) {
                    stringOperands[i] = buffer.string
                } else if (opcode < 100 && opcode != InstructionDefinition.RETURN.opcode
                    && opcode != InstructionDefinition.POP_INT.opcode && opcode != InstructionDefinition.POP_STRING.opcode) {
                    intOperands[i] = buffer.int
                } else {
                    intOperands[i] = buffer.uByte.toInt()
                }
                opcodes[i++] = opcode
            }
            val instructions = Array(opcodeCount) {
                val opcode = opcodes[it]
                val instrDef = InstructionDefinition.byOpcode[opcode]
                    ?: InstructionDefinition(opcode, String.format("%03d", opcode)).apply {
                        logger.warn("InstructionDefinition $opcode is not implemented")
                    }
                if(intOperands[it] != null) {
                    if(opcodes[it] == InstructionDefinition.SWITCH.opcode) {
                        SwitchInstruction(instrDef.opcode, instrDef.name, switches[intOperands[it]!!])
                    } else {
                        IntInstruction(instrDef.opcode, instrDef.name, intOperands[it]!!)
                    }
                } else {
                    val stringOp= stringOperands[it]
                        ?: throw IOException("Could not find string operand for instruction ${instrDef.name}.")
                    StringInstruction(instrDef.opcode, instrDef.name, stringOp)
                }
            }
            return MachineScript(id, instructions, localIntCount, localStringCount, intArgumentCount, stringArgumentCount)
        }
    }
}