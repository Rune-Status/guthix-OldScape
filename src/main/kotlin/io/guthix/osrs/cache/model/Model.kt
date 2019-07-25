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
package io.guthix.osrs.cache.model

import io.guthix.cache.js5.io.smallSmart
import io.guthix.cache.js5.io.uByte
import io.guthix.cache.js5.io.uShort
import java.nio.ByteBuffer

@ExperimentalUnsignedTypes
class Model(var id: Int) {
    var vertexCount = 0
    var vertexPositionsX: IntArray? = null
    var vertexPositionsY: IntArray? = null
    var vertexPositionsZ: IntArray? = null
    var faceCount: Int = 0
    var faceVertexIndices1: IntArray? = null
    var faceVertexIndices2: IntArray? = null
    var faceVertexIndices3: IntArray? = null
    var faceAlphas: ByteArray? = null
    var faceColors: UShortArray? = null
    var faceRenderPriorities: ByteArray? = null
    var faceRenderTypes: ByteArray? = null
    var textureTriangleCount: Int = 0
    var textureTriangleVertexIndices1: UShortArray? = null
    var textureTriangleVertexIndices2: UShortArray? = null
    var textureTriangleVertexIndices3: UShortArray? = null
    var texturePrimaryColors: UShortArray? = null
    var faceTextures: Array<UShort?>? = null
    var textureCoordinates: Array<UByte?>? = null
    var textureRenderTypes: ByteArray? = null
    var vertexSkins: IntArray? = null
    var faceSkins: IntArray? = null
    var priority: Byte = 0
    var aShortArray2574: UShortArray? = null
    var aShortArray2575: UShortArray? = null
    var aShortArray2577: UShortArray? = null
    var aShortArray2578: UShortArray? = null
    var aByteArray2580: ByteArray? = null
    var aShortArray2586: UShortArray? = null
    private var vertexGroups: Array<IntArray>? = null
    private var origVX: IntArray? = null
    private var origVY: IntArray? = null
    private var origVZ: IntArray? = null
    var maxPriority: Int = 0
    var animOffsetX: Int = 0
    var animOffsetY: Int = 0
    var animOffsetZ: Int = 0

    companion object {
        fun decode(id: Int, buffer: ByteBuffer): Model {
            val model = Model(id)
            return if (buffer.get(buffer.limit() - 1).toInt() == -1 && buffer.get(buffer.limit() - 2).toInt() == -1) {
                decodeNew(model, buffer)
            } else {
                decodeOld(model, buffer)
            }
        }

        @ExperimentalUnsignedTypes
        fun decodeNew(model: Model, buffer: ByteBuffer): Model {
            val var2 = buffer.duplicate()
            val var3 = buffer.duplicate()
            val var4 = buffer.duplicate()
            val var5 = buffer.duplicate()
            val var6 = buffer.duplicate()
            val var7 = buffer.duplicate()
            val var8 = buffer.duplicate()
            var2.position(var2.limit() - 23)
            val verticeCount = var2.uShort.toInt()
            val triangleCount = var2.uShort.toInt()
            val textureTriangleCount = var2.uByte.toInt()
            val var13 = var2.uByte.toInt()
            val modelPriority = var2.uByte
            val var50 = var2.uByte.toInt()
            val var17 = var2.uByte.toInt()
            val modelTexture = var2.uByte.toInt()
            val modelVertexSkins = var2.uByte.toInt()
            val var20 = var2.uShort.toInt()
            val var21 = var2.uShort.toInt()
            val var42 = var2.uShort.toInt()
            val var22 = var2.uShort.toInt()
            val var38 = var2.uShort.toInt()
            var textureAmount = 0
            var var23 = 0
            var var29 = 0
            if (textureTriangleCount > 0) {
                model.textureRenderTypes = ByteArray(textureTriangleCount)
                var2.position(0)
                for (i in 0 until textureTriangleCount) {
                    model.textureRenderTypes!![i] = var2.get()
                    val renderType = model.textureRenderTypes!![i]
                    if (renderType.toInt() == 0) {
                        textureAmount++
                    }
                    if (renderType in 1..3) {
                        var23++
                        if (renderType.toInt() == 2) {
                            var29++
                        }
                    }
                }
            }
            val renderTypePos = textureTriangleCount + verticeCount
            var position = renderTypePos
            if (var13 == 1) {
                position += triangleCount
            }
            val var49 = position
            position += triangleCount
            val priorityPos = position
            if (modelPriority == UByte.MAX_VALUE) {
                position += triangleCount
            }

            val triangleSkinPos = position
            if (var17 == 1) {
                position += triangleCount
            }

            val var35 = position
            if (modelVertexSkins == 1) {
                position += verticeCount
            }

            val alphaPos = position
            if (var50 == 1) {
                position += triangleCount
            }

            val var11 = position
            position += var22
            val texturePos = position
            if (modelTexture == 1) {
                position += triangleCount * 2
            }
            val textureCoordPos = position
            position += var38
            val colorPos = position
            position += triangleCount * 2
            val var40 = position
            position += var20
            val var41 = position
            position += var21
            val var9 = position
            position += var42
            val var43 = position
            position += textureAmount * 6
            val var37 = position
            position += var23 * 6
            val var48 = position
            position += var23 * 6
            val var56 = position
            position += var23 * 2
            val var45 = position
            position += var23
            val var46 = position
            position += var23 * 2 + var29 * 2
            model.vertexCount = verticeCount
            model.faceCount = triangleCount
            model.textureTriangleCount = textureTriangleCount
            model.vertexPositionsX = IntArray(verticeCount)
            model.vertexPositionsY = IntArray(verticeCount)
            model.vertexPositionsZ = IntArray(verticeCount)
            model.faceVertexIndices1 = IntArray(triangleCount)
            model.faceVertexIndices2 = IntArray(triangleCount)
            model.faceVertexIndices3 = IntArray(triangleCount)
            if (modelVertexSkins == 1) {
                model.vertexSkins = IntArray(verticeCount)
            }
            if (var13 == 1) {
                model.faceRenderTypes = ByteArray(triangleCount)
            }
            if (modelPriority == UByte.MAX_VALUE) {
                model.faceRenderPriorities = ByteArray(triangleCount)
            } else {
                model.priority = modelPriority.toByte()
            }
            if (var50 == 1) {
                model.faceAlphas = ByteArray(triangleCount)
            }
            if (var17 == 1) {
                model.faceSkins = IntArray(triangleCount)
            }
            if (modelTexture == 1) {
                model.faceTextures = arrayOfNulls(triangleCount)
            }
            if (modelTexture == 1 && textureTriangleCount > 0) {
                model.textureCoordinates = arrayOfNulls(triangleCount)
            }

            model.faceColors = UShortArray(triangleCount)
            if (textureTriangleCount > 0) {
                model.textureTriangleVertexIndices1 = UShortArray(textureTriangleCount)
                model.textureTriangleVertexIndices2 = UShortArray(textureTriangleCount)
                model.textureTriangleVertexIndices3 = UShortArray(textureTriangleCount)
                if (var23 > 0) {
                    model.aShortArray2574 = UShortArray(var23)
                    model.aShortArray2575 = UShortArray(var23)
                    model.aShortArray2586 = UShortArray(var23)
                    model.aShortArray2577 = UShortArray(var23)
                    model.aByteArray2580 = ByteArray(var23)
                    model.aShortArray2578 = UShortArray(var23)
                }

                if (var29 > 0) {
                    model.texturePrimaryColors = UShortArray(var29)
                }
            }

            var2.position(textureTriangleCount)
            var3.position(var40)
            var4.position(var41)
            var5.position(var9)
            var6.position(var35)
            var vX = 0
            var vY = 0
            var vZ = 0
            var vertexXOffset: Int
            var vertexYOffset: Int
            var vertexZOffset: Int
            for(i in 0 until verticeCount) {
                val vertexFlags = var2.uByte.toInt()
                vertexXOffset = 0
                vertexYOffset = 0
                vertexZOffset = 0
                if (vertexFlags and 1 != 0) {
                    vertexXOffset = var3.smallSmart.toInt()
                }
                if (vertexFlags and 2 != 0) {
                    vertexYOffset = var4.smallSmart.toInt()
                }
                if (vertexFlags and 4 != 0) {
                    vertexZOffset = var5.smallSmart.toInt()
                }
                model.vertexPositionsX!![i] = vX + vertexXOffset
                model.vertexPositionsY!![i] = vY + vertexYOffset
                model.vertexPositionsZ!![i] = vZ + vertexZOffset
                vX = model.vertexPositionsX!![i]
                vY = model.vertexPositionsY!![i]
                vZ = model.vertexPositionsZ!![i]
                if (modelVertexSkins == 1) {
                    model.vertexSkins!![i] = var6.uByte.toInt()
                }
            }

            var2.position(colorPos)
            var3.position(renderTypePos)
            var4.position(priorityPos)
            var5.position(alphaPos)
            var6.position(triangleSkinPos)
            var7.position(texturePos)
            var8.position(textureCoordPos)
            for(i in 0 until triangleCount) {
                model.faceColors!![i] = var2.uShort
                if (modelPriority == UByte.MAX_VALUE) {
                    model.faceRenderPriorities!![i] = var4.get()
                }
                if (var13 == 1) {
                    model.faceRenderTypes!![i] = var3.get()
                }
                if (var50 == 1) {
                    model.faceAlphas!![i] = var5.get()
                }
                if (var17 == 1) {
                    model.faceSkins!![i] = var6.uByte.toInt()
                }
                if (modelTexture == 1) {
                    model.faceTextures!![i] = (var7.uShort.toInt() - 1).toUShort()
                }
                if (model.textureCoordinates != null && model.faceTextures!![i] != null) {
                    model.textureCoordinates!![i] = (var8.uByte.toInt() - 1).toUByte()
                }
            }

            var2.position(var11)
            var3.position(var49)
            var trianglePointX = 0
            var trianglePointY = 0
            var trianglePointZ = 0
            vertexYOffset = 0
            for(triangle in 0 until triangleCount) {
                when(var3.uByte.toInt()) {
                    1 -> {
                        trianglePointX = var2.smallSmart.toInt() + vertexYOffset
                        trianglePointY = var2.smallSmart.toInt() + trianglePointX
                        trianglePointZ = var2.smallSmart.toInt() + trianglePointY
                        vertexYOffset = trianglePointZ
                        model.faceVertexIndices1!![triangle] = trianglePointX
                        model.faceVertexIndices2!![triangle] = trianglePointY
                        model.faceVertexIndices3!![triangle] = trianglePointZ
                    }
                    2 -> {
                        trianglePointY = trianglePointZ
                        trianglePointZ = var2.smallSmart.toInt() + vertexYOffset
                        vertexYOffset = trianglePointZ
                        model.faceVertexIndices1!![triangle] = trianglePointX
                        model.faceVertexIndices2!![triangle] = trianglePointY
                        model.faceVertexIndices3!![triangle] = trianglePointZ
                    }
                    3 -> {
                        trianglePointX = trianglePointZ
                        trianglePointZ = var2.smallSmart.toInt() + vertexYOffset
                        vertexYOffset = trianglePointZ
                        model.faceVertexIndices1!![triangle] = trianglePointX
                        model.faceVertexIndices2!![triangle] = trianglePointY
                        model.faceVertexIndices3!![triangle] = trianglePointZ
                    }
                    4 -> {
                        val resultTrianglePointx = trianglePointX
                        trianglePointX = trianglePointY
                        trianglePointY = resultTrianglePointx
                        trianglePointZ = var2.smallSmart.toInt() + vertexYOffset
                        vertexYOffset = trianglePointZ
                        model.faceVertexIndices1!![triangle] = trianglePointX
                        model.faceVertexIndices2!![triangle] = resultTrianglePointx
                        model.faceVertexIndices3!![triangle] = trianglePointZ
                    }
                }
            }

            var2.position(var43)
            var3.position(var37)
            var4.position(var48)
            var5.position(var56)
            var6.position(var45)
            var7.position(var46)
            for (texIndex in 0 until textureTriangleCount) {
                val type = model.textureRenderTypes!![texIndex].toInt() and 255
                if (type == 0) {
                    model.textureTriangleVertexIndices1!![texIndex] = var2.uShort
                    model.textureTriangleVertexIndices2!![texIndex] = var2.uShort
                    model.textureTriangleVertexIndices3!![texIndex] = var2.uShort
                }
                if (type == 1) {
                    model.textureTriangleVertexIndices1!![texIndex] = var3.uShort
                    model.textureTriangleVertexIndices2!![texIndex] = var3.uShort
                    model.textureTriangleVertexIndices3!![texIndex] = var3.uShort
                    model.aShortArray2574!![texIndex] = var4.uShort
                    model.aShortArray2575!![texIndex] = var4.uShort
                    model.aShortArray2586!![texIndex] = var4.uShort
                    model.aShortArray2577!![texIndex] = var5.uShort
                    model.aByteArray2580!![texIndex] = var6.get()
                    model.aShortArray2578!![texIndex] = var7.uShort
                }

                if (type == 2) {
                    model.textureTriangleVertexIndices1!![texIndex] = var3.uShort
                    model.textureTriangleVertexIndices2!![texIndex] = var3.uShort
                    model.textureTriangleVertexIndices3!![texIndex] = var3.uShort
                    model.aShortArray2574!![texIndex] = var4.uShort
                    model.aShortArray2575!![texIndex] = var4.uShort
                    model.aShortArray2586!![texIndex] = var4.uShort
                    model.aShortArray2577!![texIndex] = var5.uShort
                    model.aByteArray2580!![texIndex] = var6.get()
                    model.aShortArray2578!![texIndex] = var7.uShort
                    model.texturePrimaryColors!![texIndex] = var7.uShort
                }

                if (type == 3) {
                    model.textureTriangleVertexIndices1!![texIndex] = var3.uShort
                    model.textureTriangleVertexIndices2!![texIndex] = var3.uShort
                    model.textureTriangleVertexIndices3!![texIndex] = var3.uShort
                    model.aShortArray2574!![texIndex] = var4.uShort
                    model.aShortArray2575!![texIndex] = var4.uShort
                    model.aShortArray2586!![texIndex] = var4.uShort
                    model.aShortArray2577!![texIndex] = var5.uShort
                    model.aByteArray2580!![texIndex] = var6.get()
                    model.aShortArray2578!![texIndex] = var7.uShort
                }
            }

            var2.position(position)
            vertexZOffset = var2.uByte.toInt()
            if (vertexZOffset != 0) {
                var2.uShort
                var2.uShort
                var2.uShort
                var2.int
            }
            return model
        }

        fun decodeOld(model: Model, buffer: ByteBuffer): Model {
            var var2 = false
            var var43 = false
            val var5 = buffer.duplicate()
            val var39 = buffer.duplicate()
            val var26 = buffer.duplicate()
            val var9 = buffer.duplicate()
            val var3 = buffer.duplicate()
            var5.position(buffer.limit() - 18)
            val var10 = var5.uShort.toInt()
            val var11 = var5.uShort.toInt()
            val var12 = var5.uByte.toInt()
            val var13 = var5.uByte.toInt()
            val var14 = var5.uByte
            val var30 = var5.uByte.toInt()
            val var15 = var5.uByte.toInt()
            val var28 = var5.uByte.toInt()
            val var27 = var5.uShort.toInt()
            val var20 = var5.uShort.toInt()
            val var36 = var5.uShort.toInt()
            val var23 = var5.uShort.toInt()
            val var16: Int = 0
            var var46 = var16 + var10
            val var24 = var46
            var46 += var11
            val var25 = var46
            if (var14 == UByte.MAX_VALUE) {
                var46 += var11
            }

            val var4 = var46
            if (var15 == 1) {
                var46 += var11
            }

            val var42 = var46
            if (var13 == 1) {
                var46 += var11
            }

            val var37 = var46
            if (var28 == 1) {
                var46 += var10
            }

            val var29 = var46
            if (var30 == 1) {
                var46 += var11
            }

            val var44 = var46
            var46 += var23
            val var17 = var46
            var46 += var11 * 2
            val var32 = var46
            var46 += var12 * 6
            val var34 = var46
            var46 += var27
            val var35 = var46
            var46 += var20
            val var10000 = var46 + var36
            model.vertexCount = var10
            model.faceCount = var11
            model.textureTriangleCount = var12
            model.vertexPositionsX = IntArray(var10)
            model.vertexPositionsY = IntArray(var10)
            model.vertexPositionsZ = IntArray(var10)
            model.faceVertexIndices1 = IntArray(var11)
            model.faceVertexIndices2 = IntArray(var11)
            model.faceVertexIndices3 = IntArray(var11)
            if (var12 > 0) {
                model.textureRenderTypes = ByteArray(var12)
                model.textureTriangleVertexIndices1 = UShortArray(var12)
                model.textureTriangleVertexIndices2 = UShortArray(var12)
                model.textureTriangleVertexIndices3 = UShortArray(var12)
            }

            if (var28 == 1) {
                model.vertexSkins = IntArray(var10)
            }

            if (var13 == 1) {
                model.faceRenderTypes = ByteArray(var11)
                model.textureCoordinates = arrayOfNulls(var11)
                model.faceTextures = arrayOfNulls(var11)
            }

            if (var14 == UByte.MAX_VALUE) {
                model.faceRenderPriorities = ByteArray(var11)
            } else {
                model.priority = var14.toByte()
            }

            if (var30 == 1) {
                model.faceAlphas = ByteArray(var11)
            }

            if (var15 == 1) {
                model.faceSkins = IntArray(var11)
            }

            model.faceColors = UShortArray(var11)
            var5.position(var16)
            var39.position(var34)
            var26.position(var35)
            var9.position(var46)
            var3.position(var37)
            var var41 = 0
            var var33 = 0
            var var19 = 0

            var var6: Int
            var var7: Int
            var var8: Int
            var var18: Int
            var var31: Int
            for(i in 0 until var10) {
                var8 = var5.uByte.toInt()
                var31 = 0
                var6 = 0
                var7 = 0
                if (var8 and 1 != 0) {
                    var31 = var39.smallSmart.toInt()
                }
                if (var8 and 2 != 0) {
                    var6 = var26.smallSmart.toInt()
                }
                if (var8 and 4 != 0) {
                    var7 = var9.smallSmart.toInt()
                }
                model.vertexPositionsX!![i] = var41 + var31
                model.vertexPositionsY!![i] = var33 + var6
                model.vertexPositionsZ!![i] = var19 + var7
                var41 = model.vertexPositionsX!![i]
                var33 = model.vertexPositionsY!![i]
                var19 = model.vertexPositionsZ!![i]
                if (var28 == 1) {
                    model.vertexSkins!![i] = var3.uByte.toInt()
                }
            }

            var5.position(var17)
            var39.position(var42)
            var26.position(var25)
            var9.position(var29)
            var3.position(var4)
            for(i in 0 until var11) {
                model.faceColors!![i] = var5.uShort
                if (var13 == 1) {
                    var8 = var39.uByte.toInt()
                    if (var8 and 1 == 1) {
                        model.faceRenderTypes!![i] = 1
                        var2 = true
                    } else {
                        model.faceRenderTypes!![i] = 0
                    }

                    if (var8 and 2 == 2) {
                        model.textureCoordinates!![i] = (var8 shr 2).toUByte()
                        model.faceTextures!![i] = model.faceColors!![i]
                        model.faceColors!![i] = 127u
                        if (model.faceTextures!![i] != null) {
                            var43 = true
                        }
                    } else {
                        model.textureCoordinates!![i] = null
                        model.faceTextures!![i] = null
                    }
                }

                if (var14 == UByte.MAX_VALUE) {
                    model.faceRenderPriorities!![i] = var26.get()
                }

                if (var30 == 1) {
                    model.faceAlphas!![i] = var9.get()
                }

                if (var15 == 1) {
                    model.faceSkins!![i] = var3.uByte.toInt()
                }
            }

            var5.position(var44)
            var39.position(var24)
            var18 = 0
            var8 = 0
            var31 = 0
            var6 = 0

            var var21: Int
            var var22: Int
            var7 = 0
            for(i in 0 until var11) {
                var22 = var39.uByte.toInt()
                if (var22 == 1) {
                    var18 = var5.smallSmart.toInt() + var6
                    var8 = var5.smallSmart.toInt() + var18
                    var31 = var5.smallSmart.toInt() + var8
                    var6 = var31
                    model.faceVertexIndices1!![var7] = var18
                    model.faceVertexIndices2!![var7] = var8
                    model.faceVertexIndices3!![var7] = var31
                }

                if (var22 == 2) {
                    var8 = var31
                    var31 = var5.smallSmart.toInt() + var6
                    var6 = var31
                    model.faceVertexIndices1!![var7] = var18
                    model.faceVertexIndices2!![var7] = var8
                    model.faceVertexIndices3!![var7] = var31
                }

                if (var22 == 3) {
                    var18 = var31
                    var31 = var5.smallSmart.toInt() + var6
                    var6 = var31
                    model.faceVertexIndices1!![var7] = var18
                    model.faceVertexIndices2!![var7] = var8
                    model.faceVertexIndices3!![var7] = var31
                }

                if (var22 == 4) {
                    var21 = var18
                    var18 = var8
                    var8 = var21
                    var31 = var5.smallSmart.toInt() + var6
                    var6 = var31
                    model.faceVertexIndices1!![var7] = var18
                    model.faceVertexIndices2!![var7] = var21
                    model.faceVertexIndices3!![var7] = var31
                }
                ++var7
            }

            var5.position(var32)

            var7 = 0
            for(i in 0 until var12) {
                model.textureRenderTypes!![var7] = 0
                model.textureTriangleVertexIndices1!![i] = var5.uShort
                model.textureTriangleVertexIndices2!![i] = var5.uShort
                model.textureTriangleVertexIndices3!![i] = var5.uShort
            }

            if (model.textureCoordinates != null) {
                var var45 = false

                var22 = 0
                for(i in 0 until var11) {
                    if (model.textureCoordinates!![var22] != null) {
                        var21 = model.textureCoordinates!![var22]!!.toInt()
                        if (model.textureTriangleVertexIndices1!![var21].toInt() and '\uffff'.toInt() ==
                            model.faceVertexIndices1!![i]
                            && model.textureTriangleVertexIndices2!![var21].toInt() and '\uffff'.toInt() ==
                            model.faceVertexIndices2!![i]
                            && model.textureTriangleVertexIndices3!![var21].toInt() and '\uffff'.toInt() ==
                            model.faceVertexIndices3!![i]
                        ) {
                            model.textureCoordinates!![i] = null
                        } else {
                            var45 = true
                        }
                    }
                }
                if (!var45) {
                    model.textureCoordinates = null
                }
            }
            if (!var43) {
                model.faceTextures = null
            }
            if (!var2) {
                model.faceRenderTypes = null
            }
            return model
        }
    }
}