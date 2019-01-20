package io.github.bartvhelvert.jagex.fs.osrs.config

import io.github.bartvhelvert.jagex.fs.io.uByte
import io.github.bartvhelvert.jagex.fs.io.uShort
import io.github.bartvhelvert.jagex.fs.osrs.Config
import io.github.bartvhelvert.jagex.fs.osrs.ConfigCompanion
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.nio.ByteBuffer


data class SpotAnimConfig @ExperimentalUnsignedTypes constructor(
    override val id: Int,
    val animationId: UShort?,
    val rotation: UShort,
    val resizeY: UShort,
    val resizeX: UShort,
    val modelId: UShort,
    val ambient: UByte,
    val contrast: UByte,
    val textureReplace: UShortArray?,
    val textureFind: UShortArray?,
    val colorFind: UShortArray?,
    val colorReplace: UShortArray?
) : Config(id) {
    @ExperimentalUnsignedTypes
    override fun encode(): ByteBuffer {
        val byteStr = ByteArrayOutputStream()
        DataOutputStream(byteStr).use { os ->
            if(modelId.toInt() != 0) {
                os.writeOpcode(1)
                os.writeShort(modelId.toInt())
            }
            animationId?.let {
                os.writeOpcode(2)
                os.writeShort(animationId.toInt())
            }
            if(resizeX.toInt() != 128) {
                os.writeOpcode(4)
                os.writeOpcode(resizeX.toInt())
            }
            if(resizeY.toInt() != 128) {
                os.writeOpcode(5)
                os.writeOpcode(resizeY.toInt())
            }
            if(rotation.toInt() != 0) {
                os.writeOpcode(6)
                os.writeShort(rotation.toInt())
            }
            if(ambient.toInt() != 0) {
                os.writeOpcode(7)
                os.writeByte(ambient.toInt())
            }
            if(contrast.toInt() != 0) {
                os.writeOpcode(8)
                os.writeByte(contrast.toInt())
            }
            if (colorFind != null && colorReplace != null) {
                os.writeOpcode(40)
                os.writeByte(colorFind.size)
                for (i in 0 until colorFind.size) {
                    os.writeShort(colorFind[i].toInt())
                    os.writeShort(colorReplace[i].toInt())
                }
            }
            if (textureFind != null && textureReplace != null) {
                os.writeOpcode(41)
                os.writeByte(textureFind.size)
                for (i in 0 until textureReplace.size) {
                    os.writeShort(textureFind[i].toInt())
                    os.writeShort(textureReplace[i].toInt())
                }
            }
            os.writeOpcode(0)
        }
        return ByteBuffer.wrap(byteStr.toByteArray())
    }

    companion object : ConfigCompanion<SpotAnimConfig>() {
        override val id = 13

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, buffer: ByteBuffer): SpotAnimConfig {
            var animationId: UShort? = null
            var rotation: UShort = 0u
            var resizeY: UShort = 128u
            var resizeX: UShort = 128u
            var modelId: UShort = 0u
            var ambient: UByte = 0u
            var contrast: UByte = 0u
            var textureReplace: UShortArray? = null
            var textureFind: UShortArray? = null
            var colorFind: UShortArray? = null
            var colorReplace: UShortArray? = null

            decoder@ while (true) {
                val opcode = buffer.uByte.toInt()
                when (opcode) {
                    0 -> break@decoder
                    1 -> modelId = buffer.uShort
                    2 -> animationId = buffer.uShort
                    4 -> resizeX = buffer.uShort
                    5 -> resizeY = buffer.uShort
                    6 -> rotation = buffer.uShort
                    7 -> ambient = buffer.uByte
                    8 -> contrast = buffer.uByte
                    40 -> {
                        val size = buffer.uByte.toInt()
                        colorFind = UShortArray(size)
                        colorReplace = UShortArray(size)
                        for (i in 0 until size) {
                            colorFind[i] = buffer.uShort
                            colorReplace[i] = buffer.uShort
                        }
                    }
                    41 -> {
                        val size = buffer.uByte.toInt()
                        textureFind = UShortArray(size)
                        textureReplace = UShortArray(size)
                        for (i in 0 until size) {
                            textureFind[i] = buffer.uShort
                            textureReplace[i] = buffer.uShort
                        }
                    }
                    else -> throw IOException("Did not recognise opcode $opcode")
                }
            }
            return SpotAnimConfig(id, animationId, rotation, resizeY, resizeX, modelId, ambient, contrast,
                textureReplace, textureFind, colorFind, colorReplace
            )
        }
    }
}