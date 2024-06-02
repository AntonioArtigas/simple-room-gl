#version 460 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aNormal;
layout (location = 2) in vec2 aTexCoord;

// Uh we don't do any model projections...
uniform mat4 view;
uniform mat4 projection;

out vec3 vNormal;
out vec2 vTexCoord;
out vec3 vFragPos;

void main() {
    vNormal = aNormal;
    vTexCoord = aTexCoord;
    vFragPos = aPos;

    gl_Position = projection * view * vec4(aPos, 1.0);
}