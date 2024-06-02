package simpleroomgl.engine

import simpleroomgl.gfx.Camera
import simpleroomgl.gfx.Renderer
import simpleroomgl.scene.TestScene
import org.lwjgl.glfw.GLFW

class Engine {
    companion object {
        const val MS_PER_UPDATE = 1.0 / 60.0
        const val WIDTH = 1024
        const val HEIGHT = 768
        const val FOV = 75f
    }

    // Engine only things.
    private val window: Window
    private val camera = Camera(FOV, WIDTH.toFloat(), HEIGHT.toFloat())
    private val renderer: Renderer
    private val input: Input

    // These can be accessed by the scenes.
    val assets: AssetLoader

    private val scene: TestScene


    init {
        Window.init()

        window = Window(WIDTH, HEIGHT)
        renderer = Renderer()
        input = Input()
        assets = AssetLoader()

        camera.position.y = 1f
        camera.update()

        scene = TestScene(this)
    }

    private fun cleanup() {
        scene.dispose()
        Window.terminate()
    }

    fun start() {
        // Mouse input. Works the free cam look.
        var oldX = 0f
        var oldY = 0f
        val sensitivity = 0.1f
        GLFW.glfwSetInputMode(window.handle, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED)
        GLFW.glfwSetCursorPosCallback(window.handle) { _, xPos, yPos ->
            val xOffset = xPos.toFloat() - oldX
            val yOffset = oldY - yPos.toFloat()

            oldX = xPos.toFloat()
            oldY = yPos.toFloat()

            camera.yaw += xOffset * sensitivity
            camera.pitch += yOffset * sensitivity

            camera.pitch = camera.pitch.coerceIn(-80f, 80f)
        }

        var running = true
        GLFW.glfwSetKeyCallback(window.handle) { _, key, _, action, _ ->
            if (action != GLFW.GLFW_PRESS) {
                return@glfwSetKeyCallback
            }

            when (key) {
                GLFW.GLFW_KEY_ESCAPE -> running = false
                GLFW.GLFW_KEY_P -> renderer.toggleWireframe()
                GLFW.GLFW_KEY_R -> println(camera.forward.negate())
            }
        }

//        GLFW.glfwSetMouseButtonCallback(window.handle) { window, button, action, mods ->
//
//        }

        // Very basic fixed update game loop.
        var previous = System.currentTimeMillis() / 1000.0
        var lag = 0.0
        while (running && !window.shouldClose()) {
            val current = System.currentTimeMillis() / 1000.0
            val elapsed = current - previous
            lag += elapsed
            previous = current

            while (lag >= MS_PER_UPDATE) {
                Window.pollEvents()

                camera.update()

                lag -= MS_PER_UPDATE
            }

            renderer.drawScene(camera, scene)

            window.swap()
        }

        cleanup()
    }
}