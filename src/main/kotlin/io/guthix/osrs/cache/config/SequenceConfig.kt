/*
 * Copyright (C) 2019 Guthix
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package io.guthix.osrs.cache.config

import io.guthix.cache.js5.io.uByte
import io.guthix.cache.js5.io.uMedium
import io.guthix.cache.js5.io.uShort
import io.guthix.cache.js5.io.writeMedium
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.nio.ByteBuffer

@ExperimentalUnsignedTypes
data class SequenceConfig(override val id: Int) : Config(id) {
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

    @ExperimentalUnsignedTypes
    override fun encode(): ByteBuffer {
        val byteStr = ByteArrayOutputStream()
        DataOutputStream(byteStr).use { os ->
            frameLengths?.let {
                if(frameLengths!!.size != frameIds!!.size) throw IOException("Frame lengths don't match frame ids.")
                os.writeOpcode(1)
                os.writeShort(frameLengths!!.size)
                frameLengths!!.forEach { length ->
                    os.writeShort(length.toInt())
                }
                frameIds!!.forEach { id ->
                    os.writeShort(id and 0xFFFF)
                }
                frameIds!!.forEach { id ->
                    os.writeShort(id shr 16)
                }
            }
            frameStep?.let {
                os.writeOpcode(2)
                os.writeShort(frameStep!!.toInt())
            }
            interleaveLeave?.let {
                os.writeOpcode(3)
                os.writeByte(interleaveLeave!!.size - 1)
                for(i in 0 until interleaveLeave!!.size - 1) {
                    os.writeShort(interleaveLeave!![i])
                }
            }
            if(stretches) os.writeOpcode(4)
            if(forcedPriority.toInt() != 5) {
                os.writeOpcode(5)
                os.writeByte(forcedPriority.toInt())
            }
            leftHandItem?.let {
                os.writeOpcode(6)
                os.writeShort(leftHandItem!!.toInt())
            }
            rightHandItem?.let {
                os.writeOpcode(7)
                os.writeShort(rightHandItem!!.toInt())
            }
            if(maxLoops.toInt() != 99) {
                os.writeOpcode(8)
                os.writeByte(maxLoops.toInt())
            }
            precedenceAnimating?.let {
                os.writeOpcode(9)
                os.writeByte(precedenceAnimating!!.toInt())
            }
            priority?.let {
                os.writeOpcode(10)
                os.writeByte(priority!!.toInt())
            }
            if(replyMode.toInt() != 2) {
                os.writeOpcode(11)
                os.writeByte(replyMode.toInt())
            }
            field3048?.let {
                os.writeOpcode(12)
                os.writeByte(field3048!!.size)
                field3048!!.forEach { id ->
                    os.writeShort(id and 0xFFFF)
                }
                field3048!!.forEach { id ->
                    os.writeShort(id shr 16)
                }
            }
            field3056?.let {
                os.writeOpcode(13)
                os.writeByte(field3056!!.size)
                field3056!!.forEach { id ->
                    os.writeMedium(id)
                }
            }
            os.writeOpcode(0)
        }
        return ByteBuffer.wrap(byteStr.toByteArray())
    }

    @ExperimentalUnsignedTypes
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SequenceConfig) return false
        if (id != other.id) return false
        if (frameIds != null) {
            if (other.frameIds == null) return false
            if (!frameIds!!.contentEquals(other.frameIds!!)) return false
        } else if (other.frameIds != null) return false
        if (field3048 != null) {
            if (other.field3048 == null) return false
            if (!field3048!!.contentEquals(other.field3048!!)) return false
        } else if (other.field3048 != null) return false
        if (frameLengths != other.frameLengths) return false
        if (interleaveLeave != null) {
            if (other.interleaveLeave == null) return false
            if (!interleaveLeave!!.contentEquals(other.interleaveLeave!!)) return false
        } else if (other.interleaveLeave != null) return false
        if (stretches != other.stretches) return false
        if (forcedPriority != other.forcedPriority) return false
        if (maxLoops != other.maxLoops) return false
        if (field3056 != null) {
            if (other.field3056 == null) return false
            if (!field3056!!.contentEquals(other.field3056!!)) return false
        } else if (other.field3056 != null) return false
        if (precedenceAnimating != other.precedenceAnimating) return false
        if (leftHandItem != other.leftHandItem) return false
        if (rightHandItem != other.rightHandItem) return false
        if (replyMode != other.replyMode) return false
        if (frameStep != other.frameStep) return false
        if (priority != other.priority) return false
        return true
    }

    @ExperimentalUnsignedTypes
    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (frameIds?.contentHashCode() ?: 0)
        result = 31 * result + (field3048?.contentHashCode() ?: 0)
        result = 31 * result + (frameLengths?.hashCode() ?: 0)
        result = 31 * result + (interleaveLeave?.contentHashCode() ?: 0)
        result = 31 * result + stretches.hashCode()
        result = 31 * result + forcedPriority.hashCode()
        result = 31 * result + maxLoops.hashCode()
        result = 31 * result + (field3056?.contentHashCode() ?: 0)
        result = 31 * result + (precedenceAnimating?.hashCode() ?: 0)
        result = 31 * result + (leftHandItem?.hashCode() ?: 0)
        result = 31 * result + (rightHandItem?.hashCode() ?: 0)
        result = 31 * result + replyMode.hashCode()
        result = 31 * result + (frameStep?.hashCode() ?: 0)
        result = 31 * result + (priority?.hashCode() ?: 0)
        return result
    }

    companion object : ConfigCompanion<SequenceConfig>() {
        override val id = 12

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, data: ByteArray): SequenceConfig {
            val buffer = ByteBuffer.wrap(data)
            val sequenceConfig = SequenceConfig(id)
            decoder@ while (true) {
                when (val opcode = buffer.uByte.toInt()) {
                    0 -> break@decoder
                    1 -> {
                        val length = buffer.uShort.toInt()
                        sequenceConfig.frameLengths = UShortArray(length) { buffer.uShort }
                        sequenceConfig.frameIds = IntArray(length) { buffer.uShort.toInt() }
                        sequenceConfig.frameIds!!.forEachIndexed { i, _ ->
                            sequenceConfig.frameIds!![i] += buffer.uShort.toInt() shl 16
                        }
                    }
                    2 -> sequenceConfig.frameStep = buffer.uShort
                    3 -> {
                        val length = buffer.uByte.toInt()
                        sequenceConfig.interleaveLeave = IntArray(length + 1)
                        for(i in 0 until length) {
                            sequenceConfig.interleaveLeave!![i] = buffer.uByte.toInt()
                        }
                        sequenceConfig.interleaveLeave!![length] = 9999999
                    }
                    4 -> sequenceConfig.stretches = true
                    5 -> sequenceConfig.forcedPriority = buffer.uByte
                    6 -> sequenceConfig.leftHandItem = buffer.uShort
                    7 -> sequenceConfig.rightHandItem = buffer.uShort
                    8 -> sequenceConfig.maxLoops = buffer.uByte
                    9 -> sequenceConfig.precedenceAnimating = buffer.uByte
                    10 -> sequenceConfig.priority = buffer.uByte
                    11 -> sequenceConfig.replyMode = buffer.uByte
                    12 -> {
                        val length = buffer.uByte.toInt()
                        sequenceConfig.field3048 = IntArray(length) {
                            buffer.uShort.toInt()
                        }
                        for (index in 0 until length) {
                            sequenceConfig.field3048!![index] += buffer.uShort.toInt() shl 16
                        }
                    }
                    13 -> {
                        val length = buffer.uByte.toInt()
                        sequenceConfig.field3056 = IntArray(length) { buffer.uMedium }
                    }
                    else -> throw IOException("Did not recognise opcode $opcode.")
                }
            }
            sequenceConfig.precedenceAnimating?.let {
                sequenceConfig.precedenceAnimating = if(sequenceConfig.interleaveLeave != null) 2u else 0u

            }
            sequenceConfig.priority?.let {
                sequenceConfig.priority = if(sequenceConfig.interleaveLeave != null) 2u else 0u
            }
            return sequenceConfig
        }
    }
}