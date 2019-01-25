package io.github.bartvhelvert.jagex.fs.osrs.config

import io.github.bartvhelvert.jagex.fs.io.uByte
import io.github.bartvhelvert.jagex.fs.io.uMedium
import io.github.bartvhelvert.jagex.fs.io.writeMedium
import java.awt.Color
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.nio.ByteBuffer

@ExperimentalUnsignedTypes
data class OverlayConfig(override val id: Int) : Config(id) {
    var color = Color(0)
    var texture: UByte? = null
    var isHidden = true
    var otherColor: Color? = null

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
                os.writeByte(texture!!.toInt())
            }
            if(!isHidden) os.writeOpcode(5)
            otherColor?.let {
                os.writeOpcode(7)
                os.writeMedium(otherColor!!.rgb)
            }
            os.writeOpcode(0)
        }
        return ByteBuffer.wrap(byteStr.toByteArray())
    }

    companion object : ConfigCompanion<OverlayConfig>() {
        override val id = 4

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, buffer: ByteBuffer): OverlayConfig {
            val overlayConfig = OverlayConfig(id)
            decoder@ while (true) {
                val opcode = buffer.uByte.toInt()
                when (opcode) {
                    0 -> break@decoder
                    1 -> overlayConfig.color = Color(buffer.uMedium)
                    2 -> overlayConfig.texture = buffer.uByte
                    5 -> overlayConfig.isHidden = false
                    7 -> overlayConfig.otherColor = Color(buffer.uMedium)
                    else -> throw IOException("Did not recognise opcode $opcode")
                }
            }
            return overlayConfig
        }


    }
}