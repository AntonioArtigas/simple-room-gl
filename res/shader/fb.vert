#version 460 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aTexCoord;

uniform mat4 view;
uniform mat4 projection;

out vec2 vTexCoord;

void main() {
    vTexCoord = aTexCoord;
    gl_Position = projection * view * vec4(aPos, 1.0);
}