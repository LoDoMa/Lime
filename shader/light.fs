uniform vec4 lightColor;
uniform vec2 lightPos;
uniform float lightRadius;

uniform float lowerAngle;
uniform float upperAngle;

varying vec2 pos;

void main()
{
	float deltaX = pos.x - lightPos.x;
	float deltaY = pos.y - lightPos.y;
	float distance = sqrt(deltaX * deltaX + deltaY * deltaY);

	vec4 rescolor = vec4(0.0, 0.0, 0.0, 0.0);
	if (distance <= lightRadius)
	{
		float angle = atan(pos.y - lightPos.y, pos.x - lightPos.x);
		if(((upperAngle < lowerAngle) && (angle >= lowerAngle || angle <= upperAngle))
		|| ((upperAngle >= lowerAngle) && (angle >= lowerAngle && angle <= upperAngle)))
		{
			float alpha = (lightRadius - distance) / lightRadius;
			rescolor = vec4(lightColor.rgb, lightColor.a * alpha);
		}
	}
	gl_FragColor = rescolor;
}