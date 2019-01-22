package io.github.bartvhelvert.jagex.fs.osrs.config

import io.github.bartvhelvert.jagex.fs.io.nullableLargeSmart
import io.github.bartvhelvert.jagex.fs.io.uByte
import io.github.bartvhelvert.jagex.fs.io.uShort
import io.github.bartvhelvert.jagex.fs.io.writeNullableLargeSmart
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.nio.ByteBuffer

data class HitBarConfig @ExperimentalUnsignedTypes constructor(
    override val id: Int,
    val field3310: UByte,
    val field3307: UByte,
    val field3312: UShort?,
    val field3308: UByte,
    val field3313: UShort,
    val field3315: Int?,
    val field3316: Int?,
    val healthScale: UByte,
    val field3318: UByte
) : Config(id) {
    @ExperimentalUnsignedTypes
    override fun encode(): ByteBuffer {
        val byteStr = ByteArrayOutputStream()
        DataOutputStream(byteStr).use { os ->
            if(field3310 != UByte.MAX_VALUE) {
                os.writeOpcode(2)
                os.writeByte(field3310.toInt())
            }
            if(field3307 != UByte.MAX_VALUE) {
                os.writeOpcode(3)
                os.writeByte(field3307.toInt())
            }
            field3312?.let {
                if(field3312.toInt() != 0) throw IOException("Field3312 should be 0")
                os.writeOpcode(4)
            }
            if(field3313.toInt() != 70) {
                os.writeOpcode(5)
                os.writeShort(field3313.toInt())
            }
            field3315?.let {
                os.writeOpcode(7)
                os.writeNullableLargeSmart(field3315)
            }
            field3316?.let {
                os.writeOpcode(8)
                os.writeNullableLargeSmart(field3316)
            }
            field3312?.let {
                os.writeOpcode(11)
                os.writeShort(field3312.toInt())
            }
            if(healthScale.toInt() != 30) {
                os.writeOpcode(14)
                os.writeByte(healthScale.toInt())
            }
            if(field3318.toInt() != 0) {
                os.writeOpcode(15)
                os.writeByte(field3318.toInt())
            }
            os.writeOpcode(0)
        }
        return ByteBuffer.wrap(byteStr.toByteArray())
    }

    companion object : ConfigCompanion<HitBarConfig>() {
        override val id = 33

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, buffer: ByteBuffer): HitBarConfig {
            var field3310: UByte = UByte.MAX_VALUE
            var field3307: UByte = UByte.MAX_VALUE
            var field3312: UShort? = null
            val field3308: UByte = 1u
            var field3313: UShort = 70u
            var field3315: Int? = null
            var field3316: Int? = null
            var healthScale: UByte = 30u
            var field3318: UByte = 0u

            decoder@ while (true) {
                val opcode = buffer.uByte.toInt()
                when(opcode) {
                    0 -> break@decoder
                    1 -> buffer.uShort
                    2 -> field3310 = buffer.uByte
                    3 -> field3307 = buffer.uByte
                    4 -> field3312 = 0u
                    5 -> field3313 = buffer.uShort
                    6 -> buffer.uByte
                    7 -> field3315 = buffer.nullableLargeSmart
                    8 -> field3316 = buffer.nullableLargeSmart
                    11 -> field3312 = buffer.uShort
                    14 -> healthScale = buffer.uByte
                    15 -> field3318 = buffer.uByte
                    else -> throw IOException("Did not recognise opcode $opcode")
                }
            }
            return HitBarConfig(id, field3310, field3307, field3312, field3308, field3313, field3315, field3316,
                healthScale,field3318
            )
        }

    }
}