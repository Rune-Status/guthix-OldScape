package io.github.bartvhelvert.jagex.fs.osrs.xtea

@ExperimentalUnsignedTypes
class MapXtea(val id: Int, val key: IntArray) {
    val x get() = id shr 8
    val y get() = id and UByte.MAX_VALUE.toInt()
}