
uniform sampler2D sceneTex;
uniform int renderWidth;
uniform int renderHeight;

void main() 
{ 
vec2 texCoord = gl_FragCoord;
	vec4 col = texture2D(sceneTex,vec2(texCoord.x/renderWidth, texCoord.y/renderHeight));
	
	float coltot = col.x+col.y+col.z;
	if (coltot > 2.0f)
		col[3] = coltot-1.0f;
	else
		col[3] = 0.0f;
		
	gl_FragColor = col;
}