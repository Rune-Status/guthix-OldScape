package io.github.bartvhelvert.jagex.fs.osrs.config

import io.github.bartvhelvert.jagex.fs.io.*
import io.github.bartvhelvert.jagex.fs.osrs.ConfigFile
import io.github.bartvhelvert.jagex.fs.osrs.ConfigFileCompanion
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.nio.ByteBuffer

class ParamConfig(
    id: Int,
    val stackType: Char?,
    val autoDisable: Boolean,
    val defaultInt: Int?,
    val defaultString: String?
) : ConfigFile(id) {
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

    companion object : ConfigFileCompanion<ConfigFile>() {
        override val id = 11

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, buffer: ByteBuffer): ConfigFile {
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
                }
            }
            return ParamConfig(id, stackType, autoDisable, defaultInt, defaultString)
        }
    }
}