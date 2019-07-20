package io.guthix.osrs.cache.config

import io.guthix.cache.js5.io.uByte
import java.io.IOException
import java.nio.ByteBuffer

data class VarClientConfig(override val id: Int) : Config(id) {
    var isPeristent = false

    override fun encode(): ByteBuffer = if(isPeristent) {
        ByteBuffer.allocate(2).apply {
            put(2)
            put(0)
        }
    } else {
        ByteBuffer.allocate(1).apply { put(0) }
    }

    companion object : ConfigCompanion<VarClientConfig>() {
        override val id = 19

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, buffer: ByteBuffer): VarClientConfig {
            val varClientConfig = VarClientConfig(id)
            decoder@ while (true) {
                when (val opcode = buffer.uByte.toInt()) {
                    0 -> break@decoder
                    2 -> varClientConfig.isPeristent = true
                    else -> throw IOException("Did not recognise opcode $opcode.")
                }
            }
            return varClientConfig
        }
    }
}