package io.github.bartvhelvert.jagex.fs.osrs.config

import io.github.bartvhelvert.jagex.fs.io.uByte
import io.github.bartvhelvert.jagex.fs.io.uShort
import java.io.IOException
import java.nio.ByteBuffer

@ExperimentalUnsignedTypes
data class InventoryConfig(override val id: Int) : Config(id) {
    var capacity: UShort = 0u

    @ExperimentalUnsignedTypes
    override fun encode(): ByteBuffer = if(capacity.toInt() != 0) {
        ByteBuffer.allocate(4).apply {
            put(2)
            putShort(capacity.toShort())
            put(0)
        }
    } else {
        ByteBuffer.allocate(1).apply {
            put(0)
        }
    }

    companion object : ConfigCompanion<InventoryConfig>() {
        override val id = 5

        @ExperimentalUnsignedTypes
        override fun decode(id: Int, buffer: ByteBuffer): InventoryConfig {
            val inventoryConfig = InventoryConfig(id)
            decoder@ while (true) {
                val opcode = buffer.uByte.toInt()
                when (opcode) {
                    0 -> break@decoder
                    2 -> inventoryConfig.capacity = buffer.uShort
                    else -> throw IOException("Did not recognise opcode $opcode")
                }
            }
            return inventoryConfig
        }
    }
}