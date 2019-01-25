package io.github.bartvhelvert.jagex.fs.osrs.dict

import io.github.bartvhelvert.jagex.fs.Dictionary
import io.github.bartvhelvert.jagex.fs.DictionaryCompanion
import io.github.bartvhelvert.jagex.fs.JagexCache
import io.github.bartvhelvert.jagex.fs.osrs.image.Texture

class TextureDictionary(
    val textures: List<Texture>
) : Dictionary {
    companion object : DictionaryCompanion<TextureDictionary>() {
        override val id = 9

        @ExperimentalUnsignedTypes
        fun load(cache: JagexCache): TextureDictionary {
            val textures = mutableListOf<Texture>()
            cache.readArchives(TextureDictionary.id).forEach { _, archive ->
                archive.files.forEach { _, file ->
                    textures.add(Texture.decode(file.data))
                }
            }
            return TextureDictionary(textures)
        }
    }
}