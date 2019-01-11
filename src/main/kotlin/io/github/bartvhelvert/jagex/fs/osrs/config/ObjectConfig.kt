package io.github.bartvhelvert.jagex.fs.osrs.config

import io.github.bartvhelvert.jagex.fs.io.string
import io.github.bartvhelvert.jagex.fs.io.uByte
import io.github.bartvhelvert.jagex.fs.io.uShort
import io.github.bartvhelvert.jagex.fs.osrs.ConfigFile
import io.github.bartvhelvert.jagex.fs.osrs.ConfigFileCompanion
import io.github.bartvhelvert.jagex.fs.osrs.params
import java.nio.ByteBuffer

class ObjectConfig @ExperimentalUnsignedTypes constructor(
    id: Int,
    val name: String,
    val width: UByte,
    val length: UByte,
    val varpID: UShort?,
    val mapIconId: UShort?,
    val configId: UShort?,
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
    val mapSceneID: UShort?,
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
    val configChangeDest: Array<UShort?>?,
    val ambientSoundId: UShort?,
    val anInt2112: UShort,
    val anInt2113: UShort,
    val anInt2083: UByte,
    val anIntArray2084: UShortArray?,
    val params: MutableMap<Int, Any>?
): ConfigFile(id) {
    override fun encode() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object : ConfigFileCompanion<ConfigFile>() {
        override val id = 6

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, buffer: ByteBuffer): ConfigFile {
            var name = "null"
            var width: UByte= 1u
            var length: UByte = 1u
            var varpId: UShort? = null
            var mapIconId: UShort? = null
            var configId: UShort? = null
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
            var mapSceneID: UShort? = null
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
            var configChangeDest: Array<UShort?>? = null
            var ambientSoundId: UShort? = null
            var anInt2112: UShort = 0u
            var anInt2113: UShort = 0u
            var anInt2083: UByte = 0u
            var anIntArray2084: UShortArray? = null
            var params: MutableMap<Int, Any>? = null

            decoder@ while (true) {
                val opcode = buffer.uByte.toInt()
                when (opcode) {
                    0 -> {
                        break@decoder
                    }
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
                        if(animationId == UShort.MAX_VALUE) animationId = null
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
                    68 -> mapSceneID = buffer.uShort
                    69 -> accessBlock = buffer.uByte
                    70 -> offsetX = buffer.short
                    71 -> offsetHeight = buffer.short
                    72 -> offsetY = buffer.short
                    73 -> obstructsGround = true
                    74 -> isHollow = true
                    75 -> supportItems = buffer.uByte
                    77 -> {
                        varpId = buffer.uShort
                        if(varpId == UShort.MAX_VALUE) varpId = null
                        configId = buffer.uShort
                        if(configId == UShort.MAX_VALUE) configId = null
                        val size = buffer.uByte.toInt()
                        configChangeDest = arrayOfNulls(size + 2)
                        for(i in 0 until configChangeDest!!.size - 1) {
                            configChangeDest[i] = buffer.uShort
                            if(configChangeDest[i] == UShort.MAX_VALUE) configChangeDest = null
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
                    92 -> {
                        varpId = buffer.uShort
                        if(varpId == UShort.MAX_VALUE) varpId = null
                        configId = buffer.uShort
                        if(configId == UShort.MAX_VALUE) configId = null
                        var lastEntry: UShort? = buffer.uShort
                        if(lastEntry == UShort.MAX_VALUE) lastEntry = null
                        val size = buffer.uShort.toInt()
                        configChangeDest = arrayOfNulls(size + 2)
                        for(i in 0 until configChangeDest!!.size - 1) {
                            configChangeDest[i] = buffer.uShort
                            if(configChangeDest[i] == UShort.MAX_VALUE) configChangeDest = null
                        }
                        configChangeDest[size + 1] = lastEntry

                    }
                    249 -> params = buffer.params
                    else -> error(opcode)
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
            return ObjectConfig(id, name, width, length, varpId, mapIconId, configId, options, clipType, isClipped,
                modelClipped, isHollow, impenetrable, accessBlock, objectModels, objectTypes, colorReplace, colorFind,
                textureFind, textureReplace, anInt2088, animationId, ambient, contrast, mapSceneID, modelSizeX,
                modelSizeHeight, modelSizeY, offsetX, offsetHeight, offsetY, decorDisplacement, isMirrored,
                obstructsGround, nonFlatShading, contouredGround, supportItems, configChangeDest, ambientSoundId,
                anInt2112, anInt2113, anInt2083, anIntArray2084, params
            )
        }
    }
}