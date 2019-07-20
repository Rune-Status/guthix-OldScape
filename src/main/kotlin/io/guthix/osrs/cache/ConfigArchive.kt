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

import io.guthix.cache.js5.Js5Cache
import io.guthix.osrs.cache.config.*

@ExperimentalUnsignedTypes
data class ConfigArchive constructor(
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
    val varClientConfigs: Map<Int, VarClientConfig>,
    val varPlayerConfigs: Map<Int, VarPlayerConfig>
) {
    companion object {
        const val id = 2

        @ExperimentalUnsignedTypes
        fun load(cache: Js5Cache): ConfigArchive =
            ConfigArchive(
                AreaConfig.load(cache.readGroup(id, AreaConfig.id)),
                EnumConfig.load(cache.readGroup(id, EnumConfig.id)),
                HitBarConfig.load(cache.readGroup(id, HitBarConfig.id)),
                HitMarkConfig.load(cache.readGroup(id, HitMarkConfig.id)),
                IdentKitConfig.load(cache.readGroup(id, IdentKitConfig.id)),
                InventoryConfig.load(cache.readGroup(id, InventoryConfig.id)),
                ItemConfig.load(cache.readGroup(id, ItemConfig.id)),
                NpcConfig.load(cache.readGroup(id, NpcConfig.id)),
                ObjectConfig.load(cache.readGroup(id, ObjectConfig.id)),
                OverlayConfig.load(cache.readGroup(id, OverlayConfig.id)),
                ParamConfig.load(cache.readGroup(id, ParamConfig.id)),
                SequenceConfig.load(cache.readGroup(id, SequenceConfig.id)),
                SpotAnimConfig.load(cache.readGroup(id, SpotAnimConfig.id)),
                StructConfig.load(cache.readGroup(id, StructConfig.id)),
                UnderlayConfig.load(cache.readGroup(id, UnderlayConfig.id)),
                VarbitConfig.load(cache.readGroup(id, VarbitConfig.id)),
                VarClientConfig.load(cache.readGroup(id, VarClientConfig.id)),
                VarPlayerConfig.load(cache.readGroup(id, VarPlayerConfig.id))
            )
    }
}