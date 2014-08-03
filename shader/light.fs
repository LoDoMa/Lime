uniform vec4 lightColor;
uniform vec2 lightPos;
uniform float lightRadius;

varying vec2 pos;

void main()
{
	float deltaX = pos.x - lightPos.x;
	float deltaY = pos.y - lightPos.y;
	float distance = sqrt(deltaX * deltaX + deltaY * deltaY);

	if (distance <= lightRadius)
	{
		float alpha = (lightRadius - distance) / lightRadius;
		gl_FragColor = vec4(lightColor.rgb, lightColor.a * alpha);
	}
}