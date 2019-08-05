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
package io.guthix.osrs.cache.config

import io.guthix.cache.js5.io.*
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.nio.ByteBuffer

@ExperimentalUnsignedTypes
data class HitMarkConfig(override val id: Int) : Config(id) {
    var field3353: Int? = null
    var field3364 = 16777215
    var field3355: UShort = 70u
    var field3358: Int? = null
    var field3357: Int? = null
    var field3350: Int? = null
    var field3359: Int? = null
    var field3365: Short = 0
    var field3361: Short = 0
    var field3354: UShort? = null
    var field3363: String? = ""
    var field3360: UByte? = null
    var field3347: Short = 0
    var varpId: UShort? = null
    var varp32Id: UShort? = null
    var configs: Array<UShort?>? = null
    @ExperimentalUnsignedTypes
    override fun encode(): ByteBuffer {
        val byteStr = ByteArrayOutputStream()
        DataOutputStream(byteStr).use { os ->
            field3353?.let {
                os.writeOpcode(1)
                os.writeNullableLargeSmart(field3353)
            }
            if(field3364 != 16777215) {
                os.writeOpcode(2)
                os.writeMedium(field3364)
            }
            field3358?.let {
                os.writeOpcode(3)
                os.writeNullableLargeSmart(field3358)
            }
            field3350?.let {
                os.writeOpcode(4)
                os.writeNullableLargeSmart(field3350)
            }
            field3357?.let {
                os.writeOpcode(5)
                os.writeNullableLargeSmart(field3357)
            }
            field3359?.let {
                os.writeOpcode(6)
                os.writeNullableLargeSmart(field3359)
            }
            if(field3365.toInt() != 0) {
                os.writeOpcode(7)
                os.writeShort(field3365.toInt())
            }
            if(field3363 != "" && field3363 != null) {
                os.writeOpcode(8)
                os.writeString(field3363!!)
            }
            if(field3355.toInt() != 70) {
                os.writeOpcode(9)
                os.writeShort(field3355.toInt())
            }
            if(field3361.toInt() != 0) {
                os.writeOpcode(10)
                os.writeShort(field3361.toInt())
            }
            field3354?.let {
                if(field3354!!.toInt() == 0) {
                    os.writeOpcode(11)
                } else {
                    os.writeOpcode(14)
                    os.writeShort(field3354!!.toInt())
                }
            }
            field3360?.let {
                os.writeOpcode(12)
                os.writeByte(field3360!!.toInt())
            }
            if(field3347.toInt() != 0) {
                os.writeOpcode(13)
                os.writeShort(field3347.toInt())
            }
            if(configs != null) {
                if(configs!!.last() != null) os.writeOpcode(18) else os.writeOpcode(17)
                if(varpId == null) os.writeShort(UShort.MAX_VALUE.toInt()) else os.writeShort(varpId!!.toInt())
                if(varp32Id == null) os.writeShort(UShort.MAX_VALUE.toInt()) else os.writeShort(varp32Id!!.toInt())
                if(configs!!.last() != null) os.writeShort(configs!!.last()!!.toInt())
                os.writeByte(configs!!.size - 2)
                for(i in 0 until configs!!.size - 1) {
                    if(configs!![i] != null) {
                        os.writeShort(UShort.MAX_VALUE.toInt())
                    } else {
                        os.writeShort(configs!![i]!!.toInt())
                    }
                }
            }
            os.writeOpcode(0)
        }
        return ByteBuffer.wrap(byteStr.toByteArray())
    }

    @ExperimentalUnsignedTypes
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HitMarkConfig) return false
        if (id != other.id) return false
        if (field3353 != other.field3353) return false
        if (field3364 != other.field3364) return false
        if (field3355 != other.field3355) return false
        if (field3358 != other.field3358) return false
        if (field3357 != other.field3357) return false
        if (field3350 != other.field3350) return false
        if (field3359 != other.field3359) return false
        if (field3365 != other.field3365) return false
        if (field3361 != other.field3361) return false
        if (field3354 != other.field3354) return false
        if (field3363 != other.field3363) return false
        if (field3360 != other.field3360) return false
        if (field3347 != other.field3347) return false
        if (varpId != other.varpId) return false
        if (varp32Id != other.varp32Id) return false
        if (configs != null) {
            if (other.configs == null) return false
            if (!configs!!.contentEquals(other.configs!!)) return false
        } else if (other.configs != null) return false
        return true
    }

    @ExperimentalUnsignedTypes
    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (field3353 ?: 0)
        result = 31 * result + field3364
        result = 31 * result + field3355.hashCode()
        result = 31 * result + (field3358 ?: 0)
        result = 31 * result + (field3357 ?: 0)
        result = 31 * result + (field3350 ?: 0)
        result = 31 * result + (field3359 ?: 0)
        result = 31 * result + field3365
        result = 31 * result + field3361
        result = 31 * result + (field3354?.hashCode() ?: 0)
        result = 31 * result + (field3363?.hashCode() ?: 0)
        result = 31 * result + (field3360?.hashCode() ?: 0)
        result = 31 * result + field3347
        result = 31 * result + (varpId?.hashCode() ?: 0)
        result = 31 * result + (varp32Id?.hashCode() ?: 0)
        result = 31 * result + (configs?.contentHashCode() ?: 0)
        return result
    }

    companion object : ConfigCompanion<HitMarkConfig>() {
        override val id = 32

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, data: ByteArray): HitMarkConfig {
            val buffer = ByteBuffer.wrap(data)
            val hitmarkConfig = HitMarkConfig(id)
            decoder@ while (true) {
                when(val opcode = buffer.uByte.toInt()) {
                    0 -> break@decoder
                    1 -> hitmarkConfig.field3353 = buffer.nullableLargeSmart
                    2 -> hitmarkConfig.field3364 = buffer.uMedium
                    3 -> hitmarkConfig.field3358 = buffer.nullableLargeSmart
                    4 -> hitmarkConfig.field3350 = buffer.nullableLargeSmart
                    5 -> hitmarkConfig.field3357 = buffer.nullableLargeSmart
                    6 -> hitmarkConfig.field3359 = buffer.nullableLargeSmart
                    7 -> hitmarkConfig.field3365 = buffer.short
                    8 -> hitmarkConfig.field3363 = buffer.prefixedString
                    9 -> hitmarkConfig.field3355 = buffer.uShort
                    10 -> hitmarkConfig.field3361 = buffer.short
                    11 -> hitmarkConfig.field3354 = 0u
                    12 -> hitmarkConfig.field3360 = buffer.uByte
                    13 -> hitmarkConfig.field3347 = buffer.short
                    14 -> hitmarkConfig.field3354 = buffer.uShort
                    17, 18 -> {
                        hitmarkConfig.varpId = buffer.uShort
                        if(hitmarkConfig.varpId!!.toInt() == UShort.MAX_VALUE.toInt()) hitmarkConfig.varpId = null
                        hitmarkConfig.varp32Id = buffer.uShort
                        if(hitmarkConfig.varp32Id!!.toInt() == UShort.MAX_VALUE.toInt()) hitmarkConfig.varp32Id = null
                        val lastEntry = if(opcode == 18) {
                            val entry = buffer.uShort
                            if(entry == UShort.MAX_VALUE) null else entry
                        } else null
                        val size = buffer.uByte.toInt()
                        hitmarkConfig.configs = arrayOfNulls(size + 2)
                        for(i in 0 until hitmarkConfig.configs!!.size - 1) {
                            var config: UShort? = buffer.uShort
                            if(config!!.toInt() == UShort.MAX_VALUE.toInt()) config = null
                            hitmarkConfig.configs!![i] = config
                        }
                        if(opcode == 18) {
                            hitmarkConfig. configs!![size + 1] = lastEntry
                        }
                    }
                    else -> throw IOException("Did not recognise opcode $opcode.")
                }
            }
            return hitmarkConfig
        }
    }
}