package io.github.bartvhelvert.jagex.fs.osrs.config

import io.github.bartvhelvert.jagex.fs.io.putMedium
import io.github.bartvhelvert.jagex.fs.io.uByte
import io.github.bartvhelvert.jagex.fs.io.uMedium
import io.github.bartvhelvert.jagex.fs.osrs.Config
import io.github.bartvhelvert.jagex.fs.osrs.ConfigCompanion
import java.io.IOException
import java.nio.ByteBuffer


class UnderlayConfig(id: Int, val color: Int) : Config(id) {
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
            var color = 0
            decoder@ while (true) {
                val opcode = buffer.uByte.toInt()
                when (opcode) {
                    0 -> break@decoder
                    1 -> color = buffer.uMedium
                    else -> throw IOException("Did not recognise opcode $opcode")
                }
            }
            return UnderlayConfig(id, color)
        }
    }
}