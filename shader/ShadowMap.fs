#version 120

precision highp float;

#define PI 3.14

varying vec2 vTexCoord;

uniform sampler2D uTexture;
uniform float uResolution;

const float THRESHOLD = 0.75;

void main(void)
{
    float distance = 1.0;
    for (float y = 0.0; y < uResolution; y += 1.0)
    {
        vec2 norm = vec2(vTexCoord.s, y / uResolution) * 2.0 - 1.0;
        float theta = PI * 1.5 + norm.x * PI;
        float r = (1.0 + norm.y) * 0.5;

        vec2 coord = vec2(-r * sin(theta), -r * cos(theta)) / 2.0 + 0.5;

        vec4 data = texture2D(uTexture, coord);
        float dst = y / uResolution;

        float caster = data.a;
        if (caster > THRESHOLD)
        {
            distance = min(distance, dst);
            break;
        }
    }
    gl_FragColor = vec4(vec3(distance), 1.0);
}