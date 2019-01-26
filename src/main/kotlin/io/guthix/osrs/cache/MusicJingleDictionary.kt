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
package io.guthix.osrs.cache

import io.guthix.cache.fs.Dictionary
import io.guthix.cache.fs.DictionaryCompanion
import io.guthix.cache.fs.JagexCache
import io.guthix.osrs.cache.sound.MidiFile

class MusicJingleDictionary(
    val tracks: List<MidiFile>
) : Dictionary {
    companion object : DictionaryCompanion<MusicTrackDictionary>() {
        override val id = 11

        @ExperimentalUnsignedTypes
        fun load(cache: JagexCache): MusicTrackDictionary {
            val tracks = mutableListOf<MidiFile>()
            cache.readArchives(MusicTrackDictionary.id).forEach { _, archive ->
                archive.files.forEach { _, file ->
                    tracks.add(MidiFile.decode(file.data))
                }
            }
            return MusicTrackDictionary(tracks)
        }
    }
}