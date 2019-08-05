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
package io.guthix.osrs.cache.plane

import io.guthix.cache.js5.io.*
import java.nio.ByteBuffer

@ExperimentalUnsignedTypes
data class Component(val id: Int) {
    var hasScript = false
    var menuType = 0
    var contentType: UShort = 0u
    var originalX: Short = 0
    var originalY: Short = 0
    var originalWidth = 0
    var originalHeight = 0
    var opacity: UByte = 0u
    var parentId: Int? = null
    var hoveredSiblingId: UShort? = null
    var cs1InstructionCount: Array<Array<UShort?>>? = null
    var scrollHeight: UShort = 0u
    var scrollWidth: UShort = 0u
    var isHidden: Boolean = false
    var itemIds: IntArray? = null
    var itemQuantities: IntArray? = null
    var clickMask = 0
    var xPitch: Short = 0
    var yPitch: Short = 0
    var xOffsets: ShortArray? = null
    var yOffsets: ShortArray? = null
    var sprites: Array<Int?>? = null
    var configActions: Array<String?>? = null
    var isFilled = false
    var xTextAlignment: UByte = 0u
    var yTextAlignment: UByte = 0u
    var lineHeight: UByte = 0u
    var fontId: UShort? = null
    var textIsShadowed = false
    var text = ""
    var alternateText = ""
    var textColor = 0
    var alternateTextColor = 0
    var hoveredTextColor = 0
    var alternateHoveredTextColor = 0
    var spriteId: Int? = null
    var alternateSpriteId: Int? = null
    var modelType = 1
    var modelId: UShort? = null
    var alternateModelId: UShort? = null
    var animationId: UShort? = null
    var alternateAnimationId: UShort? = null
    var modelZoom: UShort = 100u
    var rotationX: UShort = 0u
    var rotationY: UShort = 0u
    var rotationZ: UShort = 0u
    var targetVerb = ""
    var spellName = ""
    var tooltip = "Ok"
    var dynamicWidth: Byte = 0
    var buttonType: Byte = 0
    var dynamicX: Byte = 0
    var dynamicY: Byte = 0
    var noClickThrough = false
    var textureId: UShort = 0u
    var spriteTiling = false
    var borderThickness: UByte = 0u
    var sprite2 = 0
    var flippedVertically: Boolean? = null
    var flippedHorizontally: Boolean? = null
    var offsetX2d: Short = 0
    var offsetY2d: Short = 0
    var orthogonal = false
    var modelHeightOverride: UShort = 0u
    var lineWidth: UByte = 0u
    var lineDirection = false
    var opBase: String = ""
    var actions: Array<String?>? = null
    var dragDeadZone: UByte = 0u
    var dragDeadTime: UByte = 0u
    var dragRenderBehavior = false
    var onLoadListener: Array<Any?>? = null
    var onMouseOverListener: Array<Any?>? = null
    var onMouseLeaveListener: Array<Any?>? = null
    var onTargetLeaveListener: Array<Any?>? = null
    var onTargetEnterListener: Array<Any?>? = null
    var onVarTransmitListener: Array<Any?>? = null
    var onInvTransmitListener: Array<Any?>? = null
    var onStatTransmitListener: Array<Any?>? = null
    var onTimerListener: Array<Any?>? = null
    var onOpListener: Array<Any?>? = null
    var onMouseRepeatListener: Array<Any?>? = null
    var onClickListener: Array<Any?>? = null
    var onClickRepeatListener: Array<Any?>? = null
    var onReleaseListener: Array<Any?>? = null
    var onHoldListener: Array<Any?>? = null
    var onDragListener: Array<Any?>? = null
    var onDragCompleteListener: Array<Any?>? = null
    var onScrollWheelListener: Array<Any?>? = null
    var varTransmitTriggers: IntArray? = null
    var invTransmitTriggers: IntArray? = null
    var statTransmitTriggers: IntArray? = null

    companion object {
        @ExperimentalUnsignedTypes
        fun decode(id: Int, data: ByteArray): Component {
            val buffer = ByteBuffer.wrap(data)
            return when(buffer.peak().toInt()) {
                -1 -> decodeIf3(id, buffer)
                else -> decodeIf1(id, buffer)
            }
        }

        @ExperimentalUnsignedTypes
        private fun decodeIf1(id: Int, buffer: ByteBuffer): Component {
            val iFace = Component(id)
            iFace.hasScript = false
            val type = buffer.uByte.toInt()
            iFace.menuType = buffer.uByte.toInt()
            iFace.contentType = buffer.uShort
            iFace.originalX = buffer.short
            iFace.originalY = buffer.short
            iFace.originalWidth = buffer.uShort.toInt()
            iFace.originalHeight = buffer.uShort.toInt()
            iFace.opacity = buffer.uByte
            iFace.parentId = buffer.uShort.toInt()
            iFace.parentId = if(iFace.parentId == UShort.MAX_VALUE.toInt()) {
                null
            } else {
                iFace.parentId!! + (id and 0xFFFF.inv())
            }
            iFace.hoveredSiblingId = buffer.uShort
            if(iFace.hoveredSiblingId  == UShort.MAX_VALUE) iFace.hoveredSiblingId = null
            val operatorSize = buffer.uByte.toInt()
            if (operatorSize > 0) {
                val alternateOperators = UByteArray(operatorSize)
                val alternateRhs = UShortArray(operatorSize)
                for(i in 0 until operatorSize) {
                    alternateOperators[i] = buffer.uByte
                    alternateRhs[i] = buffer.uShort
                }
            }
            val cs1InstructionCount = buffer.uByte.toInt()
            iFace.cs1InstructionCount = if (cs1InstructionCount > 0) {
                Array(cs1InstructionCount) {
                    val byteCodeSize = buffer.uShort.toInt()
                    val instructions = Array(byteCodeSize) {
                        var byteCode: UShort? = buffer.uShort
                        if(byteCode == UShort.MAX_VALUE) byteCode = null
                        byteCode
                    }
                    instructions
                }
            } else null
            if(type == 0) {
                iFace.scrollHeight = buffer.uShort
                iFace.isHidden = buffer.uByte.toInt() == 1
            }
            if(type == 1) {
                buffer.uShort
                buffer.uByte
            }
            if (type == 2) {
                iFace.itemIds = IntArray(iFace.originalX * iFace.originalY)
                iFace.itemQuantities = IntArray(iFace.originalX * iFace.originalY)
                if (buffer.uByte.toInt() == 1) iFace.clickMask = iFace.clickMask or 0x10000000
                if (buffer.uByte.toInt() == 1) iFace.clickMask = iFace.clickMask or 0x40000000
                if (buffer.uByte.toInt() == 1) iFace.clickMask = iFace.clickMask or -0x80000000
                if (buffer.uByte.toInt() == 1) iFace.clickMask = iFace.clickMask or 0x20000000
                iFace.xPitch = buffer.uByte.toShort()
                iFace.yPitch = buffer.uByte.toShort()
                iFace.xOffsets = ShortArray(20)
                iFace.yOffsets = ShortArray(20)
                iFace.sprites = arrayOfNulls(20)
                for(i in 0 until 20) {
                    if (buffer.uByte.toInt() == 1) {
                        iFace.xOffsets!![i] = buffer.short
                        iFace.yOffsets!![i] = buffer.short
                        iFace.sprites!![i] = buffer.int
                    } else {
                        iFace.sprites!![i] = null
                    }
                }
                iFace.configActions = arrayOfNulls(5)
                for(i in 0 until 5) {
                    val configAction = buffer.string
                    if (configAction.isNotEmpty()) {
                        iFace.configActions!![i] = configAction
                        iFace.clickMask = iFace.clickMask or (1 shl (i + 23))
                    }
                }
            }
            iFace.isFilled = if(type == 3) buffer.uByte.toInt() == 1 else false
            if (type == 4 || type == 1) {
                iFace.xTextAlignment  = buffer.uByte
                iFace.yTextAlignment  = buffer.uByte
                iFace.lineHeight  = buffer.uByte
                iFace.fontId = buffer.uShort
                if (iFace.fontId == UShort.MAX_VALUE) iFace.fontId = null
                iFace.textIsShadowed = buffer.uByte.toInt() == 1
            }
            if (type == 4) {
                iFace.text = buffer.string
                iFace.alternateText  = buffer.string
            }
            if (type == 1 || type == 3 || type == 4) {
                iFace.textColor = buffer.int
            }
            if (type == 3 || type == 4) {
                iFace.alternateTextColor = buffer.int
                iFace.hoveredTextColor = buffer.int
                iFace.alternateHoveredTextColor = buffer.int
            }
            if (type == 5) {
                iFace.spriteId = buffer.int
                iFace.alternateSpriteId = buffer.int
            }
            if (type == 6) {
                iFace.modelType = 1
                iFace.modelId = buffer.uShort
                if (iFace.modelId == UShort.MAX_VALUE) iFace.modelId = null
                iFace.alternateModelId = buffer.uShort
                if (iFace.alternateModelId == UShort.MAX_VALUE) iFace.alternateModelId = null
                iFace.animationId = buffer.uShort
                if (iFace.animationId == UShort.MAX_VALUE) iFace.animationId = null
                iFace.alternateAnimationId = buffer.uShort
                if (iFace.alternateAnimationId == UShort.MAX_VALUE) iFace.alternateAnimationId = null
                iFace.modelZoom = buffer.uShort
                iFace.rotationX = buffer.uShort
                iFace.rotationZ = buffer.uShort
            }
            if (type == 7) {
                iFace.itemIds = IntArray(iFace.originalHeight * iFace.originalWidth)
                iFace.itemQuantities = IntArray(iFace.originalWidth * iFace.originalHeight)
                iFace.xTextAlignment = buffer.uByte
                iFace.fontId = buffer.uShort
                if (iFace.fontId == UShort.MAX_VALUE) iFace.fontId = null
                iFace.textIsShadowed = buffer.uByte.toInt() == 1
                iFace.textColor = buffer.int
                iFace.xPitch = buffer.short
                iFace.yPitch = buffer.short
                if (buffer.uByte.toInt() == 1) iFace.clickMask = iFace.clickMask or 0x40000000
                iFace.configActions = arrayOfNulls(5)
                for(i in 0 until 5) {
                    val configAction = buffer.string
                    if (configAction.isNotEmpty()) {
                        iFace.configActions!![i] = configAction
                        iFace.clickMask = iFace.clickMask or (1 shl (i + 23))
                    }
                }
            }
            if (type == 8) iFace.text = buffer.string
            if (iFace.menuType == 2 || type == 2) {
                iFace.targetVerb = buffer.string
                iFace.spellName = buffer.string
                val upperMasks = buffer.uShort.toInt() and 0x3F
                iFace.clickMask = (upperMasks shl 11) or iFace.clickMask
            }
            if (iFace.menuType == 1 || iFace.menuType == 4 || iFace.menuType == 5 || iFace.menuType == 6) {
                iFace.tooltip = buffer.string
                if (iFace.tooltip.isEmpty()) {
                    if (iFace.menuType == 1) iFace.tooltip = "Ok"
                    if (iFace.menuType == 4) iFace.tooltip = "Select"
                    if (iFace.menuType == 5) iFace .tooltip = "Select"
                    if (iFace.menuType == 6) iFace.tooltip = "Continue"
                }
            }
            if (iFace.menuType == 1 || iFace.menuType == 4 || iFace.menuType == 5) {
                iFace.clickMask = iFace.clickMask or 0x400000
            }
            if (iFace.menuType == 6) iFace.clickMask = iFace.clickMask or 0x1
            return iFace
        }

        private fun decodeIf3(id: Int, buffer: ByteBuffer): Component {
            val iFace = Component(id)
            buffer.uByte
            iFace.hasScript = true
            val type = buffer.uByte.toInt()
            iFace.contentType = buffer.uShort
            iFace.originalX = buffer.short
            iFace.originalY = buffer.short
            iFace.originalWidth = buffer.uShort.toInt()
            if (type == 9) {
                iFace.originalHeight = buffer.short.toInt()
            } else {
                iFace.originalHeight = buffer.uShort.toInt()
            }
            iFace.dynamicWidth = buffer.get()
            iFace.buttonType = buffer.get()
            iFace.dynamicX = buffer.get()
            iFace.dynamicY = buffer.get()
            iFace.parentId = buffer.uShort.toInt()
            iFace.parentId = if(iFace.parentId == UShort.MAX_VALUE.toInt()) {
                null
            } else {
                iFace.parentId!! + (id and -0x10000)
            }
            iFace.isHidden = buffer.uByte.toInt() == 1
            if (type == 0) {
                iFace.scrollWidth = buffer.uShort
                iFace.scrollHeight = buffer.uShort
                iFace.noClickThrough = buffer.uByte.toInt() == 1
            }
            if (type == 5) {
                iFace.spriteId = buffer.int
                iFace.textureId = buffer.uShort
                iFace.spriteTiling = buffer.uByte.toInt() == 1
                iFace.opacity = buffer.uByte
                iFace.borderThickness = buffer.uByte
                iFace.sprite2 = buffer.int
                iFace.flippedVertically = buffer.uByte.toInt() == 1
                iFace.flippedHorizontally = buffer.uByte.toInt() == 1
            }
            if (type == 6) {
                iFace.modelType = 1
                iFace.modelId = buffer.uShort
                if (iFace.modelId == UShort.MAX_VALUE) iFace.modelId = null
                iFace.offsetX2d = buffer.short
                iFace.offsetY2d = buffer.short
                iFace.rotationX = buffer.uShort
                iFace.rotationZ = buffer.uShort
                iFace.rotationY = buffer.uShort
                iFace.modelZoom = buffer.uShort
                iFace.animationId = buffer.uShort
                if (iFace.animationId == UShort.MAX_VALUE) iFace.animationId = null
                iFace.orthogonal = buffer.uByte.toInt() == 1
                buffer.uShort
                if (iFace.dynamicWidth.toInt() != 0) iFace.modelHeightOverride = buffer.uShort
                if (iFace.buttonType.toInt() != 0) buffer.uShort
            }
            if (type == 4) {
                iFace.fontId = buffer.uShort
                if (iFace.fontId == UShort.MAX_VALUE) iFace.fontId = null
                iFace.text = buffer.string
                iFace.lineHeight = buffer.uByte
                iFace.xTextAlignment = buffer.uByte
                iFace.yTextAlignment = buffer.uByte
                iFace.textIsShadowed = buffer.uByte.toInt() == 1
                iFace.textColor = buffer.int
            }
            if (type == 3) {
                iFace.textColor = buffer.int
                iFace.isFilled = buffer.uByte.toInt() == 1
                iFace.opacity = buffer.uByte
            }
            if (type == 9) {
                iFace.lineWidth = buffer.uByte
                iFace.textColor = buffer.int
                iFace.lineDirection = buffer.uByte.toInt() == 1
            }

            iFace.clickMask = buffer.uMedium
            iFace.opBase = buffer.string
            val actionCount = buffer.uByte.toInt()
            if (actionCount > 0) {
                iFace.actions = arrayOfNulls(actionCount)
                for (int_1 in 0 until actionCount) {
                    iFace.actions!![int_1] = buffer.string
                }
            }
            iFace.dragDeadZone = buffer.uByte
            iFace.dragDeadTime = buffer.uByte
            iFace.dragRenderBehavior = buffer.uByte.toInt() == 1
            iFace.targetVerb = buffer.string
            iFace.onLoadListener = decodeListener(buffer)
            iFace.onMouseOverListener = decodeListener(buffer)
            iFace.onMouseLeaveListener = decodeListener(buffer)
            iFace.onTargetLeaveListener = decodeListener(buffer)
            iFace.onTargetEnterListener = decodeListener(buffer)
            iFace.onVarTransmitListener = decodeListener(buffer)
            iFace.onInvTransmitListener = decodeListener(buffer)
            iFace.onStatTransmitListener = decodeListener(buffer)
            iFace.onTimerListener = decodeListener(buffer)
            iFace.onOpListener = decodeListener(buffer)
            iFace.onMouseRepeatListener = decodeListener(buffer)
            iFace.onClickListener = decodeListener(buffer)
            iFace.onClickRepeatListener = decodeListener(buffer)
            iFace.onReleaseListener = decodeListener(buffer)
            iFace.onHoldListener = decodeListener(buffer)
            iFace.onDragListener = decodeListener(buffer)
            iFace.onDragCompleteListener = decodeListener(buffer)
            iFace.onScrollWheelListener = decodeListener(buffer)
            iFace.varTransmitTriggers = decodeTriggers(buffer)
            iFace.invTransmitTriggers = decodeTriggers(buffer)
            iFace.statTransmitTriggers = decodeTriggers(buffer)
            return iFace
        }

        private fun decodeListener(buffer: ByteBuffer): Array<Any?>? {
            val size = buffer.uByte.toInt()
            return if (size == 0) {
                null
            } else {
                val objects = arrayOfNulls<Any?>(size)
                for (i in 0 until size) {
                    val opcode = buffer.uByte.toInt()
                    if (opcode == 0) {
                        objects[i] = buffer.int
                    } else if (opcode == 1) {
                        objects[i] = buffer.string
                    }
                }
                objects
            }
        }

        private fun decodeTriggers(buffer: ByteBuffer): IntArray? {
            val size = buffer.uByte.toInt()
            return if (size == 0) {
                null
            } else {
                val triggers = IntArray(size)
                for (i in 0 until size) {
                    triggers[i] = buffer.int
                }
                triggers
            }
        }
    }
}