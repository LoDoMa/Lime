varying vec2 texCoord;

void main()
{
	gl_Position = ftransform();
	texCoord = gl_MultiTexCoord0.xy;
}