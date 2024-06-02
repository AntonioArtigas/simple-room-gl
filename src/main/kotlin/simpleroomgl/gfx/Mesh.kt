package simpleroomgl.gfx

import simpleroomgl.gfx.gl.Disposable
import simpleroomgl.gfx.gl.VertexArray
import simpleroomgl.gfx.gl.VertexBuffer
import org.lwjgl.opengl.GL46C
import org.lwjgl.system.MemoryUtil

class Mesh(
    private val vertices: List<Vertex>,
    private val indices: List<Int>,
    val material: Material
) : Disposable {
    private val vao = VertexArray.create()
    private val vbo = VertexBuffer.create(VertexBuffer.Target.ARRAY)
    private val ibo = VertexBuffer.create(VertexBuffer.Target.ELEMENT)

    init {
        vao.bind()
        val size = vertices.size * Vertex.stride()

        val verts = MemoryUtil.memAllocFloat(size)
        val inds = MemoryUtil.memAllocInt(indices.size)

        for (vertex in vertices) {
            verts.put(vertex.position.x)
            verts.put(vertex.position.y)
            verts.put(vertex.position.z)

            verts.put(vertex.normal.x)
            verts.put(vertex.normal.y)
            verts.put(vertex.normal.z)

            verts.put(vertex.uv.x)
            verts.put(vertex.uv.y)
        }

        for (index in indices) {
            inds.put(index)
        }

        // It's important to flip the buffers before passing it to LWJGL!
        verts.flip()
        inds.flip()

        vbo.bind()
        vbo.data(verts, VertexBuffer.Usage.STATIC_DRAW)

        ibo.bind()
        ibo.data(inds, VertexBuffer.Usage.STATIC_DRAW)

        vao.linkAttrib(0, 3, GL46C.GL_FLOAT, false, Vertex.stride(), Vertex.positionOffset())
        vao.enableAttrib(0)

        vao.linkAttrib(1, 3, GL46C.GL_FLOAT, false, Vertex.stride(), Vertex.normalOffset())
        vao.enableAttrib(1)

        vao.linkAttrib(2, 2, GL46C.GL_FLOAT, false, Vertex.stride(), Vertex.uvOffset())
        vao.enableAttrib(2)

        MemoryUtil.memFree(verts)
        MemoryUtil.memFree(inds)
    }

    fun draw() {
        vao.bind()

        material.textures.forEach { it.bind() }

        if (indices.isEmpty()) {
            GL46C.glDrawArrays(GL46C.GL_TRIANGLES, 0, vertices.size)
        } else {
            GL46C.glDrawElements(GL46C.GL_TRIANGLES, indices.size, GL46C.GL_UNSIGNED_INT, 0L)
        }
    }

    override fun dispose() {
        vbo.dispose()
        ibo.dispose()
        vao.dispose()

        material.dispose()
    }
}