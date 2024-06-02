package simpleroomgl.scene

import simpleroomgl.engine.Engine
import simpleroomgl.gfx.Light
import simpleroomgl.gfx.Model
import simpleroomgl.gfx.PointLight

/**
 * A scene is pretty much a container for objects to render. It describes models and lights to be rendered.
 */
abstract class Scene(val engine: Engine) {
    val models: List<Model>
        get() = _models

    val lights: List<Light>
        get() = _lights

    var pointLight: PointLight? = null
//    var ambientLight: AmbientLight? = null

    private val _models = mutableListOf<Model>()
    private val _lights = mutableListOf<Light>()

    fun addModel(model: Model) {
        _models.add(model)
    }

    fun addLight(light: Light) {
        _lights.add(light)
    }

    abstract fun dispose()
}