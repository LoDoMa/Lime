#version 120

precision highp float;

#define PI 3.14

varying vec2 texCoord;

uniform sampler2D texture;
uniform vec2 resolution;

const float THRESHOLD = 0.75;

void main(void)
{
    float distance = 1.0;
    for (float y = 0.0; y < resolution.y; y += 1.0)
    {
        vec2 norm = vec2(texCoord.s, y / resolution.y) * 2.0 - 1.0;
        float theta = PI * 1.5 + norm.x * PI;
        float r = (1.0 + norm.y) * 0.5;

        vec2 coord = vec2(-r * sin(theta), -r * cos(theta)) / 2.0 + 0.5;

        vec4 data = texture2D(texture, coord);
        float dst = y / resolution.y;

        float caster = data.a;
        if (caster > THRESHOLD)
        {
            distance = min(distance, dst);
            break;
        }
    }
    gl_FragColor = vec4(vec3(distance), 1.0);
}