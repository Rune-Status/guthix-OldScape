package io.github.bartvhelvert.jagex.fs.osrs

import io.github.bartvhelvert.jagex.fs.Archive
import io.github.bartvhelvert.jagex.fs.Cache
import java.io.DataOutputStream
import java.nio.ByteBuffer

class ConfigDictionary : Dictionary() {
    override val id = 2

    companion object {
        fun load(cache: Cache) {
            //TODO
        }
    }
}

abstract class ConfigFile(val id: Int) {
    abstract fun encode()

    protected fun DataOutputStream.writeOpcode(opcode: Int) = writeByte(opcode)
}

abstract class ConfigFileCompanion<out T: ConfigFile> {
    abstract val id: Int

    fun load(archive: Archive): Map<Int, T> {
        val configs = mutableMapOf<Int, T>()
        archive.attributes.fileAttributes.forEach {
            configs[it.key] = decode(it.key, archive.fileData[it.key])
        }
        return configs
    }

    abstract fun decode(id: Int, buffer: ByteBuffer): T
}

