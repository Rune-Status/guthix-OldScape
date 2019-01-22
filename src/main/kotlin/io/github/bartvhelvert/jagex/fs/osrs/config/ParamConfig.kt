package io.github.bartvhelvert.jagex.fs.osrs.config

import io.github.bartvhelvert.jagex.fs.io.*
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.nio.ByteBuffer

data class ParamConfig(
    override val id: Int,
    val stackType: Char?,
    val autoDisable: Boolean,
    val defaultInt: Int?,
    val defaultString: String?
) : Config(id) {
    override fun encode(): ByteBuffer {
        val byteStr = ByteArrayOutputStream()
        DataOutputStream(byteStr).use { os ->
            stackType?.let {
                os.writeOpcode(1)
                os.writeByte(toEncodedChar(stackType))
            }
            defaultInt?.let {
                os.writeOpcode(2)
                os.writeInt(defaultInt)
            }
            if(!autoDisable) os.writeOpcode(4)
            defaultString?.let {
                os.writeOpcode(5)
                os.writeString(defaultString)
            }
            os.writeOpcode(0)
        }
        return ByteBuffer.wrap(byteStr.toByteArray())
    }

    companion object : ConfigCompanion<ParamConfig>() {
        override val id = 11

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, buffer: ByteBuffer): ParamConfig {
            var stackType: Char? = null
            var autoDisable: Boolean = true
            var defaultInt: Int? = null
            var defaultString: String? = null

            decoder@ while (true) {
                val opcode = buffer.uByte.toInt()
                when(opcode) {
                    0 -> break@decoder
                    1 -> {
                        val charId = buffer.uByte
                        if(charId.toInt() == 0) throw IOException("Char id can't be 0")
                        stackType = toJagexChar(charId.toInt())
                    }
                    2 -> defaultInt = buffer.int
                    4 -> autoDisable = false
                    5 -> defaultString = buffer.string
                    else -> throw IOException("Did not recognise opcode $opcode")
                }
            }
            return ParamConfig(id, stackType, autoDisable, defaultInt, defaultString)
        }
    }
}