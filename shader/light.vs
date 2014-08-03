
varying vec2 pos;

void main()
{
	gl_Position = ftransform();
	pos = gl_Vertex.xy;
}