/**
 * This file is part of Guthix OldScape.
 *
 * Guthix OldScape is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Guthix OldScape is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Foobar. If not, see <https://www.gnu.org/licenses/>.
 */
package io.guthix.oldscape.cache.config

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import java.io.IOException

data class SequenceConfig(
    override val id: Int,
    val frameIds: IntArray? = null,
    val field3048: IntArray? = null,
    val frameLengths: IntArray? = null,
    val interleave: IntArray? = null,
    val stretches: Boolean = false,
    val forcedPriority: Short = 5,
    val maxLoops: Short = 99,
    val field3056: IntArray? = null,
    val precedenceAnimating: Short? = null,
    val leftHandItem: Int? = null,
    val rightHandItem: Int? = null,
    val replyMode: Short = 2,
    val frameStep: Int? = null,
    val priority: Short? = null

) : Config(id) {
    override fun encode(): ByteBuf {
        val data = Unpooled.buffer()
        frameLengths?.let { frameLengths -> frameIds?.let { frameIds ->
            if(frameLengths.size != frameIds.size) throw IOException("Frame lengths don't match frame ids.")
            data.writeOpcode(1)
            data.writeShort(frameLengths.size)
            frameLengths.forEach {
                data.writeShort(it)
            }
            frameIds.forEach {
                data.writeShort(it and 0xFFFF)
            }
            frameIds.forEach {
                data.writeShort(it shr 16)
            }
        } }
        frameStep?.let {
            data.writeOpcode(2)
            data.writeShort(it)
        }
        interleave?.let {
            data.writeOpcode(3)
            data.writeByte(it.size - 1)
            for(i in 0 until it.size - 1) {
                data.writeShort(it[i])
            }
        }
        if(stretches) data.writeOpcode(4)
        if(forcedPriority.toInt() != 5) {
            data.writeOpcode(5)
            data.writeByte(forcedPriority.toInt())
        }
        leftHandItem?.let {
            data.writeOpcode(6)
            data.writeShort(it)
        }
        rightHandItem?.let {
            data.writeOpcode(7)
            data.writeShort(it)
        }
        if(maxLoops.toInt() != 99) {
            data.writeOpcode(8)
            data.writeByte(maxLoops.toInt())
        }
        precedenceAnimating?.let {
            data.writeOpcode(9)
            data.writeByte(it.toInt())
        }
        priority?.let {
            data.writeOpcode(10)
            data.writeByte(it.toInt())
        }
        if(replyMode.toInt() != 2) {
            data.writeOpcode(11)
            data.writeByte(replyMode.toInt())
        }
        field3048?.let { field3048 ->
            data.writeOpcode(12)
            data.writeByte(field3048.size)
            field3048.forEach {
                data.writeShort(it and 0xFFFF)
            }
            field3048.forEach {
                data.writeShort(it shr 16)
            }
        }
        field3056?.let {
            data.writeOpcode(13)
            data.writeByte(field3056!!.size)
            field3056!!.forEach { id ->
                data.writeMedium(id)
            }
        }
        data.writeOpcode(0)
        return data
    }

    companion object : ConfigCompanion<SequenceConfig>() {
        override val id = 12

        override fun decode(id: Int, data: ByteBuf): SequenceConfig {
            var frameIds: IntArray? = null
            var field3048: IntArray? = null
            var frameLengths: IntArray? = null
            var interleave: IntArray? = null
            var stretches = false
            var forcedPriority: Short = 5
            var maxLoops: Short = 99
            var field3056: IntArray? = null
            var precedenceAnimating: Short? = null
            var leftHandItem: Int? = null
            var rightHandItem: Int? = null
            var replyMode: Short = 2
            var frameStep: Int? = null
            var priority: Short? = null

            decoder@ while (true) {
                when (val opcode = data.readUnsignedByte().toInt()) {
                    0 -> break@decoder
                    1 -> {
                        val length = data.readUnsignedShort()
                        frameLengths = IntArray(length) { data.readUnsignedShort() }
                        frameIds = IntArray(length) { data.readUnsignedShort() }
                        for(i in 0 until length) {
                            frameIds[i] += data.readUnsignedShort() shl 16
                        }
                    }
                    2 -> frameStep = data.readUnsignedShort()
                    3 -> {
                        val length = data.readUnsignedByte().toInt()
                        interleave = IntArray(length + 1)
                        for(i in 0 until length) {
                            interleave[i] = data.readUnsignedByte().toInt()
                        }
                        interleave[length] = 9999999
                    }
                    4 -> stretches = true
                    5 -> forcedPriority = data.readUnsignedByte()
                    6 -> leftHandItem = data.readUnsignedShort()
                    7 -> rightHandItem = data.readUnsignedShort()
                    8 -> maxLoops = data.readUnsignedByte()
                    9 -> precedenceAnimating = data.readUnsignedByte()
                    10 -> priority = data.readUnsignedByte()
                    11 -> replyMode = data.readUnsignedByte()
                    12 -> {
                        val length = data.readUnsignedByte().toInt()
                        val array = IntArray(length) { data.readUnsignedShort() }
                        for (i in 0 until length) {
                            array[i] += data.readUnsignedShort() shl 16
                        }
                        field3048 = array
                    }
                    13 -> {
                        val length = data.readUnsignedByte().toInt()
                        field3056 = IntArray(length) { data.readUnsignedMedium() }
                    }
                    else -> throw IOException("Did not recognise opcode $opcode.")
                }
            }
            precedenceAnimating?.let {
                precedenceAnimating = if(interleave != null) 2 else 0

            }
            priority?.let {
                priority = if(interleave != null) 2 else 0
            }
            return SequenceConfig(id, frameIds, field3048, frameLengths, interleave, stretches, forcedPriority,
                maxLoops, field3056, precedenceAnimating, leftHandItem, rightHandItem, replyMode, frameStep, priority
            )
        }
    }
}