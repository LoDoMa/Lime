#version 120

uniform sampler2D uTexture;

varying vec2 vTexCoord;
varying vec4 vColor;

void main()
{
	vec4 texfrag = texture2D(uTexture, vTexCoord);
	gl_FragColor = texfrag * vColor;
}