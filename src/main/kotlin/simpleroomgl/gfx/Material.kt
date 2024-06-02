package simpleroomgl.gfx

import simpleroomgl.gfx.gl.Disposable
import simpleroomgl.gfx.gl.Texture
import org.joml.Vector3f
import org.lwjgl.opengl.GL46C

class Material(
    val ambient: Vector3f,
    val diffuse: Vector3f,
    val specular: Vector3f,
    val shininess: Float,
    val textures: List<Texture>
) : Disposable {
    fun apply() {
        // NOTE: OpenGL guarantees us 16 slots. We barely use more than 2, so we're good.
        textures.forEachIndexed { index, it ->
            GL46C.glActiveTexture(GL46C.GL_TEXTURE0 + index)
            it.bind()
        }
    }

    override fun dispose() {
        textures.forEach { it.dispose() }
    }
}