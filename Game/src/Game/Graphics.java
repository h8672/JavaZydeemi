/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector2f;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;

class FBOTexPair
{
    public FBOTexPair(int FBO, int Tex) {
        this.FBO = FBO;
        this.Tex = Tex;
    }
    public int FBO;
    public int Tex;
}


/**
 *
 * @author MacodiusMaximus
 */
public class Graphics
{
    
    
    
    private static boolean enableFBO;
    private static boolean allowFBO;
    
    private static boolean enableShaders;
    private static boolean allowShaders;
    
    private static int shaderBlurProgram;
    private static int shaderBlur;
    
    private static int shaderGlowMap;
    private static int shaderGlowMapProgram;
    
    private static int shaderProgram;
    private static int shaderDisplacer;
    private static int shaderVertexDefault;
    
    private static int FBOBlur;
    private static int FBOBlurTex;
    private static int FBOBlur2;
    private static int FBOBlur2Tex;
    
    private static int FBOGlow;
    private static int FBOGlowTex;
    
    private static int FBOScene;
    private static int FBOSceneTex;
    private static int FBODepth;
    private static int viewWidth;
    private static int viewHeight;
    private static Vector2f camera;
    private static ArrayList<TextRendererFont> fontArray;
    
    private static ArrayList<TextureData> textureArray;
    
    private static HashSet<Renderable> renderableList;

    /** Lataa textuurin tiedostosta
     *
     * @param file
     * @return tekstuurin tiedot sisältävä TextureData
     */
    public static TextureData loadTexture(String file)
    {
        TextureData tex;
        tex = new TextureData();
        InputStream inFile;
        
        try
        {
            inFile = new FileInputStream(file);
            try 
            {
                    PNGDecoder decoder = new PNGDecoder(inFile);


                    ByteBuffer buf = ByteBuffer.allocateDirect(4*decoder.getWidth()*decoder.getHeight());
                    decoder.decode(buf, decoder.getWidth()*4, Format.RGBA);
                    buf.flip();
                    
                    int newTexID = GL11.glGenTextures();
                    
                    tex.glID = newTexID;
                    tex.width = decoder.getWidth();
                    tex.height = decoder.getHeight();
                    
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, newTexID);
                    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
                    
                    
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MIN_FILTER,GL11.GL_NEAREST);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MAG_FILTER,GL11.GL_NEAREST);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
                    tex.loaded = true;
                    textureArray.add(tex);
            }
            finally
            {
                inFile.close();
            }
            
        }
        catch (Exception e)
        {
            System.out.println("Error while loading texture '" + file + "' :"+e.toString());
            return tex;
        }
        return tex;
    }
    
    /**
     *
     * @return
     */
    public static boolean getShaders() {
        return enableShaders;
    }

    /**
     *
     * @param enableShaders
     */
    public static void setShaders(boolean enableShaders) {
        if (allowShaders)
            Graphics.enableShaders = enableShaders;
        else
            Graphics.enableShaders = false;
    }

    /**  haetaan kameran sijainti
     *
     * @return camera coordinates
     */
    public static Vector2f getCamera() {
        return camera;
    }

    /** asetetaan kameran sijainti
     *
     * @param Camera
     */
    public static void setCamera(Vector2f Camera) {
        Graphics.camera = Camera;
    }
    
    private static int compileShader(String filename,int shaderType) throws Exception
    {
        int shader;
        byte[] bytes = Files.readAllBytes(Paths.get(filename));
        String data = new String(bytes,StandardCharsets.UTF_8);
        shader = GL20.glCreateShader(shaderType);
        GL20.glShaderSource(shader, data);
        GL20.glCompileShader(shader);
        if(GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_TRUE)
        {
            System.out.println("Shader "+filename+" compiled succesfully.");
        }
        else
        {
            
            System.out.println("Failed to compile shader "+filename+".");
            throw new Exception();
        }
        return shader;

    }
    
    private static FBOTexPair generateScreenFBO()
    {
        int fbo = GL30.glGenFramebuffers();
        
        int tex = GL11.glGenTextures();
        
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,tex);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MIN_FILTER,GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MAG_FILTER,GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8 ,viewWidth, viewHeight, 0, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE , (ByteBuffer)null);
        
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,fbo);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER,GL30.GL_COLOR_ATTACHMENT0,GL11.GL_TEXTURE_2D,tex,0);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,0);
        return new FBOTexPair(fbo,tex);
    }
    
    /**  käynnistää openGL jutut
     *
     * @param WindowW window height
     * @param WindowH window width
     * @return returns true if successful
     */
    public static boolean init(int WindowW, int WindowH)
    {
        viewWidth = WindowW;
        viewHeight = WindowH;
        
        //asetetaan kuvaruudun projektio
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, viewWidth, viewHeight, 0 , 1, -1);
        
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glAlphaFunc (GL11.GL_GREATER, 0.1f ) ;

        //muita openGL asetuksia
        GL11.glClearColor( 0.0f, 0.0f, 0.0f, 0.0f );    //musta tausta
        GL11.glEnable(GL11.GL_TEXTURE_2D);              //textuurit päälle
        GL11.glEnable(GL11.GL_DEPTH_TEST);              //textuurit päälle
        GL11.glDepthFunc(GL11.GL_LESS);
        GL11.glClearDepth( 1.0f );
        GL11.glColor3f(1.0f,1.0f,1.0f); //valkoinen väri päälle muuten vaan
        
        
        
        if(!(GLContext.getCapabilities().GL_EXT_framebuffer_object))
        {
            System.out.println("Framebuffers not supported!");
            allowShaders = false;
            allowFBO = false;
        }
        else
        {
            allowFBO = true;
            //Framebufferin alustus
            FBOTexPair tmp = generateScreenFBO();
            FBOScene = tmp.FBO;
            FBOSceneTex = tmp.Tex;
            
            tmp = generateScreenFBO();
            FBOBlur= tmp.FBO;
            FBOBlurTex = tmp.Tex;
            
            tmp = generateScreenFBO();
            FBOBlur2 = tmp.FBO;
            FBOBlur2Tex = tmp.Tex;
            
            tmp = generateScreenFBO();
            FBOGlow = tmp.FBO;
            FBOGlowTex = tmp.Tex;
            
            FBODepth = GL30.glGenRenderbuffers();
            GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER,FBODepth); //alustetaan scenen framebufferin depthbuffer
            GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24, viewWidth, viewHeight);
            
            //sidotaan scenen framebuffer depthbufferiin
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,FBOScene);
            GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, FBODepth);
            
            
            
            try {  
                shaderDisplacer = compileShader("./data/shaders/displace.frag",GL20.GL_FRAGMENT_SHADER);
                shaderVertexDefault = compileShader("./data/shaders/def.vert",GL20.GL_VERTEX_SHADER);
                shaderBlur = compileShader("./data/shaders/blur.frag",GL20.GL_FRAGMENT_SHADER);
                shaderGlowMap = compileShader("./data/shaders/glow.frag",GL20.GL_FRAGMENT_SHADER);
                shaderProgram = GL20.glCreateProgram();
                shaderBlurProgram = GL20.glCreateProgram();
                shaderGlowMapProgram = GL20.glCreateProgram();
                //ShaderGlowMapProgram = GL20.glCreateProgram();

                GL20.glAttachShader(shaderProgram,shaderVertexDefault);
                GL20.glAttachShader(shaderProgram,shaderDisplacer);
                GL20.glLinkProgram(shaderProgram);

                GL20.glAttachShader(shaderBlurProgram,shaderBlur);
                GL20.glLinkProgram(shaderBlurProgram);

                GL20.glAttachShader(shaderGlowMapProgram,shaderGlowMap);
                GL20.glLinkProgram(shaderGlowMapProgram);
                allowShaders = true;
            }
            catch (Exception e)
            {
                System.out.println("Failed to initialize shaders...");
                allowShaders = false;
            }
            
        }
        enableShaders = allowShaders;
        textureArray = new ArrayList<>();
        camera = new Vector2f();
        
        TextRendererFont.init();
        loadTexture("./data/tekstuuri.png");
        
        fontArray = new ArrayList<>();
        
        TextRendererFont font  = new TextRendererFont();
        font.load("./data/font");
        
        fontArray.add(font);
        
        renderableList = new HashSet<>();
        return true;
    }
    
    /** Palauttaa fontin
     *
     * @return
     */
    public static TextRendererFont getFont()
    {
        return fontArray.get(0);
    }
    
    /** Lisää Renderablen piirtolistaan
     *  käytä jokaiselle objekitlle jonka haluat piirrettäväksi
     * 
     * @param renderable piirtolistalle lisättävä objekti
     */
    public static void registerRenderable(Renderable renderable)
    {
        renderableList.add(renderable);
    }
    
    /** Poistaa Renderablen piirtolistasta
     *  käytä kun objekti tapetaan/poistetaan
     *
     * @param renderable
     */
    public static void removeRenderable(Renderable renderable)
    {
        renderableList.remove(renderable);
    }

    /** Piirtää spriten 
     * 
     * @param tex TextureData
     * @param pos sijainti
     */
    public static void drawSprite(TextureData tex, Vector2f pos)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(pos.x,pos.y,0.0f);
        bindAndPrintTexture(tex);
        GL11.glPopMatrix();
    }
    
    /** Piirtää keskitetyn spriten pyörityksillä
     * 
     * @param tex TextureData
     * @param pos sijainti
     * @param rot kulma asteina, kasvaa vastapäivään
     */
    public static void drawSpriteCentered(TextureData tex, Vector2f pos, float rot)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(pos.x,pos.y,0.0f);
        GL11.glRotatef(rot,0,0,1);
        bindAndPrintCenteredTexture(tex);
        GL11.glPopMatrix();
    }
    
    /** Piirtää keskitetyn teksturoidun neliön
     * muokkaa GL matriisia, käytä varoen
     * @param tex TextureData object to use
     */
    public static void bindAndPrintCenteredTexture(TextureData tex)
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,tex.glID);

        GL11.glScalef(tex.width, tex.height, 1.0f);
        
        GL11.glBegin(GL11.GL_QUADS);
        
        GL11.glTexCoord2f(0,0);
        GL11.glVertex2f(-0.5f,-0.5f);
        
        GL11.glTexCoord2f(1,0);
        GL11.glVertex2f(0.5f,-0.5f);
        
        GL11.glTexCoord2f(1,1);
        GL11.glVertex2f(0.5f,0.5f);
        
        GL11.glTexCoord2f(0,1);
        GL11.glVertex2f(-0.5f,0.5f);
        
        GL11.glEnd();
    }
    
    /** Piirtää teksturoidun neliön
     * muokkaa GL matriisia, käytä varoen
     * @param tex TextureData object to use
     */
    public static void bindAndPrintTexture(TextureData tex)
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,tex.glID);

        GL11.glScalef(tex.width, tex.height, 1.0f);
        
        GL11.glBegin(GL11.GL_QUADS);
        
        GL11.glTexCoord2f(0,0);
        GL11.glVertex2f(0,0);
        
        GL11.glTexCoord2f(1,0);
        GL11.glVertex2f(1f,0f);
        
        GL11.glTexCoord2f(1,1);
        GL11.glVertex2f(1f,1f);
        
        GL11.glTexCoord2f(0,1);
        GL11.glVertex2f(0f,1f);
        
        GL11.glEnd();

    }
    
    private static void renderFBO(int fbo)
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbo);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0.0f,1.0f);
        GL11.glVertex2f(0,0);
        GL11.glTexCoord2f(1.0f,1.0f);
        GL11.glVertex2f(viewWidth,0);
        GL11.glTexCoord2f(1.0f,0.0f);
        GL11.glVertex2f(viewWidth,viewHeight);
        GL11.glTexCoord2f(0.0f,0.0f);
        GL11.glVertex2f(0,viewHeight);
        GL11.glEnd();
    }
    
    private static void renderBlur()
    {
        
         //GlowData
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,FBOGlow);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        
        GL20.glUseProgram(shaderGlowMapProgram);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, FBOSceneTex);
        
        int sTex = GL20.glGetUniformLocation(shaderGlowMapProgram, "sceneTex");
        int sW = GL20.glGetUniformLocation(shaderGlowMapProgram, "renderWidth");
        int sH = GL20.glGetUniformLocation(shaderGlowMapProgram, "renderHeight");

        GL20.glUniform1i(sTex,0);
        GL20.glUniform1i(sW,viewWidth);
        GL20.glUniform1i(sH,viewHeight);
        renderFBO(FBOSceneTex);
        
        //Ensimmäinen Blur
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,FBOBlur);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        
        GL20.glUseProgram(shaderBlurProgram);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, FBOGlowTex);
        
        sTex = GL20.glGetUniformLocation(shaderBlurProgram, "sceneTex");
        sW = GL20.glGetUniformLocation(shaderBlurProgram, "renderWidth");
        sH = GL20.glGetUniformLocation(shaderBlurProgram, "renderHeight");
        int sSamples = GL20.glGetUniformLocation(shaderBlurProgram, "samples");
        int sDepth = GL20.glGetUniformLocation(shaderBlurProgram, "depth");
        int sPower = GL20.glGetUniformLocation(shaderBlurProgram, "power");
        int sMultiplier = GL20.glGetUniformLocation(shaderBlurProgram, "multiplier");
        int sMode = GL20.glGetUniformLocation(shaderBlurProgram, "mode");

        GL20.glUniform1i(sTex,0);
        GL20.glUniform1i(sW,viewWidth);
        GL20.glUniform1i(sH,viewHeight);
        
        GL20.glUniform1f(sPower, 1f);
        GL20.glUniform1f(sDepth, 2.0f);
        GL20.glUniform1i(sSamples, 48);
        GL20.glUniform1i(sMode,0);
        GL20.glUniform1f(sMultiplier,2.8f);
        
        
        renderFBO(FBOGlowTex);
        //Toinen Blur

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,FBOBlur2);

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, FBOBlurTex);
        
        GL20.glUniform1i(sMode,1);
        
        renderFBO(FBOBlurTex);
        
        // Blurrien piirto
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,0);
        
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        
        GL20.glUseProgram(0);
        
        renderFBO(FBOSceneTex);
        
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE); //Additive blend coolille bloomille
        
        renderFBO(FBOBlur2Tex);
        
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }
    
    private static void renderRenderables()
    {
        for (Renderable ren : renderableList)
        {
            if (ren.isVisible())
            {
                GL11.glPushMatrix();
                int depth = ren.getDepth();
                float fd = ((-100-depth)/(200.0f));
                GL11.glTranslatef(0,0, fd);
                ren.render();
                GL11.glPopMatrix();
            }
        }
    }
    /** Piirtää openGL jutut
     *
     */
    public static void render()
    {
        
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glLoadIdentity();
        GL11.glTranslatef(-camera.x, -camera.y,-0.0f);
        
        if (enableShaders)
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,FBOScene);
        else
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,0);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        
        renderRenderables();
        
        GL11.glPushMatrix();
        GL11.glTranslatef(0,0,-1.0f);
        
        fontArray.get(0).renderTextCool("TEKSTIIIIIIIIIIII", new Vector2f(16,16),1.0f);
        GL11.glPopMatrix();
        
        GL11.glTranslatef(0,0,0.99f);
        
        
        TextureData tiili = textureArray.get(0);
        
        drawSpriteCentered(tiili,new Vector2f(222,222),Main.getTime());
        
        if (tiili.loaded)
        {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,tiili.glID);


            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(0,0);
            GL11.glVertex2f(0,0);

            GL11.glTexCoord2f(viewWidth/tiili.width,0);
            GL11.glVertex2f(viewWidth,0f);
            GL11.glTexCoord2f(viewWidth/tiili.width,viewHeight/tiili.height);
            GL11.glVertex2f(viewWidth,viewHeight);
            GL11.glTexCoord2f(0,viewHeight/tiili.height);
            GL11.glVertex2f(0f,viewHeight);
            GL11.glEnd();
        }
        
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        
        if (enableShaders)
            renderBlur();
       
        
    }
    
}
