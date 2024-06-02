package simpleroomgl.gfx

import org.joml.Vector2f
import org.joml.Vector3f

data class Vertex(
    val position: Vector3f = Vector3f(),
    val normal: Vector3f = Vector3f(),
    val uv: Vector2f = Vector2f()
) {
    companion object {
        // - 3 floats for position.
        // - 3 floats for normal.
        // - 2 floats for texture coordinates (UVs).
        // Multiplied by 4 since floats are 4 bytes.
        fun stride() = (3 + 3 + 2) * Float.SIZE_BYTES

        fun positionOffset() = (0 * Float.SIZE_BYTES).toLong()
        fun normalOffset() = (3 * Float.SIZE_BYTES).toLong()
        fun uvOffset() = (6 * Float.SIZE_BYTES).toLong()
    }
}