
uniform sampler2D sceneTex;
uniform int renderWidth;
uniform int renderHeight;
uniform int samples;
uniform float depth;
uniform float power;
uniform float multiplier;
uniform int mode;

void main() 
{ 

	vec2 texCoord = gl_FragCoord;
	
	int i;
	vec4 colMean = vec4(0,0,0,0);
	float mults = 1.0f;
	for (i = 0; i < samples*2; i++)
	{
		int trueI = 0;
		if (i < samples)
			trueI = i+1;
		else
			trueI = -(i-samples)-1;
		
		vec2 tex2c = texCoord;
		if (mode == 1)
			tex2c.y += (trueI)*depth;
		else
			tex2c.x += (trueI)*depth;
			
		tex2c = vec2(tex2c.x/renderWidth, tex2c.y/renderHeight);
		
		vec4 col = texture2D(sceneTex,tex2c);
		
		float mult = pow((samples-abs(trueI))/samples,power);
	
		col = col*mult;
		
		colMean += col;
		mults+=1.0f*mult;
	}
	colMean += texture2D(sceneTex,vec2(texCoord.x/renderWidth, texCoord.y/renderHeight));
	
	mults/= multiplier;
	colMean = colMean/mults;
	
	gl_FragColor = colMean;
}