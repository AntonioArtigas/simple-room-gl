package simpleroomgl.gfx.gl

import org.joml.Matrix4fc
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.opengl.GL46C
import org.lwjgl.system.MemoryStack
import java.nio.file.Files
import java.nio.file.Path

class Shader private constructor(val handle: Int) : Disposable {
    enum class Type(val value: Int) {
        VERTEX(GL46C.GL_VERTEX_SHADER),
        FRAGMENT(GL46C.GL_FRAGMENT_SHADER)
    }

    companion object {
        private fun compileShader(type: Type, source: String): Int {
            val shader = GL46C.glCreateShader(type.value)
            GL46C.glShaderSource(shader, source)

            GL46C.glCompileShader(shader)
            if (GL46C.glGetShaderi(shader, GL46C.GL_COMPILE_STATUS) == GL46C.GL_FALSE) {
                println("Failed to load shader:")
                println(GL46C.glGetShaderInfoLog(shader))
            }

            return shader
        }

        fun fromPaths(vertPath: String, fragPath: String): Shader {
            val vertSource = Files.readString(Path.of(vertPath))
            val fragSource = Files.readString(Path.of(fragPath))

            val vert = compileShader(Type.VERTEX, vertSource)
            val frag = compileShader(Type.FRAGMENT, fragSource)

            val program = linkProgram(vert, frag)

            return Shader(program)
        }

        private fun linkProgram(vert: Int, frag: Int): Int {
            val program = GL46C.glCreateProgram()

            GL46C.glAttachShader(program, vert)
            GL46C.glAttachShader(program, frag)
            GL46C.glLinkProgram(program)

            if (GL46C.glGetProgrami(program, GL46C.GL_LINK_STATUS) == GL46C.GL_FALSE) {
                println("Failed to link program:")
                println(GL46C.glGetProgramInfoLog(program))
            }

            GL46C.glDetachShader(program, vert)
            GL46C.glDetachShader(program, frag)

            GL46C.glDeleteShader(vert)
            GL46C.glDeleteShader(frag)

            return program
        }
    }

    fun uniformMat4f(name: String, mat: Matrix4fc, transpose: Boolean = false) {
        val location = GL46C.glGetUniformLocation(handle, name)

        MemoryStack.stackPush().use {
            val values = it.mallocFloat(16)
            mat.get(values)

            GL46C.glUniformMatrix4fv(location, transpose, values)

        }
    }

    fun uniform3f(name: String, vec: Vector3f) {
        val location = GL46C.glGetUniformLocation(handle, name)
        GL46C.glUniform3f(location, vec.x, vec.y, vec.z)
    }

    fun uniform4f(name: String, vec: Vector4f) {
        val location = GL46C.glGetUniformLocation(handle, name)
        GL46C.glUniform4f(location, vec.x, vec.y, vec.z, vec.w)
    }

    fun use() {
        GL46C.glUseProgram(handle)
    }

    override fun dispose() {
        GL46C.glDeleteProgram(handle)
    }

    fun uniform1f(name: String, value: Float) {
        val location = GL46C.glGetUniformLocation(handle, name)
        GL46C.glUniform1f(location, value)
    }

    fun uniform1i(name: String, value: Int) {
        val location = GL46C.glGetUniformLocation(handle, name)
        GL46C.glUniform1i(location, value)
    }
}