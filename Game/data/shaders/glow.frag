
uniform sampler2D sceneTex;
uniform int renderWidth;
uniform int renderHeight;

void main() 
{ 
vec2 texCoord = gl_FragCoord;
	vec4 col = texture2D(sceneTex,vec2(texCoord.x/renderWidth, texCoord.y/renderHeight));
	
	float coltot = col.x+col.y+col.z;
	if (coltot > 1.5f)
	{
		coltot = (coltot-1.5f)/2.5f;
		col[3] = coltot;
	}
	else
		col[3] = 0;
		
	gl_FragColor = col;
}