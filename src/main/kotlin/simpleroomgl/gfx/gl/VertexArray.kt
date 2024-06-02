package simpleroomgl.gfx.gl

import org.lwjgl.opengl.GL33C

class VertexArray private constructor(val handle: Int) : Disposable {
    companion object {
        fun create() = VertexArray(GL33C.glGenVertexArrays())
    }

    fun bind() {
        GL33C.glBindVertexArray(handle)
    }

    fun enableAttrib(index: Int) {
        GL33C.glEnableVertexAttribArray(index)
    }

    fun linkAttrib(index: Int, size: Int, type: Int, normalized: Boolean, stride: Int, offset: Long) {
        GL33C.glVertexAttribPointer(index, size, type, normalized, stride, offset)
    }

    override fun dispose() {
        GL33C.glDeleteVertexArrays(handle)
    }
}