precision highp float;

uniform sampler2D occlusionMap;

varying vec2 texCoord;

void main()
{
	gl_FragColor = texture2D(occlusionMap, texCoord).rgba;
}