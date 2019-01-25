package io.github.bartvhelvert.jagex.fs.osrs.config

import io.github.bartvhelvert.jagex.fs.io.uByte
import io.github.bartvhelvert.jagex.fs.io.uShort
import java.io.IOException
import java.nio.ByteBuffer

@ExperimentalUnsignedTypes
data class VarbitConfig(override val id: Int) : Config(id) {
    var varpId: UShort = 0u
    var lsb: UByte = 0u
    var msb: UByte = 0u

    @ExperimentalUnsignedTypes
    override fun encode(): ByteBuffer  = if(varpId.toInt() != 0 && lsb.toInt() != 0 && msb.toInt() != 0) {
        ByteBuffer.allocate(6).apply {
            put(1)
            putShort(varpId.toShort())
            put(lsb.toByte())
            put(msb.toByte())
            put(0)
        }
    } else {
        ByteBuffer.allocate(1).apply { put(0) }
    }

    companion object : ConfigCompanion<VarbitConfig>() {
        override val id = 14

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, buffer: ByteBuffer): VarbitConfig {
            val varbitConfig = VarbitConfig(id)
            decoder@ while (true) {
                val opcode = buffer.get().toInt() and 0xFF
                when(opcode) {
                    0 -> break@decoder
                    1 -> {
                        varbitConfig.varpId = buffer.uShort
                        varbitConfig.lsb = buffer.uByte
                        varbitConfig.msb = buffer.uByte
                    }
                    else -> throw IOException("Did not recognise opcode $opcode")
                }
            }
            return varbitConfig
        }
    }
}