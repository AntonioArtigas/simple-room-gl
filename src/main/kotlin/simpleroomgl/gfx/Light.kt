package simpleroomgl.gfx

import org.joml.Vector3f

sealed class Light(
    val ambient: Vector3f,
    val diffuse: Vector3f,
    val specular: Vector3f
)

data class Attenuation(val constant: Float, val linear: Float, val exponent: Float)

class DirectionalLight(val direction: Vector3f, ambient: Vector3f, diffuse: Vector3f, specular: Vector3f) :
    Light(ambient, diffuse, specular)

class PointLight(
    val position: Vector3f,
    val attenuation: Attenuation,
    ambient: Vector3f,
    diffuse: Vector3f,
    specular: Vector3f
) : Light(ambient, diffuse, specular)