package io.github.bartvhelvert.jagex.fs.osrs.config

import io.github.bartvhelvert.jagex.fs.io.params
import io.github.bartvhelvert.jagex.fs.io.uByte
import io.github.bartvhelvert.jagex.fs.io.writeParams
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.nio.ByteBuffer

data class StructConfig(override val id: Int) : Config(id) {
    var params: HashMap<Int, Any>? = null

    override fun encode(): ByteBuffer {
        val byteStr = ByteArrayOutputStream()
        DataOutputStream(byteStr).use { os ->
            params?.let {
                os.writeOpcode(249)
                os.writeParams(params!!)
            }
            os.writeOpcode(0)
        }
        return ByteBuffer.wrap(byteStr.toByteArray())
    }

    companion object : ConfigCompanion<StructConfig>() {
        override val id = 34

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, buffer: ByteBuffer): StructConfig {
            val structConfig = StructConfig(id)
            decoder@ while (true) {
                val opcode = buffer.uByte.toInt()
                when (opcode) {
                    0 -> break@decoder
                    249 -> structConfig.params = buffer.params
                    else -> throw IOException("Did not recognise opcode $opcode")
                }
            }
            return structConfig
        }
    }
}