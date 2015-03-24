#version 120

precision highp float;

#define PI 3.14

varying vec2 vTexCoord;
varying vec4 vColor;

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

    gl_FragColor = vColor * vec4(vec3(1.0), smoothstep(1.0, 0.0, r));
}