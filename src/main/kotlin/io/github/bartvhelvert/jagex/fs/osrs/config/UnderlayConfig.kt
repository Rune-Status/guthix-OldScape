package io.github.bartvhelvert.jagex.fs.osrs.config

import io.github.bartvhelvert.jagex.fs.io.putMedium
import io.github.bartvhelvert.jagex.fs.io.uByte
import io.github.bartvhelvert.jagex.fs.io.uMedium
import java.io.IOException
import java.nio.ByteBuffer

data class UnderlayConfig(override val id: Int) : Config(id) {
    var color = 0

    override fun encode(): ByteBuffer = if(color != 0) {
        ByteBuffer.allocate(2).apply {
            put(1)
            putMedium(color)
            put(0)
        }
    } else {
        ByteBuffer.allocate(1).apply { put(0) }
    }

    companion object : ConfigCompanion<UnderlayConfig>() {
        override val id = 1

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, buffer: ByteBuffer): UnderlayConfig {
            val underlayConfig = UnderlayConfig(id)
            decoder@ while (true) {
                val opcode = buffer.uByte.toInt()
                when (opcode) {
                    0 -> break@decoder
                    1 -> underlayConfig.color = buffer.uMedium
                    else -> throw IOException("Did not recognise opcode $opcode")
                }
            }
            return underlayConfig
        }
    }
}