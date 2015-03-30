#version 120

#define PI 3.14159265359

varying vec2 vTexCoord;
varying vec4 vColor;

uniform sampler2D uTexture;
uniform float uResolution;

float sample(vec2 coord, float r)
{
    return step(r, texture2D(uTexture, coord).r + 0.003);
}

float smoothstep(float edge0, float edge1, float x)
{
    float t = clamp((x - edge0) / (edge1 - edge0), 0.0, 1.0);
    return t * t * (3.0 - 2.0 * t);
}

void main(void)
{
    vec2 norm = vTexCoord.st * 2.0 - 1.0;
    float theta = atan(norm.y, norm.x);
    float r = length(norm); 
    float coord = (theta + PI) / (2.0 * PI);

    vec2 tc = vec2(coord, 0.0);

    float center = sample(tc, r);
    float blur = (1.0 / uResolution) * smoothstep(0.0, 1.0, r);

    float sum = 0.0;

    sum += sample(vec2(tc.x - 4.0 * blur, tc.y), r) * 0.05;
    sum += sample(vec2(tc.x - 3.0 * blur, tc.y), r) * 0.09;
    sum += sample(vec2(tc.x - 2.0 * blur, tc.y), r) * 0.12;
    sum += sample(vec2(tc.x - 1.0 * blur, tc.y), r) * 0.15;

    sum += center * 0.16;

    sum += sample(vec2(tc.x + 1.0 * blur, tc.y), r) * 0.15;
    sum += sample(vec2(tc.x + 2.0 * blur, tc.y), r) * 0.12;
    sum += sample(vec2(tc.x + 3.0 * blur, tc.y), r) * 0.09;
    sum += sample(vec2(tc.x + 4.0 * blur, tc.y), r) * 0.05;

    gl_FragColor = vColor * vec4(vec3(1.0), sum * smoothstep(1.0, 0.0, r));
}