package simpleroomgl.scene

import simpleroomgl.engine.Engine
import simpleroomgl.gfx.Attenuation
import simpleroomgl.gfx.PointLight
import org.joml.Vector3f

class TestScene(engine: Engine) : Scene(engine) {
    private val model = engine.assets.getModel("res/model/room.obj")

    init {
        pointLight = PointLight(
            Vector3f(0f, 2f, 0f),
            Attenuation(1.0f, 0.35f, 0.44f), // Values taken from the OGRE3D light attenuation page.
            Vector3f(1f),
            Vector3f(0.8f),
            Vector3f(1f)
        )
        addModel(model)
    }

    override fun dispose() {
        model.dispose()
    }
}