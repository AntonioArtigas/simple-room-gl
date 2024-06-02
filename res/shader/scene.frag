#version 460 core

#define MAX_POINT_LIGHTS 2

struct PointLight {
    vec3 position;

// Attenuation parameters.
    float constant;
    float linear;
    float quadratic;

// Colors.
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

struct Material {
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
    float shininess;
};

in vec3 vNormal;
in vec2 vTexCoord;
in vec3 vFragPos;

uniform vec3 viewPos;
uniform PointLight pointLight;// Just the light in the room.
uniform Material material;

out vec4 oColor;

vec3 CalcPointLight(PointLight light, vec3 normal, vec3 fragPos, vec3 viewDir) {
    vec3 lightDir = normalize(light.position - fragPos);

    // Diffuse shading.
    float diff = max(dot(normal, lightDir), 0.0);

    // Specular shading.
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);

    // Attenuation.
    float distance = length(light.position - fragPos);
    float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));

    // Combine results.
    vec3 ambient = light.ambient * material.ambient * material.diffuse;
    vec3 diffuse = light.diffuse * diff * material.diffuse;
    vec3 specular = light.specular * spec * material.specular;

    ambient *= attenuation;
    diffuse *= attenuation;
    specular *= attenuation;

    return ambient + diffuse + specular;
}



void main() {
    //    material.ambient;
    //    vTexCoord;
    vec3 norm = normalize(vNormal);
    vec3 viewDir = normalize(viewPos - vFragPos);
    vec3 result = CalcPointLight(pointLight, norm, vFragPos, viewDir);

    oColor = vec4(result, 1.0);
}