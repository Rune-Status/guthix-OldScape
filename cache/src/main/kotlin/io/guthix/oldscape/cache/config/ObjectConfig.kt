/**
 * This file is part of Guthix OldScape.
 *
 * Guthix OldScape is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Guthix OldScape is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Foobar. If not, see <https://www.gnu.org/licenses/>.
 */
package io.guthix.oldscape.cache.config

import io.guthix.buffer.readStringCP1252
import io.guthix.buffer.writeStringCP1252
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import java.io.IOException

data class ObjectConfig(
    override val id: Int,
    val name: String = "null",
    val width: Short= 1,
    val length: Short = 1,
    val mapIconId: Int? = null,
    val options: Array<String?> = arrayOfNulls<String>(5),
    val clipType: Int = 2,
    val isClipped: Boolean = true,
    val modelClipped: Boolean = false,
    val isHollow: Boolean = false,
    val impenetrable: Boolean = true,
    val accessBlock: Short = 0,
    val objectModels: IntArray? = null,
    val objectTypes: ShortArray? = null,
    val colorReplace: IntArray? = null,
    val colorFind: IntArray? = null,
    val textureFind: IntArray? = null,
    val textureReplace: IntArray? = null,
    val anInt2088: Short? = null,
    val animationId: Int? = null,
    val ambient: Byte = 0,
    val contrast: Int = 0,
    val mapSceneId: Int? = null,
    val modelSizeX: Int = 128,
    val modelSizeHeight: Int = 128,
    val modelSizeY: Int = 128,
    val offsetX: Short = 0,
    val offsetHeight: Short = 0,
    val offsetY: Short = 0,
    val decorDisplacement: Short = 16,
    val isMirrored: Boolean = false,
    val obstructsGround: Boolean = false,
    val nonFlatShading: Boolean = false,
    val contouredGround: Int? = null,
    val supportItems: Short? = null,
    val transformVarbit: Int? = null,
    val transformVarp: Int? = null,
    val transforms: Array<Int?>? = null,
    val ambientSoundId: Int? = null,
    val anInt2112: Int = 0,
    val anInt2113: Int = 0,
    val anInt2083: Short = 0,
    val anIntArray2084: IntArray? = null,
    val params: MutableMap<Int, Any>? = null
) : Config(id) {
    override fun encode(): ByteBuf {
        val data = Unpooled.buffer()
        objectModels?.let {
            data.writeOpcode(1)
            data.writeByte(it.size)
            for(i in it.indices) {
                data.writeShort(it[i])
                data.writeByte(it[i])
            }
        }
        if(name != "null") {
            data.writeOpcode(2)
            data.writeStringCP1252(name)
        }
        if(objectTypes == null) {
            objectModels?.let { objectModels ->
                data.writeOpcode(5)
                data.writeByte(objectModels.size)
                for(model in objectModels) {
                    data.writeShort(model)
                }
            }
        }
        if(width.toInt() != 1) {
            data.writeOpcode(14)
            data.writeByte(width.toInt())
        }
        if(length.toInt() != 1) {
            data.writeOpcode(15)
            data.writeByte(length.toInt())
        }
        if(!impenetrable) {
            if(clipType == 0) {
                data.writeOpcode(17)
            } else {
                data.writeOpcode(18)
            }
        }
        anInt2088?.let {
            data.writeByte(it.toInt())
        }
        contouredGround?.let {
            if(contouredGround == 0) {
                data.writeOpcode(21)
            } else {
                data.writeOpcode(81)
                data.writeByte(it / 256)
            }
        }
        if(nonFlatShading) data.writeOpcode(22)
        if(modelClipped) data.writeOpcode(23)
        if(animationId == null) data.writeShort(65535) else data.writeShort(animationId.toInt())
        if(clipType == 1) data.writeOpcode(27)
        if(decorDisplacement.toInt() != 16) {
            data.writeOpcode(28)
            data.writeByte(decorDisplacement.toInt())
        }
        if(ambient.toInt() != 0) {
            data.writeOpcode(29)
            data.writeByte(ambient.toInt())
        }
        if(contrast != 0) {
            data.writeOpcode(39)
            data.writeByte(contrast)
        }
        options.forEachIndexed { i, str ->
            if(str != null && str != "Hidden") {
                data.writeOpcode(30 + i)
                data.writeStringCP1252(str)
            }
        }
        colorFind?.let { colorFind -> colorReplace?.let { colorReplace->
            data.writeOpcode(40)
            data.writeByte(colorFind.size)
            for (i in colorFind.indices) {
                data.writeShort(colorFind[i])
                data.writeShort(colorReplace[i])
            }
        } }
        textureFind?.let { textureFind -> textureReplace?.let { textureReplace->
            data.writeOpcode(41)
            data.writeByte(textureFind.size)
            for (i in textureFind.indices) {
                data.writeShort(textureFind[i])
                data.writeShort(textureReplace[i])
            }
        } }
        if(isMirrored) data.writeOpcode(62)
        if(!isClipped) data.writeOpcode(64)
        if(modelSizeX != 128) {
            data.writeOpcode(65)
            data.writeShort(modelSizeX)
        }
        if(modelSizeHeight != 128) {
            data.writeOpcode(66)
            data.writeShort(modelSizeHeight)
        }
        if(modelSizeY != 128) {
            data.writeOpcode(67)
            data.writeShort(modelSizeY)
        }
        mapSceneId?.let {
            data.writeOpcode(68)
            data.writeShort(it)
        }
        if(accessBlock.toInt() != 0) {
            data.writeOpcode(69)
            data.writeByte(accessBlock.toInt())
        }
        if(offsetX.toInt() != 0) {
            data.writeOpcode(70)
            data.writeShort(offsetX.toInt())
        }
        if(offsetHeight.toInt() != 0) {
            data.writeOpcode(71)
            data.writeShort(offsetHeight.toInt())
        }
        if(offsetY.toInt() != 0) {
            data.writeOpcode(72)
            data.writeShort(offsetY.toInt())
        }
        if(obstructsGround) data.writeOpcode(73)
        if(isHollow) data.writeOpcode(74)
        supportItems?.let {
            data.writeOpcode(75)
            data.writeByte(it.toInt())
        }
        transforms?.let { transforms ->
            data.writeOpcode(if(transforms.last() == null) 118 else 106)
            if(transformVarbit == null) data.writeShort(65535) else data.writeShort(transformVarbit!!.toInt())
            if(transformVarp == null) data.writeShort(65535) else data.writeShort(transformVarp!!.toInt())
            transforms.last()?.let { data.writeShort(it) }
            val size = transforms.size - 2
            data.writeByte(size)
            for(i in 0..size) {
                val transform = transforms[i]
                if(transform == null) {
                    data.writeShort(65535)
                } else {
                    data.writeShort(transform)
                }
            }
        }
        ambientSoundId?.let {
            data.writeOpcode(78)
            data.writeShort(it)
            data.writeByte(anInt2083.toInt())
        }
        anIntArray2084?.let { intArray ->
            data.writeOpcode(79)
            data.writeShort(anInt2112)
            data.writeShort(anInt2113)
            data.writeByte(anInt2083.toInt())
            data.writeByte(intArray.size)
            intArray.forEach { data.writeShort(it) }
        }
        mapIconId?.let {
            data.writeOpcode(82)
            data.writeShort(it)
        }
        params?.let {
            data.writeOpcode(249)
            data.writeParams(it)
        }
        data.writeOpcode(0)
        return data
    }

    companion object : ConfigCompanion<ObjectConfig>() {
        override val id = 6

        override fun decode(id: Int, data: ByteBuf): ObjectConfig {
            var name = "null"
            var width: Short= 1
            var length: Short = 1
            var mapIconId: Int? = null
            val options = arrayOfNulls<String>(5)
            var clipType = 2
            var isClipped = true
            var modelClipped = false
            var isHollow = false
            var impenetrable = true
            var accessBlock: Short = 0
            var objectModels: IntArray? = null
            var objectTypes: ShortArray? = null
            var colorReplace: IntArray? = null
            var colorFind: IntArray? = null
            var textureFind: IntArray? = null
            var textureReplace: IntArray? = null
            var anInt2088: Short? = null
            var animationId: Int? = null
            var ambient: Byte = 0
            var contrast = 0
            var mapSceneId: Int? = null
            var modelSizeX: Int = 128
            var modelSizeHeight: Int = 128
            var modelSizeY: Int = 128
            var offsetX: Short = 0
            var offsetHeight: Short = 0
            var offsetY: Short = 0
            var decorDisplacement: Short = 16
            var isMirrored = false
            var obstructsGround = false
            var nonFlatShading = false
            var contouredGround: Int? = null
            var supportItems: Short? = null
            var transformVarbit: Int? = null
            var transformVarp: Int? = null
            var transforms: Array<Int?>? = null
            var ambientSoundId: Int? = null
            var anInt2112: Int = 0
            var anInt2113: Int = 0
            var anInt2083: Short = 0
            var anIntArray2084: IntArray? = null
            var params: MutableMap<Int, Any>? = null
            decoder@ while (true) {
                when (val opcode = data.readUnsignedByte().toInt()) {
                    0 -> break@decoder
                    1 -> {
                        val size = data.readUnsignedByte().toInt()
                        if (size > 0) {
                            objectModels = IntArray(size)
                            objectTypes = ShortArray(size)
                            for (i in 0 until size) {
                                objectModels[i] = data.readUnsignedShort()
                                objectTypes[i] = data.readUnsignedByte()
                            }
                        }
                    }
                    2 -> name = data.readStringCP1252()
                    5 -> {
                        val size = data.readUnsignedByte().toInt()
                        if (size > 0) {
                            objectTypes = null
                            objectModels = IntArray(size) { data.readUnsignedShort() }
                        }
                    }
                    14 -> width = data.readUnsignedByte()
                    15 -> length = data.readUnsignedByte()
                    17 -> {
                        clipType = 0
                        impenetrable = false
                    }
                    18 -> impenetrable = false
                    19 -> anInt2088 = data.readUnsignedByte()
                    21 -> contouredGround = 0
                    22 -> nonFlatShading = true
                    23 -> modelClipped = true
                    24 -> {
                        animationId = data.readUnsignedShort()
                        if(animationId!!.toInt() == 65535) {
                            animationId = null
                        }
                    }
                    27 -> clipType = 1
                    28 -> decorDisplacement = data.readUnsignedByte()
                    29 -> ambient = data.readByte()
                    39 -> contrast = data.readByte().toInt() * 25
                    in 30..34 -> options[opcode - 30] = data.readStringCP1252().takeIf { it != "Hidden" }
                    40 -> {
                        val amount = data.readUnsignedByte().toInt()
                        colorFind = IntArray(amount)
                        colorReplace = IntArray(amount)
                        for (i in 0 until amount) {
                            colorFind[i] = data.readUnsignedShort()
                            colorReplace[i] = data.readUnsignedShort()
                        }
                    }
                    41 -> {
                        val amount = data.readUnsignedByte().toInt()
                        textureFind = IntArray(amount)
                        textureReplace = IntArray(amount)
                        for (i in 0 until amount) {
                            textureFind[i] = data.readUnsignedShort()
                            textureReplace[i] = data.readUnsignedShort()
                        }
                    }
                    62 -> isMirrored = true
                    64 -> isClipped = false
                    65 -> modelSizeX = data.readUnsignedShort()
                    66 -> modelSizeHeight = data.readUnsignedShort()
                    67 -> modelSizeY = data.readUnsignedShort()
                    68 -> mapSceneId = data.readUnsignedShort()
                    69 -> accessBlock = data.readUnsignedByte()
                    70 -> offsetX = data.readShort()
                    71 -> offsetHeight = data.readShort()
                    72 -> offsetY = data.readShort()
                    73 -> obstructsGround = true
                    74 -> isHollow = true
                    75 -> supportItems = data.readUnsignedByte()
                    77, 92 -> {
                        transformVarbit = data.readUnsignedShort()
                        transformVarbit = if(transformVarbit == 65535) null else transformVarbit
                        transformVarp = data.readUnsignedShort()
                        transformVarp = if(transformVarbit == 65535) null else transformVarp
                        val lastEntry = if(opcode == 92) {
                            val entry = data.readUnsignedShort()
                            if(entry == 65535) null else entry
                        } else null
                        val size = data.readUnsignedByte().toInt()
                        transforms = arrayOfNulls<Int?>(size + 2)
                        for(i in 0..size) {
                            val transform = data.readUnsignedShort()
                            transforms[i] = if(transform == 65535) null else transform
                        }
                        if(opcode == 92) {
                            transforms[size + 1] = lastEntry
                        }
                    }
                    78 -> {
                        ambientSoundId = data.readUnsignedShort()
                        anInt2083 = data.readUnsignedByte()
                    }
                    79 -> {
                        anInt2112 = data.readUnsignedShort()
                        anInt2113 = data.readUnsignedShort()
                        anInt2083 = data.readUnsignedByte()
                        val size = data.readUnsignedByte().toInt()
                        anIntArray2084 = IntArray(size) { data.readUnsignedShort() }
                    }
                    81 -> contouredGround = data.readUnsignedByte() * 256
                    82 -> mapIconId = data.readUnsignedShort()
                    249 -> params = data.readParams()
                    else -> throw IOException("Did not recognise opcode $opcode.")
                }
            }
            if (anInt2088 == null) {
                anInt2088 = 0
                if ((objectModels != null && (objectTypes == null)
                            || objectTypes?.get(0)?.toInt() == 10)
                ) {
                    anInt2088 = 1
                }
                for (i in 0 until 5) {
                    if (options[i] != null) {
                        anInt2088 = 1
                    }
                }
            }
            if (supportItems == null) {
                supportItems = if(clipType != 0) 1 else 0
            }
            if (isHollow) {
                clipType = 0
                impenetrable = false
            }
            return ObjectConfig(id, name, width, length, mapIconId, options, clipType, isClipped, modelClipped,
                isHollow, impenetrable, accessBlock, objectModels, objectTypes, colorReplace, colorFind, textureFind,
                textureReplace, anInt2088, animationId, ambient, contrast, mapSceneId, modelSizeX, modelSizeHeight,
                modelSizeY, offsetX, offsetHeight, offsetY, decorDisplacement, isMirrored, obstructsGround,
                nonFlatShading, contouredGround, supportItems, transformVarbit, transformVarp, transforms,
                ambientSoundId, anInt2112, anInt2113, anInt2083, anIntArray2084, params
            )
        }
    }
}