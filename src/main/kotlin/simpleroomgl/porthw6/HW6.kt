package simpleroomgl.porthw6

import simpleroomgl.engine.Engine
import simpleroomgl.gfx.gl.Shader
import simpleroomgl.gfx.gl.Texture
import org.joml.Matrix4f
import org.joml.Vector2f
import org.lwjgl.opengl.GL46C
import org.lwjgl.system.MemoryUtil

class HW6(private val width: Int, private val height: Int) {
    private val fov = 90f
    private val xOffset = 0f
    private val yOffset = 0f
    private var xRotation = 0f
    private var yRotation = (Math.PI / 8f).toFloat()
    private val angle = 0f

    private val models = ArrayList<Model>()
    private val projection =
        Matrix4f().setPerspective(Math.toRadians(fov.toDouble()).toFloat(), (width / height).toFloat(), 0.001f, 100f)

    private val shader = Shader.fromPaths("res/shader/hw6.vert", "res/shader/hw6.frag")
    private val brickDiffuse = Texture.fromPath("res/texture/brickwall.png")
    private val brickNormal = Texture.fromPath("res/texture/brickwall-normal.png")

    init {
        shader.use()
        shader.uniformMat4f("proj", projection)

        GL46C.glEnable(GL46C.GL_DEPTH_TEST)

        rebuildMesh(2)

    }

    private fun rebuildMesh(maxIter: Int) {
        models.forEach { it.delete() }
        models.clear()

        carpet(1, maxIter, 0f, 0f, 1f / 3f)
    }

    private fun rect(x: Float, y: Float, sideLen: Float) {
        val vao = GL46C.glGenVertexArrays()
        GL46C.glBindVertexArray(vao)

        val indices = Model.generateIndices()
        val positions = Model.generatePositions(sideLen)
        val positionsVec = Model.generatePositionsVector(sideLen)
        val normals = Model.generateNormals()
        val uvs = Model.generateUvs()
        val uvsVec = Model.generateUvsVector()
        val (tangents, biTangents) = Model.generateTangents(
            positionsVec,
            uvsVec,
            indices
        )

        val indMemory = MemoryUtil.memAllocInt(indices.size)
        indices.forEach { indMemory.put(it) }
        indMemory.flip()
        val indBuf = GL46C.glGenBuffers()
        GL46C.glBindBuffer(GL46C.GL_ELEMENT_ARRAY_BUFFER, indBuf)
        GL46C.glBufferData(
            GL46C.GL_ELEMENT_ARRAY_BUFFER,
            indMemory,
            GL46C.GL_STATIC_DRAW
        )
        MemoryUtil.memFree(indMemory)

        val posMemory = MemoryUtil.memAllocFloat(positions.size)
        positions.forEach { posMemory.put(it) }
        posMemory.flip()
        val posBuf = GL46C.glGenBuffers()
        GL46C.glBindBuffer(GL46C.GL_ARRAY_BUFFER, posBuf)
        GL46C.glBufferData(GL46C.GL_ARRAY_BUFFER, posMemory, GL46C.GL_STATIC_DRAW)
        GL46C.glVertexAttribPointer(0, 3, GL46C.GL_FLOAT, false, 0, 0)
        GL46C.glEnableVertexAttribArray(0)
        MemoryUtil.memFree(posMemory)

        val normalMemory = MemoryUtil.memAllocFloat(normals.size)
        normals.forEach { normalMemory.put(it) }
        normalMemory.flip()
        val normalBuf = GL46C.glGenBuffers()
        GL46C.glBindBuffer(GL46C.GL_ARRAY_BUFFER, normalBuf)
        GL46C.glBufferData(GL46C.GL_ARRAY_BUFFER, normalMemory, GL46C.GL_STATIC_DRAW)
        GL46C.glVertexAttribPointer(1, 3, GL46C.GL_FLOAT, false, 0, 0)
        GL46C.glEnableVertexAttribArray(1)
        MemoryUtil.memFree(normalMemory)

        val uvMemory = MemoryUtil.memAllocFloat(uvs.size)
        val uvBuf = GL46C.glGenBuffers()
        uvs.forEach { uvMemory.put(it) }
        uvMemory.flip()
        GL46C.glBindBuffer(GL46C.GL_ARRAY_BUFFER, uvBuf)
        GL46C.glBufferData(GL46C.GL_ARRAY_BUFFER, uvMemory, GL46C.GL_STATIC_DRAW)
        GL46C.glVertexAttribPointer(2, 2, GL46C.GL_FLOAT, false, 0, 0)
        GL46C.glEnableVertexAttribArray(2)

        val tangentMemory = MemoryUtil.memAllocFloat(tangents.size)
        tangents.forEach { tangentMemory.put(it) }
        tangentMemory.flip()
        val tangentBuf = GL46C.glGenBuffers()
        GL46C.glBindBuffer(GL46C.GL_ARRAY_BUFFER, tangentBuf)
        GL46C.glBufferData(GL46C.GL_ARRAY_BUFFER, tangentMemory, GL46C.GL_STATIC_DRAW)
        GL46C.glVertexAttribPointer(3, 3, GL46C.GL_FLOAT, false, 0, 0)
        GL46C.glEnableVertexAttribArray(3)
        MemoryUtil.memFree(tangentMemory)

        val biTangentMemory = MemoryUtil.memAllocFloat(biTangents.size)
        biTangents.forEach { biTangentMemory.put(it) }
        biTangentMemory.flip()
        val biTangentBuf = GL46C.glGenBuffers()
        GL46C.glBindBuffer(GL46C.GL_ARRAY_BUFFER, biTangentBuf)
        GL46C.glBufferData(GL46C.GL_ARRAY_BUFFER, biTangentMemory, GL46C.GL_STATIC_DRAW)
        GL46C.glVertexAttribPointer(4, 3, GL46C.GL_FLOAT, false, 0, 0)
        GL46C.glEnableVertexAttribArray(4)
        MemoryUtil.memFree(biTangentMemory)

        // Used later to make model matrices that offset to proper position.
        val model = Model(
            vao,
            posBuf,
            normalBuf,
            uvBuf,
            tangentBuf,
            biTangentBuf,
            Vector2f(x, y)
        )

        models.add(model)
    }

    private fun carpet(iter: Int, maxIter: Int, x: Float, y: Float, sideLen: Float) {
        rect(x, y, sideLen)
        if (iter < maxIter) {
            for (column in 0..2) {
                for (row in 0..2) {
                    if (column != 1 || row != 1) {
                        carpet(
                            iter + 1,
                            maxIter,
                            x - sideLen * 2 + column * sideLen * 2,
                            y - sideLen * 2 + row * sideLen * 2,
                            sideLen / 3f
                        )
                    }
                }
            }
        }
    }

    fun render() {
        shader.use()
        GL46C.glActiveTexture(GL46C.GL_TEXTURE0)
        brickDiffuse.bind()
        GL46C.glActiveTexture(GL46C.GL_TEXTURE1)
        brickNormal.bind()
        projection
            .setPerspective(Math.toRadians(fov.toDouble()).toFloat(), (width / height).toFloat(), 0.001f, 100f)
            .translate(xOffset, yOffset, -1.5f)
            .rotateZ(angle)

        shader.uniformMat4f("proj", projection)

        shader.uniform1i("tex", 0)
        shader.uniform1i("normalMap", 1)
        GL46C.glClearColor(1f, 0f, 0f, 1f)
        GL46C.glClear(GL46C.GL_COLOR_BUFFER_BIT or GL46C.GL_DEPTH_BUFFER_BIT)
        xRotation += 1f * Engine.MS_PER_UPDATE.toFloat()

        models.forEach {
            GL46C.glBindVertexArray(it.vao)
            it.model
                .identity()
                .translate(it.center.x, it.center.y, 0f)
                .rotateX(yRotation)
                .rotateY(xRotation)
            shader.uniformMat4f("model", it.model)
            GL46C.glDrawElements(GL46C.GL_TRIANGLES, 36, GL46C.GL_UNSIGNED_INT, 0)
        }
    }
}