package simpleroomgl.porthw6

import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.opengl.GL46C

class Model(
    val vao: Int,
    val posBuf: Int,
    val normBuf: Int,
    val uvBuf: Int,
    val tangentBuf: Int,
    val biTangentBuf: Int,
    val center: Vector2f
) {
    companion object {
        fun generatePositions(sideLen: Float): List<Float> {
            val positions = mutableListOf<Float>()

            // Back.
            positions.addAll(arrayOf(-sideLen, -sideLen, -sideLen)) // Bottom left.
            positions.addAll(arrayOf(sideLen, -sideLen, -sideLen)) // Bottom right.
            positions.addAll(arrayOf(sideLen, sideLen, -sideLen)) // Top right.
            positions.addAll(arrayOf(-sideLen, sideLen, -sideLen)) // Top left.

            // Front.
            positions.addAll(arrayOf(-sideLen, -sideLen, sideLen))
            positions.addAll(arrayOf(sideLen, -sideLen, sideLen))
            positions.addAll(arrayOf(sideLen, sideLen, sideLen))
            positions.addAll(arrayOf(-sideLen, sideLen, sideLen))

            // Left.
            positions.addAll(arrayOf(-sideLen, -sideLen, -sideLen))
            positions.addAll(arrayOf(-sideLen, -sideLen, sideLen))
            positions.addAll(arrayOf(-sideLen, sideLen, sideLen))
            positions.addAll(arrayOf(-sideLen, sideLen, -sideLen))

            // Right.
            positions.addAll(arrayOf(sideLen, -sideLen, -sideLen))
            positions.addAll(arrayOf(sideLen, -sideLen, sideLen))
            positions.addAll(arrayOf(sideLen, sideLen, sideLen))
            positions.addAll(arrayOf(sideLen, sideLen, -sideLen))

            // Bottom.
            positions.addAll(arrayOf(-sideLen, -sideLen, -sideLen))
            positions.addAll(arrayOf(sideLen, -sideLen, -sideLen))
            positions.addAll(arrayOf(sideLen, -sideLen, sideLen))
            positions.addAll(arrayOf(-sideLen, -sideLen, sideLen))

            // Top.
            positions.addAll(arrayOf(-sideLen, sideLen, -sideLen))
            positions.addAll(arrayOf(sideLen, sideLen, -sideLen))
            positions.addAll(arrayOf(sideLen, sideLen, sideLen))
            positions.addAll(arrayOf(-sideLen, sideLen, sideLen))

            return positions
        }

        fun generatePositionsVector(sideLen: Float): List<Vector3f> {
            // Define the model around the origin!
            val positions = mutableListOf<Vector3f>()

            // Back.
            positions.add(Vector3f(-sideLen, -sideLen, -sideLen)) // Bottom left.
            positions.add(Vector3f(sideLen, -sideLen, -sideLen)) // Bottom right.
            positions.add(Vector3f(sideLen, sideLen, -sideLen)) // Top right.
            positions.add(Vector3f(-sideLen, sideLen, -sideLen)) // Top left.

            // Front.
            positions.add(Vector3f(-sideLen, -sideLen, sideLen))
            positions.add(Vector3f(sideLen, -sideLen, sideLen))
            positions.add(Vector3f(sideLen, sideLen, sideLen))
            positions.add(Vector3f(-sideLen, sideLen, sideLen))

            // Left.
            positions.add(Vector3f(-sideLen, -sideLen, -sideLen))
            positions.add(Vector3f(-sideLen, -sideLen, sideLen))
            positions.add(Vector3f(-sideLen, sideLen, sideLen))
            positions.add(Vector3f(-sideLen, sideLen, -sideLen))

            // Right.
            positions.add(Vector3f(sideLen, -sideLen, -sideLen))
            positions.add(Vector3f(sideLen, -sideLen, sideLen))
            positions.add(Vector3f(sideLen, sideLen, sideLen))
            positions.add(Vector3f(sideLen, sideLen, -sideLen))

            // Bottom.
            positions.add(Vector3f(-sideLen, -sideLen, -sideLen))
            positions.add(Vector3f(sideLen, -sideLen, -sideLen))
            positions.add(Vector3f(sideLen, -sideLen, sideLen))
            positions.add(Vector3f(-sideLen, -sideLen, sideLen))

            // Top.
            positions.add(Vector3f(-sideLen, sideLen, -sideLen))
            positions.add(Vector3f(sideLen, sideLen, -sideLen))
            positions.add(Vector3f(sideLen, sideLen, sideLen))
            positions.add(Vector3f(-sideLen, sideLen, sideLen))

            return positions
        }

        fun generateIndices(): List<Int> {
            val indices = mutableListOf<Int>()

            for (i in 0..<36 step 4) {
                indices.addAll(arrayOf(i, i + 1, i + 2))
                indices.addAll(arrayOf(i, i + 2, i + 3))
            }

            return indices
        }

        fun generateNormals(): List<Float> {
            val normals = mutableListOf<Float>()

            normals.addAll(arrayOf(0f, 0f, -1f))
            normals.addAll(arrayOf(0f, 0f, -1f))
            normals.addAll(arrayOf(0f, 0f, -1f))
            normals.addAll(arrayOf(0f, 0f, -1f))

            normals.addAll(arrayOf(0f, 0f, 1f))
            normals.addAll(arrayOf(0f, 0f, 1f))
            normals.addAll(arrayOf(0f, 0f, 1f))
            normals.addAll(arrayOf(0f, 0f, 1f))

            normals.addAll(arrayOf(-1f, 0f, 0f))
            normals.addAll(arrayOf(-1f, 0f, 0f))
            normals.addAll(arrayOf(-1f, 0f, 0f))
            normals.addAll(arrayOf(-1f, 0f, 0f))

            normals.addAll(arrayOf(1f, 0f, 0f))
            normals.addAll(arrayOf(1f, 0f, 0f))
            normals.addAll(arrayOf(1f, 0f, 0f))
            normals.addAll(arrayOf(1f, 0f, 0f))

            normals.addAll(arrayOf(0f, -1f, 0f))
            normals.addAll(arrayOf(0f, -1f, 0f))
            normals.addAll(arrayOf(0f, -1f, 0f))
            normals.addAll(arrayOf(0f, -1f, 0f))

            normals.addAll(arrayOf(0f, 1f, 0f))
            normals.addAll(arrayOf(0f, 1f, 0f))
            normals.addAll(arrayOf(0f, 1f, 0f))
            normals.addAll(arrayOf(0f, 1f, 0f))

            return normals
        }

        fun generateUvs(): List<Float> {
            val uvs = mutableListOf<Float>()

            // Back.
            uvs.addAll(arrayOf(0.0f, 0.0f))
            uvs.addAll(arrayOf(1.0f, 0.0f))
            uvs.addAll(arrayOf(1.0f, 1.0f))
            uvs.addAll(arrayOf(0.0f, 1.0f))

            // Front.
            uvs.addAll(arrayOf(0.0f, 0.0f))
            uvs.addAll(arrayOf(1.0f, 0.0f))
            uvs.addAll(arrayOf(1.0f, 1.0f))
            uvs.addAll(arrayOf(0.0f, 1.0f))

            // Left.
            uvs.addAll(arrayOf(0.0f, 0.0f))
            uvs.addAll(arrayOf(1.0f, 0.0f))
            uvs.addAll(arrayOf(1.0f, 1.0f))
            uvs.addAll(arrayOf(0.0f, 1.0f))

            // Right.
            uvs.addAll(arrayOf(0.0f, 0.0f))
            uvs.addAll(arrayOf(1.0f, 0.0f))
            uvs.addAll(arrayOf(1.0f, 1.0f))
            uvs.addAll(arrayOf(0.0f, 1.0f))

            // Bottom.
            uvs.addAll(arrayOf(0.0f, 0.0f))
            uvs.addAll(arrayOf(1.0f, 0.0f))
            uvs.addAll(arrayOf(1.0f, 1.0f))
            uvs.addAll(arrayOf(0.0f, 1.0f))

            // Top.
            uvs.addAll(arrayOf(0.0f, 0.0f))
            uvs.addAll(arrayOf(1.0f, 0.0f))
            uvs.addAll(arrayOf(1.0f, 1.0f))
            uvs.addAll(arrayOf(0.0f, 1.0f))

            return uvs
        }

        fun generateUvsVector(): List<Vector2f> {
            val uvs = mutableListOf<Vector2f>()

            // Back.
            uvs.add(Vector2f(0.0f, 0.0f))
            uvs.add(Vector2f(1.0f, 0.0f))
            uvs.add(Vector2f(1.0f, 1.0f))
            uvs.add(Vector2f(0.0f, 1.0f))

            // Front.
            uvs.add(Vector2f(0.0f, 0.0f))
            uvs.add(Vector2f(1.0f, 0.0f))
            uvs.add(Vector2f(1.0f, 1.0f))
            uvs.add(Vector2f(0.0f, 1.0f))

            // Left.
            uvs.add(Vector2f(0.0f, 0.0f))
            uvs.add(Vector2f(1.0f, 0.0f))
            uvs.add(Vector2f(1.0f, 1.0f))
            uvs.add(Vector2f(0.0f, 1.0f))

            // Right.
            uvs.add(Vector2f(0.0f, 0.0f))
            uvs.add(Vector2f(1.0f, 0.0f))
            uvs.add(Vector2f(1.0f, 1.0f))
            uvs.add(Vector2f(0.0f, 1.0f))

            // Bottom.
            uvs.add(Vector2f(0.0f, 0.0f))
            uvs.add(Vector2f(1.0f, 0.0f))
            uvs.add(Vector2f(1.0f, 1.0f))
            uvs.add(Vector2f(0.0f, 1.0f))

            // Top.
            uvs.add(Vector2f(0.0f, 0.0f))
            uvs.add(Vector2f(1.0f, 0.0f))
            uvs.add(Vector2f(1.0f, 1.0f))
            uvs.add(Vector2f(0.0f, 1.0f))

            return uvs
        }

        fun generateTangents(
            positions: List<Vector3f>,
            uvs: List<Vector2f>,
            indices: List<Int>
        ): Pair<List<Float>, List<Float>> {
            val tangents = mutableListOf<Float>()
            val biTangents = mutableListOf<Float>()

            for (i in 0..<24 step 3) {
                val v0 = positions[indices[i]]
                val v1 = positions[indices[i + 1]]
                val v2 = positions[indices[i + 2]]

                val uv0 = uvs[indices[i]]
                val uv1 = uvs[indices[i + 1]]
                val uv2 = uvs[indices[i + 2]]

                val edge1 = v1.sub(v0, Vector3f())
                val edge2 = v2.sub(v0, Vector3f())

                val deltaUv1 = uv1.sub(uv0, Vector2f())
                val deltaUv2 = uv2.sub(uv0, Vector2f())

                val f = 1.0f / (deltaUv1.x * deltaUv2.y - deltaUv1.y * deltaUv2.x)
                val tangent =
                    (edge1.mul(deltaUv2.y, Vector3f()).sub(edge2.mul(deltaUv1.y, Vector3f()))).mul(f, Vector3f())
                val biTangent =
                    (edge2.mul(-deltaUv1.x, Vector3f()).sub(edge2.mul(deltaUv2.x, Vector3f()))).mul(f, Vector3f())

                tangents.addAll(arrayOf(tangent.x, tangent.y, tangent.z))
                tangents.addAll(arrayOf(tangent.x, tangent.y, tangent.z))
                tangents.addAll(arrayOf(tangent.x, tangent.y, tangent.z))

                biTangents.addAll(arrayOf(biTangent.x, biTangent.y, biTangent.z))
                biTangents.addAll(arrayOf(biTangent.x, biTangent.y, biTangent.z))
                biTangents.addAll(arrayOf(biTangent.x, biTangent.y, biTangent.z))
            }

            return Pair(tangents, biTangents)
        }
    }

    val model = Matrix4f()

    fun delete() {
        GL46C.glDeleteVertexArrays(vao)
        GL46C.glDeleteBuffers(posBuf)
        GL46C.glDeleteBuffers(normBuf)
        GL46C.glDeleteBuffers(uvBuf)
        GL46C.glDeleteBuffers(tangentBuf)
        GL46C.glDeleteBuffers(biTangentBuf)
    }
}