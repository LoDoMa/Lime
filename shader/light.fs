varying vec2 pos;

uniform vec4 color;
uniform float radius;
uniform vec2 center;

void main()
{
	float dx = pos.x - center.x;
	float dy = pos.y - center.y;
	float dist = sqrt(dx * dx + dy * dy);
	if (dist <= radius)
	{
		float mul = dist / radius;
		gl_FragColor = vec4(color.rgb, color.a * (1 - mul));
	}
}