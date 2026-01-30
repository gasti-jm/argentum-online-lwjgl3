#version 330 core

in vec2 vUV;
in vec4 vColor;

out vec4 FragColor;

uniform sampler2D uTexture;

void main() {
    vec4 tex = texture(uTexture, vUV);

    vec4 finalColor = tex * vColor;

    if (tex.a < 0.5) discard;

    FragColor = finalColor;
}