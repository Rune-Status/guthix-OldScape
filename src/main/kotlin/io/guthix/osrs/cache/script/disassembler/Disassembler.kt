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
package io.guthix.osrs.cache.script.disassembler

import io.guthix.osrs.cache.script.AssemblyScript
import io.guthix.osrs.cache.script.Instruction
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class Disassembler {
    fun disassemble(script: AssemblyScript): String {
        val jumps = needsLabel(script)
        val writer = StringBuilder()
        script.instructions.forEachIndexed { i, opcode ->
            val instruction = Instruction.byOpcode[opcode]
            if(instruction == null) logger.warn("Instruction with opcode $opcode not implemented")
            if (jumps[i]) { writer.append("LABEL").append(i).append(":\n") }
            val name = if (instruction?.name != null) {
                instruction.name
            } else {
                String.format("%03d", opcode)
            }
            writer.append(String.format("   %-22s", name))
            if (script.intOperands[i] != null && shouldWriteIntOperand(opcode, script.intOperands[i])) {
                if (isJump(opcode)) {
                    writer.append(" LABEL").append(i + script.intOperands[i]!! + 1)
                } else {
                    writer.append(" ").append(script.intOperands[i])
                }
            }
            if (script.stringOperands[i] != null) {
                writer.append(" \"").append(script.stringOperands[i]).append("\"")
            }

            if (instruction == Instruction.SWITCH) {
                val switchMap = script.switches[script.intOperands[i]!!]

                for (entry in switchMap.entries) {
                    val value = entry.key
                    val jump = entry.value

                    writer.append("\n")
                    writer.append("      ").append(value).append(": LABEL").append(i + jump + 1)
                }
            }
            writer.append("\n")
        }
        return writer.toString()
    }

    private fun needsLabel(script: AssemblyScript): BooleanArray {
        val jumped = BooleanArray(script.instructions.size)

        for (i in 0 until script.instructions.size) {
            val opcode = script.instructions[i]
            val intOp = script.intOperands[i]

            if (opcode == Instruction.SWITCH.opcode) {
                val switchMap = script.switches[intOp!!]
                for (entry in switchMap.entries) {
                    val offset = entry.value
                    val to = i + offset + 1
                    assert(to >= 0 && to < script.instructions.size)
                    jumped[to] = true
                }
            }

            if (!isJump(opcode)) {
                continue
            }

            val to = i + intOp!! + 1
            assert(to >= 0 && to < script.instructions.size)

            jumped[to] = true
        }
        return jumped
    }

    private fun isJump(opcode: Int): Boolean {
        return when (opcode) {
            Instruction.JUMP.opcode, Instruction.IF_ICMPEQ.opcode, Instruction.IF_ICMPGE.opcode,
            Instruction.IF_ICMPGT.opcode, Instruction.IF_ICMPLE.opcode, Instruction.IF_ICMPLT.opcode,
            Instruction.IF_ICMPNE.opcode -> true
            else -> false
        }
    }

    private fun shouldWriteIntOperand(opcode: Int, operand: Int?): Boolean {
        if (opcode == Instruction.SWITCH.opcode) {
            // table follows instruction
            return false
        }

        if (operand != 0) {
            // always write non-zero operand
            return true
        }

        when (opcode) {
            Instruction.ICONST.opcode, Instruction.ILOAD.opcode, Instruction.SLOAD.opcode, Instruction.ISTORE.opcode,
            Instruction.SSTORE.opcode -> return true
        }

        // int operand is not used, don't write it
        return false
    }
}

