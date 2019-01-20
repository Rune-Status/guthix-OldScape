package io.github.bartvhelvert.jagex.fs.osrs.config

import io.github.bartvhelvert.jagex.fs.io.*
import io.github.bartvhelvert.jagex.fs.osrs.Config
import io.github.bartvhelvert.jagex.fs.osrs.ConfigCompanion
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.nio.ByteBuffer


class AreaConfig @ExperimentalUnsignedTypes constructor(
    id: Int,
    val spriteId: Int?,
    val field3032: Int?,
    val name: String?,
    val field3033: Int?,
    val field3034: UByte,
    val shortArray: ShortArray?,
    val aString1970: String?,
    val flags: UByte,
    val intarray35: IntArray?,
    val anInt1980: UShort?,
    val byteArray33: ByteArray?,
    val stringArray: Array<String?>
) : Config(id) {
    @ExperimentalUnsignedTypes
    override fun encode(): ByteBuffer {
        val byteStr = ByteArrayOutputStream()
        DataOutputStream(byteStr).use { os ->
            spriteId?.let {
                os.writeOpcode(1)
                os.writeNullableSmart(spriteId)
            }
            field3032?.let {
                os.writeOpcode(2)
                os.writeNullableSmart(field3032)
            }
            name?.let {
                os.writeOpcode(3)
                os.writeString(name)
            }
            field3033?.let {
                os.writeOpcode(4)
                os.writeMedium(field3033)
            }
            if(field3034.toInt() != 0) {
                os.writeOpcode(6)
                os.writeByte(field3034.toInt())
            }
            if(flags.toInt() != 0) {
                os.writeOpcode(7)
                os.writeByte(flags.toInt())
            }
            stringArray.forEachIndexed { i, s ->
                s?.let {
                    os.writeOpcode(i + 10)
                    os.writeString(s)
                }
            }
            if(shortArray != null && intarray35 != null && byteArray33 != null) {
                os.writeOpcode(15)
                os.writeByte(shortArray.size / 2)
                shortArray.forEach { os.writeShort(it.toInt()) }
                os.writeInt(0)
                os.writeByte(intarray35.size)
                intarray35.forEach { os.writeInt(it) }
                byteArray33.forEach { os.writeByte(it.toInt()) }
            }
            aString1970?.let {
                os.writeOpcode(17)
                os.writeString(aString1970)
            }
            anInt1980?.let {
                os.writeOpcode(19)
                os.writeShort(anInt1980.toInt())
            }
            os.writeOpcode(0)
        }
        return ByteBuffer.wrap(byteStr.toByteArray())
    }

    companion object : ConfigCompanion<AreaConfig>() {
        override val id = 35

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, buffer: ByteBuffer): AreaConfig {
            var spriteId: Int? = null
            var field3032: Int? = null
            var name: String? = null
            var field3033: Int? = null
            var field3034: UByte = 0u
            var shortArray: ShortArray? = null
            var aString1970: String? = null
            var flags: UByte = 0u
            var intarray35: IntArray? = null
            var anInt1980: UShort? = null
            var byteArray33: ByteArray? = null
            val stringArray = arrayOfNulls<String>(5)

            decoder@ while (true) {
                val opcode = buffer.uByte.toInt()
                when (opcode) {
                    0 -> break@decoder
                    1 -> spriteId = buffer.nullableSmart
                    2 -> field3032 = buffer.nullableSmart
                    3 -> name = buffer.string
                    4 -> field3033 = buffer.uMedium
                    5 -> buffer.uMedium
                    6 -> field3034 = buffer.uByte
                    7 -> flags = buffer.uByte
                    8 -> buffer.uByte
                    in 10..14 -> stringArray[opcode - 10] = buffer.string
                    15 -> {
                        val size = buffer.uByte.toInt()
                        shortArray = ShortArray(size * 2) {
                            buffer.short
                        }
                        buffer.int
                        val size2 = buffer.uByte.toInt()
                        intarray35 = IntArray(size2) {
                            buffer.int
                        }
                        byteArray33 = ByteArray(size2) {
                            buffer.get()
                        }
                    }
                    17 -> aString1970 = buffer.string
                    18 -> buffer.smart
                    19 -> anInt1980 = buffer.uShort
                    21 -> buffer.int
                    22 -> buffer.int
                    23 -> repeat(3) { buffer.uByte }
                    24 -> repeat(2) { buffer.short }
                    25 -> buffer.nullableSmart
                    28 -> buffer.uByte
                    29 -> buffer.uByte
                    30 -> buffer.uByte
                    else -> throw IOException("Did not recognise opcode $opcode")
                }
            }

            return AreaConfig(id, spriteId, field3032, name, field3033, field3034, shortArray, aString1970, flags,
                intarray35, anInt1980, byteArray33, stringArray
            )
        }
    }
}