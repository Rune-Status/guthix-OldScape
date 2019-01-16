package io.github.bartvhelvert.jagex.fs.osrs.config

import io.github.bartvhelvert.jagex.fs.io.uByte
import io.github.bartvhelvert.jagex.fs.osrs.ConfigFile
import io.github.bartvhelvert.jagex.fs.osrs.ConfigFileCompanion
import java.nio.ByteBuffer

class VarClientString(id: Int, val isSerializable: Boolean) : ConfigFile(id) {
    override fun encode(): ByteBuffer = if(isSerializable) {
        ByteBuffer.allocate(2).apply {
            put(2)
            put(0)
        }
    } else {
        ByteBuffer.allocate(1).apply { put(0) }
    }

    companion object : ConfigFileCompanion<VarClientString>() {
        override val id = 15

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, buffer: ByteBuffer): VarClientString {
            var isSerializable = false
            decoder@ while (true) {
                val opcode = buffer.uByte.toInt()
                when (opcode) {
                    0 -> break@decoder
                    2 -> isSerializable = true
                }
            }
            return VarClientString(id, isSerializable)
        }
    }
}