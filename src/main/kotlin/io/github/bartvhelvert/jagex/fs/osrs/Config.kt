package io.github.bartvhelvert.jagex.fs.osrs

import io.github.bartvhelvert.jagex.fs.Archive
import io.github.bartvhelvert.jagex.fs.Dictionary
import io.github.bartvhelvert.jagex.fs.DictionaryCompanion
import io.github.bartvhelvert.jagex.fs.JagexCache
import io.github.bartvhelvert.jagex.fs.osrs.config.IdentKitConfig
import java.io.DataOutputStream
import java.nio.ByteBuffer

class ConfigDictionary(
    val identKitConfigs: Map<Int, IdentKitConfig>
) : Dictionary {
    companion object : DictionaryCompanion<ConfigDictionary>() {
        override val id = 2

        @ExperimentalUnsignedTypes
        override fun load(cache: JagexCache): ConfigDictionary = ConfigDictionary(
            IdentKitConfig.load(cache.readArchive(id, IdentKitConfig.id))
        )
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
