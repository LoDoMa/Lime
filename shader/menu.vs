
varying vec4 color;
varying vec2 texCoord;

void main()
{
	color = gl_Color;
	gl_Position = ftransform();
	texCoord = gl_MultiTexCoord0.xy;
}