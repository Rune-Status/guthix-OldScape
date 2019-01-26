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
import io.github.bartvhelvert.jagex.fs.osrs.config.*

data class ConfigDictionary @ExperimentalUnsignedTypes constructor(
    val areaConfigs: Map<Int, AreaConfig>,
    val enumConfigs: Map<Int, EnumConfig>,
    val hitBarConfig: Map<Int, HitBarConfig>,
    val hitMarkConfigss: Map<Int, HitMarkConfig>,
    val identKitConfigs: Map<Int, IdentKitConfig>,
    val inventoryConfigs: Map<Int, InventoryConfig>,
    val itemConfigs: Map<Int, ItemConfig>,
    val npcConfigs: Map<Int, NpcConfig>,
    val objectConfigs: Map<Int, ObjectConfig>,
    val overlayConfigs: Map<Int, OverlayConfig>,
    val paramConfigs: Map<Int, ParamConfig>,
    val sequenceConfigs: Map<Int, SequenceConfig>,
    val spotAnimConfigs: Map<Int, SpotAnimConfig>,
    val structConfigs: Map<Int, StructConfig>,
    val underlayConfigs: Map<Int, UnderlayConfig>,
    val varbitConfigs: Map<Int, VarbitConfig>,
    val varClientIntConfigs: Map<Int, VarClientIntConfig>,
    val varClientStringConfigs: Map<Int, VarClientStringConfig>,
    val varPlayerConfigs: Map<Int, VarPlayerConfig>
) : Dictionary {
    companion object : DictionaryCompanion<ConfigDictionary>() {
        override val id = 2

        @ExperimentalUnsignedTypes
        fun load(cache: JagexCache): ConfigDictionary =
            ConfigDictionary(
                AreaConfig.load(
                    cache.readArchive(
                        id,
                        AreaConfig.id
                    )
                ),
                EnumConfig.load(
                    cache.readArchive(
                        id,
                        EnumConfig.id
                    )
                ),
                HitBarConfig.load(
                    cache.readArchive(
                        id,
                        HitBarConfig.id
                    )
                ),
                HitMarkConfig.load(
                    cache.readArchive(
                        id,
                        HitMarkConfig.id
                    )
                ),
                IdentKitConfig.load(
                    cache.readArchive(
                        id,
                        IdentKitConfig.id
                    )
                ),
                InventoryConfig.load(
                    cache.readArchive(
                        id,
                        InventoryConfig.id
                    )
                ),
                ItemConfig.load(
                    cache.readArchive(
                        id,
                        ItemConfig.id
                    )
                ),
                NpcConfig.load(
                    cache.readArchive(
                        id,
                        NpcConfig.id
                    )
                ),
                ObjectConfig.load(
                    cache.readArchive(
                        id,
                        ObjectConfig.id
                    )
                ),
                OverlayConfig.load(
                    cache.readArchive(
                        id,
                        OverlayConfig.id
                    )
                ),
                ParamConfig.load(
                    cache.readArchive(
                        id,
                        ParamConfig.id
                    )
                ),
                SequenceConfig.load(
                    cache.readArchive(
                        id,
                        SequenceConfig.id
                    )
                ),
                SpotAnimConfig.load(
                    cache.readArchive(
                        id,
                        SpotAnimConfig.id
                    )
                ),
                StructConfig.load(
                    cache.readArchive(
                        id,
                        StructConfig.id
                    )
                ),
                UnderlayConfig.load(
                    cache.readArchive(
                        id,
                        UnderlayConfig.id
                    )
                ),
                VarbitConfig.load(
                    cache.readArchive(
                        id,
                        VarbitConfig.id
                    )
                ),
                VarClientIntConfig.load(
                    cache.readArchive(
                        id,
                        VarClientIntConfig.id
                    )
                ),
                VarClientStringConfig.load(
                    cache.readArchive(
                        id,
                        VarClientStringConfig.id
                    )
                ),
                VarPlayerConfig.load(
                    cache.readArchive(
                        id,
                        VarPlayerConfig.id
                    )
                )
            )
    }
}