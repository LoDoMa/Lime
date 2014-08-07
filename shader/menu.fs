uniform sampler2D texture;

varying vec4 color;
varying vec2 texCoord;

void main()
{
	vec4 texColor = texture2D(texture, texCoord).rgba;
	gl_FragColor = texColor * color.rgba;
}