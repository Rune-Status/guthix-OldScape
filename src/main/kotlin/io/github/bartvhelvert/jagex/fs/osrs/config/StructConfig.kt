package io.github.bartvhelvert.jagex.fs.osrs.config

import io.github.bartvhelvert.jagex.fs.io.params
import io.github.bartvhelvert.jagex.fs.io.uByte
import io.github.bartvhelvert.jagex.fs.io.writeParams
import io.github.bartvhelvert.jagex.fs.osrs.Config
import io.github.bartvhelvert.jagex.fs.osrs.ConfigCompanion
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.nio.ByteBuffer

class StructConfig(id: Int, val params: HashMap<Int, Any>?) : Config(id) {
    override fun encode(): ByteBuffer {
        val byteStr = ByteArrayOutputStream()
        DataOutputStream(byteStr).use { os ->
            params?.let {
                os.writeOpcode(249)
                os.writeParams(params)
            }
            os.writeOpcode(0)
        }
        return ByteBuffer.wrap(byteStr.toByteArray())
    }

    companion object : ConfigCompanion<StructConfig>() {
        override val id = 34

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, buffer: ByteBuffer): StructConfig {
            var params: HashMap<Int, Any>? = null
            decoder@ while (true) {
                val opcode = buffer.uByte.toInt()
                when (opcode) {
                    0 -> break@decoder
                    249 -> params = buffer.params
                }
            }
            return StructConfig(id, params)
        }
    }
}