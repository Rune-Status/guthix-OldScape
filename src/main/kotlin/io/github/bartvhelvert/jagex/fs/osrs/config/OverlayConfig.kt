package io.github.bartvhelvert.jagex.fs.osrs.config

import io.github.bartvhelvert.jagex.fs.io.uByte
import io.github.bartvhelvert.jagex.fs.io.uMedium
import io.github.bartvhelvert.jagex.fs.io.writeMedium
import io.github.bartvhelvert.jagex.fs.osrs.Config
import io.github.bartvhelvert.jagex.fs.osrs.ConfigCompanion
import java.awt.Color
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.nio.ByteBuffer

class OverlayConfig @ExperimentalUnsignedTypes constructor(
    id: Int,
    val color: Color,
    val texture: UByte?,
    val isHidden: Boolean,
    val otherColor: Color?
) : Config(id) {
    @ExperimentalUnsignedTypes
    override fun encode(): ByteBuffer {
        val byteStr = ByteArrayOutputStream()
        DataOutputStream(byteStr).use { os ->
            if(color.rgb != 0) {
                os.writeOpcode(1)
                os.writeMedium(color.rgb)
            }
            texture?.let {
                os.writeOpcode(2)
                os.writeByte(texture.toInt())
            }
            if(!isHidden) os.writeOpcode(5)
            otherColor?.let {
                os.writeOpcode(7)
                os.writeMedium(otherColor.rgb)
            }
            os.writeOpcode(0)
        }
        return ByteBuffer.wrap(byteStr.toByteArray())
    }

    companion object : ConfigCompanion<OverlayConfig>() {
        override val id = 4

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, buffer: ByteBuffer): OverlayConfig {
            var color = Color(0)
            var texture: UByte? = null
            var isHidden = true
            var otherColor: Color? = null
            decoder@ while (true) {
                val opcode = buffer.uByte.toInt()
                when (opcode) {
                    0 -> break@decoder
                    1 -> color = Color(buffer.uMedium)
                    2 -> texture = buffer.uByte
                    5 -> isHidden = false
                    7 -> otherColor = Color(buffer.uMedium)
                    else -> throw IOException("Did not recognise opcode $opcode")
                }
            }
            return OverlayConfig(id, color, texture, isHidden, otherColor)
        }


    }
}