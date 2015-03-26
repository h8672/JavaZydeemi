
uniform sampler2D sceneTex;
uniform int renderWidth;
uniform int renderHeight;
uniform int shockCount;
uniform vec2 shockPos[10];
uniform float shockSize[10];

void main() 
{ 
	vec2 texCoord = gl_FragCoord;
	for (int i = 0; i < shockCount; i++)
	{
		vec2 pos = shockPos[i];
		float dist = distance(texCoord, pos);
		float size = shockSize[i];
		float maxSize = 180;
		float power = 60;
		float exp = 0.5;
		float displace = -10*pow(((maxSize-size)/maxSize),2);
		
		
		if ((dist <= (size+power)) && (dist >= (size - power)) ) 
		{
			float diff = (dist - size); 
			diff = (power-abs(diff))/power;

			diff = pow(abs(diff),exp)*displace;
			vec2 diffVec = normalize(texCoord - pos); 
			texCoord = texCoord + (diffVec * diff);
			
		}
	}
	
	gl_FragColor = texture2D(sceneTex, vec2(texCoord.x/renderWidth, texCoord.y/renderHeight));
}