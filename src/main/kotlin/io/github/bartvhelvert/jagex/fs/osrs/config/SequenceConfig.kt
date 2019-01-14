package io.github.bartvhelvert.jagex.fs.osrs.config

import io.github.bartvhelvert.jagex.fs.io.uByte
import io.github.bartvhelvert.jagex.fs.io.uMedium
import io.github.bartvhelvert.jagex.fs.io.uShort
import io.github.bartvhelvert.jagex.fs.io.writeMedium
import io.github.bartvhelvert.jagex.fs.osrs.ConfigFile
import io.github.bartvhelvert.jagex.fs.osrs.ConfigFileCompanion
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.nio.ByteBuffer

class SequenceConfig @ExperimentalUnsignedTypes constructor(
    id: Int,
    val frameIds: IntArray?,
    val field3048: IntArray?,
    val frameLengths: UShortArray?,
    val interleaveLeave: IntArray?,
    val stretches: Boolean,
    val forcedPriority: UByte,
    val maxLoops: UByte,
    val field3056: IntArray?,
    val precedenceAnimating: UByte?,
    val leftHandItem: UShort?,
    val rightHandItem: UShort?,
    val replyMode: UByte,
    val frameStep: UShort?,
    val priority: UByte?
) : ConfigFile(id) {
    @ExperimentalUnsignedTypes
    override fun encode(): ByteBuffer {
        val byteStr = ByteArrayOutputStream()
        DataOutputStream(byteStr).use { os ->
            frameLengths?.let {
                if(frameLengths.size != frameIds!!.size) throw IOException("Frame lengths don't match frame ids")
                os.writeOpcode(1)
                os.writeShort(frameLengths.size)
                frameLengths.forEach { length ->
                    os.writeShort(length.toInt())
                }
                frameIds.forEach { id ->
                    os.writeShort(id and 0xFFFF)
                }
                frameIds.forEach { id ->
                    os.writeShort(id shr 16)
                }
            }
            frameStep?.let {
                os.writeOpcode(2)
                os.writeShort(frameStep.toInt())
            }
            interleaveLeave?.let {
                os.writeOpcode(3)
                os.writeByte(interleaveLeave.size - 1)
                for(i in 0 until interleaveLeave.size - 1) {
                    os.writeShort(interleaveLeave[i])
                }
            }
            if(stretches) os.writeOpcode(4)
            if(forcedPriority.toInt() != 5) {
                os.writeOpcode(5)
                os.writeByte(forcedPriority.toInt())
            }
            leftHandItem?.let {
                os.writeOpcode(6)
                os.writeShort(leftHandItem.toInt())
            }
            rightHandItem?.let {
                os.writeOpcode(7)
                os.writeShort(rightHandItem.toInt())
            }
            if(maxLoops.toInt() != 99) {
                os.writeOpcode(8)
                os.writeByte(maxLoops.toInt())
            }
            precedenceAnimating?.let {
                os.writeOpcode(9)
                os.writeByte(precedenceAnimating.toInt())
            }
            priority?.let {
                os.writeOpcode(10)
                os.writeByte(priority.toInt())
            }
            if(replyMode.toInt() != 2) {
                os.writeOpcode(11)
                os.writeByte(replyMode.toInt())
            }
            field3048?.let {
                os.writeOpcode(12)
                os.writeByte(field3048.size)
                field3048.forEach { id ->
                    os.writeShort(id and 0xFFFF)
                }
                field3048.forEach { id ->
                    os.writeShort(id shr 16)
                }
            }
            field3056?.let {
                os.writeOpcode(13)
                os.writeByte(field3056.size)
                field3056.forEach { id ->
                    os.writeMedium(id)
                }
            }
            os.writeOpcode(0)
            return ByteBuffer.wrap(byteStr.toByteArray())
        }
    }

    companion object : ConfigFileCompanion<SequenceConfig>() {
        override val id = 12

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, buffer: ByteBuffer): SequenceConfig {
            var frameIds: IntArray? = null
            var field3048: IntArray? = null
            var frameLengths: UShortArray? = null
            var interleaveLeave: IntArray? = null
            var stretches = false
            var forcedPriority: UByte = 5u
            var maxLoops: UByte = 99u
            var field3056: IntArray? = null
            var precedenceAnimating: UByte? = null
            var leftHandItem: UShort? = null
            var rightHandItem: UShort? = null
            var replyMode: UByte = 2u
            var frameStep: UShort? = null
            var priority: UByte? = null

            decoder@ while (true) {
                val opcode = buffer.uByte.toInt()
                when (opcode) {
                    0 -> break@decoder
                    1 -> {
                        val length = buffer.uShort.toInt()
                        frameLengths = UShortArray(length) { buffer.uShort }
                        frameIds = IntArray(length) { buffer.uShort.toInt() }
                        frameIds.forEachIndexed { i, _ ->
                            frameIds[i] += buffer.uShort.toInt() shl 16
                        }
                    }
                    2 -> frameStep = buffer.uShort
                    3 -> {
                        val length = buffer.uByte.toInt()
                        interleaveLeave = IntArray(length + 1)
                        for(i in 0 until length) {
                            interleaveLeave[i] = buffer.uShort.toInt()
                        }
                        interleaveLeave[length] = 9999999
                    }
                    4 -> stretches = true
                    5 -> forcedPriority = buffer.uByte
                    6 -> leftHandItem = buffer.uShort
                    7 -> rightHandItem = buffer.uShort
                    8 -> maxLoops = buffer.uByte
                    9 -> precedenceAnimating = buffer.uByte
                    10 -> priority = buffer.uByte
                    11 -> replyMode = buffer.uByte
                    12 -> {
                        val length = buffer.uByte.toInt()
                        field3048 = IntArray(length) {
                            buffer.uShort.toInt()
                        }
                        for (index in 0 until length) {
                            field3048[index] += buffer.uShort.toInt() shl 16
                        }
                    }
                    13 -> {
                        val length = buffer.uByte.toInt()
                        field3056 = IntArray(length) { buffer.uMedium }
                    }
                    else -> throw IOException("Did not recognise opcode $opcode")
                }
            }
            return SequenceConfig(id, frameIds, field3048, frameLengths, interleaveLeave, stretches, forcedPriority,
                maxLoops, field3056, precedenceAnimating, leftHandItem, rightHandItem, replyMode, frameStep, priority
            )
        }
    }
}