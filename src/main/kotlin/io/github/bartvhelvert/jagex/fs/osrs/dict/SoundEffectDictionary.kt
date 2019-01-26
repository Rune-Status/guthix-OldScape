/*
GNU LGPL V3
Copyright (C) 2019 Bart van Helvert
B.A.J.v.Helvert@gmail.com

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with this program; if not, write to the Free Software Foundation,
Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package io.github.bartvhelvert.jagex.fs.osrs.dict

import io.github.bartvhelvert.jagex.fs.Dictionary
import io.github.bartvhelvert.jagex.fs.DictionaryCompanion
import io.github.bartvhelvert.jagex.fs.JagexCache
import io.github.bartvhelvert.jagex.fs.osrs.sound.SoundEffect

class SoundEffectDictionary(
    val soundEffects: List<SoundEffect>
) : Dictionary {
    companion object : DictionaryCompanion<SoundEffectDictionary>() {
        override val id = 4

        @ExperimentalUnsignedTypes
        fun load(cache: JagexCache): SoundEffectDictionary {
            val soundEffects = mutableListOf<SoundEffect>()
            cache.readArchives(id).forEach { _, archive ->
                archive.files.forEach { _, file ->
                    soundEffects.add(SoundEffect.decode(file.data)) //TODO fix the decoding
                }
            }
            return SoundEffectDictionary(soundEffects)
        }
    }
}
