package simpleroomgl.gfx

import simpleroomgl.engine.Engine
import simpleroomgl.gfx.gl.Framebuffer
import simpleroomgl.gfx.gl.Shader
import simpleroomgl.gfx.gl.VertexArray
import simpleroomgl.gfx.gl.VertexBuffer
import simpleroomgl.porthw6.HW6
import simpleroomgl.scene.Scene
import org.lwjgl.opengl.GL46C
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil

class Renderer {
    private val fbShader = Shader.fromPaths("res/shader/fb.vert", "res/shader/fb.frag")

    private val sceneShader = Shader.fromPaths(
        "res/shader/scene.vert",
        "res/shader/scene.frag"
    )

    private val quad: VertexArray
    private val quadVertices: VertexBuffer
    private val quadIndices: VertexBuffer
    private val framebuffer: Framebuffer
    private val hw6: HW6

    private var wireframe = false

    init {
        // Setup a debug callback to point out dumb mistakes.
        GL46C.glEnable(GL46C.GL_DEBUG_OUTPUT)
        GL46C.glDebugMessageCallback(
            { source, type, _, severity, length, message, _ ->
                val messageStr = MemoryUtil.memUTF8(message, length)

                println("$source -> $type[$severity]: $messageStr")
            },
            0
        )

        framebuffer = Framebuffer.create(Engine.WIDTH, Engine.HEIGHT)
        hw6 = HW6(Engine.WIDTH, Engine.HEIGHT)

        quad = VertexArray.create()
        quad.bind()

        quadVertices = VertexBuffer.create(VertexBuffer.Target.ARRAY)

        quadIndices = VertexBuffer.create(VertexBuffer.Target.ELEMENT)

        MemoryStack.stackPush().use {
            val verts = it.floats(
                -0.33501f, 0.86977f, -1.5148f, 0f, 1f, // top left
                0.19207f, 0.86977f, -1.5148f, 1f, 1f, // top right
                0.19207f, 0.59293f, -1.5148f, 1f, 0f, // bottom right
                -0.33501f, 0.59293f, -1.5148f, 0f, 0f, // bottom left
            )
            quadVertices.bind()
//            val verts = it.floats(
//                -0.33501f, 0.86977f, 1.5148f, 0f, 1f, // top left
//                0.19207f, 0.86977f, 1.5148f, 1f, 1f, // top right
//                0.19207f, 0.59293f, 1.5148f, 1f, 0f, // bottom right
//                -0.35501f, 0.59293f, 1.5148f, 0f, 0f, // bottom left
//            )

//            val verts = it.floats(
//                -1f,  1f, -2f, 0f, 1f, // top left
//                 1f,  1f, -2f, 1f, 1f, // top right
//                 1f, -1f, -2f, 1f, 0f,  // bottom right
//                -1f, -1f, -2f, 0f, 0f // bottom left
//            )
            quadVertices.data(verts, VertexBuffer.Usage.STATIC_DRAW)

            quadIndices.bind()
            val inds = it.ints(
                0, 1, 3,
                1, 2, 3
            )
            quadIndices.data(inds, VertexBuffer.Usage.STATIC_DRAW)
        }

        quad.linkAttrib(0, 3, GL46C.GL_FLOAT, false, 5 * Float.SIZE_BYTES, 0)
        quad.enableAttrib(0)

        quad.linkAttrib(1, 2, GL46C.GL_FLOAT, false, 5 * Float.SIZE_BYTES, (3 * Float.SIZE_BYTES).toLong())
        quad.enableAttrib(1)

        resetState()
    }

    /**
     * Toggle wireframe rendering.
     */
    fun toggleWireframe() {
        wireframe = !wireframe

        if (wireframe) {
            GL46C.glPolygonMode(GL46C.GL_FRONT_AND_BACK, GL46C.GL_LINE)
        } else {
            GL46C.glPolygonMode(GL46C.GL_FRONT_AND_BACK, GL46C.GL_FILL)
        }
    }

    private fun resetState() {
        // Cull back facing triangles.
        GL46C.glFrontFace(GL46C.GL_CCW)
        GL46C.glCullFace(GL46C.GL_BACK)

        // Anti-aliasing?
        GL46C.glEnable(GL46C.GL_MULTISAMPLE)

        // Depth testing.
        GL46C.glEnable(GL46C.GL_DEPTH_TEST)

        // Clear to black.
        GL46C.glClearColor(0f, 0f, 0f, 0f)
    }

    private fun drawModel(camera: Camera, model: Model) {
        sceneShader.uniformMat4f("view", camera.view)
        sceneShader.uniformMat4f("projection", camera.projection)
        sceneShader.uniform3f("viewPos", camera.position)

        model.meshes.forEach {
            sceneShader.uniform3f("material.ambient", it.material.ambient)
            sceneShader.uniform3f("material.diffuse", it.material.diffuse)
            sceneShader.uniform3f("material.specular", it.material.specular)
            sceneShader.uniform1f("material.shininess", it.material.shininess)
            it.draw()
        }
    }

    fun drawScene(camera: Camera, scene: Scene) {
        GL46C.glClear(GL46C.GL_COLOR_BUFFER_BIT or GL46C.GL_DEPTH_BUFFER_BIT)

        // Render hw6 onto framebuffer.
        framebuffer.bind()
        resetState()
        hw6.render()
        framebuffer.unbind()

        resetState()

        sceneShader.use()
        scene.pointLight?.let {
            sceneShader.uniform3f("pointLight.position", it.position)

            sceneShader.uniform1f("pointLight.constant", it.attenuation.constant)
            sceneShader.uniform1f("pointLight.linear", it.attenuation.linear)
            sceneShader.uniform1f("pointLight.exponent", it.attenuation.exponent)

            sceneShader.uniform3f("pointLight.ambient", it.ambient)
            sceneShader.uniform3f("pointLight.diffuse", it.diffuse)
            sceneShader.uniform3f("pointLight.specular", it.specular)
        }

        scene.models.forEach {
            drawModel(camera, it)
        }

        // Draw framebuffer to quad!
        fbShader.use()
        GL46C.glActiveTexture(GL46C.GL_TEXTURE0)
        framebuffer.colorAttachment.bind()
        fbShader.uniformMat4f("view", camera.view)
        fbShader.uniformMat4f("projection", camera.projection)
        quad.bind()
        GL46C.glDrawElements(GL46C.GL_TRIANGLES, 6, GL46C.GL_UNSIGNED_INT, 0L)
    }
}