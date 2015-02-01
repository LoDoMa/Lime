
uniform sampler2D occlusion;
uniform sampler2D brightness;
uniform sampler2D light;

varying vec2 texCoord;

void main()
{
	vec4 occlusionColor = texture2D(occlusion, texCoord).rgba;
	vec4 brightnessColor = texture2D(brightness, texCoord).rgba;
	vec4 lightColor = texture2D(light, texCoord).rgba;

	if (occlusionColor.a > 0)
		gl_FragColor = occlusionColor * brightnessColor;
	else
		gl_FragColor = lightColor;
}