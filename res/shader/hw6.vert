#version 460 core

layout(location = 0) in vec3 aPos;
layout(location = 1) in vec3 aNormal;
layout(location = 2) in vec2 aTex;
layout(location = 3) in vec3 aTangent;
layout(location = 4) in vec3 aBiTangent;

uniform mat4 model;
uniform mat4 proj;

out vec2 vTex;
out mat3 TBN;

void main() {
    vec3 T = normalize(vec3(model * vec4(aTangent, 0.0f)));
    vec3 B = normalize(vec3(model * vec4(aBiTangent, 0.0f)));
    vec3 N = normalize(vec3(model * vec4(aNormal, 0.0f)));

    // DON'T tranpose the matrix...
    // it breaks the TBN matrix...
    TBN = mat3(T, B, N);

    vec3 vert = vec3(model * vec4(aPos, 1.0f));

    vTex = aTex;

    gl_Position = proj * vec4(vert, 1.0f);
}
