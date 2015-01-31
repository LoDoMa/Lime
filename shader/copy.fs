uniform sampler2D light;
uniform sampler2D world;

varying vec2 texCoord;

void main()
{
	vec4 lightColor = texture2D(light, texCoord).rgba;
	vec4 worldColor = texture2D(world, texCoord).rgba;
	if (worldColor.a > 0)
	{
		gl_FragColor = vec4(worldColor.rgb * lightColor.rgb, worldColor.a);
	}
	else
	{
		gl_FragColor = gl_FragColor * 0.5;
	}
}