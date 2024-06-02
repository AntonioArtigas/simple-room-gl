#version 460 core

in vec2 vTexCoord;

uniform sampler2D screen;

out vec4 oColor;

void main() {
    vec3 col = texture(screen, vTexCoord).rgb;
    oColor = vec4(col, 1.0);
}
