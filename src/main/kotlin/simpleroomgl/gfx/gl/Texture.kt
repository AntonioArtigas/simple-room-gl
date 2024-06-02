package simpleroomgl.gfx.gl

import org.lwjgl.opengl.GL33C
import org.lwjgl.opengl.GL46C
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import java.nio.file.Files
import java.nio.file.Path

class Texture private constructor(val handle: Int) : Disposable {
    companion object {
        fun create() = Texture(GL46C.glGenTextures())

        fun fromPath(path: String): Texture {
            if (!Files.exists(Path.of(path))) {
                error("No image at $path!")
            }

            MemoryStack.stackPush().use {
                val pWidth = it.mallocInt(1)
                val pHeight = it.mallocInt(1)
                val pChannels = it.mallocInt(1)

                STBImage.stbi_set_flip_vertically_on_load(true)
                val data = STBImage.stbi_load(path, pWidth, pHeight, pChannels, 0)!!

                val texture = GL33C.glGenTextures()
                GL33C.glBindTexture(GL33C.GL_TEXTURE_2D, texture)
                GL33C.glTexImage2D(GL33C.GL_TEXTURE_2D, 0, GL33C.GL_RGB, pWidth[0], pHeight[0], 0, GL33C.GL_RGBA, GL33C.GL_UNSIGNED_BYTE, data)
                GL33C.glGenerateMipmap(GL33C.GL_TEXTURE_2D)

                STBImage.stbi_image_free(data)

                return Texture(texture)
            }
        }
    }

    fun bind() {
        GL33C.glBindTexture(GL33C.GL_TEXTURE_2D, handle)
    }

    override fun dispose() {
        GL33C.glDeleteTextures(handle)
    }
}