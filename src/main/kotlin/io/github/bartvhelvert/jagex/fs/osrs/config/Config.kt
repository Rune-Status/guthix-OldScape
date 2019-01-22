package io.github.bartvhelvert.jagex.fs.osrs.config

import io.github.bartvhelvert.jagex.fs.Archive
import java.io.DataOutputStream
import java.nio.ByteBuffer

abstract class Config(open val id: Int) {
    abstract fun encode(): ByteBuffer

    protected fun DataOutputStream.writeOpcode(opcode: Int) = writeByte(opcode)
}

abstract class ConfigCompanion<out T: Config> {
    abstract val id: Int

    @ExperimentalUnsignedTypes
    fun load(archive: Archive): Map<Int, T> {
        val configs = mutableMapOf<Int, T>()
        archive.files.forEach{ fileId, file ->
            configs[fileId] = decode(fileId, file.data)
        }
        return configs
    }

    @ExperimentalUnsignedTypes
    abstract fun decode(id: Int, buffer: ByteBuffer): T
}
