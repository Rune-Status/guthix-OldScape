package io.github.bartvhelvert.jagex.fs.osrs.dict

import io.github.bartvhelvert.jagex.fs.Dictionary
import io.github.bartvhelvert.jagex.fs.DictionaryCompanion
import io.github.bartvhelvert.jagex.fs.JagexCache
import io.github.bartvhelvert.jagex.fs.osrs.sound.MidiFile

class MusicJingleDictionary(
    val tracks: List<MidiFile>
): Dictionary {
    companion object : DictionaryCompanion<MusicTrackDictionary>() {
        override val id = 11

        @ExperimentalUnsignedTypes
        override fun load(cache: JagexCache): MusicTrackDictionary {
            val tracks = mutableListOf<MidiFile>()
            cache.readContainers(id).forEach { id, container ->
                tracks.add(MidiFile.decode(container.data))
            }
            return MusicTrackDictionary(tracks)
        }
    }
}