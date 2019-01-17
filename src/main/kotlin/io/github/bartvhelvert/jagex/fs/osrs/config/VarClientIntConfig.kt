package io.github.bartvhelvert.jagex.fs.osrs.config

import io.github.bartvhelvert.jagex.fs.io.uByte
import io.github.bartvhelvert.jagex.fs.osrs.Config
import io.github.bartvhelvert.jagex.fs.osrs.ConfigCompanion
import java.io.IOException
import java.nio.ByteBuffer

class VarClientIntConfig(id: Int, val isSerializable: Boolean) : Config(id) {
    override fun encode(): ByteBuffer = if(isSerializable) {
        ByteBuffer.allocate(2).apply {
            put(2)
            put(0)
        }
    } else {
        ByteBuffer.allocate(1).apply { put(0) }
    }

    companion object : ConfigCompanion<VarClientIntConfig>() {
        override val id = 19

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, buffer: ByteBuffer): VarClientIntConfig {
            var isSerializable = false
            decoder@ while (true) {
                val opcode = buffer.uByte.toInt()
                when (opcode) {
                    0 -> break@decoder
                    2 -> isSerializable = true
                    else -> throw IOException("Did not recognise opcode $opcode")
                }
            }
            return VarClientIntConfig(id, isSerializable)
        }
    }
}