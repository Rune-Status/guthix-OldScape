package io.github.bartvhelvert.jagex.fs.osrs

import io.github.bartvhelvert.jagex.fs.io.string
import io.github.bartvhelvert.jagex.fs.io.uByte
import io.github.bartvhelvert.jagex.fs.io.uMedium
import java.nio.ByteBuffer

@ExperimentalUnsignedTypes
val ByteBuffer.params get(): HashMap<Int, Any> {
    fun nextPowerOfTwo(value: Int): Int {
        var result = value
        --result
        result = result or result.ushr(1)
        result = result or result.ushr(2)
        result = result or result.ushr(4)
        result = result or result.ushr(8)
        result = result or result.ushr(16)
        println(result)
        return result + 1
    }

    val size = uByte.toInt()
    val params = HashMap<Int, Any>(nextPowerOfTwo(uByte.toInt()))
    for(i in 0 until size) {
        val isString = uByte.toInt() == 1
        params[uMedium] = if(isString) string else int
    }
    return params
}

