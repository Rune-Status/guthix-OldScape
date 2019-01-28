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
package io.guthix.osrs.cache

import io.guthix.cache.fs.Dictionary
import io.guthix.cache.fs.DictionaryCompanion
import io.guthix.cache.fs.JagexCache
import io.guthix.osrs.cache.plane.Texture

class TextureDictionary(
    val textures: List<Texture>
) : Dictionary {
    companion object : DictionaryCompanion<TextureDictionary>() {
        override val id = 9

        @ExperimentalUnsignedTypes
        fun load(cache: JagexCache): TextureDictionary {
            val textures = mutableListOf<Texture>()
            cache.readArchives(id).forEach { _, archive ->
                archive.files.forEach { _, file ->
                    textures.add(Texture.decode(file.data))
                }
            }
            return TextureDictionary(textures)
        }
    }
}