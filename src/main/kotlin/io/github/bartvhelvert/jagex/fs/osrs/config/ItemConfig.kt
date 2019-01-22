package io.github.bartvhelvert.jagex.fs.osrs.config

import io.github.bartvhelvert.jagex.fs.io.*
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.nio.ByteBuffer

data class ItemConfig @ExperimentalUnsignedTypes constructor(
    override val id: Int,
    val name: String,
    val resizeX: UShort,
    val resizeY: UShort,
    val resizeZ: UShort,
    var xan2d: UShort,
    var yan2d: UShort,
    var zan2d: UShort,
    var xoff2d: Short,
    var yoff2d: Short,
    var zoom2d: Int,
    val cost: Int,
    val tradable: Boolean,
    val stackable: Boolean,
    val model: UShort,
    val members: Boolean,
    val colorFind: UShortArray?,
    val colorReplace: UShortArray?,
    val textureFind: UShortArray?,
    val textureReplace: UShortArray?,
    val ambient: Int,
    val contrast: Int,
    val countCo: Array<UShort?>?,
    val countObj: Array<UShort?>?,
    val groundActions: Array<String?>,
    val iop: Array<String?>,
    val maleModel0: UShort?,
    val maleModel1: UShort?,
    val maleModel2: UShort?,
    val maleOffset: UByte,
    val maleHeadModel: UShort?,
    val maleHeadModel2: UShort?,
    val femaleModel0: UShort?,
    val femaleModel1: UShort?,
    val femaleModel2: UShort?,
    val femaleOffset: UByte,
    val femaleHeadModel: UShort?,
    val femaleHeadModel2: UShort?,
    val notedId: UShort?,
    val notedTemplate: UShort?,
    val team: UByte,
    val shiftClickDropIndex: Byte,
    val boughtId: UShort?,
    val boughtTemplate: UShort?,
    val placeholderId: UShort?,
    val placeholderTemplateId: UShort?,
    val params: HashMap<Int, Any>?
) : Config(id) {
    @ExperimentalUnsignedTypes
    override fun encode(): ByteBuffer {
        val byteStr = ByteArrayOutputStream()
        DataOutputStream(byteStr).use { os ->
            if(model.toInt() != 0) {
                os.writeOpcode(1)
                os.writeShort(model.toInt())
            }
            if(name != "null") {
                os.writeOpcode(2)
                os.writeString(name)
            }
            if(zoom2d != 200000) {
                os.writeOpcode(4)
                os.writeShort(zoom2d)
            }
            if(xan2d.toInt() != 0) {
                os.writeOpcode(5)
                os.writeShort(xan2d.toInt())
            }
            if(yan2d.toInt() != 0) {
                os.writeOpcode(6)
                os.writeShort(yan2d.toInt())
            }
            if(xoff2d.toInt() != 0) {
                os.writeOpcode(7)
                if(xoff2d < 0) os.writeShort(xoff2d + 65536) else os.writeShort(xoff2d.toInt())
            }
            if(yoff2d.toInt() != 0) {
                os.writeOpcode(8)
                if(yoff2d < 0) os.writeShort(yoff2d + 65536) else os.writeShort(yoff2d.toInt())
            }
            if(stackable) os.writeOpcode(11)
            if(cost != 1) {
                os.writeOpcode(12)
                os.writeInt(cost)
            }
            if(members) os.writeOpcode(16)
            maleModel0?.let {
                os.writeOpcode(23)
                os.writeShort(maleModel0.toInt())
                os.writeByte(maleOffset.toInt())
            }
            maleModel1?.let {
                os.writeOpcode(24)
                os.writeShort(maleModel1.toInt())
            }
            femaleModel0?.let {
                os.writeOpcode(25)
                os.writeShort(femaleModel0.toInt())
                os.writeByte(femaleOffset.toInt())
            }
            femaleModel1?.let {
                os.writeOpcode(26)
                os.writeShort(femaleModel1.toInt())
            }
            groundActions.forEachIndexed { i, str ->
                if(str != null && str != "Hidden" && str != "Take") {
                    os.writeOpcode(30 + i)
                    os.writeString(str)
                }
            }
            iop.forEachIndexed { i, str ->
                if(str != null && str != "Hidden" && str != "Drop") {
                    os.writeOpcode(35 + i)
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
            if(shiftClickDropIndex.toInt() != -2) {
                os.writeOpcode(42)
                os.writeByte(shiftClickDropIndex.toInt())
            }
            if(tradable) os.writeOpcode(65)
            maleModel2?.let {
                os.writeOpcode(78)
                os.writeShort(maleModel2.toInt())
            }
            femaleModel2?.let {
                os.writeOpcode(79)
                os.writeShort(femaleModel2.toInt())
            }
            maleHeadModel?.let {
                os.writeOpcode(90)
                os.writeShort(maleHeadModel.toInt())
            }
            femaleHeadModel?.let {
                os.writeOpcode(91)
                os.writeShort(femaleHeadModel.toInt())
            }
            maleHeadModel2?.let {
                os.writeOpcode(92)
                os.writeShort(maleHeadModel2.toInt())
            }
            femaleHeadModel2?.let {
                os.writeOpcode(93)
                os.writeShort(femaleHeadModel2.toInt())
            }
            if(zan2d.toInt() != 0) {
                os.writeOpcode(95)
                os.writeShort(zan2d.toInt())
            }
            notedId?.let {
                os.writeOpcode(97)
                os.writeShort(notedId.toInt())
            }
            notedTemplate?.let {
                os.writeOpcode(98)
                os.writeShort(notedTemplate.toInt())
            }
            countObj?.forEachIndexed { i, obj ->
                if(obj != null && countCo!![i] != null) {
                    os.writeOpcode(100 + i)
                    os.writeShort(obj.toInt())
                    os.writeShort(countCo[i]!!.toInt())
                }
            }
            if(resizeX.toInt() != 128) {
                os.writeOpcode(110)
                os.writeShort(resizeX.toInt())
            }
            if(resizeY.toInt() != 128) {
                os.writeOpcode(111)
                os.writeShort(resizeY.toInt())
            }
            if(resizeZ.toInt() != 128) {
                os.writeOpcode(112)
                os.writeShort(resizeZ.toInt())
            }
            if(ambient != 0) {
                os.writeOpcode(113)
                os.writeByte(ambient)
            }
            if(contrast != 0) {
                os.writeOpcode(114)
                os.writeByte(contrast)
            }
            if(team.toInt() != 0) {
                os.writeOpcode(115)
                os.writeByte(team.toInt())
            }
            boughtId?.let {
                os.writeOpcode(139)
                os.writeShort(boughtId.toInt())
            }
            boughtTemplate?.let {
                os.writeOpcode(140)
                os.writeShort(boughtTemplate.toInt())
            }
            placeholderId?.let {
                os.writeOpcode(148)
                os.writeShort(placeholderId.toInt())
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
        if (other !is ItemConfig) return false
        if (id != other.id) return false
        if (name != other.name) return false
        if (resizeX != other.resizeX) return false
        if (resizeY != other.resizeY) return false
        if (resizeZ != other.resizeZ) return false
        if (xan2d != other.xan2d) return false
        if (yan2d != other.yan2d) return false
        if (zan2d != other.zan2d) return false
        if (xoff2d != other.xoff2d) return false
        if (yoff2d != other.yoff2d) return false
        if (zoom2d != other.zoom2d) return false
        if (cost != other.cost) return false
        if (tradable != other.tradable) return false
        if (stackable != other.stackable) return false
        if (model != other.model) return false
        if (members != other.members) return false
        if (colorFind != other.colorFind) return false
        if (colorReplace != other.colorReplace) return false
        if (textureFind != other.textureFind) return false
        if (textureReplace != other.textureReplace) return false
        if (ambient != other.ambient) return false
        if (contrast != other.contrast) return false
        if (countCo != null) {
            if (other.countCo == null) return false
            if (!countCo.contentEquals(other.countCo)) return false
        } else if (other.countCo != null) return false
        if (countObj != null) {
            if (other.countObj == null) return false
            if (!countObj.contentEquals(other.countObj)) return false
        } else if (other.countObj != null) return false
        if (!groundActions.contentEquals(other.groundActions)) return false
        if (!iop.contentEquals(other.iop)) return false
        if (maleModel0 != other.maleModel0) return false
        if (maleModel1 != other.maleModel1) return false
        if (maleModel2 != other.maleModel2) return false
        if (maleOffset != other.maleOffset) return false
        if (maleHeadModel != other.maleHeadModel) return false
        if (maleHeadModel2 != other.maleHeadModel2) return false
        if (femaleModel0 != other.femaleModel0) return false
        if (femaleModel1 != other.femaleModel1) return false
        if (femaleModel2 != other.femaleModel2) return false
        if (femaleOffset != other.femaleOffset) return false
        if (femaleHeadModel != other.femaleHeadModel) return false
        if (femaleHeadModel2 != other.femaleHeadModel2) return false
        if (notedId != other.notedId) return false
        if (notedTemplate != other.notedTemplate) return false
        if (team != other.team) return false
        if (shiftClickDropIndex != other.shiftClickDropIndex) return false
        if (boughtId != other.boughtId) return false
        if (boughtTemplate != other.boughtTemplate) return false
        if (placeholderId != other.placeholderId) return false
        if (placeholderTemplateId != other.placeholderTemplateId) return false
        if (params != other.params) return false
        return true
    }

    @ExperimentalUnsignedTypes
    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + resizeX.hashCode()
        result = 31 * result + resizeY.hashCode()
        result = 31 * result + resizeZ.hashCode()
        result = 31 * result + xan2d.hashCode()
        result = 31 * result + yan2d.hashCode()
        result = 31 * result + zan2d.hashCode()
        result = 31 * result + xoff2d
        result = 31 * result + yoff2d
        result = 31 * result + zoom2d
        result = 31 * result + cost
        result = 31 * result + tradable.hashCode()
        result = 31 * result + stackable.hashCode()
        result = 31 * result + model.hashCode()
        result = 31 * result + members.hashCode()
        result = 31 * result + (colorFind?.hashCode() ?: 0)
        result = 31 * result + (colorReplace?.hashCode() ?: 0)
        result = 31 * result + (textureFind?.hashCode() ?: 0)
        result = 31 * result + (textureReplace?.hashCode() ?: 0)
        result = 31 * result + ambient
        result = 31 * result + contrast
        result = 31 * result + (countCo?.contentHashCode() ?: 0)
        result = 31 * result + (countObj?.contentHashCode() ?: 0)
        result = 31 * result + groundActions.contentHashCode()
        result = 31 * result + iop.contentHashCode()
        result = 31 * result + (maleModel0?.hashCode() ?: 0)
        result = 31 * result + (maleModel1?.hashCode() ?: 0)
        result = 31 * result + (maleModel2?.hashCode() ?: 0)
        result = 31 * result + maleOffset.hashCode()
        result = 31 * result + (maleHeadModel?.hashCode() ?: 0)
        result = 31 * result + (maleHeadModel2?.hashCode() ?: 0)
        result = 31 * result + (femaleModel0?.hashCode() ?: 0)
        result = 31 * result + (femaleModel1?.hashCode() ?: 0)
        result = 31 * result + (femaleModel2?.hashCode() ?: 0)
        result = 31 * result + femaleOffset.hashCode()
        result = 31 * result + (femaleHeadModel?.hashCode() ?: 0)
        result = 31 * result + (femaleHeadModel2?.hashCode() ?: 0)
        result = 31 * result + (notedId?.hashCode() ?: 0)
        result = 31 * result + (notedTemplate?.hashCode() ?: 0)
        result = 31 * result + team.hashCode()
        result = 31 * result + shiftClickDropIndex
        result = 31 * result + (boughtId?.hashCode() ?: 0)
        result = 31 * result + (boughtTemplate?.hashCode() ?: 0)
        result = 31 * result + (placeholderId?.hashCode() ?: 0)
        result = 31 * result + (placeholderTemplateId?.hashCode() ?: 0)
        result = 31 * result + (params?.hashCode() ?: 0)
        return result
    }

    companion object : ConfigCompanion<ItemConfig>() {
        override val id = 10

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, buffer: ByteBuffer): ItemConfig {
            var name = "null"
            var resizeX: UShort = 128u
            var resizeY: UShort = 128u
            var resizeZ: UShort = 128u
            var xan2d: UShort = 0u
            var yan2d: UShort = 0u
            var zan2d: UShort = 0u
            var xoff2d: Short = 0
            var yoff2d: Short = 0
            var zoom2d = 200000
            var cost = 1
            var tradable = false
            var stackable = false
            var model: UShort = 0u
            var members = false
            var colorFind: UShortArray? = null
            var colorReplace: UShortArray? = null
            var textureFind: UShortArray? = null
            var textureReplace: UShortArray? = null
            var ambient = 0
            var contrast = 0
            var countCo: Array<UShort?>? = null
            var countObj: Array<UShort?>? = null
            val groundActions = arrayOf(null, null, "Take", null, null)
            val iop= arrayOf(null, null, null, null, "Drop")
            var maleModel0: UShort? = null
            var maleModel1: UShort? = null
            var maleModel2: UShort? = null
            var maleOffset: UByte = 0u
            var maleHeadModel: UShort? = null
            var maleHeadModel2: UShort? = null
            var femaleModel0: UShort? = null
            var femaleModel1: UShort? = null
            var femaleModel2: UShort? = null
            var femaleOffset: UByte = 0u
            var femaleHeadModel: UShort? = null
            var femaleHeadModel2: UShort? = null
            var notedId: UShort? = null
            var notedTemplate: UShort? = null
            var team: UByte = 0u
            var shiftClickDropIndex: Byte = -2
            var boughtId: UShort? = null
            var boughtTemplate: UShort? = null
            var placeholderId: UShort? = null
            var placeholderTemplateId: UShort? = null
            var params: HashMap<Int, Any>? = null

            decoder@ while (true) {
                val opcode = buffer.uByte.toInt()
                when (opcode) {
                    0 -> break@decoder
                    1 -> model = buffer.uShort
                    2 -> name = buffer.string
                    4 -> zoom2d = buffer.uShort.toInt()
                    5 -> xan2d = buffer.uShort
                    6 -> yan2d = buffer.uShort
                    7 -> {
                        val temp= buffer.uShort
                        xoff2d = if (temp.toInt() > Short.MAX_VALUE) {
                            (temp.toInt() - 65536).toShort()
                        } else {
                            temp.toShort()
                        }
                    }
                    8 -> {
                        val temp= buffer.uShort
                        UShort.MAX_VALUE
                        yoff2d = if (temp.toInt() > Short.MAX_VALUE) {
                            (temp.toInt() - 65536).toShort()
                        } else {
                            temp.toShort()
                        }
                    }
                    11 -> stackable = true
                    12 -> cost = buffer.int
                    16 -> members = true
                    23 -> {
                        maleModel0 = buffer.uShort
                        maleOffset = buffer.uByte
                    }
                    24 -> maleModel1 = buffer.uShort
                    25 -> {
                        femaleModel0 = buffer.uShort
                        femaleOffset = buffer.uByte
                    }
                    26 -> femaleModel1 = buffer.uShort
                    in 30..34 -> groundActions[opcode - 30] = buffer.string.takeIf { it != "Hidden" }
                    in 35..39 -> iop[opcode - 35] = buffer.string
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
                    42 -> shiftClickDropIndex = buffer.get()
                    65 -> tradable = true
                    78 -> maleModel2 = buffer.uShort
                    79 -> femaleModel2 = buffer.uShort
                    90 -> maleHeadModel = buffer.uShort
                    91 -> femaleHeadModel = buffer.uShort
                    92 -> maleHeadModel2 = buffer.uShort
                    93 -> femaleHeadModel2 = buffer.uShort
                    95 -> zan2d = buffer.uShort
                    97 -> notedId = buffer.uShort
                    98 -> notedTemplate = buffer.uShort
                    in 100..109 -> {
                        if (countObj == null) {
                            countObj = arrayOfNulls<UShort?>(10)
                            countCo = arrayOfNulls<UShort?>(10)
                        }
                        countObj[opcode - 100] = buffer.uShort
                        countCo!![opcode - 100] = buffer.uShort
                    }
                    110 -> resizeX = buffer.uShort
                    111 -> resizeY = buffer.uShort
                    112 -> resizeZ = buffer.uShort
                    113 -> ambient = buffer.get().toInt()
                    114 -> contrast = buffer.get() * 5
                    115 -> team = buffer.uByte
                    139 -> boughtId = buffer.uShort
                    140 -> boughtTemplate = buffer.uShort
                    148 -> placeholderId = buffer.uShort
                    149 -> placeholderTemplateId = buffer.uShort
                    249 -> params = buffer.params
                    else -> throw IOException("Did not recognise opcode $opcode")
                }
            }
            return ItemConfig(id, name, resizeX, resizeY, resizeZ, xan2d, yan2d, zan2d, xoff2d, yoff2d, zoom2d, cost,
                tradable, stackable, model, members, colorFind, colorReplace, textureFind,
                textureReplace, ambient, contrast, countCo, countObj, groundActions, iop, maleModel0,
                maleModel1, maleModel2, maleOffset, maleHeadModel, maleHeadModel2, femaleModel0, femaleModel1,
                femaleModel2, femaleOffset, femaleHeadModel, femaleHeadModel2, notedId, notedTemplate, team,
                shiftClickDropIndex, boughtId, boughtTemplate, placeholderId, placeholderTemplateId, params
            )

        }
    }
}