package simpleroomgl.gfx.gl

import org.lwjgl.opengl.GL33C
import java.nio.FloatBuffer
import java.nio.IntBuffer

class VertexBuffer private constructor(val handle: Int, val target: Target) : Disposable {
    companion object {
        fun create(target: Target) = VertexBuffer(GL33C.glGenBuffers(), target)
    }

    enum class Target(val value: Int) {
        ARRAY(GL33C.GL_ARRAY_BUFFER),
        ELEMENT(GL33C.GL_ELEMENT_ARRAY_BUFFER)
    }

    enum class Usage(val value: Int) {
        STATIC_DRAW(GL33C.GL_STATIC_DRAW)
    }

    fun bind() {
        GL33C.glBindBuffer(target.value, handle)
    }

    fun data(data: FloatBuffer, usage: Usage) {
        GL33C.glBufferData(target.value, data, usage.value)
    }

    fun data(data: IntBuffer, usage: Usage) {
        GL33C.glBufferData(target.value, data, usage.value)
    }

    override fun dispose() {
        GL33C.glDeleteBuffers(handle)
    }
}