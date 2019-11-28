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

data class ItemConfig(
    override val id: Int,
    val model: Int = 0,
    val name: String = "null",
    val zoom2d: Int = 2000,
    val xan2d: Int = 0,
    val yan2d: Int = 0,
    val zan2d: Int = 0,
    val xoff2d: Int = 0,
    val yoff2d: Int = 0,
    val stackable: Boolean = false,
    val cost: Int = 1,
    val members: Boolean = false,
    val groundActions: Array<String?> = arrayOf(null, null, "Take", null, null),
    val iop: Array<String?> = arrayOf(null, null, null, null, "Drop"),
    val shiftClickDropIndex: Byte = -2,
    val maleModel0: Int? = null,
    val maleModel1: Int? = null,
    val maleModel2: Int? = null,
    val maleOffset: Short = 0,
    val femaleModel0: Int? = null,
    val femaleModel1: Int? = null,
    val femaleModel2: Int? = null,
    val femaleOffset: Short = 0,
    val maleHeadModel: Int? = null,
    val maleHeadModel2: Int? = null,
    val femaleHeadModel: Int? = null,
    val femaleHeadModel2: Int? = null,
    val notedId: Int? = null,
    val notedTemplate: Int? = null,
    val resizeX: Int = 128,
    val resizeY: Int = 128,
    val resizeZ: Int = 128,
    val ambient: Byte = 0,
    val contrast: Byte = 0,
    val team: Short = 0,
    val tradable: Boolean = false,
    val colorFind: IntArray? = null,
    val colorReplace: IntArray? = null,
    val textureFind: IntArray? = null,
    val textureReplace: IntArray? = null,
    val countCo: IntArray? = null,
    val countObj: IntArray? = null,
    val boughtId: Int? = null,
    val boughtTemplate: Int? = null,
    val placeholderId: Int? = null,
    val placeholderTemplateId: Int? = null,
    val params: MutableMap<Int, Any>? = null

) : Config(id) {
    override fun encode(): ByteBuf {
        val data = Unpooled.buffer()
        if(model != 0) {
            data.writeOpcode(1)
            data.writeShort(model)
        }
        if(name != "null") {
            data.writeOpcode(2)
            data.writeStringCP1252(name)
        }
        if(zoom2d != 2000) {
            data.writeOpcode(4)
            data.writeShort(zoom2d)
        }
        if(xan2d != 0) {
            data.writeOpcode(5)
            data.writeShort(xan2d)
        }
        if(yan2d != 0) {
            data.writeOpcode(6)
            data.writeShort(yan2d)
        }
        if(xoff2d != 0) {
            data.writeOpcode(7)
            if(xoff2d < 0) data.writeShort(xoff2d + 65536) else data.writeShort(xoff2d)
        }
        if(yoff2d != 0) {
            data.writeOpcode(8)
            if(yoff2d < 0) data.writeShort(yoff2d + 65536) else data.writeShort(yoff2d)
        }
        if(stackable) data.writeOpcode(11)
        if(cost != 1) {
            data.writeOpcode(12)
            data.writeInt(cost)
        }
        if(members) data.writeOpcode(16)
        maleModel0?.let {
            data.writeOpcode(23)
            data.writeShort(it)
            data.writeByte(maleOffset.toInt())
        }
        maleModel1?.let {
            data.writeOpcode(24)
            data.writeShort(it)
        }
        femaleModel0?.let {
            data.writeOpcode(25)
            data.writeShort(it)
            data.writeByte(femaleOffset.toInt())
        }
        femaleModel1?.let {
            data.writeOpcode(26)
            data.writeShort(it)
        }
        groundActions.forEachIndexed { i, str ->
            if(str != null && str != "Hidden" && str != "Take") {
                data.writeOpcode(30 + i)
                data.writeStringCP1252(str)
            }
        }
        iop.forEachIndexed { i, str ->
            if(str != null && str != "Hidden" && str != "Drop") {
                data.writeOpcode(35 + i)
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
        if(shiftClickDropIndex.toInt() != -2) {
            data.writeOpcode(42)
            data.writeByte(shiftClickDropIndex.toInt())
        }
        if(tradable) data.writeOpcode(65)
        maleModel2?.let {
            data.writeOpcode(78)
            data.writeShort(it)
        }
        femaleModel2?.let {
            data.writeOpcode(79)
            data.writeShort(it)
        }
        maleHeadModel?.let {
            data.writeOpcode(90)
            data.writeShort(it)
        }
        femaleHeadModel?.let {
            data.writeOpcode(91)
            data.writeShort(it)
        }
        maleHeadModel2?.let {
            data.writeOpcode(92)
            data.writeShort(it)
        }
        femaleHeadModel2?.let {
            data.writeOpcode(93)
            data.writeShort(it)
        }
        if(zan2d != 0) {
            data.writeOpcode(95)
            data.writeShort(zan2d)
        }
        notedId?.let {
            data.writeOpcode(97)
            data.writeShort(it)
        }
        notedTemplate?.let {
            data.writeOpcode(98)
            data.writeShort(it)
        }
        countObj?.forEachIndexed { i, obj ->
            val countCo = countCo?.get(i) ?: throw IOException("Found OBJ but no CO.")  // ??
            data.writeOpcode(100 + i)
            data.writeShort(obj)
            data.writeShort(countCo)
        }
        if(resizeX != 128) {
            data.writeOpcode(110)
            data.writeShort(resizeX)
        }
        if(resizeY != 128) {
            data.writeOpcode(111)
            data.writeShort(resizeY)
        }
        if(resizeZ != 128) {
            data.writeOpcode(112)
            data.writeShort(resizeZ)
        }
        if(ambient.toInt() != 0) {
            data.writeOpcode(113)
            data.writeByte(ambient.toInt())
        }
        if(contrast.toInt() != 0) {
            data.writeOpcode(114)
            data.writeByte(contrast.toInt())
        }
        if(team.toInt() != 0) {
            data.writeOpcode(115)
            data.writeByte(team.toInt())
        }
        boughtId?.let {
            data.writeOpcode(139)
            data.writeShort(it)
        }
        boughtTemplate?.let {
            data.writeOpcode(140)
            data.writeShort(it)
        }
        placeholderId?.let {
            data.writeOpcode(148)
            data.writeShort(it)
        }
        params?.let {
            data.writeOpcode(249)
            data.writeParams(it)
        }
        data.writeOpcode(0)
        return data
    }

    companion object : ConfigCompanion<ItemConfig>() {
        override val id = 10

        override fun decode(id: Int, data: ByteBuf): ItemConfig {
            var model = 0
            var name = "null"
            var zoom2d = 2000
            var xan2d = 0
            var yan2d = 0
            var zan2d = 0
            var xoff2d = 0
            var yoff2d = 0
            var stackable = false
            var cost = 1
            var members = false
            val groundActions = arrayOf(null, null, "Take", null, null)
            val iop= arrayOf(null, null, null, null, "Drop")
            var shiftClickDropIndex: Byte = -2
            var maleModel0: Int? = null
            var maleModel1: Int? = null
            var maleModel2: Int? = null
            var maleOffset: Short = 0
            var femaleModel0: Int? = null
            var femaleModel1: Int? = null
            var femaleModel2: Int? = null
            var femaleOffset: Short = 0
            var maleHeadModel: Int? = null
            var maleHeadModel2: Int? = null
            var femaleHeadModel: Int? = null
            var femaleHeadModel2: Int? = null
            var notedId: Int? = null
            var notedTemplate: Int? = null
            var resizeX = 128
            var resizeY = 128
            var resizeZ = 128
            var ambient: Byte = 0
            var contrast: Byte = 0
            var team: Short = 0
            var tradable = false
            var colorFind: IntArray? = null
            var colorReplace: IntArray? = null
            var textureFind: IntArray? = null
            var textureReplace: IntArray? = null
            var countCo: IntArray? = null
            var countObj: IntArray? = null
            var boughtId: Int? = null
            var boughtTemplate: Int? = null
            var placeholderId: Int? = null
            var placeholderTemplateId: Int? = null
            var params: MutableMap<Int, Any>? = null
            decoder@ while (true) {
                when (val opcode = data.readUnsignedByte().toInt()) {
                    0 -> break@decoder
                    1 -> model = data.readUnsignedShort()
                    2 -> name = data.readStringCP1252()
                    4 -> zoom2d = data.readUnsignedShort()
                    5 -> xan2d = data.readUnsignedShort()
                    6 -> yan2d = data.readUnsignedShort()
                    7 -> {
                        val temp= data.readUnsignedShort()
                        xoff2d = if (temp > 32767) temp - 65536 else temp
                    }
                    8 -> {
                        val temp= data.readUnsignedShort()
                        yoff2d = if (temp > 32767) temp - 65536 else temp
                    }
                    11 -> stackable = true
                    12 -> cost = data.readInt()
                    16 -> members = true
                    23 -> {
                        maleModel0 = data.readUnsignedShort()
                        maleOffset = data.readUnsignedByte()
                    }
                    24 -> maleModel1 = data.readUnsignedShort()
                    25 -> {
                        femaleModel0 = data.readUnsignedShort()
                        femaleOffset = data.readUnsignedByte()
                    }
                    26 -> femaleModel1 = data.readUnsignedShort()
                    in 30..34 -> groundActions[opcode - 30] = data.readStringCP1252().takeIf {
                        it != "Hidden"
                    }
                    in 35..39 -> iop[opcode - 35] = data.readStringCP1252()
                    40 -> {
                        val colorsSize = data.readUnsignedByte().toInt()
                        colorFind = IntArray(colorsSize)
                        colorReplace = IntArray(colorsSize)
                        for (i in 0 until colorsSize) {
                            colorFind[i] = data.readUnsignedShort()
                            colorReplace[i] = data.readUnsignedShort()
                        }
                    }
                    41 -> {
                        val texturesSize = data.readUnsignedByte().toInt()
                        textureFind = IntArray(texturesSize)
                        textureReplace = IntArray(texturesSize)
                        for (i in 0 until texturesSize) {
                            textureFind[i] = data.readUnsignedShort()
                            textureReplace[i] = data.readUnsignedShort()
                        }
                    }
                    42 -> shiftClickDropIndex = data.readByte()
                    65 -> tradable = true
                    78 -> maleModel2 = data.readUnsignedShort()
                    79 -> femaleModel2 = data.readUnsignedShort()
                    90 -> maleHeadModel = data.readUnsignedShort()
                    91 -> femaleHeadModel = data.readUnsignedShort()
                    92 -> maleHeadModel2 = data.readUnsignedShort()
                    93 -> femaleHeadModel2 = data.readUnsignedShort()
                    95 -> zan2d = data.readUnsignedShort()
                    97 -> notedId = data.readUnsignedShort()
                    98 -> notedTemplate = data.readUnsignedShort()
                    in 100..109 -> {
                        if (countObj == null) {
                            countObj = IntArray(10)
                            countCo = IntArray(10)
                        }
                        countObj[opcode - 100] = data.readUnsignedShort()
                        countCo!![opcode - 100] = data.readUnsignedShort()
                    }
                    110 -> resizeX = data.readUnsignedShort()
                    111 -> resizeY = data.readUnsignedShort()
                    112 -> resizeZ = data.readUnsignedShort()
                    113 -> ambient = data.readByte()
                    114 -> contrast = data.readByte()
                    115 -> team = data.readUnsignedByte()
                    139 -> boughtId = data.readUnsignedShort()
                    140 -> boughtTemplate = data.readUnsignedShort()
                    148 -> placeholderId = data.readUnsignedShort()
                    149 -> placeholderTemplateId = data.readUnsignedShort()
                    249 -> params = data.readParams()
                    else -> throw IOException("Did not recognise opcode $opcode.")
                }
            }
            return ItemConfig(id, model, name, zoom2d, xan2d, yan2d, zan2d, xoff2d, yoff2d, stackable, cost, members,
                groundActions, iop, shiftClickDropIndex, maleModel0, maleModel1, maleModel2, maleOffset, femaleModel0,
                femaleModel1, femaleModel2, femaleOffset, maleHeadModel, maleHeadModel2, femaleHeadModel,
                femaleHeadModel2, notedId, notedTemplate, resizeX, resizeY, resizeZ, ambient, contrast, team, tradable,
                colorFind, colorReplace, textureFind, textureReplace, countCo, countObj, boughtId, boughtTemplate,
                placeholderId, placeholderTemplateId, params
            )
        }
    }
}