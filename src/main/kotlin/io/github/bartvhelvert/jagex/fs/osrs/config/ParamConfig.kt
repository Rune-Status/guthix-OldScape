package io.github.bartvhelvert.jagex.fs.osrs.config

import io.github.bartvhelvert.jagex.fs.io.*
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.nio.ByteBuffer

data class ParamConfig(override val id: Int) : Config(id) {
    var stackType: Char? = null
    var autoDisable: Boolean = true
    var defaultInt: Int? = null
    var defaultString: String? = null

    override fun encode(): ByteBuffer {
        val byteStr = ByteArrayOutputStream()
        DataOutputStream(byteStr).use { os ->
            stackType?.let {
                os.writeOpcode(1)
                os.writeByte(toEncodedChar(stackType!!))
            }
            defaultInt?.let {
                os.writeOpcode(2)
                os.writeInt(defaultInt!!)
            }
            if(!autoDisable) os.writeOpcode(4)
            defaultString?.let {
                os.writeOpcode(5)
                os.writeString(defaultString!!)
            }
            os.writeOpcode(0)
        }
        return ByteBuffer.wrap(byteStr.toByteArray())
    }

    companion object : ConfigCompanion<ParamConfig>() {
        override val id = 11

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, buffer: ByteBuffer): ParamConfig {
            val paramConfig = ParamConfig(id)
            decoder@ while (true) {
                val opcode = buffer.uByte.toInt()
                when(opcode) {
                    0 -> break@decoder
                    1 -> {
                        val charId = buffer.uByte
                        if(charId.toInt() == 0) throw IOException("Char id can't be 0")
                        paramConfig.stackType = toJagexChar(charId.toInt())
                    }
                    2 -> paramConfig.defaultInt = buffer.int
                    4 -> paramConfig.autoDisable = false
                    5 -> paramConfig.defaultString = buffer.string
                    else -> throw IOException("Did not recognise opcode $opcode")
                }
            }
            return paramConfig
        }
    }
}