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

import io.guthix.cache.js5.Js5Group
import java.io.DataOutputStream
import java.nio.ByteBuffer

abstract class Config(open val id: Int) {
    abstract fun encode(): ByteBuffer

    protected fun DataOutputStream.writeOpcode(opcode: Int) = writeByte(opcode)
}

abstract class ConfigCompanion<out T: Config> {
    abstract val id: Int

    @ExperimentalUnsignedTypes
    fun load(archive: Js5Group): Map<Int, T> {
        val configs = mutableMapOf<Int, T>()
        archive.files.forEach{ (fileId, file) ->
            configs[fileId] = decode(fileId, file.data)
        }
        return configs
    }

    @ExperimentalUnsignedTypes
    abstract fun decode(id: Int, data: ByteArray): T
}
