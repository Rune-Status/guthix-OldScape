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

import io.guthix.cache.js5.Js5Archive
import io.guthix.osrs.cache.plane.Texture

class TextureArchive(val textures: List<Texture>) {
    companion object {
        const val id = 9

        fun load(archive: Js5Archive): TextureArchive {
            val textures = mutableListOf<Texture>()
            archive.groupSettings.forEach { (groupId, _) ->
                val group = archive.readGroup(groupId)
                group.files.forEach { (fileId, file) ->
                    textures.add(Texture.decode(fileId, file.data))
                }
            }
            return TextureArchive(textures)
        }
    }
}