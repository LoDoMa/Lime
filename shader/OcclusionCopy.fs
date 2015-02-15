#version 120

precision highp float;

uniform sampler2D uOcclusionMap;

varying vec2 vTexCoord;

void main()
{
	gl_FragColor = texture2D(uOcclusionMap, vTexCoord);
}