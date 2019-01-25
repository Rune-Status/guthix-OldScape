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