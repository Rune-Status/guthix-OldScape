package io.github.bartvhelvert.jagex.fs.osrs.dict

import io.github.bartvhelvert.jagex.fs.Dictionary
import io.github.bartvhelvert.jagex.fs.DictionaryCompanion
import io.github.bartvhelvert.jagex.fs.JagexCache
import io.github.bartvhelvert.jagex.fs.osrs.plane.Widget

class WidgetDictionary(
    val interfaces: List<Widget>
) : Dictionary {

    companion object : DictionaryCompanion<WidgetDictionary>() {
        override val id = 3

        @ExperimentalUnsignedTypes
        fun load(cache: JagexCache): WidgetDictionary {
            val widgets = mutableListOf<Widget>()
            cache.readArchives(SpriteDictionary.id).forEach { archiveId, archive ->
                archive.files.forEach { fileId, file ->
                    val widgetId = (archiveId shl 16) + fileId
                    widgets.add(Widget.decode(widgetId, file.data)) //TODO fix decoding
                }
            }
            return WidgetDictionary(widgets)
        }
    }
}