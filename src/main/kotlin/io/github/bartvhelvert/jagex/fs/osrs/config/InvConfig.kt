package io.github.bartvhelvert.jagex.fs.osrs.config

import io.github.bartvhelvert.jagex.fs.io.uByte
import io.github.bartvhelvert.jagex.fs.io.uShort
import java.io.IOException
import java.nio.ByteBuffer

data class InvConfig @ExperimentalUnsignedTypes constructor(
    override val id: Int,
    val capacity: UShort
) : Config(id) {
    @ExperimentalUnsignedTypes
    override fun encode(): ByteBuffer = if(capacity.toInt() != 0) {
        ByteBuffer.allocate(4).apply {
            put(2)
            putShort(capacity.toShort())
            put(0)
        }
    } else {
        ByteBuffer.allocate(1).apply {
            put(0)
        }
    }

    companion object : ConfigCompanion<InvConfig>() {
        override val id = 5

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, buffer: ByteBuffer): InvConfig {
            var capacity: UShort = 0u
            decoder@ while (true) {
                val opcode = buffer.uByte.toInt()
                when (opcode) {
                    0 -> break@decoder
                    2 -> capacity = buffer.uShort
                    else -> throw IOException("Did not recognise opcode $opcode")
                }
            }
            return InvConfig(id, capacity)
        }
    }
}