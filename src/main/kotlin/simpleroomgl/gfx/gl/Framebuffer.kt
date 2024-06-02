package simpleroomgl.gfx.gl

import org.lwjgl.opengl.GL46C
import java.nio.ByteBuffer

class Framebuffer private constructor(val handle: Int, val width: Int, val height: Int) : Disposable {
    companion object {
        fun create(width: Int, height: Int): Framebuffer = Framebuffer(GL46C.glGenFramebuffers(), width, height)
    }

    val colorAttachment = Texture.create()
    val depthStencilAttachment: Int

    init {
        GL46C.glBindFramebuffer(GL46C.GL_FRAMEBUFFER, handle)

        colorAttachment.bind()
        GL46C.glTexImage2D(
            GL46C.GL_TEXTURE_2D,
            0,
            GL46C.GL_RGB,
            width,
            height,
            0,
            GL46C.GL_RGB,
            GL46C.GL_UNSIGNED_BYTE,
            null as ByteBuffer?
        )

        GL46C.glTexParameteri(GL46C.GL_TEXTURE_2D, GL46C.GL_TEXTURE_MIN_FILTER, GL46C.GL_LINEAR)
        GL46C.glTexParameteri(GL46C.GL_TEXTURE_2D, GL46C.GL_TEXTURE_MAG_FILTER, GL46C.GL_LINEAR)

        GL46C.glFramebufferTexture2D(
            GL46C.GL_FRAMEBUFFER,
            GL46C.GL_COLOR_ATTACHMENT0,
            GL46C.GL_TEXTURE_2D,
            colorAttachment.handle,
            0
        )


        depthStencilAttachment = GL46C.glGenRenderbuffers()
        GL46C.glBindRenderbuffer(GL46C.GL_RENDERBUFFER, depthStencilAttachment)
        GL46C.glRenderbufferStorage(GL46C.GL_RENDERBUFFER, GL46C.GL_DEPTH24_STENCIL8, width, height)
        GL46C.glFramebufferRenderbuffer(
            GL46C.GL_FRAMEBUFFER,
            GL46C.GL_DEPTH_STENCIL_ATTACHMENT,
            GL46C.GL_RENDERBUFFER,
            depthStencilAttachment
        )

//        val status = GL46C.glCheckFramebufferStatus(GL46C.GL_FRAMEBUFFER) == GL46C.GL_FRAMEBUFFER_COMPLETE
//        if (status) {
//            println("Framebuffer raring to go!")
//        }

        GL46C.glBindTexture(GL46C.GL_TEXTURE_2D, 0)
        GL46C.glBindRenderbuffer(GL46C.GL_RENDERBUFFER, 0)
        GL46C.glBindFramebuffer(GL46C.GL_FRAMEBUFFER, 0)
    }

    fun bind() {
        GL46C.glBindFramebuffer(GL46C.GL_FRAMEBUFFER, handle)
    }

    fun unbind() {
        GL46C.glBindFramebuffer(GL46C.GL_FRAMEBUFFER, 0)
    }

    override fun dispose() {
        GL46C.glDeleteFramebuffers(handle)
    }
}