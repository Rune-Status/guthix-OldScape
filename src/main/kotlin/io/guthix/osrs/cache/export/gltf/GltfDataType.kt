package io.guthix.osrs.cache.export.gltf

enum class GltfDataType(val opcode: Int, val size: Int) {
    BYTE(5120, 1),
    UNSIGNED_BYTE(5121, BYTE.size),
    SHORT(5122, 2),
    UNSIGNED_SHORT(5123, SHORT.size),
    UNSIGNED_INT(5125, 4),
    FLOAT(5126, 4)
}