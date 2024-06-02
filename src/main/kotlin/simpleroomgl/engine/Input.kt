package simpleroomgl.engine

import org.joml.Vector2f

class Input(val lockCursor: Boolean = true) {
    private val previous = Vector2f()
    private val offset = Vector2f()
    private var mouseSensitivity = 0.1f

    var yaw = 0f
    var pitch = 0f

    private var firstMouse = false

//    enum class Key(val value: Int) {
//        W(GLFW.GLFW_KEY_W),
//        A(GLFW.GLFW_KEY_A),
//        S(GLFW.GLFW_KEY_S),
//        D(GLFW.GLFW_KEY_D),
//    }

    fun onMouseMove(xPos: Double, yPos: Double) {
        if (!firstMouse) {
            previous.set(xPos)
            previous.set(yPos)
            firstMouse = true
        }

        offset.set(
            xPos - previous.x,
            previous.y - yPos
        ).mul(mouseSensitivity)

        previous.set(xPos, yPos)

        yaw += offset.x
        pitch += offset.y
    }
}