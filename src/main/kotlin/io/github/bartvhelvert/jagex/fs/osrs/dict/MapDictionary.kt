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
package io.github.bartvhelvert.jagex.fs.osrs.dict

import io.github.bartvhelvert.jagex.fs.Dictionary
import io.github.bartvhelvert.jagex.fs.DictionaryCompanion
import io.github.bartvhelvert.jagex.fs.JagexCache
import io.github.bartvhelvert.jagex.fs.osrs.map.Region
import io.github.bartvhelvert.jagex.fs.osrs.xtea.MapXtea
import java.io.IOException

class MapDictionary (
    val regions: Map<Int, Region>
) : Dictionary {
    companion object : DictionaryCompanion<MapDictionary>() {
        override val id = 5

        @ExperimentalUnsignedTypes
        fun load(cache: JagexCache, xteas: List<MapXtea>): MapDictionary {
            val regions = mutableMapOf<Int, Region>()
            xteas.forEach {
                val landData = cache.readArchive(id, "m${it.x}_${it.y}")
                val mapData = cache.readArchive(id, "l${it.x}_${it.y}", it.key)
                if(landData.files.size != 1 || mapData.files.size != 1) {
                    throw IOException("Map archive has ${landData.files.size} files but can only have 1")
                }
                regions[it.id] = Region.decode(landData.files[0]!!.data, mapData.files[0]!!.data, it.x, it.y)
            }
            return MapDictionary(regions)
        }
    }
}