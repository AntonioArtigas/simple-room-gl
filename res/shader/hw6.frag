#version 460 core

precision highp float;

in vec2 vTex;
in mat3 TBN;

uniform sampler2D tex;
uniform sampler2D normalMap;

out vec4 outColor;

// Cheating a little here. Light position is hard coded here...
const vec3 lightColor = vec3(1.0f, 1.0f, 1.0f);
const vec3 lightDir = normalize(vec3(0.5f, 0.7f, 1));
const float ambientStrength = 0.1f;

void main() {
    vec3 ambient = ambientStrength * lightColor;

    vec3 normal = texture(normalMap, vTex).rgb;
    normal = normal * 2.0 - 1.0; // Map from tangent space to [0.0, 1.0].
    normal = TBN * normalize(normal);
    float diff = max(dot(normal, lightDir), 0.0f);
    vec3 diffuse = diff * lightColor;

    vec3 objectColor = texture(tex, vTex).rgb;

    vec3 result = (ambient + diffuse) * objectColor;
    outColor = vec4(result, 1.0f);
    }
