package simpleroomgl.gfx

import org.joml.Matrix4f
import org.joml.Vector3f
import kotlin.math.cos
import kotlin.math.sin

class Camera(fov: Float, width: Float, height: Float, near: Float = 0.001f, far: Float = 100f) {
    val position = Vector3f(0f, 0f, 0f)

    val up = Vector3f(0f, 1f, 0f)
    val right = Vector3f(1f, 0f, 0f)
    val forward = Vector3f(0f, 0f, -1f)
    val target = Vector3f().set(position).add(forward)

    var yaw = -90f // A yaw of 0 would make us face 90 degrees right of 0, 0, 0
    var pitch = 0f

    val view = Matrix4f()
    val projection = Matrix4f()
        .setPerspective(Math.toRadians(fov.toDouble()).toFloat(), width / height, near, far)!!

    fun update() {
        val yawRad = Math.toRadians(yaw.toDouble())
        val pitchRad = Math.toRadians(pitch.toDouble())

        forward.x = (cos(yawRad) * cos(pitchRad)).toFloat()
        forward.y = sin(pitchRad).toFloat()
        forward.z = (sin(yawRad) * cos(pitchRad)).toFloat()
        forward.normalize()

        target.set(position).add(forward)
        view.setLookAt(position, target, up)
    }
}