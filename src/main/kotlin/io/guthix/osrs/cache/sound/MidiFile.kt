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
package io.guthix.osrs.cache.sound

import io.guthix.cache.js5.io.*
import java.io.IOException
import java.nio.ByteBuffer

class MidiFile(
    val midi: ByteArray
) {
    companion object {

        // Headers
        private const val MTHD_MAGIC = 1297377380
        private const val MTRK_MAGIC = 1297379947

        // Major MIDI Messages. Bottom 4 bits are the channel.
        private const val NOTE_ON = 144
        private const val NOTE_OFF = 128
        private const val CONTROL_CHANGE = 176
        private const val PITCH_WHEEL_CHANGE = 224
        private const val CHANNEL_PRESSURE = 208
        private const val POLYPHONIC_KEY_PRESSURE = 160
        private const val PROGRAM_CHANGE = 192

        // Meta Events
        private const val META = 255
        private const val END_OF_TRACK = 47
        private const val TEMPO = 81

        private const val JAG_NOTE_ON = 0
        private const val JAG_NOTE_OFF = 1
        private const val JAG_CONTROL_CHANGE = 2
        private const val JAG_PITCH_BEND = 3
        private const val JAG_CHANNEL_PRESSURE = 4
        private const val JAG_POLY_PRESSURE = 5
        private const val JAG_PROGRAM_CHANGE = 6
        private const val JAG_END_OF_TRACK = 7
        private const val JAG_TEMPO = 23

        // Controller messages
        private const val CONTROLLER_BANK_SELECT = 0
        private const val CONTROLLER_MODULATION_WHEEL = 1
        private const val CONTROLLER_CHANNEL_VOLUME = 7
        private const val CONTROLLER_PAN = 10
        private const val CONTROLLER_BANK_SELECT_2 = 32
        private const val CONTROLLER_MODULATION_WHEEL2 = 33
        private const val CONTROLLER_CHANNEL_VOLUME_2 = 39
        private const val CONTROLLER_PAN_2 = 42
        private const val CONTROLLER_DAMPER_PEDAL = 64
        private const val CONTROLLER_PORTAMENTO = 65
        private const val CONTROLLER_NON_REGISTERED_PARAMETER_NUMBER_LSB = 98
        private const val CONTROLLER_NON_REGISTERED_PARAMETER_NUMBER_MSB = 99
        private const val CONTROLLER_REGISTERED_PARAMETER_NUMBER_LSB = 100
        private const val CONTROLLER_REGISTERED_PARAMETER_NUMBER_MSB = 101
        private const val CONTROLLER_ALL_SOUND_OFF = 120
        private const val CONTROLLER_RESET_ALL_CONTROLLERS = 121
        private const val CONTROLLER_ALL_NOTES_OFF = 123

        @ExperimentalUnsignedTypes
        fun decode(data: ByteArray): MidiFile {
            val buffer = ByteBuffer.wrap(data)
            buffer.position(buffer.limit() - 3)
            val tracks = buffer.uByte.toInt()
            val division = buffer.uShort.toInt()
            var offset = 14 + tracks * 10
            buffer.position(0)
            var tempoOpcodes = 0
            var ctrlChangeOpcodes = 0
            var noteOnOpcodes = 0
            var noteOffOpcodes = 0
            var wheelChangeOpcodes = 0
            var chnnlAfterTchOpcodes = 0
            var keyAfterTchOpcodes = 0
            var progmChangeOpcodes = 0
            for(track in 0 until tracks) {
                var opcode = -1
                while (true) {
                    val controlChangeIndex = buffer.uByte.toInt()
                    if (controlChangeIndex != opcode) offset++
                    opcode = controlChangeIndex and 0xF
                    if (controlChangeIndex == JAG_END_OF_TRACK) break
                    when {
                        controlChangeIndex == JAG_TEMPO -> tempoOpcodes++
                        opcode == JAG_NOTE_ON -> noteOnOpcodes++
                        opcode == JAG_NOTE_OFF -> noteOffOpcodes++
                        opcode == JAG_CONTROL_CHANGE -> ctrlChangeOpcodes++
                        opcode == JAG_PITCH_BEND -> wheelChangeOpcodes++
                        opcode == JAG_CHANNEL_PRESSURE -> chnnlAfterTchOpcodes++
                        opcode == JAG_POLY_PRESSURE -> keyAfterTchOpcodes++
                        opcode == JAG_PROGRAM_CHANGE -> progmChangeOpcodes++
                        else -> throw IOException("Track opcode $opcode not implemented.")
                    }
                }
            }

            offset += 5 * tempoOpcodes
            offset += 2 * (noteOnOpcodes + noteOffOpcodes + ctrlChangeOpcodes + wheelChangeOpcodes + keyAfterTchOpcodes)
            offset += chnnlAfterTchOpcodes + progmChangeOpcodes
            val marker1 = buffer.position()
            val opcode = tracks + tempoOpcodes + ctrlChangeOpcodes + noteOnOpcodes + noteOffOpcodes +
                    wheelChangeOpcodes + chnnlAfterTchOpcodes + keyAfterTchOpcodes + progmChangeOpcodes

            for(i in 0 until opcode) {
                buffer.varInt
            }
            offset += buffer.position() - marker1
            var controlChangeIndex = buffer.position()
            var modulationWheelSize = 0
            var modulationWheel2Size = 0
            var channelVolumeSize = 0
            var channelVolume2Size = 0
            var panSize = 0
            var pan2Size = 0
            var nonRegisteredMsbSize = 0
            var nonRegisteredLsbSize = 0
            var registeredNumberMsb = 0
            var registeredLsbSize = 0
            var commandsSize = 0
            var otherSize = 0
            var controllerNumber = 0
            for(i in 0 until ctrlChangeOpcodes) {
                controllerNumber = controllerNumber + buffer.get() and Byte.MAX_VALUE.toInt()
                if (controllerNumber == CONTROLLER_BANK_SELECT || controllerNumber == CONTROLLER_BANK_SELECT_2) {
                    progmChangeOpcodes++
                } else if (controllerNumber == CONTROLLER_MODULATION_WHEEL) {
                    modulationWheelSize++
                } else if (controllerNumber == CONTROLLER_MODULATION_WHEEL2) {
                    modulationWheel2Size++
                } else if (controllerNumber == CONTROLLER_CHANNEL_VOLUME) {
                    channelVolumeSize++
                } else if (controllerNumber == CONTROLLER_CHANNEL_VOLUME_2) {
                    channelVolume2Size++
                } else if (controllerNumber == CONTROLLER_PAN) {
                    panSize++
                } else if (controllerNumber == CONTROLLER_PAN_2) {
                    pan2Size++
                } else if (controllerNumber == CONTROLLER_NON_REGISTERED_PARAMETER_NUMBER_MSB) {
                    nonRegisteredMsbSize++
                } else if (controllerNumber == CONTROLLER_NON_REGISTERED_PARAMETER_NUMBER_LSB) {
                    nonRegisteredLsbSize++
                } else if (controllerNumber == CONTROLLER_REGISTERED_PARAMETER_NUMBER_MSB) {
                    registeredNumberMsb++
                } else if (controllerNumber == CONTROLLER_REGISTERED_PARAMETER_NUMBER_LSB) {
                    registeredLsbSize++
                } else if (controllerNumber != CONTROLLER_DAMPER_PEDAL
                    && controllerNumber != CONTROLLER_PORTAMENTO
                    && controllerNumber != CONTROLLER_ALL_SOUND_OFF
                    && controllerNumber != CONTROLLER_RESET_ALL_CONTROLLERS
                    && controllerNumber != CONTROLLER_ALL_NOTES_OFF
                ) {
                    otherSize++
                } else {
                    commandsSize++
                }
            }

            var commandsIndex = buffer.position()
            buffer.skip(commandsSize)
            var polyPressureIndex = buffer.position()
            buffer.skip(keyAfterTchOpcodes)
            var channelPressureIndex = buffer.position()
            buffer.skip(chnnlAfterTchOpcodes)
            var pitchWheelHighIndex = buffer.position()
            buffer.skip(wheelChangeOpcodes)
            var modulationWheelOffset = buffer.position()
            buffer.skip(modulationWheelSize)
            var channelVolumeOffset = buffer.position()
            buffer.skip(channelVolumeSize)
            var panOffset = buffer.position()
            buffer.skip(panSize)
            var notesIndex = buffer.position()
            buffer.skip(noteOnOpcodes + noteOffOpcodes + keyAfterTchOpcodes)
            var notesOnIndex = buffer.position()
            buffer.skip(noteOnOpcodes)
            var otherIndex = buffer.position()
            buffer.skip(otherSize)
            var notesOffIndex = buffer.position()
            buffer.skip(noteOffOpcodes)
            var modulationWheel2Offset = buffer.position()
            buffer.skip(modulationWheel2Size)
            var channelVolume2Offset = buffer.position()
            buffer.skip(channelVolume2Size)
            var pan2Offset = buffer.position()
            buffer.skip(pan2Size)
            var programChangeIndex = buffer.position()
            buffer.skip(progmChangeOpcodes)
            var pitchWheelLowIndex = buffer.position()
            buffer.skip(wheelChangeOpcodes)
            var nonRegisteredMsbIndex = buffer.position()
            buffer.skip(nonRegisteredMsbSize)
            var nonRegisteredLsbIndex = buffer.position()
            buffer.skip(nonRegisteredLsbSize)
            var registeredMsbIndex = buffer.position()
            buffer.skip(registeredNumberMsb)
            var registeredLsbIndex = buffer.position()
            buffer.skip(registeredLsbSize)
            var tempoOffset = buffer.position()
            buffer.skip((tempoOpcodes * 3))

            val midiBuff = ByteBuffer.allocate(offset + 1)
            midiBuff.putInt(MTHD_MAGIC) // MThd header
            midiBuff.putInt(6) // length of header
            midiBuff.putShort(if (tracks > 1) 1 else 0) // format
            midiBuff.putShort(tracks.toShort()) // tracks
            midiBuff.putShort(division.toShort()) // division
            buffer.position(marker1)
            var var52 = 0
            var var53 = 0
            var var54 = 0
            var var55 = 0
            var var56 = 0
            var var57 = 0
            var var58 = 0
            val var59 = IntArray(128)
            controllerNumber  = 0
            var var29 = 0
            writer@ for (var60 in 0 until tracks) {
                midiBuff.putInt(MTRK_MAGIC)
                midiBuff.skip(4) // length gets written here later
                val var61 = midiBuff.position()
                var curJagOpcode = -1

                while (true) {
                    val deltaTick = buffer.varInt
                    midiBuff.putVarInt(deltaTick)
                    val status = buffer.array()[var29++].toInt() and UByte.MAX_VALUE.toInt()
                    val shouldWriteOpcode = status != curJagOpcode
                    curJagOpcode = status and 0xF
                    if (status == JAG_END_OF_TRACK) {
                        midiBuff.put(META.toByte())
                        midiBuff.put(END_OF_TRACK.toByte()) // type - end of track
                        midiBuff.put(0.toByte()) // length
                        midiBuff.writeLength(midiBuff.position() - var61)
                        continue@writer
                    }

                    if (status == JAG_TEMPO) {
                        midiBuff.put(META.toByte()) // meta event FF
                        midiBuff.put(TEMPO.toByte()) // type - set tempo
                        midiBuff.put(3.toByte()) // length
                        midiBuff.put(buffer.array()[tempoOffset++])
                        midiBuff.put(buffer.array()[tempoOffset++])
                        midiBuff.put(buffer.array()[tempoOffset++])
                    } else {
                        var52 = var52 xor (status shr 4)
                        when(curJagOpcode) {
                            JAG_NOTE_ON -> {
                                if (shouldWriteOpcode) midiBuff.put((NOTE_ON + var52).toByte())
                                var53 += buffer.array()[notesIndex++].toInt()
                                var54 += buffer.array()[notesOnIndex++].toInt()
                                midiBuff.put((var53 and Byte.MAX_VALUE.toInt()).toByte())
                                midiBuff.put((var54 and Byte.MAX_VALUE.toInt()).toByte())
                            }
                            JAG_NOTE_OFF -> {
                                if (shouldWriteOpcode) midiBuff.put((NOTE_OFF + var52).toByte())
                                var53 += buffer.array()[notesIndex++].toInt()
                                var55 += buffer.array()[notesOffIndex++].toInt()
                                midiBuff.put((var53 and Byte.MAX_VALUE.toInt()).toByte())
                                midiBuff.put((var55 and Byte.MAX_VALUE.toInt()).toByte())
                            }
                            JAG_CONTROL_CHANGE -> {
                                if (shouldWriteOpcode) {
                                    midiBuff.put((CONTROL_CHANGE + var52).toByte())
                                }
                                controllerNumber = controllerNumber + buffer.array()[controlChangeIndex++] and
                                        Byte.MAX_VALUE.toInt()
                                midiBuff.put(controllerNumber.toByte())
                                val result = if(
                                    controllerNumber == CONTROLLER_BANK_SELECT
                                    || controllerNumber == CONTROLLER_BANK_SELECT_2
                                ) {
                                    buffer.array()[programChangeIndex++]
                                } else if (controllerNumber == CONTROLLER_MODULATION_WHEEL) {
                                    buffer.array()[modulationWheelOffset++]
                                } else if (controllerNumber == CONTROLLER_MODULATION_WHEEL2) {
                                    buffer.array()[modulationWheel2Offset++]
                                } else if (controllerNumber == CONTROLLER_CHANNEL_VOLUME) {
                                    buffer.array()[channelVolumeOffset++]
                                } else if (controllerNumber == CONTROLLER_CHANNEL_VOLUME_2) {
                                    buffer.array()[channelVolume2Offset++]
                                } else if (controllerNumber == CONTROLLER_PAN) {
                                    buffer.array()[panOffset++]
                                } else if (controllerNumber == CONTROLLER_PAN_2) {
                                    buffer.array()[pan2Offset++]
                                } else if (controllerNumber == CONTROLLER_NON_REGISTERED_PARAMETER_NUMBER_MSB) {
                                    buffer.array()[nonRegisteredMsbIndex++]
                                } else if (controllerNumber == CONTROLLER_NON_REGISTERED_PARAMETER_NUMBER_LSB) {
                                    buffer.array()[nonRegisteredLsbIndex++]
                                } else if (controllerNumber == CONTROLLER_REGISTERED_PARAMETER_NUMBER_MSB) {
                                    buffer.array()[registeredMsbIndex++]
                                } else if (controllerNumber == CONTROLLER_REGISTERED_PARAMETER_NUMBER_LSB) {
                                    buffer.array()[registeredLsbIndex++]
                                } else if (
                                    controllerNumber != CONTROLLER_DAMPER_PEDAL &&
                                    controllerNumber != CONTROLLER_PORTAMENTO &&
                                    controllerNumber != CONTROLLER_ALL_SOUND_OFF &&
                                    controllerNumber != CONTROLLER_RESET_ALL_CONTROLLERS &&
                                    controllerNumber != CONTROLLER_ALL_NOTES_OFF
                                ) {
                                     buffer.array()[otherIndex++]
                                } else {
                                    buffer.array()[commandsIndex++]
                                }

                                val var67 = result + var59[controllerNumber]
                                var59[controllerNumber] = var67
                                midiBuff.put((var67 and Byte.MAX_VALUE.toInt()).toByte())
                            }
                            JAG_PITCH_BEND -> {
                                if (shouldWriteOpcode)  midiBuff.put((PITCH_WHEEL_CHANGE + var52).toByte())
                                var56 += buffer.array()[pitchWheelLowIndex++].toInt()
                                var56 += buffer.array()[pitchWheelHighIndex++].toInt() shl 7
                                midiBuff.put((var56 and Byte.MAX_VALUE.toInt()).toByte())
                                midiBuff.put((var56 shr 7 and Byte.MAX_VALUE.toInt()).toByte())
                            }
                            JAG_CHANNEL_PRESSURE -> {
                                if (shouldWriteOpcode) midiBuff.put((CHANNEL_PRESSURE + var52).toByte())
                                var57 += buffer.array()[channelPressureIndex++].toInt()
                                midiBuff.put((var57 and Byte.MAX_VALUE.toInt()).toByte())
                            }
                            JAG_POLY_PRESSURE -> {
                                if (shouldWriteOpcode) midiBuff.put((POLYPHONIC_KEY_PRESSURE + var52).toByte())
                                var53 += buffer.array()[notesIndex++].toInt()
                                var58 += buffer.array()[polyPressureIndex++].toInt()
                                midiBuff.put((var53 and Byte.MAX_VALUE.toInt()).toByte())
                                midiBuff.put((var58 and Byte.MAX_VALUE.toInt()).toByte())
                            }
                            JAG_PROGRAM_CHANGE -> {
                                if (shouldWriteOpcode) midiBuff.put((PROGRAM_CHANGE + var52).toByte())
                                midiBuff.put(buffer.array()[programChangeIndex++])
                            } else -> throw IOException("Did not recognise jag track opcode $curJagOpcode.")
                        }
                    }
                }
            }
            return MidiFile(midiBuff.array())
        }

        private fun ByteBuffer.writeLength(length: Int) {
            val pos = position()
            position(pos - length - 4)
            putInt(length)
            position(pos)
        }
    }
}