package simpleroomgl.engine

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GLCapabilities
import org.lwjgl.system.MemoryUtil

class Window(val width: Int, val height: Int) {
    companion object {
        private lateinit var errorCallback: GLFWErrorCallback

        fun init() {
            errorCallback = GLFWErrorCallback.createPrint().set()
            if (!GLFW.glfwInit()) {
                error("Could not initialize GLFW.")
            }
        }

        fun terminate() {
            GLFW.glfwTerminate()
            errorCallback.free()
        }

        fun pollEvents() {
            GLFW.glfwPollEvents()
        }
    }

    val handle: Long
    val caps: GLCapabilities

    init {
        GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 4)
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4)
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 6)
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE)
        handle = GLFW.glfwCreateWindow(width, height, "HW7", MemoryUtil.NULL, MemoryUtil.NULL)
        if (handle == MemoryUtil.NULL) {
            error("Could not create window.")
        }

        GLFW.glfwMakeContextCurrent(handle)
        caps = GL.createCapabilities()
    }

//    fun attachInput(input: Input) {
//        if (input.lockCursor) {
//            GLFW.glfwSetInputMode(handle, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED)
//        }
//
//        GLFW.glfwSetCursorPosCallback(handle) { _, xPos, yPos ->
//            input.onMouseMove(xPos, yPos)
//        }
//    }

    fun shouldClose(): Boolean = GLFW.glfwWindowShouldClose(handle)

    fun swap() {
        GLFW.glfwSwapBuffers(handle)
    }
}