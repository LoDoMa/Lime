#version 130

uniform sampler2D texture;

varying vec3 color;
varying vec2 texcoord;

void main()
{
	vec4 texturecolor = texture2D(texture, texcoord) * vec4(color, 1);
	vec2 tempcolor = texturecolor.xy;
	
	int rcx = int(tempcolor.x * 255);
	int rcy = int(tempcolor.y * 255);
	
	if(mod(gl_FragCoord.x, 50) < 25.0 && mod(gl_FragCoord.y, 50) < 25.0)
	{ 
		rcx = ~rcx;
		rcy = ~rcy;
	}
	
	if(mod(gl_FragCoord.x, 50) >= 25.0 && mod(gl_FragCoord.y, 50) >= 25.0)
	{ 
		rcx = ~rcx;
		rcy = ~rcy;
	}
	
	float rcxa = rcx / 255.0;
	float rcya = rcy / 255.0;
    vec2 resultcolor = vec2(rcxa, rcya);
	gl_FragColor = vec4(resultcolor, texturecolor.zw);
}
