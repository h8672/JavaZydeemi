
uniform sampler2D sceneTex;
uniform int renderWidth;
uniform int renderHeight;
uniform float power;
uniform float divider;
uniform float offset;


void main() 
{ 

	vec2 texCoord = gl_FragCoord;

	float mod = sin(texCoord.x/divider+offset)*power;
	
	texCoord.x += mod;
	float bgmul = 1.0f;
	bgmul *= pow(1.03,mod);
	
	if (texCoord.x < 0)
		texCoord.x = 0;
	if (texCoord.y < 0)
		texCoord.y = 0;
	if (texCoord.x >= renderWidth)
		texCoord.x = renderWidth-1;
	if (texCoord.y >= renderHeight)
		texCoord.y = renderHeight-1;

	texCoord = vec2(texCoord.x/renderWidth, texCoord.y/renderHeight);
	vec4 color = texture2D(sceneTex, texCoord);
	color.x *= bgmul;
	color.y *= bgmul;
	color.z *= bgmul;
	gl_FragColor = color;
}