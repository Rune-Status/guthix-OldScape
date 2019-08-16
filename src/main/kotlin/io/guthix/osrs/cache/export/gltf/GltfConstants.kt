package io.guthix.osrs.cache.export.gltf

import de.javagl.jgltf.impl.v2.*

val jagexAsset: Asset by lazy {
    Asset().apply {
        version = "2.0"
        copyright = "Jagex Ltd."
    }
}

val indexAccessor: Accessor by lazy {
    Accessor().apply {
        bufferView = 0
        byteOffset = 0
        componentType = GltfDataType.UNSIGNED_BYTE.opcode
        count = 3
        type = "SCALAR"
        max = arrayOf(2)
        min = arrayOf(0)
    }
}

val indexBufferView: BufferView by lazy {
    BufferView().apply {
        buffer = 0
        byteOffset = 0
        byteLength = 3
        target = GltfBufferType.ELEMENT_ARRAY_BUFFER.opcode
    }
}

val singleNodeScene: Scene by lazy {
    Scene().apply {
        addNodes(0)
    }
}

val singleMeshNode: Node by lazy {
    Node().apply {
        mesh = 0
    }
}