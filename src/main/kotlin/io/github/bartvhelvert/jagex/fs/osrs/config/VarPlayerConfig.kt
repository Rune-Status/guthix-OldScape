package io.github.bartvhelvert.jagex.fs.osrs.config

import io.github.bartvhelvert.jagex.fs.io.uByte
import io.github.bartvhelvert.jagex.fs.io.uShort
import java.io.IOException
import java.nio.ByteBuffer

data class VarPlayerConfig @ExperimentalUnsignedTypes constructor(override val id: Int, val type: UShort) : Config(id) {
    @ExperimentalUnsignedTypes
    override fun encode(): ByteBuffer = if(type.toInt() != 0) {
        ByteBuffer.allocate(2).apply {
            put(5)
            putShort(type.toShort())
            put(0)
        }
    } else {
        ByteBuffer.allocate(1).apply { put(0) }
    }

    companion object : ConfigCompanion<VarPlayerConfig>() {
        override val id = 16

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, buffer: ByteBuffer): VarPlayerConfig {
            var type: UShort = 0u
            decoder@ while (true) {
                val opcode = buffer.uByte.toInt()
                when (opcode) {
                    0 -> break@decoder
                    5 -> type = buffer.uShort
                    else -> throw IOException("Did not recognise opcode $opcode")
                }
            }
            return VarPlayerConfig(id, type)
        }
    }
}