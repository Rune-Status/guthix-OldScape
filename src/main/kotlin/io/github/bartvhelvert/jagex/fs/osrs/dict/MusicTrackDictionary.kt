package io.github.bartvhelvert.jagex.fs.osrs.dict

import io.github.bartvhelvert.jagex.fs.Dictionary
import io.github.bartvhelvert.jagex.fs.DictionaryCompanion
import io.github.bartvhelvert.jagex.fs.JagexCache
import io.github.bartvhelvert.jagex.fs.osrs.sound.MidiFile

class MusicTrackDictionary(
    val tracks: List<MidiFile>
): Dictionary {

    companion object : DictionaryCompanion<MusicTrackDictionary>() {
        override val id = 6

        @ExperimentalUnsignedTypes
        override fun load(cache: JagexCache): MusicTrackDictionary {
            val tracks = mutableListOf<MidiFile>()
            cache.readArchives(id).forEach { _, archive ->
                archive.files.forEach { _, file ->
                    tracks.add(MidiFile.decode(file.data))
                }
            }
            return MusicTrackDictionary(tracks)
        }
    }
}

