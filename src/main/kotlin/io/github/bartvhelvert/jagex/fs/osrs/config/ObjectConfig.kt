package io.github.bartvhelvert.jagex.fs.osrs.config

import io.github.bartvhelvert.jagex.fs.io.*
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.nio.ByteBuffer

data class ObjectConfig @ExperimentalUnsignedTypes constructor(
    override val id: Int,
    val name: String,
    val width: UByte,
    val length: UByte,
    val varpId: UShort?,
    val mapIconId: UShort?,
    val varp32Id: UShort?,
    val options: Array<String?>,
    val clipType: Int,
    val isClipped: Boolean,
    val modelClipped: Boolean,
    val isHollow: Boolean,
    val impenetrable: Boolean,
    val accessBlock: UByte,
    val objectModels: UShortArray?,
    val objectTypes: UByteArray?,
    val colorReplace: UShortArray?,
    val colorFind: UShortArray?,
    val textureFind: UShortArray?,
    val textureReplace: UShortArray?,
    val anInt2088: UByte?,
    val animationId: UShort?,
    val ambient: Int,
    val contrast: Int,
    val mapSceneId: UShort?,
    val modelSizeX: UShort,
    val modelSizeHeight: UShort,
    val modelSizeY: UShort,
    val offsetX: Short,
    val offsetHeight: Short,
    val offsetY: Short,
    val decorDisplacement: UByte,
    val isMirrored: Boolean,
    val obstructsGround: Boolean,
    val nonFlatShading: Boolean,
    val contouredGround: Int?,
    val supportItems: UByte?,
    val configs: Array<UShort?>?,
    val ambientSoundId: UShort?,
    val anInt2112: UShort,
    val anInt2113: UShort,
    val anInt2083: UByte,
    val anIntArray2084: UShortArray?,
    val params: HashMap<Int, Any>?
): Config(id) {
    @ExperimentalUnsignedTypes
    override fun encode(): ByteBuffer {
        val byteStr = ByteArrayOutputStream()
        DataOutputStream(byteStr).use { os ->
            objectModels?.let {
                os.writeOpcode(1)
                os.writeByte(objectModels.size)
                for(i in 0 until objectModels.size) {
                    os.writeShort(objectModels[i].toInt())
                    os.writeByte(objectModels[i].toInt())
                }
            }
            if(name != "null") {
                os.writeOpcode(2)
                os.writeString(name)
            }
            if(objectTypes == null && objectModels != null) {
                os.writeOpcode(5)
                os.writeByte(objectModels.size)
                for(i in 0 until objectModels.size) {
                    os.writeShort(objectModels[i].toInt())
                }
            }
            if(width.toInt() != 1) {
                os.writeOpcode(14)
                os.writeByte(width.toInt())
            }
            if(length.toInt() != 1) {
                os.writeOpcode(15)
                os.writeByte(length.toInt())
            }
            if(!impenetrable) {
                if(clipType == 0) {
                    os.writeOpcode(17)
                } else {
                    os.writeOpcode(18)
                }
            }
            anInt2088?.let {
                os.writeByte(anInt2088.toInt())
            }
            contouredGround?.let {
                if(contouredGround == 0) {
                    os.writeOpcode(21)
                } else {
                    os.writeOpcode(81)
                    os.writeByte(contouredGround / 256)
                }
            }
            if(nonFlatShading) os.writeOpcode(22)
            if(modelClipped) os.writeOpcode(23)
            if(animationId == null) os.writeShort(UShort.MAX_VALUE.toInt()) else os.writeShort(animationId.toInt())
            if(clipType == 1) os.writeOpcode(27)
            if(decorDisplacement.toInt() != 16) {
                os.writeOpcode(28)
                os.writeByte(decorDisplacement.toInt())
            }
            if(ambient != 0) {
                os.writeOpcode(29)
                os.writeByte(ambient)
            }
            if(contrast != 0) {
                os.writeOpcode(39)
                os.writeByte(contrast)
            }
            options.forEachIndexed { i, str ->
                if(str != null && str != "Hidden") {
                    os.writeOpcode(30 + i)
                    os.writeString(str)
                }
            }
            if (colorFind != null && colorReplace != null) {
                os.writeOpcode(40)
                os.writeByte(colorFind.size)
                for (i in 0 until colorFind.size) {
                    os.writeShort(colorFind[i].toInt())
                    os.writeShort(colorReplace[i].toInt())
                }
            }
            if (textureFind != null && textureReplace != null) {
                os.writeOpcode(41)
                os.writeByte(textureFind.size)
                for (i in 0 until textureReplace.size) {
                    os.writeShort(textureFind[i].toInt())
                    os.writeShort(textureReplace[i].toInt())
                }
            }
            if(isMirrored) os.writeOpcode(62)
            if(!isClipped) os.writeOpcode(64)
            if(modelSizeX.toInt() != 128) {
                os.writeOpcode(65)
                os.writeShort(modelSizeX.toInt())
            }
            if(modelSizeHeight.toInt() != 128) {
                os.writeOpcode(66)
                os.writeShort(modelSizeHeight.toInt())
            }
            if(modelSizeY.toInt() != 128) {
                os.writeOpcode(67)
                os.writeShort(modelSizeY.toInt())
            }
            mapSceneId?.let {
                os.writeOpcode(68)
                os.writeShort(mapSceneId.toInt())
            }
            if(accessBlock.toInt() != 0) {
                os.writeOpcode(69)
                os.writeByte(accessBlock.toInt())
            }
            if(offsetX.toInt() != 0) {
                os.writeOpcode(70)
                os.writeShort(offsetX.toInt())
            }
            if(offsetHeight.toInt() != 0) {
                os.writeOpcode(71)
                os.writeShort(offsetHeight.toInt())
            }
            if(offsetY.toInt() != 0) {
                os.writeOpcode(72)
                os.writeShort(offsetY.toInt())
            }
            if(obstructsGround) os.writeOpcode(73)
            if(isHollow) os.writeOpcode(74)
            supportItems?.let {
                os.writeOpcode(75)
                os.writeByte(supportItems.toInt())
            }
            if(configs != null) {
                if(configs.last() != null) os.writeOpcode(92) else os.writeOpcode(77)
                if(varpId == null) os.writeShort(UShort.MAX_VALUE.toInt()) else os.writeShort(varpId.toInt())
                if(varp32Id == null) os.writeShort(UShort.MAX_VALUE.toInt()) else os.writeShort(varp32Id.toInt())
                if(configs.last() != null) os.writeShort(configs.last()!!.toInt())
                os.writeByte(configs.size - 2)
                for(i in 0 until configs.size - 1) {
                    if(configs[i] != null) {
                        os.writeShort(UShort.MAX_VALUE.toInt())
                    } else {
                        os.writeShort(configs[i]!!.toInt())
                    }
                }
            }
            if(ambientSoundId != null) {
                os.writeOpcode(78)
                os.writeShort(ambientSoundId.toInt())
                os.writeByte(anInt2083.toInt())
            }
            if(anIntArray2084 != null) {
                os.writeOpcode(79)
                os.writeShort(anInt2112.toInt())
                os.writeShort(anInt2113.toInt())
                os.writeByte(anInt2083.toInt())
                os.writeByte(anIntArray2084.size)
                anIntArray2084.forEach { os.writeShort(it.toInt()) }
            }
            mapIconId?.let {
                os.writeOpcode(82)
                os.writeShort(mapIconId.toInt())
            }
            params?.let {
                os.writeOpcode(249)
                os.writeParams(params)
            }
            os.writeOpcode(0)
        }
        return ByteBuffer.wrap(byteStr.toByteArray())
    }

    @ExperimentalUnsignedTypes
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ObjectConfig) return false
        if (id != other.id) return false
        if (name != other.name) return false
        if (width != other.width) return false
        if (length != other.length) return false
        if (varpId != other.varpId) return false
        if (mapIconId != other.mapIconId) return false
        if (varp32Id != other.varp32Id) return false
        if (!options.contentEquals(other.options)) return false
        if (clipType != other.clipType) return false
        if (isClipped != other.isClipped) return false
        if (modelClipped != other.modelClipped) return false
        if (isHollow != other.isHollow) return false
        if (impenetrable != other.impenetrable) return false
        if (accessBlock != other.accessBlock) return false
        if (objectModels != other.objectModels) return false
        if (objectTypes != other.objectTypes) return false
        if (colorReplace != other.colorReplace) return false
        if (colorFind != other.colorFind) return false
        if (textureFind != other.textureFind) return false
        if (textureReplace != other.textureReplace) return false
        if (anInt2088 != other.anInt2088) return false
        if (animationId != other.animationId) return false
        if (ambient != other.ambient) return false
        if (contrast != other.contrast) return false
        if (mapSceneId != other.mapSceneId) return false
        if (modelSizeX != other.modelSizeX) return false
        if (modelSizeHeight != other.modelSizeHeight) return false
        if (modelSizeY != other.modelSizeY) return false
        if (offsetX != other.offsetX) return false
        if (offsetHeight != other.offsetHeight) return false
        if (offsetY != other.offsetY) return false
        if (decorDisplacement != other.decorDisplacement) return false
        if (isMirrored != other.isMirrored) return false
        if (obstructsGround != other.obstructsGround) return false
        if (nonFlatShading != other.nonFlatShading) return false
        if (contouredGround != other.contouredGround) return false
        if (supportItems != other.supportItems) return false
        if (configs != null) {
            if (other.configs == null) return false
            if (!configs.contentEquals(other.configs)) return false
        } else if (other.configs != null) return false
        if (ambientSoundId != other.ambientSoundId) return false
        if (anInt2112 != other.anInt2112) return false
        if (anInt2113 != other.anInt2113) return false
        if (anInt2083 != other.anInt2083) return false
        if (anIntArray2084 != other.anIntArray2084) return false
        if (params != other.params) return false
        return true
    }

    @ExperimentalUnsignedTypes
    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + width.hashCode()
        result = 31 * result + length.hashCode()
        result = 31 * result + (varpId?.hashCode() ?: 0)
        result = 31 * result + (mapIconId?.hashCode() ?: 0)
        result = 31 * result + (varp32Id?.hashCode() ?: 0)
        result = 31 * result + options.contentHashCode()
        result = 31 * result + clipType
        result = 31 * result + isClipped.hashCode()
        result = 31 * result + modelClipped.hashCode()
        result = 31 * result + isHollow.hashCode()
        result = 31 * result + impenetrable.hashCode()
        result = 31 * result + accessBlock.hashCode()
        result = 31 * result + (objectModels?.hashCode() ?: 0)
        result = 31 * result + (objectTypes?.hashCode() ?: 0)
        result = 31 * result + (colorReplace?.hashCode() ?: 0)
        result = 31 * result + (colorFind?.hashCode() ?: 0)
        result = 31 * result + (textureFind?.hashCode() ?: 0)
        result = 31 * result + (textureReplace?.hashCode() ?: 0)
        result = 31 * result + (anInt2088?.hashCode() ?: 0)
        result = 31 * result + (animationId?.hashCode() ?: 0)
        result = 31 * result + ambient
        result = 31 * result + contrast
        result = 31 * result + (mapSceneId?.hashCode() ?: 0)
        result = 31 * result + modelSizeX.hashCode()
        result = 31 * result + modelSizeHeight.hashCode()
        result = 31 * result + modelSizeY.hashCode()
        result = 31 * result + offsetX
        result = 31 * result + offsetHeight
        result = 31 * result + offsetY
        result = 31 * result + decorDisplacement.hashCode()
        result = 31 * result + isMirrored.hashCode()
        result = 31 * result + obstructsGround.hashCode()
        result = 31 * result + nonFlatShading.hashCode()
        result = 31 * result + (contouredGround ?: 0)
        result = 31 * result + (supportItems?.hashCode() ?: 0)
        result = 31 * result + (configs?.contentHashCode() ?: 0)
        result = 31 * result + (ambientSoundId?.hashCode() ?: 0)
        result = 31 * result + anInt2112.hashCode()
        result = 31 * result + anInt2113.hashCode()
        result = 31 * result + anInt2083.hashCode()
        result = 31 * result + (anIntArray2084?.hashCode() ?: 0)
        result = 31 * result + (params?.hashCode() ?: 0)
        return result
    }

    companion object : ConfigCompanion<ObjectConfig>() {
        override val id = 6

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, buffer: ByteBuffer): ObjectConfig {
            var name = "null"
            var width: UByte= 1u
            var length: UByte = 1u
            var varpId: UShort? = null
            var mapIconId: UShort? = null
            var varp32Id: UShort? = null
            val options = arrayOfNulls<String>(5)
            var clipType = 2
            var isClipped = true
            var modelClipped = false
            var isHollow = false
            var impenetrable = true
            var accessBlock: UByte = 0u
            var objectModels: UShortArray? = null
            var objectTypes: UByteArray? = null
            var colorReplace: UShortArray? = null
            var colorFind: UShortArray? = null
            var textureFind: UShortArray? = null
            var textureReplace: UShortArray? = null
            var anInt2088: UByte? = null
            var animationId: UShort? = null
            var ambient = 0
            var contrast = 0
            var mapSceneId: UShort? = null
            var modelSizeX: UShort = 128u
            var modelSizeHeight: UShort = 128u
            var modelSizeY: UShort = 128u
            var offsetX: Short = 0
            var offsetHeight: Short = 0
            var offsetY: Short = 0
            var decorDisplacement: UByte = 16u
            var isMirrored = false
            var obstructsGround = false
            var nonFlatShading = false
            var contouredGround: Int? = null
            var supportItems: UByte? = null
            var configs: Array<UShort?>? = null
            var ambientSoundId: UShort? = null
            var anInt2112: UShort = 0u
            var anInt2113: UShort = 0u
            var anInt2083: UByte = 0u
            var anIntArray2084: UShortArray? = null
            var params: HashMap<Int, Any>? = null

            decoder@ while (true) {
                val opcode = buffer.uByte.toInt()
                when (opcode) {
                    0 -> break@decoder
                    1 -> {
                        val size = buffer.uByte.toInt()
                        if (size > 0) {
                            objectModels = UShortArray(size)
                            objectTypes = UByteArray(size)
                            for (i in 0 until size) {
                                objectModels[i] = buffer.uShort
                                objectTypes[i] = buffer.uByte
                            }
                        }
                    }
                    2 -> name = buffer.string
                    5 -> {
                        val size = buffer.uByte.toInt()
                        if (size > 0) {
                            objectTypes = null
                            objectModels = UShortArray(size) { buffer.uShort }
                        }
                    }
                    14 -> width = buffer.uByte
                    15 -> length = buffer.uByte
                    17 -> {
                        clipType = 0
                        impenetrable = false
                    }
                    18 -> impenetrable = false
                    19 -> anInt2088 = buffer.uByte
                    21 -> contouredGround = 0
                    22 -> nonFlatShading = true
                    23 -> modelClipped = true
                    24 -> {
                        animationId = buffer.uShort
                        if(animationId.toInt() == UShort.MAX_VALUE.toInt()) animationId = null
                    }
                    27 -> clipType = 1
                    28 -> decorDisplacement = buffer.uByte
                    29 -> ambient = buffer.get().toInt()
                    39 -> contrast = buffer.get().toInt() * 25
                    in 30..34 -> options[opcode - 30] = buffer.string.takeIf { it != "Hidden" }
                    40 -> {
                        val size = buffer.uByte.toInt()
                        colorFind = UShortArray(size)
                        colorReplace = UShortArray(size)
                        for (i in 0 until size) {
                            colorFind[i] = buffer.uShort
                            colorReplace[i] = buffer.uShort
                        }
                    }
                    41 -> {
                        val size = buffer.uByte.toInt()
                        textureFind = UShortArray(size)
                        textureReplace = UShortArray(size)
                        for (i in 0 until size) {
                            textureFind[i] = buffer.uShort
                            textureReplace[i] = buffer.uShort
                        }
                    }
                    62 -> isMirrored = true
                    64 -> isClipped = false
                    65 -> modelSizeX = buffer.uShort
                    66 -> modelSizeHeight = buffer.uShort
                    67 -> modelSizeY = buffer.uShort
                    68 -> mapSceneId = buffer.uShort
                    69 -> accessBlock = buffer.uByte
                    70 -> offsetX = buffer.short
                    71 -> offsetHeight = buffer.short
                    72 -> offsetY = buffer.short
                    73 -> obstructsGround = true
                    74 -> isHollow = true
                    75 -> supportItems = buffer.uByte
                    77, 92 -> {
                        varpId = buffer.uShort
                        if(varpId.toInt() == UShort.MAX_VALUE.toInt()) varpId = null
                        varp32Id = buffer.uShort
                        if(varp32Id.toInt() == UShort.MAX_VALUE.toInt()) varp32Id = null
                        val lastEntry = if(opcode == 92) {
                            val entry = buffer.uShort
                            if(entry == UShort.MAX_VALUE) null else entry
                        } else null
                        val size = buffer.uByte.toInt()
                        configs = arrayOfNulls(size + 2)
                        for(i in 0 until configs.size - 1) {
                            var config: UShort? = buffer.uShort
                            if(config!!.toInt() == UShort.MAX_VALUE.toInt()) config = null
                            configs[i] = config
                        }
                        if(opcode == 92) {
                            configs[size + 1] = lastEntry
                        }
                    }
                    78 -> {
                        ambientSoundId = buffer.uShort
                        anInt2083 = buffer.uByte
                    }
                    79 -> {
                        anInt2112 = buffer.uShort
                        anInt2113 = buffer.uShort
                        anInt2083 = buffer.uByte
                        val size = buffer.uByte.toInt()
                        anIntArray2084 = UShortArray(size) { buffer.uShort }
                    }
                    81 -> contouredGround = buffer.uByte.toInt() * 256
                    82 -> mapIconId = buffer.uShort
                    249 -> params = buffer.params
                    else -> throw IOException("Did not recognise opcode $opcode")
                }
            }
            if (anInt2088 == null) {
                anInt2088 = 0u
                if (objectModels != null && (objectTypes == null || objectTypes[0].toInt() == 10)) {
                    anInt2088 = 1u
                }
                for (i in 0 until 5) {
                    if (options[i] != null) {
                        anInt2088 = 1u
                    }
                }
            }
            if (supportItems == null) {
                supportItems = if(clipType != 0) 1u else 0u
            }
            if (isHollow) {
                clipType = 0
                impenetrable = false
            }
            return ObjectConfig(id, name, width, length, varpId, mapIconId, varp32Id, options, clipType, isClipped,
                modelClipped, isHollow, impenetrable, accessBlock, objectModels, objectTypes, colorReplace, colorFind,
                textureFind, textureReplace, anInt2088, animationId, ambient, contrast, mapSceneId, modelSizeX,
                modelSizeHeight, modelSizeY, offsetX, offsetHeight, offsetY, decorDisplacement, isMirrored,
                obstructsGround, nonFlatShading, contouredGround, supportItems, configs, ambientSoundId,
                anInt2112, anInt2113, anInt2083, anIntArray2084, params
            )
        }
    }
}