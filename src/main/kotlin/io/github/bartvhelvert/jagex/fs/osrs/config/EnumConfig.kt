package io.github.bartvhelvert.jagex.fs.osrs.config

import io.github.bartvhelvert.jagex.fs.io.string
import io.github.bartvhelvert.jagex.fs.io.uByte
import io.github.bartvhelvert.jagex.fs.io.uShort
import io.github.bartvhelvert.jagex.fs.io.writeString
import io.github.bartvhelvert.jagex.fs.osrs.Config
import io.github.bartvhelvert.jagex.fs.osrs.ConfigCompanion
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.nio.ByteBuffer


class EnumConfig(
    id: Int,
    val keyType: Char,
    val valType: Char,
    val defaultString: String,
    val defaultInt: Int,
    val keyValuePairs: Map<Int, Any>
) : Config(id) {
    override fun encode(): ByteBuffer {
        val byteStr = ByteArrayOutputStream()
        DataOutputStream(byteStr).use { os ->
            if (keyType.toInt() != 0) {
                os.writeOpcode(1)
                os.writeByte(keyType.toInt())
            }
            if (valType.toInt() != 0) {
                os.writeOpcode(2)
                os.writeByte(valType.toInt())
            }
            if(defaultString != "null") {
                os.writeOpcode(3)
                os.writeString(defaultString)
            }
            if(defaultInt != 0) {
                os.writeOpcode(4)
                os.writeInt(defaultInt)
            }
            when {
                keyValuePairs.all { it.value is String } -> {
                    os.writeOpcode(5)
                    keyValuePairs.forEach { key, value ->
                        os.writeInt(key)
                        os.writeString(value as String)
                    }
                }
                keyValuePairs.all { it.value is Int } -> {
                    os.writeOpcode(6)
                    keyValuePairs.forEach { key, value ->
                        os.writeInt(key)
                        os.writeInt(value as Int)
                    }
                }
                else -> throw IOException("Enum can only contain ints or strings")
            }
            os.writeOpcode(0)
        }
        return ByteBuffer.wrap(byteStr.toByteArray())
    }

    companion object : ConfigCompanion<EnumConfig>() {
        override val id = 8

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, buffer: ByteBuffer): EnumConfig {
            var keyType: Char = 0.toChar()
            var valType: Char = 0.toChar()
            var defaultString = "null"
            var defaultInt = 0
            val keyValuePairs = mutableMapOf<Int, Any>()
            decoder@ while (true) {
                val opcode = buffer.uByte.toInt()
                when (opcode) {
                    0 -> break@decoder
                    1 -> keyType = buffer.uByte.toShort().toChar()
                    2 -> valType = buffer.uByte.toShort().toChar()
                    3 -> defaultString = buffer.string
                    4 -> defaultInt = buffer.int
                    5 -> {
                        val length = buffer.short.toInt()
                        for (i in 0 until length) {
                            val key = buffer.int
                            keyValuePairs[key] = buffer.string
                        }
                    }
                    6 -> {
                        val length = buffer.uShort.toInt()
                        for (i in 0 until length) {
                            val key = buffer.int
                            keyValuePairs[key] = buffer.int
                        }
                    }
                    else -> throw IOException("Did not recognise opcode $opcode")
                }
            }
            return EnumConfig(id, keyType, valType, defaultString, defaultInt, keyValuePairs)
        }
    }
}