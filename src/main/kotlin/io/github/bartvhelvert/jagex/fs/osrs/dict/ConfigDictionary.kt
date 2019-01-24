package io.github.bartvhelvert.jagex.fs.osrs.dict

import io.github.bartvhelvert.jagex.fs.Dictionary
import io.github.bartvhelvert.jagex.fs.DictionaryCompanion
import io.github.bartvhelvert.jagex.fs.JagexCache
import io.github.bartvhelvert.jagex.fs.osrs.config.*

data class ConfigDictionary(
    val areaConfigs: Map<Int, AreaConfig>,
    val enumConfigs: Map<Int, EnumConfig>,
    val hitBarConfig: Map<Int, HitBarConfig>,
    val hitMarkConfigss: Map<Int, HitMarkConfig>,
    val identKitConfigs: Map<Int, IdentKitConfig>,
    val invConfigs: Map<Int, InvConfig>,
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
                InvConfig.load(
                    cache.readArchive(
                        id,
                        InvConfig.id
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