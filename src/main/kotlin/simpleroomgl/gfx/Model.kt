package simpleroomgl.gfx

import simpleroomgl.gfx.gl.Disposable
import org.joml.Matrix4f
import org.joml.Vector3f

class Model(
    val meshes: List<Mesh>,
    val position: Vector3f,
    val rotation: Vector3f,
) : Disposable {
    fun getModelMatrix(): Matrix4f {
        val model = Matrix4f()

        // Something, something, quaternions?

        // Rotate in the center then translate.
        model.rotateX(rotation.x())
        model.rotateY(rotation.y())
        model.rotateZ(rotation.z())
        model.translate(position)

        return model
    }

    override fun dispose() {
        meshes.forEach { it.dispose() }
    }
}