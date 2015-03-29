
uniform sampler2D sceneTex;
uniform int renderWidth;
uniform int renderHeight;
uniform int shockCount;
uniform vec2 shockPos[32];
uniform float shockSize[32];

void main() 
{ 
	vec2 texCoord = gl_FragCoord;
	
	vec2 diffVecTot;
	diffVecTot[0]= 0;
	diffVecTot[1]= 0;
	
	for (int i = 0; i < shockCount; i++)
	{
		vec2 pos = shockPos[i];
		float dist = distance(texCoord, pos);
		float size = shockSize[i];
		float maxSize = 180;
		float power = 80;
		float exp = 0.22;
		float phase = pow(((maxSize-size)/maxSize),2);
		float displace = -10;
		
		
		if ((dist <= (size+power)) && (dist >= (size - power)) ) 
		{
			float diff = (dist - size); 
			diff = (power-abs(diff))/power;

			
			diff = pow(abs(diff),exp)*displace*phase;
			vec2 diffVec = normalize(texCoord - pos); 
			diffVecTot += (diffVec * diff);
			
		}
	}
	
	texCoord += diffVecTot;

	
	vec4 bCol = texture2D(sceneTex, vec2(texCoord.x/renderWidth, texCoord.y/renderHeight));

	gl_FragColor = bCol; //texture2D(sceneTex, vec2(texCoord.x/renderWidth, texCoord.y/renderHeight));
}