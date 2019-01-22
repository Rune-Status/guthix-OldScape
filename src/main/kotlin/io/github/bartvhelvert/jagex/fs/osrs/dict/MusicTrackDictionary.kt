package io.github.bartvhelvert.jagex.fs.osrs.dict

import io.github.bartvhelvert.jagex.fs.Dictionary
import io.github.bartvhelvert.jagex.fs.DictionaryCompanion
import io.github.bartvhelvert.jagex.fs.JagexCache
import io.github.bartvhelvert.jagex.fs.osrs.SoundFile

class MusicTrackDictionary(
    val tracks: List<SoundFile>
): Dictionary {

    companion object : DictionaryCompanion<MusicTrackDictionary>() {
        override val id = 6

        @ExperimentalUnsignedTypes
        override fun load(cache: JagexCache): MusicTrackDictionary {
            val tracks = mutableListOf<SoundFile>()
            cache.readContainers(id).forEach { id, container ->
                tracks.add(SoundFile.decode(container.data))
            }
            return MusicTrackDictionary(tracks)
        }
    }
}

