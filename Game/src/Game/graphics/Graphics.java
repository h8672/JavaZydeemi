/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.graphics;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector2f;

class FBOTexPair
{
    public FBOTexPair(int FBO, int Tex) {
        this.FBO = FBO;
        this.Tex = Tex;
    }
    public int FBO;
    public int Tex;
}

class ShockWaveData
{
    public Vector2f pos;
    public float size;
    public float maxSize;
    public float vel;
}

class RenderableData
{
    public Renderable renderable;
    public int layer;

    RenderableData(Renderable renderable, int layer)
    {
        this.renderable = renderable;
        this.layer = layer;
    }
}

/** Grafiikkasysteemi
 * Pitää sisällään kaiken grafiikan piirtoon liittyvän
 *
 * @author MacodiusMaximus
 */
public class Graphics
{
    private static int topLeftQuadVBO;
    
    private static int MSAASamples;
    private static boolean enableMSAA;

    private static boolean allowMSAA;
    
    private static boolean enableFBO;
    private static boolean allowFBO;
    
    private static boolean enableShockWaves;
    private static boolean enableShaders;
    private static boolean allowShaders;
    
    private static int shaderShockProgram;
    private static int shaderShock;
    
    private static int shaderColorizerProgram;
    private static int shaderColorizer;
    
    private static float shockSize = 200.f;
    private static float shockDisplaceAmount = -10.f;
    private static float shockSpeed = 4.f;
    
    private final static int shaderShockMax = 32;
    
    private static LinkedList<ShockWaveData> shaderShockArray;
    
    private static int shaderProgram;
    private static int shaderDisplacer;
    private static int shaderVertexDefault;
    
    private static int FBOTemp;
    private static int FBOTempTex;
    
    private static int FBOTemp2;
    private static int FBOTemp2Tex;
    
    
    private static int FBOScene;
    private static int FBOSceneTex;
    
    private static int RBODepth;
    
    private static int viewWidth;
    private static int viewHeight;
    
   
    private static Vector2f camera;
    private static ArrayList<TextRendererFont> fontArray;
    
    private static ArrayList<ImageData> imageDataArray;
    
    
    private static HashMap<String,Texture> textureMap;
    private static HashMap<String,Animation> animationMap;
    
    private static HashSet<Renderable> renderableList;
    private static HashSet<Renderable> intermediateAdditiveRenderableList;
    private static HashSet<Renderable> intermediateAlphaRenderableList;
    private static HashSet<Renderable> menuRenderableList;
    
    private static LinkedList<Renderable> toBeDeletedRenderables;
    private static LinkedList<RenderableData> toBeAddedRenderables;

    /** Renderable layer efekteille
     *
     */
    final public static int IntermediateAdditiveLayer = 1;
    final public static int IntermediateAlphaLayer = 2;
    /** Renderable layer käyttöliittymälle
     *
     */
    final public static int MenuLayer = 3;

    /** Perus renderable layer 
     *
     */
    final public static int BaseLayer = 0;
    
    public static float getShockSize() {
        return shockSize;
    }

    public static void setShockSize(float shockSize) {
        Graphics.shockSize = shockSize;
    }
    
    public static float getShockSpeed() {
        return shockSpeed;
    }

    public static void setShockSpeed(float shockSpeed) {
        Graphics.shockSpeed = shockSpeed;
    }
    
    public static float getShockDisplaceAmount() {
        return shockDisplaceAmount;
    }

    public static void setShockDisplaceAmount(float shockDisplaceAmount) {
        Graphics.shockDisplaceAmount = shockDisplaceAmount;
    }
    

    public static int getViewWidth() {
        return viewWidth;
    }

    public static int getViewHeight() {
        return viewHeight;
    }
    
    public static boolean getShockWavesEnabled() {
        return enableShockWaves;
    }

    public static void setShockWavesEnabled(boolean enableShockWaves) {
        Graphics.enableShockWaves = enableShockWaves;
    }

    /** Lataa kuvan tiedostosta
     *
     * @param filename tiedostonimi
     * @param wrap onko openGL tekstuuri kiertyvä
     * @return kuvan tiedot sisältävä ImageData
     */
    public static ImageData loadImageData(String filename, boolean wrap)
    {
        ImageData img;
        img = new ImageData();
        img.load(filename, wrap);
        if (img.isLoaded())
            imageDataArray.add(img);
        else
            img = null;
        return img;
    }
    
    /** Luo uuden tekstuurin ImageData kuvasta
     *
     * @param name uniikki nimi
     * @param baseTex ladattu ImageData
     * @return
     */
    public static Texture generateTexture(String name, ImageData baseTex)
    {
        Texture tex = new Texture(name,baseTex);
        
        textureMap.put(name,tex);

        return tex;
    }
    
    /** Luo uuden glowmapatun tekstuurin kahdesta ImageData kuvasta
     *
     * @param name uniikki nimi
     * @param baseTex perus kuva
     * @param glowTex glowmap kuva
     * @return luotu Texture
     */
    public static Texture generateTexture(String name, ImageData baseTex, ImageData glowTex)
    {
        Texture tex = new Texture(name,baseTex,glowTex);
        
        textureMap.put(name,tex);

        return tex;
    }
    
    /** Luo uuden glowmapatun tekstuurin ImageData kuvasta
     *
     * @param name uniikki nimi
     * @param dTex ImageData, joka asetetaan tekstuurin peruskuvaksi ja glowmapiksi
     * @return luotu Texture
     */
    public static Texture generateSelfGlowingTexture(String name, ImageData dTex)
    {
        Texture tex = new Texture(name,dTex,dTex);
        
        textureMap.put(name,tex);

        return tex;
    }
    
    /** Lataa tekstuurin tiedostosta ja luo sen automaattisesti
     * <p>
     * suorittaa kaikki operaatiot tekstuurin saattamiseksi käyttökuntoon
     * @param filename tiedostonimi mistä tekstuuri ladataan
     * @param name uniikki nimi
     * @param wrap onko tekstuuri ympärikiertyvä
     * @return uusi Texture
     */
    public static Texture loadTexture(String filename,String name, boolean wrap)
    {
        Texture tex = new Texture(name,loadImageData(filename,wrap));
        textureMap.put(name,tex);
        return tex;
    }
    
    /** Palauttaa onko shaderit päällä
     *
     * @return true, jos shaderit on päällä
     */
    public static boolean getShadersEnabled() {
        return enableShaders;
    }

    /** Asettaa shaderit päälle tai pois
     * <p>
     * älä kutsu Renderablen render() metodista!
     * @param enableShaders true, niin shaderit päälle
     */
    public static void setShadersEnabled(boolean enableShaders) {
        if (allowShaders)
            Graphics.enableShaders = enableShaders;
        else
            Graphics.enableShaders = false;
    }
    
    public static GraphicsSettings getSettings()
    {
        GraphicsSettings set = new GraphicsSettings();
        set.FBOEnabled = enableFBO;
        set.MSAAEnabled = enableMSAA;
        set.MSAASamples = MSAASamples;
        set.shadersEnabled = enableShaders;
        set.shockwaveDisplaceAmount = shockDisplaceAmount;
        set.shockwaveSize = shockSize;
        set.shockwaveSpeed = shockSpeed;
        set.shockwaves = enableShockWaves;
        set.windowHeight = viewHeight;
        set.windowWidth = viewWidth;
        return set;
    }
    
    public static GraphicsSettings loadSettings()
    {
        GraphicsSettings set = new GraphicsSettings();
        try
        {
            FileInputStream in = new FileInputStream("gfxSettings");
            ObjectInputStream setIn = new ObjectInputStream(in);
            GraphicsSettings g = (GraphicsSettings)setIn.readObject();
            set = g;
            
            setIn.close();
        }
        catch (Exception e)
        {
            
        }
        finally
        {
            
        }
        return set;
    }
    
    public static void saveSettings()
    {
        GraphicsSettings set = getSettings();
        
        try
        {
            FileOutputStream out = new FileOutputStream("gfxSettings");
            ObjectOutputStream setOut = new ObjectOutputStream(out);
            setOut.writeObject(set);
            setOut.close();
        }
        catch (Exception e)
        {
            
        }
        finally
        {
            
        }
    }
    
    
    
    
    //päivättää FBOScene ja RBODepth framebufferit uudelle MSAA asetukselle
    private static void updateMSAA(boolean enable, int samples)
    {
        //Poistetaan nykyinen FBOSceneTex
        GL11.glDeleteTextures(FBOSceneTex);
        
        if (enable) //MSAA päällä
        {
            //tehdään oikeanlainen tekstuuri
            int tex = GL11.glGenTextures();
            GL11.glBindTexture(GL32.GL_TEXTURE_2D_MULTISAMPLE,tex);
            GL32.glTexImage2DMultisample(GL32.GL_TEXTURE_2D_MULTISAMPLE, samples, GL11.GL_RGBA8 ,viewWidth, viewHeight, false);
            
            //ja sidotaan se FBOSceneen
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,FBOScene);
            GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER,GL30.GL_COLOR_ATTACHMENT0,GL32.GL_TEXTURE_2D_MULTISAMPLE,tex,0);
            
        }
        else //MSAA poissa
        {
            //uusi tekstuuri
            int tex = GL11.glGenTextures();
            //perusasetuksilla
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,tex);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MIN_FILTER,GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MAG_FILTER,GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
            
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8 ,viewWidth, viewHeight, 0, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE , (ByteBuffer)null);
            
            //ja sidotaan se framebufferiin
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,FBOScene);
            GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER,GL30.GL_COLOR_ATTACHMENT0,GL11.GL_TEXTURE_2D,tex,0);
            
        }
        
        //syvyyspuskuri uusiksi myös
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER,RBODepth);
        
        if (enable) //MSAA päällä, multisamplattu renderbuffer kehiin
        {
            GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER,samples, GL14.GL_DEPTH_COMPONENT24, viewWidth, viewHeight);
        }
        else //MSAA poissa, perus tylsä ei-AA renderbuffer käyttöön
            GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24, viewWidth, viewHeight);
        
        //ja nidotaan osat yhteen
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,FBOScene);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, RBODepth);
        
    }

    /** Palauttaa onko MSAA (multisampled antialiasing) päällä
     *
     * @return true, jos MSAA päällä
     */
    public static boolean getMSAAEnabled() {
        return enableMSAA;
    }

    /** Asetetaan MSAA (multisampled antialiasing) päälle tai pois
     * ei muuta mitään jos getMSAAAllowed() on false
     * <p>
     * älä kutsu Renderablen render() metodista!
     * @param enable true asettaa MSAAn päälle, false poistaa sen käytöstä
     */
    public static void setMSAAEnabled(boolean enable) {
        if (allowMSAA)
        {
            if (enableMSAA != enable)
            {
                updateMSAA(enable,MSAASamples);
            }
            enableMSAA = enable;
            
        }
    }
    
    /** Palauttaa, voidaanko MSAAta (multisampled antialiasing) käyttää
     * 
     * @return true, jos MSAAta voidaan käyttää
     */
    public static boolean getMSAAAllowed() {
        return allowMSAA;
    }
    
    /** Palauttaa maksimaalisen AA sample määrän
     *
     * @return sample määrä
     */
    public static int getMSAAMaxSamples() {
        
        return GL11.glGetInteger(GL30.GL_MAX_SAMPLES);
    }
    
    /** Palauttaa käytetyn AA sample määrän
     *
     * @return sample määrä
     */
    public static int getMSAASamples() {
        return MSAASamples;
    }
    
    /** Asettaa käytettävän AA sample määrän
     * <p>
     * älä kutsu Renderablen render() metodista!
     *
     * @param samples käytettävien samplejen määrä
     */
    public static void setMSAASamples(int samples) {
        
        if (samples < 0 || samples > getMSAAMaxSamples())
            return;
                
        if (allowMSAA)
        {
            if (MSAASamples != samples)
            {
                updateMSAA(enableMSAA,samples);
            }
        }
        MSAASamples = samples;
    }

    /**Palauttaa kameran sijainnin
     *
     * @return kameran sijainti
     */
    public static Vector2f getCamera() {
        return camera;
    }

    /** Asetetaan kameran sijainti
     *
     * @param Camera kameran uusi sijainti
     */
    public static void setCamera(Vector2f Camera) {
        Graphics.camera = Camera;
    }
    
    //kokeilee kääntää shaderin
    //shaderType on joko GL20.GL_FRAGMENT_SHADER tai GL20.GL_VERTEX_SHADER
    private static int compileShader(String filename,int shaderType) throws Exception
    {
        int shader;
        byte[] bytes = Files.readAllBytes(Paths.get(filename));
        
        //tehdään uusi String konstruktorilla joka ottaa tavudataa ja merkistötyypin
        //koska ei jakseta lukea tiedostoa muulla lailla
        String data = new String(bytes,StandardCharsets.UTF_8);
        shader = GL20.glCreateShader(shaderType);
        
        //ladataan ja käännetään shader
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
    
    //Palauttaa toimivan framebuffer objektin ja siihen sidotun tekstuurin
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
    
    
    //Palauttaa toimivan MSAA framebuffer objektin ja siihen sidotun tekstuurin
    private static FBOTexPair generateScreenFBOMultisampled(int sampling)
    {
        int fbo = GL30.glGenFramebuffers();
        
        int tex = GL11.glGenTextures();
        
        GL11.glBindTexture(GL32.GL_TEXTURE_2D_MULTISAMPLE,tex);

        GL32.glTexImage2DMultisample(GL32.GL_TEXTURE_2D_MULTISAMPLE, sampling, GL11.GL_RGBA8 ,viewWidth, viewHeight, false);
        
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,fbo);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER,GL30.GL_COLOR_ATTACHMENT0,GL32.GL_TEXTURE_2D_MULTISAMPLE,tex,0);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,0);
        
        return new FBOTexPair(fbo,tex);
    }
    
    /** Sulkee openGL rajapinnan asetukset.
     * <p>
     * Poistaa openGL tekstuurit, frame- ja renderbufferit,
     * shaderit ja shader ohjelmat. Kutsu vain ohjelman sammutuksen yhteydessä.
     *
     */
    public static void deinit()
    {
        for (ImageData d : imageDataArray)
        {
            GL11.glDeleteTextures(d.getGLName());
        }
        imageDataArray.clear();
        textureMap.clear();
        
        GL11.glDeleteTextures(FBOSceneTex);
        GL11.glDeleteTextures(FBOTempTex);
        GL11.glDeleteTextures(FBOTemp2Tex);
        
        GL30.glDeleteFramebuffers(FBOScene);
        GL30.glDeleteFramebuffers(FBOTemp);
        GL30.glDeleteFramebuffers(FBOTemp2);
        GL30.glDeleteRenderbuffers(RBODepth);
        
        GL20.glDeleteProgram(shaderShockProgram);
        GL20.glDeleteShader(shaderShock);
        
            
        
        
    }
    
    private static void initProjection(GraphicsSettings set)
    {
        //asetetaan kuvaruudun projektio
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        //ortograafinen projektio, 2D
        GL11.glOrtho(0, viewWidth, viewHeight, 0 , 1, -1); 
        //takaisin modelview matriisiin
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }
    
    private static void initGLSettings(GraphicsSettings set)
    {
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glAlphaFunc (GL11.GL_GREATER, 0.1f ) ;

        //muita openGL asetuksia
        GL11.glClearColor( 0.0f, 0.0f, 0.0f, 0.0f );    //musta tausta
        GL11.glEnable(GL11.GL_TEXTURE_2D);              //textuurit päälle
        GL11.glEnable(GL11.GL_DEPTH_TEST);              //textuurit päälle
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glClearDepth( 1.0f );
        GL11.glColor3f(1.0f,1.0f,1.0f); //valkoinen väri päälle muuten vaan
        
        //Hienoja polygonien piirtoja
        GL11.glHint (GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glHint (GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_NICEST);
    }
    
    private static void initVBO()
    {
        
        topLeftQuadVBO = GL15.glGenBuffers();
        
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,topLeftQuadVBO);
        //4 vertexiä, 2 suuntaa (x,y)
        FloatBuffer fBuf = BufferUtils.createFloatBuffer(4*2);
        fBuf.put(0);fBuf.put(0);
        fBuf.put(1);fBuf.put(0);
        fBuf.put(1);fBuf.put(1);
        fBuf.put(0);fBuf.put(1);
        
        fBuf.rewind();
        
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER,fBuf,GL15.GL_STATIC_DRAW);
        
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        
        GL11.glVertexPointer(2, GL11.GL_FLOAT, 0, 0);
        GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, 0);
        
    }
    
    private static void initFBORelated(GraphicsSettings set)
    {
        //voidaanko käyttää framebuffereita
        //(jos koneesi sattuu olemaan viime vuosituhannelta)
        //ei vaikuta toimivan oikein software opengl modessa
        if(!(GLContext.getCapabilities().GL_EXT_framebuffer_object))
        {
            //kaikki pois, ei mitään kivaa sallita
            System.out.println("Framebuffer objects not supported!");
            allowShaders = false;
            allowFBO = false;
            allowMSAA = false;
            enableMSAA = false;
        }
        else
        {
            allowFBO = true;
            
            //voidaanko käyttää MSAA antialisingia
            if(!(GLContext.getCapabilities().GL_ARB_multisample))
            {
                allowMSAA = false;
                System.out.println("OpenGL context doesn't support MSAA!");
            }
            else
                allowMSAA = true;
            
            
            enableMSAA = allowMSAA&&set.MSAAEnabled;
            MSAASamples = Math.max(Math.min(getMSAAMaxSamples(),set.MSAASamples),0);
                     
            //Framebufferien alustus
            
            FBOTexPair tmp;
            
            if (enableMSAA) //FBOScenen tyyppi riippuu onko MSAA päällä
            {
                GL11.glEnable(GL13.GL_MULTISAMPLE);
                tmp = generateScreenFBOMultisampled(MSAASamples);
            }
            else
            {
                GL11.glDisable(GL13.GL_MULTISAMPLE);
                tmp = generateScreenFBO();
            }
            
            FBOScene = tmp.FBO;
            FBOSceneTex = tmp.Tex;
            
            //muut framebufferit
            tmp = generateScreenFBO();
            FBOTemp = tmp.FBO;
            FBOTempTex = tmp.Tex;
            
            tmp = generateScreenFBO();
            FBOTemp2 = tmp.FBO;
            FBOTemp2Tex = tmp.Tex;
            

            
            //alustetaan FBOScenen syvyyspuskuri
            RBODepth = GL30.glGenRenderbuffers();
            GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER,RBODepth); 
            
            if (enableMSAA) //syvyyspuskurin tyyppi riippuu antialisingista
                GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER,MSAASamples, GL14.GL_DEPTH_COMPONENT24, viewWidth, viewHeight);
            else
                GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24, viewWidth, viewHeight);
            
            //sidotaan FBOScene framebuffer RBODepth depthbufferiin
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,FBOScene);
            GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, RBODepth);
            
            //kokeillaan ladata shaderit
            try {
                shaderColorizer = compileShader("./data/shaders/colorize.frag",GL20.GL_FRAGMENT_SHADER);  
                shaderVertexDefault = compileShader("./data/shaders/def.vert",GL20.GL_VERTEX_SHADER);  
                shaderColorizerProgram = GL20.glCreateProgram();
                
                GL20.glAttachShader(shaderColorizerProgram,shaderColorizer);
                GL20.glAttachShader(shaderColorizerProgram,shaderVertexDefault);
                GL20.glLinkProgram(shaderColorizerProgram);
                

                shaderShock = compileShader("./data/shaders/shock.frag",GL20.GL_FRAGMENT_SHADER);
                
                
                //tehdään shader ohjelmat
                shaderShockProgram = GL20.glCreateProgram();

                //ja linkitetään asiat niihin

                GL20.glAttachShader(shaderShockProgram,shaderShock);
                GL20.glLinkProgram(shaderShockProgram);
                
                shaderShockArray = new LinkedList<>();
                
                allowShaders = true;
                System.out.println("Shaders initialized succesfully");
                
            }
            catch (Exception e)
            {
                System.out.println("Failed to initialize shaders "+e);
                allowShaders = false;
            }
            
        }
        
        if (!allowFBO)
            System.out.println("Framebuffer objects disabled");
        if (!allowShaders)
            System.out.println("Shaders disabled");
        if (!allowMSAA)
            System.out.println("Multisampling antialiasing disabled");
        enableShaders = allowShaders&&set.shadersEnabled;
        enableFBO = allowFBO&&set.FBOEnabled;
        enableShockWaves = enableShaders&&set.shockwaves;
        shockDisplaceAmount = set.shockwaveDisplaceAmount;
        shockSize = set.shockwaveSize;
        shockSpeed = set.shockwaveSpeed;
    }
    
    /**  käynnistää openGL jutut
     *
     * @param WindowW window height
     * @param WindowH window width
     * @return returns true if successful
     */
    public static boolean init(GraphicsSettings set)
    {
        //ikkunan koko
        viewWidth = set.windowWidth;
        viewHeight = set.windowHeight;
        
        initProjection(set);
        initGLSettings(set);
        initFBORelated(set);
        
        initVBO(); //Vertexbuffer objektit sun muut
        
        //perus objektien alustuksia
        imageDataArray = new ArrayList<>();
        textureMap = new HashMap<>();
        animationMap = new HashMap<>();
        camera = new Vector2f(0,0);
        
        
        //ja tehdään kasa containereja
        fontArray = new ArrayList<>();
        
        renderableList = new HashSet<>();
        intermediateAdditiveRenderableList = new HashSet<>();
        intermediateAlphaRenderableList = new HashSet<>();
        menuRenderableList = new HashSet<>();
        
        toBeAddedRenderables = new LinkedList<>();
        toBeDeletedRenderables = new LinkedList<>();
        
        //fonttien piirto kuntoon
        TextRendererFont.init();
        //ladataan uus fontti
        TextRendererFont font  = new TextRendererFont();
        font.load("./data/font");
        fontArray.add(font);
        
        //ladataan dataa (tekstuureja, yms.)
        loadData();

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
     * <p>
     *  käytä jokaiselle objekitlle jonka haluat piirrettäväksi
     * 
     * @param renderable piirtolistalle lisättävä objekti
     */
    public static void registerRenderable(Renderable renderable)
    {
        toBeAddedRenderables.add(new RenderableData(renderable,BaseLayer));
    }
    
    /** Lisää Renderablen piirtolistaan tietylle tasolle
     * <p>
     * Tiettyjen piirtotasojen numerointi on seuraava: Graphics.BaseLayer = Alin taso,
     * peligrafiikka, Graphics.IntermediateAdditiveLayer ja Graphics.IntermediateAlphaLayer = keskimmäinen taso, kaikki efektit,
     * AlphaLayeriin tyypillisesti alpha blendausta käyttävät efektit
     * Graphics.MenuLayer = päällimmäinen taso, menuille tarkoitettu
     * <p>
     *  käytä jokaiselle objektille jonka haluat piirrettäväksi tietylle tasolle kuten menuobjektit
     * 
     * @param renderable piirtolistalle lisättävä objekti
     * @param stage tason numero 0-2
     */
    public static void registerRenderable(Renderable renderable,int layer)
    {
        toBeAddedRenderables.add(new RenderableData(renderable,layer));
    }
    
    /** Poistaa Renderablen piirtolistasta
     *  käytä kun objekti tapetaan/poistetaan
     * <p>
     *  älä kutsu Renderablen render() metodista!
     *
     * @param renderable
     */
    public static void removeRenderable(Renderable renderable)
    {
        toBeDeletedRenderables.add(renderable);
        
    }
    
    //piirtää (FBO) tekstuurin koko ruudun alueelle
    private static void renderFBO(int fbo)
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbo);
        
        GL11.glPushMatrix();
        GL11.glScalef(viewWidth,viewHeight,1.0f);
        GL11.glDrawArrays(GL11.GL_QUADS, 0, 4);
        GL11.glPopMatrix();
    }
    
    //piirtää MSAA tekstuurin koko ruudun alueelle
    private static void renderFBOMultisample(int fbo)
    {
        GL11.glBindTexture(GL32.GL_TEXTURE_2D_MULTISAMPLE, fbo);
        
        GL11.glPushMatrix();
        GL11.glScalef(viewWidth,viewHeight,1.0f);
        GL11.glDrawArrays(GL11.GL_QUADS, 0, 4);
        GL11.glPopMatrix();
    }
    
    //suorittaa hienot shaderit
    private static void renderShocks()
    {

        GL11.glDisable(GL11.GL_ALPHA_TEST); //alpha test pois
        
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,0); //piirretään näytölle
        
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL20.glUseProgram(shaderShockProgram);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        
        int sTex = GL20.glGetUniformLocation(shaderShockProgram, "sceneTex");
        int sW = GL20.glGetUniformLocation(shaderShockProgram, "renderWidth");
        int sH = GL20.glGetUniformLocation(shaderShockProgram, "renderHeight");
        int sSCount = GL20.glGetUniformLocation(shaderShockProgram, "shockCount");
        int sShockPos = GL20.glGetUniformLocation(shaderShockProgram, "shockPos");
        int sShockSize = GL20.glGetUniformLocation(shaderShockProgram, "shockSize");
        int sDisplace = GL20.glGetUniformLocation(shaderShockProgram, "displace");
        int sMaxSize = GL20.glGetUniformLocation(shaderShockProgram, "maxSize");
        
        
 
        int currentShocks = shaderShockArray.size();
        int maxShocks= shaderShockMax;
        
        //täytetään shaderin uniform tiedot
        
        GL20.glUniform1i(sTex,0);
        GL20.glUniform1i(sW,viewWidth);
        GL20.glUniform1i(sH,viewHeight);
        GL20.glUniform1i(sSCount,currentShocks);
        
        GL20.glUniform1f(sDisplace,shockDisplaceAmount);
        GL20.glUniform1f(sMaxSize,shockSize);
        
        FloatBuffer posBuf = BufferUtils.createFloatBuffer(maxShocks*2);
        FloatBuffer sBuf = BufferUtils.createFloatBuffer(maxShocks);

        for (Iterator<ShockWaveData> it = shaderShockArray.iterator(); it.hasNext();)
        {
            ShockWaveData sw = it.next();
            posBuf.put(sw.pos.x);
            posBuf.put(viewHeight-sw.pos.y);
            sBuf.put(sw.size);
            sw.size+=sw.vel;
            if (sw.vel < 1)
                it.remove();
            if (sw.size > sw.maxSize)
                it.remove();
        }
        
        posBuf.rewind();
        sBuf.rewind();
   
        GL20.glUniform2(sShockPos, posBuf);
        GL20.glUniform1(sShockSize, sBuf);
        
        renderFBO(FBOTemp); //ja piiretään Scene shadereitten kera
        
        GL20.glUseProgram(0); //shaderit mäkeen, ei shadereita enään kiitos

        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }
    
    private static void renderRenderables(HashSet<Renderable> list)
    {
        
        //käydään lista läpi ja renderöidään mikäli on näkyvä
        for (Renderable ren : list)
        {
            if (ren.isVisible())
            {
                GL11.glPushMatrix(); //matriisi talteen
                
                float depth = ren.getDepth(); //getdepth palauttaa -100 - 100 
                float fd = ((-0-depth)/(200.0f)); // skaalataan välille -1.0 - 0.0
                //muutetaan z koordinaattia sen verran
                GL11.glTranslatef(0,0, fd); 
                ren.render();
                
                GL11.glPopMatrix(); //matriisi takaisin oikeaan tilaan
            }
        }
    }
    
    private static void updateRenderableLists()
    {
        for (RenderableData data : toBeAddedRenderables)
        {
            Renderable renderable = data.renderable;
            int layer = data.layer;
            switch (layer)
            {
                case IntermediateAdditiveLayer:
                    intermediateAdditiveRenderableList.add(renderable);
                    break;
                case IntermediateAlphaLayer:
                    intermediateAlphaRenderableList.add(renderable);
                    break;
                case MenuLayer:
                    menuRenderableList.add(renderable);
                    break;
                case BaseLayer:
                    renderableList.add(renderable);
                    break;
            }
        }
        
        toBeAddedRenderables.clear();
        
        for (Renderable removeRen : toBeDeletedRenderables)
        {
            if (renderableList.contains(removeRen))
            {
                renderableList.remove(removeRen);
                continue;
            }
            if (intermediateAlphaRenderableList.contains(removeRen))
            {
                intermediateAlphaRenderableList.remove(removeRen);
                continue;
            }
            if (intermediateAdditiveRenderableList.contains(removeRen))
            {
                intermediateAdditiveRenderableList.remove(removeRen);
                continue;
            }
            if (menuRenderableList.contains(removeRen))
            {
                menuRenderableList.remove(removeRen);
                continue;
            }
            
        }
        
        toBeDeletedRenderables.clear();
    }
    

    /** Piirtää openGL jutut
     *
     */
    public static void render()
    {
        //poistetaan poistettavat, lisätään lisättävät
        updateRenderableLists();
        
        
        //renderöinnin alkujutut
        
        //nollataan matriisi
        GL11.glLoadIdentity(); 
        
        //syvyyksien testaus päälle, ettei asiat piirry väärin toistensa päälle
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        
        //ja siirretään kuvaa kameran kohdalle
        GL11.glTranslatef(-camera.x, -camera.y,-0.0f);
        
        if (enableMSAA) //antialiasing käyttöön mahdollisesti
            GL11.glEnable(GL13.GL_MULTISAMPLE);
        else
            GL11.glDisable(GL13.GL_MULTISAMPLE);
        
        if (enableFBO) //jos framebufferit on käytössä
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,FBOScene);
        else
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,0);
        
        // näyttö/framebuffer tyhjäks
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        //piirretään
        renderRenderables(renderableList);
        
        //suoritetaan syvyys tarkistus alpha blendatuille objekteille
        //mutta ei piirretä syvyyspuskuriin
        GL11.glDepthMask(false);
        renderRenderables(intermediateAlphaRenderableList);
        GL11.glDepthMask(true);
        
        
        //tyhjennetään syvyyspuskuri: additiivisesti blendatut efektit näkyy aina päällimmäisinä
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        renderRenderables(intermediateAdditiveRenderableList);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        //syvyystesti pois häiritsemästä kun käsitellään framebuffereita4
        GL11.glLoadIdentity();
        
        if (enableFBO) // vai käsitelläänkö?
        {
            
            GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER,FBOTemp);
            
            //koska FBOScene saattaa käyttää MSAAta, siitä ei voi piirtää suoraan
            //kopsataan siis FBOScene antialiasoimattomaan FBOTemppiin
            
            GL30.glBlitFramebuffer(0,0,viewWidth,viewHeight, 0,0,viewWidth,viewHeight, GL11.GL_COLOR_BUFFER_BIT, GL11.GL_LINEAR);
            
            if (enableMSAA) //jos antialising oli päällä, enään ei ole
              GL11.glDisable(GL13.GL_MULTISAMPLE);

            if (enableShaders && enableShockWaves)
                renderShocks();
            else
            {
                //jos joku on ottanut cinematic bloomin pois
                //kopsataan FB vaan näytölle, ei kiinnosta
                GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER,FBOTemp);
                GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER,0);
                GL30.glBlitFramebuffer(0,0,viewWidth,viewHeight, 0,0,viewWidth,viewHeight, GL11.GL_COLOR_BUFFER_BIT, GL11.GL_LINEAR);
            }
            /*
            
            //ota kommentit pois niin framebuffereitten sisällöt piirretään
            //näytön yläreunaan riviin alpha blendauksen kera
            
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);

            GL11.glScalef(0.25f,0.25f,1);
            renderFBO(FBOBlurTex);
            GL11.glTranslatef(viewWidth,0,0);
            renderFBO(FBOBlur2Tex);
            GL11.glTranslatef(viewWidth,0,0);
            renderFBO(FBOGlowTex);
            GL11.glTranslatef(viewWidth,0,0);
            if (!enableMSAA)
              renderFBO(FBOSceneTex);
            
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_BLEND);
            */
            
        }
        GL11.glLoadIdentity();
        
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        boolean fboWasEnabled= enableFBO;
        enableFBO = false;
        
        renderRenderables(menuRenderableList);
        enableFBO = fboWasEnabled;

        String text = "RenderableCount: "
                +Integer.toString(getIntermediateRenderableCount())+" + "
                +Integer.toString(getMenuRenderableCount())+" + "
                +Integer.toString(getBaseRenderableCount())+" = "
                +Integer.toString(getRenderableCount());
        getFont().renderTextCool(text,new Vector2f(32,32),2.0f);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        
    }

    /** Palauttaa nimeä vastaavan Animation olion
     * <p>
     * Aiheuttaa virheellistä toimintaa mikäli nimi ei ole kelvollinen.
     *
     * @param anim Nimi, millä animaatio on ladattu
     * @return Animation olio, null mikäli nimeä vastaavaa oliota ei löytynyt
     */
    public static Animation getAnimation(String  anim)
    {
        try
        {
            return animationMap.get(anim);
        }
        catch (Exception e)
        {
            System.out.println("Couldn't find animation "+anim+" :"+e);
            return null;
        }
    }
    
    /** Palauttaa nimeä vastaavan Tekstuuri olion
     * <p>
     * Aiheuttaa virheellistä toimintaa mikäli nimi ei ole kelvollinen.
     *
     * @param tex Nimi, millä tekstuuri on ladattu
     * @return Texture olio, null mikäli nimeä vastaavaa oliota ei löytynyt
     */
    public static Texture getTexture(String tex)
    {
        try 
        {
            if (!textureMap.containsKey(tex))
                throw new Exception();
            return textureMap.get(tex);
        }
        catch (Exception e)
        {
            System.out.println("Couldn't find texture "+tex);
            return null;
        }
    }
    
    /** Luo shokkiaaltoefektin
     *
     * @param pos Shokkiaallon keskipiste
     * @param power Shokkiaallon voima
     */
    public static void explode(Vector2f pos, float power)
    {
        if (enableShockWaves)
        {
            if (shaderShockArray.size() >= shaderShockMax-1)
                return;
            ShockWaveData sw = new ShockWaveData();
            sw.pos = pos;
            sw.size = 0;
            sw.maxSize = shockSize;
            sw.vel = Math.max(0.1f,shockSpeed);
            shaderShockArray.add(sw);
        }
    }

    private static void loadData()
    {
        generateTexture("default",loadImageData("./data/tekstuuri.png",true));
        generateTexture("explosion1",loadImageData("./data/fire/explosion.png",false));
        generateTexture("explosiondecal",loadImageData("./data/fire/explosiondecal.png",false));
        loadTexture("./data/tyyppi.png","tyyppi1",false);
        
        Animation anim = new Animation("fieryFlames");
        anim.addFrame(generateTexture("fire1",loadImageData("./data/fire/f1.png",false)));
        anim.addFrame(generateTexture("fire2",loadImageData("./data/fire/f2.png",false)));
        anim.addFrame(generateTexture("fire3",loadImageData("./data/fire/f3.png",false)));
        anim.addFrame(generateTexture("fire4",loadImageData("./data/fire/f4.png",false)));
        anim.setRandomized(true);
        
        animationMap.put(anim.getName(),anim);
        
        anim = new Animation("flameOut");
        anim.addFrame(generateTexture("fireout1",loadImageData("./data/fire/fo1.png",false)));
        anim.addFrame(generateTexture("fireout2",loadImageData("./data/fire/fo2.png",false)));
        anim.addFrame(generateTexture("fireout3",loadImageData("./data/fire/fo3.png",false)));
        
        animationMap.put(anim.getName(),anim);
    }
    
    /** Palauttaa IntermediateLayer'n koon
     *
     * @return koko
     */
    public static int getIntermediateRenderableCount()
    {
        return intermediateAlphaRenderableList.size()+intermediateAdditiveRenderableList.size();
    }
    
    /** Palauttaa MenuLayer'n koon
     *
     * @return koko
     */
    public static int getMenuRenderableCount()
    {
        return menuRenderableList.size();
    }

    /** Palauttaa BaseLayer'n koon
     *
     * @return koko
     */
    public static int getBaseRenderableCount()
    {
        return renderableList.size();
    }

    /** Palauttaa rekisteröityjen Renderablejen määrän
     *
     * @return määrä
     */
    public static int getRenderableCount()
    {
        return getIntermediateRenderableCount()+getMenuRenderableCount()+getBaseRenderableCount();
    }

    static void enableColorizer(float R[],float G[], float B[])
    {
        if (enableShaders)
        {
            int sTex = GL20.glGetUniformLocation(Graphics.shaderColorizerProgram, "tex");

            GL20.glUseProgram(shaderColorizerProgram);

            int sR= GL20.glGetUniformLocation(shaderColorizerProgram, "colorR");
            int sG= GL20.glGetUniformLocation(shaderColorizerProgram, "colorG");
            int sB = GL20.glGetUniformLocation(shaderColorizerProgram, "colorB");


            GL20.glUniform1i(sTex,0);

            GL20.glUniform4f(sR, R[0], R[1], R[2], R[3]);
            GL20.glUniform4f(sG, G[0], G[1], G[2], G[3]);
            GL20.glUniform4f(sB, B[0], B[1], B[2], B[3]);
        }
    }
    
    static void disableColorizer()
    {
        if (enableShaders)
        GL20.glUseProgram(0);
    }
}
