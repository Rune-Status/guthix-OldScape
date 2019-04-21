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

import io.guthix.cache.fs.JagexCache
import io.guthix.osrs.cache.script.AssemblyScript
import java.io.File

class ScriptDictionary (
    val scripts: Map<Int, AssemblyScript>
)  {
    companion object  {
        const val id = 12

        @ExperimentalUnsignedTypes
        fun load(cache: JagexCache): ScriptDictionary {
            val scripts = mutableMapOf<Int, AssemblyScript>()
            cache.readArchives(id).forEach { (archiveId, archive) ->
                scripts[archiveId] = AssemblyScript.decode(archiveId, archive.files[0]!!.data) //TODO fix decoding
            }
            return ScriptDictionary(scripts)
        }
    }
}