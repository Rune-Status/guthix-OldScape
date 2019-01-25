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
