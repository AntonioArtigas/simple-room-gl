package simpleroomgl.engine

import simpleroomgl.gfx.Material
import simpleroomgl.gfx.Mesh
import simpleroomgl.gfx.Model
import simpleroomgl.gfx.Vertex
import simpleroomgl.gfx.gl.Texture
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.assimp.AIColor4D
import org.lwjgl.assimp.AIMaterial
import org.lwjgl.assimp.AIMesh
import org.lwjgl.assimp.AIString
import org.lwjgl.assimp.Assimp
import org.lwjgl.system.MemoryStack
import java.nio.IntBuffer

/**
 * Manages assets for the game.
 *
 * TODO: Models have a separate material for defining
 */
class AssetLoader {
    companion object {
        const val AI_SUCCESS = 0 // Not exposed in Assimp bindings?
    }

    fun getModel(path: String): Model {
        val scene = Assimp.aiImportFile(
            path,
            Assimp.aiProcess_Triangulate or
                    Assimp.aiProcess_RemoveRedundantMaterials or
                    Assimp.aiProcess_GenSmoothNormals or
                    Assimp.aiProcess_FixInfacingNormals
        ) ?: error("Could not find model at $path")

        if ((scene.mFlags() and Assimp.AI_SCENE_FLAGS_INCOMPLETE) > 0) {
            error("Assimp could not import file: ${Assimp.aiGetErrorString()}")
        }

        val sceneMaterials = scene.mMaterials()!!
        val aiMeshes = scene.mMeshes()!!

        val meshes = mutableListOf<Mesh>()

        // Materials for the entire scene. Meshes can use a subset of this.
        val allMaterials = mutableListOf<Material>()

        // IntBuffer? means "IntBuffer that could be null" AKA nullable.
        // Have to cast the first null as IntBuffer? or the compiler doesn't know what to do.
        for (i in 0..<scene.mNumMaterials()) {
            MemoryStack.stackPush().use {
//                println("mesh $i")
                val aiMaterial = AIMaterial.create(sceneMaterials[i])
                val textures = mutableListOf<Texture>()

                val aiMaterialPath = AIString.calloc(it)

                // Get the ambient, diffuse, and specular colors.
                val aiColor = AIColor4D.malloc(it)
                var result = Assimp.aiGetMaterialColor(
                    aiMaterial,
                    Assimp.AI_MATKEY_COLOR_AMBIENT,
                    Assimp.aiTextureType_NONE,
                    0,
                    aiColor
                )
                val ambient = if (result == AI_SUCCESS) {
                    Vector3f(aiColor.r(), aiColor.g(), aiColor.b())
                } else {
                    error("Could not get ambient color.")
                }

                result = Assimp.aiGetMaterialColor(
                    aiMaterial,
                    Assimp.AI_MATKEY_COLOR_DIFFUSE,
                    Assimp.aiTextureType_NONE,
                    0,
                    aiColor
                )
                val diffuse = if (result == AI_SUCCESS) {
                    Vector3f(aiColor.r(), aiColor.g(), aiColor.b())
                } else {
                    error("Could not get diffuse color.")
                }

                result = Assimp.aiGetMaterialColor(
                    aiMaterial,
                    Assimp.AI_MATKEY_COLOR_SPECULAR,
                    Assimp.aiTextureType_NONE,
                    0,
                    aiColor
                )
                val specular = if (result == AI_SUCCESS) {
                    Vector3f(aiColor.r(), aiColor.g(), aiColor.b())
                } else {
                    error("Could not get specular color.")
                }

                // And shininess.
                val shininessFactor = arrayOf(0f).toFloatArray()
                val pMax = arrayOf(1).toIntArray()
                result = Assimp.aiGetMaterialFloatArray(
                    aiMaterial,
                    Assimp.AI_MATKEY_SHININESS,
                    Assimp.aiTextureType_NONE,
                    0,
                    shininessFactor,
                    pMax
                )
                val shininess = if (result == AI_SUCCESS) {
                    shininessFactor[0]
                } else {
                    error("Could not get shininess.")
                }

                // Get diffuse texture.
                result = Assimp.aiGetMaterialTexture(
                    aiMaterial,
                    Assimp.aiTextureType_DIFFUSE,
                    0,
                    aiMaterialPath,
                    null as IntBuffer?,
                    null,
                    null,
                    null,
                    null,
                    null
                )
                if (result == AI_SUCCESS) {
                    val texturePath = aiMaterialPath.dataString()
                    val texture = Texture.fromPath(texturePath)
                    textures.add(texture)
                }
                // Getting no diffuse texture is fine...

                allMaterials.add(Material(ambient, diffuse, specular, shininess, textures))
            }
        }

        for (meshIdx in 0..<scene.mNumMeshes()) {
            val vertices = mutableListOf<Vertex>()
            val indices = mutableListOf<Int>()

            // Real subtle... Passing int to create gives an array but long gives a single mesh...
            val mesh = AIMesh.create(aiMeshes[meshIdx])
            val positions = mesh.mVertices()
            val normals = mesh.mNormals()!!
            val uvs =
                mesh.mTextureCoords(0)!! // There can be up to 8 texture coordinates per vertex. We only need 1.

            // Get mesh data.
            for (j in 0..<mesh.mNumVertices()) {
                val aiPosition = positions[j]
                val aiNormal = normals[j]
                val aiUv = uvs[j]

                val pos = Vector3f(
                    aiPosition.x(),
                    aiPosition.y(),
                    aiPosition.z()
                )
                val normal = Vector3f(
                    aiNormal.x(),
                    aiNormal.y(),
                    aiNormal.z()
                )
                val uv = Vector2f(
                    aiUv.x(),
                    aiUv.y()
                )

                val vert = Vertex(
                    pos,
                    normal,
                    uv
                )
                vertices.add(vert)
            }

            // Get indices data.
            val faces = mesh.mFaces()
            for (k in 0..<mesh.mNumFaces()) {
                val faceIndices = faces[k].mIndices()
                for (idx in 0..<faceIndices.capacity()) {
                    indices.add(faceIndices[idx])
                }
            }

            val materialIdx = mesh.mMaterialIndex()
            val material = allMaterials[materialIdx]

            meshes.add(Mesh(vertices, indices, material))
        }
        Assimp.aiFreeScene(scene)

        return Model(meshes, Vector3f(), Vector3f())
    }
}