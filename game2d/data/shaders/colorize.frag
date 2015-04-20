
uniform sampler2D tex;
uniform int texWidth;
uniform int texHeight;
uniform vec4 colorR;
uniform vec4 colorG;
uniform vec4 colorB;

void main() 
{ 
	vec2 texCoord = gl_FragCoord;
	vec4 col = texture2D(tex,gl_TexCoord[0].st);
	vec4 ncol;
	ncol[0] = col[0]*colorR[0]+col[1]*colorG[0]+col[2]*colorB[0];
	ncol[1] = col[0]*colorR[1]+col[1]*colorG[1]+col[2]*colorB[1];
	ncol[2] = col[0]*colorR[2]+col[1]*colorG[2]+col[2]*colorB[2];
	ncol[3] = col[3];
	
	gl_FragColor = ncol;
}