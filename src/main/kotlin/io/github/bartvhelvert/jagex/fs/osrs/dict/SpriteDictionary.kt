package io.github.bartvhelvert.jagex.fs.osrs.dict

import io.github.bartvhelvert.jagex.fs.Dictionary
import io.github.bartvhelvert.jagex.fs.DictionaryCompanion
import io.github.bartvhelvert.jagex.fs.JagexCache
import io.github.bartvhelvert.jagex.fs.osrs.image.Sprite

class SpriteDictionary(
    val sprites: List<Sprite>
): Dictionary {
    companion object : DictionaryCompanion<SpriteDictionary>() {
        override val id = 8

        @ExperimentalUnsignedTypes
        fun load(cache: JagexCache): SpriteDictionary {
            val sprites = mutableListOf<Sprite>()
            cache.readArchives(id).forEach { _, archive ->
                archive.files.forEach { _, file ->
                    sprites.add(Sprite.decode(file.data))
                }
            }
            return SpriteDictionary(sprites)
        }
    }
}