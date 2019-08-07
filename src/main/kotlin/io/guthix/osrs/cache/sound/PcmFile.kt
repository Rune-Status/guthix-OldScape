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
import java.nio.ByteBuffer

class SoundEffect @ExperimentalUnsignedTypes constructor(
    val start: UShort,
    val end: UShort,
    val instruments: Array<AudioInstrument?>
) {

    companion object {
        private const val INSTRUMENT_COUNT = 10

        @ExperimentalUnsignedTypes
        fun decode(data: ByteArray): SoundEffect {
            val buffer = ByteBuffer.wrap(data)
            val instruments = arrayOfNulls<AudioInstrument>(INSTRUMENT_COUNT)
            for (i in 0 until INSTRUMENT_COUNT) {
                val volume = buffer.uByte.toInt()
                if (volume != 0) {
                    buffer.position(buffer.position() - 1)
                    instruments[i] = AudioInstrument.decode(buffer)
                }
            }
            val start = buffer.uShort
            val end = buffer.uShort
            return SoundEffect(start, end, instruments)
        }
    }
}

class AudioInstrument @ExperimentalUnsignedTypes constructor(
    val pitch: AudioEnvelope,
    val volume: AudioEnvelope,
    val pitchModifier: AudioEnvelope?,
    val pitchModifierAmplitude: AudioEnvelope?,
    val volumeMultiplier: AudioEnvelope?,
    val volumeMultiplierAmplitude: AudioEnvelope?,
    val release: AudioEnvelope?,
    val field1397: AudioEnvelope?,
    val oscillatorVolume: UShortArray,
    val oscillatorPitch: ShortArray,
    val oscillatorDelays: UShortArray,
    val delayTime: UShort,
    val delayDecay: UShort,
    val duration: UShort,
    val offset: UShort,
    val filterEnvelope: AudioEnvelope,
    val filter: AudioFilter
) {
    companion object {
        private const val OSCILLATOR_COUNT = 10

        @ExperimentalUnsignedTypes
        fun decode(buffer: ByteBuffer): AudioInstrument {
            val pitch = AudioEnvelope.decode(buffer)
            val volume = AudioEnvelope.decode(buffer)
            val (pitchModifier, pitchModifierAmplitude) = if (buffer.uByte.toInt() != 0) {
                buffer.position(buffer.position() - 1)
                Pair(
                    AudioEnvelope.decode(buffer),
                    AudioEnvelope.decode(buffer)
                )
            } else Pair(null, null)
            val (volumeMultiplier, volumeMultiplierAmplitude) = if (buffer.uByte.toInt() != 0) {
                buffer.position(buffer.position() - 1)
                Pair(
                    AudioEnvelope.decode(buffer),
                    AudioEnvelope.decode(buffer)
                )
            } else Pair(null, null)
            val (release, field1397) = if (buffer.uByte.toInt() != 0) {
                buffer.position(buffer.position() - 1)
                Pair(
                    AudioEnvelope.decode(buffer),
                    AudioEnvelope.decode(buffer)
                )
            } else Pair(null, null)
            val oscillatorVolume = UShortArray(OSCILLATOR_COUNT)
            val oscillatorPitch = ShortArray(OSCILLATOR_COUNT)
            val oscillatorDelays = UShortArray(OSCILLATOR_COUNT)
            for (i in 0 until OSCILLATOR_COUNT) {
                val oscVolume = buffer.smallUSmart
                if (oscVolume.toInt() == 0) {
                    break
                }
                oscillatorVolume[i] = oscVolume
                oscillatorPitch[i] = buffer.smallSmart
                oscillatorDelays[i] = buffer.smallUSmart
            }

            val delayTime = buffer.smallUSmart
            val delayDecay = buffer.smallUSmart
            val duration = buffer.uShort
            val offset = buffer.uShort
            val filterEnvelope = AudioEnvelope()
            val filter = AudioFilter.decode(buffer, filterEnvelope)
            return AudioInstrument(
                pitch, volume, pitchModifier, pitchModifierAmplitude, volumeMultiplier,
                volumeMultiplierAmplitude, release, field1397, oscillatorVolume, oscillatorPitch, oscillatorDelays,
                delayTime, delayDecay, duration, offset, filterEnvelope, filter
            )
        }
    }
}

@ExperimentalUnsignedTypes
class AudioEnvelope {
    var form: UByte? = null
    var start: Int? = null
    var end: Int? = null
    var durations: UShortArray? = null
    var phases: UShortArray? = null


    @ExperimentalUnsignedTypes
    fun decodeSegments(buffer: ByteBuffer): AudioEnvelope {
        val segmentCount = buffer.uByte.toInt()
        durations = UShortArray(segmentCount)
        phases = UShortArray(segmentCount)
        for (i in 0 until segmentCount) {
            durations!![i] = buffer.uShort
            phases!![i] = buffer.uShort
        }
        return this
    }

    companion object {
        @ExperimentalUnsignedTypes
        fun decode(buffer: ByteBuffer): AudioEnvelope {
            val audioEnvelope = AudioEnvelope()
            audioEnvelope.form = buffer.uByte
            audioEnvelope.start = buffer.int
            audioEnvelope.end = buffer.int
            return audioEnvelope.decodeSegments(buffer)
        }
    }
}

class AudioFilter @ExperimentalUnsignedTypes constructor(
    pairs: UShortArray,
    unity: UShortArray,
    phases: Array<Array<UShortArray>>,
    magnitudes: Array<Array<UShortArray>>
) {

    companion object {
        const val SIZE = 2

        @ExperimentalUnsignedTypes
        fun decode(buffer: ByteBuffer, audioEnvelope: AudioEnvelope): AudioFilter {
            val pair = buffer.uByte.toInt()
            val pairs = UShortArray(SIZE)
            pairs[0] = (pair shr 4).toUShort()
            pairs[1] = (pair and 0xF).toUShort()
            val phases = Array(SIZE) { Array(SIZE) { UShortArray(
                SIZE * 2) } }
            val magnitudes = Array(SIZE) { Array(SIZE) { UShortArray(
                SIZE * 2) } }
            val unity = UShortArray(SIZE)
            if (pair != 0) {
                unity[0] = buffer.uShort
                unity[1] = buffer.uShort
                val uByte1 = buffer.uByte.toInt()
                for(i in 0 until SIZE) {
                    for(j in 0 until pairs[i].toInt()) {
                        phases[i][0][j] = buffer.uShort
                        magnitudes[i][0][j] = buffer.uShort
                    }
                }

                for(i in 0 until SIZE) {
                    for(j in 0 until pairs[i].toInt()) {
                        if (uByte1 and (1 shl i * 4 shl j) != 0) {
                            phases[i][1][j] = buffer.uShort
                            magnitudes[i][1][j] = buffer.uShort
                        } else {
                            phases[i][1][j] = phases[i][0][j]
                            magnitudes[i][1][j] = magnitudes[i][0][j]
                        }
                    }
                }

                if (uByte1 != 0 || unity[1] != unity[0]) {
                    audioEnvelope.decodeSegments(buffer)
                }
            } else {
                unity[1] = 0u
            }
            return AudioFilter(pairs, unity, phases, magnitudes)
        }
    }
}
