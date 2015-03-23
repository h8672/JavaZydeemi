
uniform sampler2D sceneTex;
uniform int renderWidth;
uniform int renderHeight;

void main() 
{ 
vec2 texCoord = gl_FragCoord;
	vec4 col = texture2D(sceneTex,vec2(texCoord.x/renderWidth, texCoord.y/renderHeight));
	
	float coltot = col.x+col.y+col.z;
	col[3] = pow(coltot/3,2.0)/4;
		
	gl_FragColor = col;
}