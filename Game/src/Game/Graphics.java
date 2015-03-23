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
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

class FBOTexPair
{
    public FBOTexPair(int FBO, int Tex) {
        this.FBO = FBO;
        this.Tex = Tex;
    }
    public int FBO;
    public int Tex;
}


/** Grafiikkasysteemi
 * Pitää sisällään kaiken grafiikan piirtoon liittyvän
 *
 * @author MacodiusMaximus
 */
public class Graphics
{
    
    private static int MSAASamples;
    private static boolean enableMSAA;

    private static boolean allowMSAA;
    
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
    
    private static int FBOTemp;
    private static int FBOTempTex;
    
    private static int FBOBlur;
    private static int FBOBlurTex;
    
    private static int FBOBlur2;
    private static int FBOBlur2Tex;
    
    private static int FBOGlow;
    private static int FBOGlowTex;
    
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
    private static HashSet<Renderable> intermediateRenderableList;
    private static HashSet<Renderable> menuRenderableList;
    
    private static LinkedList<Renderable> toBeDeletedRenderables;
    final static int IntermediateLayer = 1;
    final static int MenuLayer = 2;
    final static int BaseLayer = 0;
    

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
    
    /**  käynnistää openGL jutut
     *
     * @param WindowW window height
     * @param WindowH window width
     * @return returns true if successful
     */
    public static boolean init(int WindowW, int WindowH)
    {
        //ikkunan koko
        viewWidth = WindowW;
        viewHeight = WindowH;
        
        //asetetaan kuvaruudun projektio
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        //ortograafinen projektio, 2D
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
        
        //Hienoja polygonien piirtoja
        GL11.glHint (GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glHint (GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_NICEST);
        
        
        //voidaanko käyttää framebuffereita
        //(jos koneesi sattuu olemaan viime vuosituhannelta)
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
            
            enableMSAA = allowMSAA;
            MSAASamples = getMSAAMaxSamples();
                     
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
            FBOBlur= tmp.FBO;
            FBOBlurTex = tmp.Tex;
            
            tmp = generateScreenFBO();
            FBOBlur2 = tmp.FBO;
            FBOBlur2Tex = tmp.Tex;
            
            tmp = generateScreenFBO();
            FBOGlow = tmp.FBO;
            FBOGlowTex = tmp.Tex;

            
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
                
                shaderDisplacer = compileShader("./data/shaders/displace.frag",GL20.GL_FRAGMENT_SHADER);
                shaderVertexDefault = compileShader("./data/shaders/def.vert",GL20.GL_VERTEX_SHADER);
                shaderBlur = compileShader("./data/shaders/blur.frag",GL20.GL_FRAGMENT_SHADER);
                shaderGlowMap = compileShader("./data/shaders/glow.frag",GL20.GL_FRAGMENT_SHADER);
                
                //tehdään shader ohjelmat
                shaderProgram = GL20.glCreateProgram();
                shaderBlurProgram = GL20.glCreateProgram();
                shaderGlowMapProgram = GL20.glCreateProgram();

                //ja linkitetään asiat niihin
                GL20.glAttachShader(shaderProgram,shaderVertexDefault);
                GL20.glAttachShader(shaderProgram,shaderDisplacer);
                GL20.glLinkProgram(shaderProgram);

                GL20.glAttachShader(shaderBlurProgram,shaderBlur);
                GL20.glLinkProgram(shaderBlurProgram);

                GL20.glAttachShader(shaderGlowMapProgram,shaderGlowMap);
                GL20.glLinkProgram(shaderGlowMapProgram);
                allowShaders = true;
                System.out.println("Shaders initialized succesfully");
            }
            catch (Exception e)
            {
                System.out.println("Failed to initialize shaders");
                allowShaders = false;
            }
        }
        
        if (!allowFBO)
            System.out.println("Framebuffer objects disabled");
        if (!allowShaders)
            System.out.println("Shaders disabled");
        if (!allowMSAA)
            System.out.println("Multisampling antialiasing disabled");
        
        
        enableShaders = allowShaders;
        enableFBO = allowFBO;
        
        //perus objektien alustuksia
        imageDataArray = new ArrayList<>();
        textureMap = new HashMap<>();
        animationMap = new HashMap<>();
        camera = new Vector2f();
        
        //fonttien piirto kuntoon
        TextRendererFont.init();
        
        fontArray = new ArrayList<>();
        
        //ladataan uus fontti
        TextRendererFont font  = new TextRendererFont();
        
        font.load("./data/font");
        
        fontArray.add(font);
        
        //ja tehdään vihdoin renderableList
        renderableList = new HashSet<>();
        intermediateRenderableList = new HashSet<>();
        menuRenderableList = new HashSet<>();
        
        toBeDeletedRenderables = new LinkedList<>();
        
        
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
     * <p>
     *  älä kutsu Renderablen render() metodista!
     * 
     * @param renderable piirtolistalle lisättävä objekti
     */
    public static void registerRenderable(Renderable renderable)
    {
        renderableList.add(renderable);
    }
    
    /** Lisää Renderablen piirtolistaan tietylle tasolle
     * <p>
     * tiettyjen piirtotasojen numerointi on seuraava: 0 = Graphics.BaseLayer = Alin taso,
     * peligrafiikka, 1 = Graphics.IntermediateLayer = keskimmäinen taso, kaikki efektit,
     * 2 = Graphics.MenuLayer = päällimmäinen taso, menuille tarkoitettu
     * <p>
     *  käytä jokaiselle objektille jonka haluat piirrettäväksi tietylle tasolle kuten menuobjektit
     * <p>
     *  älä kutsu Renderablen render() metodista!
     * 
     * @param renderable piirtolistalle lisättävä objekti
     * @param stage tason numero 0-2
     */
    public static void registerRenderable(Renderable renderable,int stage)
    {
        switch (stage)
        {
            case 1:
                intermediateRenderableList.add(renderable);
                break;
            case 2:
                menuRenderableList.add(renderable);
                break;
            default:
                renderableList.add(renderable);
        }
        
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
    
    private static void initGlowDrawing()
    {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, FBOGlow);
        
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND); 
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE);
    }
    
    private static void deInitGlowDrawing()
    {
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        
        if (enableFBO)
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, FBOScene);
        else
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }


    /** Piirtää spriten 
     * 
     * @param tex TextureData
     * @param pos sijainti
     */
    public static void drawSprite(Texture tex, Vector2f pos)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(pos.x,pos.y,0.0f);
        
        GL11.glPushMatrix();
        bindAndPrintTexture(tex.getBaseImage());
        
        GL11.glPopMatrix();
        if (tex.hasGlow() && isGlowEnabled())
        {
            initGlowDrawing();
            bindAndPrintTexture(tex.getGlowImage());
            deInitGlowDrawing();
        }
        
        GL11.glPopMatrix();
    }
    
    

    /** Piirtää keskitetyn spriten
     *
     * @param tex piirrettävä Texture
     * @param pos sijainti
     * @param rot kulma asteina, kasvaa vastapäivään
     * @param scale skaala x ja y suunnissa, 1.0f on normaalikoko
     * @param glowPow tekstuurin glowmapin kerroin
     */
    public static void drawSpriteCentered(Texture tex, Vector2f pos, float rot, Vector2f scale, float glowPow)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(pos.x,pos.y,0.0f);
        GL11.glRotatef(rot,0,0,1);
        GL11.glScalef(scale.x,scale.y,0.0f);
        GL11.glPushMatrix();
        bindAndPrintCenteredTexture(tex.getBaseImage());
        GL11.glPopMatrix();
        
        if (tex.hasGlow() && isGlowEnabled())
        {
            GL11.glColor3f(glowPow, glowPow, glowPow);
            initGlowDrawing();
            bindAndPrintCenteredTexture(tex.getGlowImage());
            deInitGlowDrawing();
            GL11.glColor3f(1.0f,1.0f,1.0f);
        }
        GL11.glPopMatrix();
    }
    
    /** Piirtää keskitetyn spriten
     * 
     * @param tex piirrettävä Texture
     * @param pos sijainti
     * @param rot kulma asteina, kasvaa vastapäivään
     */
    public static void drawSpriteCentered(Texture tex, Vector2f pos, float rot)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(pos.x,pos.y,0.0f);
        GL11.glRotatef(rot,0,0,1);
        
        GL11.glPushMatrix();
        bindAndPrintCenteredTexture(tex.getBaseImage());
        GL11.glPopMatrix();
        
        if (tex.hasGlow())
        {
            initGlowDrawing();
            bindAndPrintCenteredTexture(tex.getGlowImage());
            deInitGlowDrawing();
            
        }
        GL11.glPopMatrix();
    }
    
    /** Piirtää keskitetyn spriten additiivisella blendauksella
     *
     * @param tex piirrettävä Texture
     * @param pos sijainti
     * @param rot kulma asteina
     * @param scale skaala x ja y suunnissa, 1.0f on normaalikoko
     */
    public static void drawSpriteCenteredAdditive(Texture tex, Vector2f pos, float rot, Vector2f scale)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(pos.x,pos.y,0.0f);
        GL11.glRotatef(rot,0,0,1);
        GL11.glScalef(scale.x,scale.y,0.0f);
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND); 
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE);
        
        bindAndPrintCenteredTexture(tex.getBaseImage());
        
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        
        
        GL11.glPopMatrix();
        if (isGlowEnabled())
        {
            initGlowDrawing();
            bindAndPrintCenteredTexture(tex.getBaseImage());
            deInitGlowDrawing();
        }
        GL11.glPopMatrix();
    }
    
    /** Piirtää keskitetyn teksturoidun neliön
     * muokkaa GL matriisia, käytä varoen
     * @param tex TextureData object to use
     */
    public static void bindAndPrintCenteredTexture(ImageData tex)
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,tex.getGLName());

        GL11.glScalef(tex.getWidth(), tex.getHeight(), 1.0f);
        
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
    public static void bindAndPrintTexture(ImageData tex)
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,tex.getGLName());

        GL11.glScalef(tex.getWidth(), tex.getHeight(), 1.0f);
        
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
    
    //piirtää (FBO) tekstuurin koko ruudun alueelle
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
    
    //piirtää MSAA tekstuurin koko ruudun alueelle
    private static void renderFBOMultisample(int fbo)
    {
        GL11.glBindTexture(GL32.GL_TEXTURE_2D_MULTISAMPLE, fbo);
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
    //piirtää blurria käyttämällä FBOBlur ja FBOBlur2 framebuffereita
    //shadereitten testi 
    private static void blurFBO(int FBO, int samples, float mult, float depth, float power)
    {
        //FBOBlur piiron kohteeksi
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,FBOBlur);
        //tyhjennetään se ensiksi
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        
        GL20.glUseProgram(shaderBlurProgram); //blurri käyttöön
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        
        //shaderin uniform muuttujien osoitteet
        int sTex = GL20.glGetUniformLocation(shaderBlurProgram, "sceneTex");
        int sW = GL20.glGetUniformLocation(shaderBlurProgram, "renderWidth");
        int sH = GL20.glGetUniformLocation(shaderBlurProgram, "renderHeight");
        int sSamples = GL20.glGetUniformLocation(shaderBlurProgram, "samples");
        int sDepth = GL20.glGetUniformLocation(shaderBlurProgram, "depth");
        int sPower = GL20.glGetUniformLocation(shaderBlurProgram, "power");
        int sMultiplier = GL20.glGetUniformLocation(shaderBlurProgram, "multiplier");
        int sMode = GL20.glGetUniformLocation(shaderBlurProgram, "mode");

        
        GL20.glUniform1i(sTex,0); //tekstuuriyksikkö 0
        GL20.glUniform1i(sW,viewWidth); //näytön koot
        GL20.glUniform1i(sH,viewHeight);
        
        //muita blur muuttujia
        GL20.glUniform1f(sPower, power); //potenssi, pienempi-> enemmän blurria
        GL20.glUniform1f(sDepth, depth); //"syvyys", ennemminkin samplejen väli
        GL20.glUniform1i(sSamples, samples); //sample määrä
        GL20.glUniform1f(sMultiplier, mult); //jakaja
        
        GL20.glUniform1i(sMode,0); //horisontaalinen blur ensiksi
        
        renderFBO(FBO);
        
        //Toinen Blur (vertikaalinen)

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,FBOBlur2);

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        
        GL20.glUniform1i(sMode,1); //vertikaalinen blur
        
        renderFBO(FBOBlurTex);
    }
    
    //suorittaa hienot shaderit
    private static void renderBlur()
    {
        //aloitetaan FBOGlow framebufferilla
        /*
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,FBOGlow);
        //GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        
        GL20.glUseProgram(shaderGlowMapProgram); //aktivoidaan glowmap shaderi
        GL13.glActiveTexture(GL13.GL_TEXTURE0); //tekstuuriyksiköksi 0 (varmuuden vuoksi)
    
        
        //otetaan shaderin uniform muuttujien osoitteeet...
        
        int sTex = GL20.glGetUniformLocation(shaderGlowMapProgram, "sceneTex");
        int sW = GL20.glGetUniformLocation(shaderGlowMapProgram, "renderWidth");
        int sH = GL20.glGetUniformLocation(shaderGlowMapProgram, "renderHeight");
        
        //...ja asetetaan niille arvot
        GL20.glUniform1i(sTex,0);       //tekstuuriyksikkö 0
        GL20.glUniform1i(sW,viewWidth); //ikkunan koko
        GL20.glUniform1i(sH,viewHeight);
        
        renderFBO(FBOTempTex); //ja piirretään scene glow framebufferiin
        */
        GL11.glDisable(GL11.GL_ALPHA_TEST); //alpha test pois
    
        
        //blurrataan!!!
        blurFBO(FBOGlowTex,18,17f,1f,0.5f);
        
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, FBOGlow);
        
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        
        // Blurrien piirto
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,0); //FB 0 eli oikea ruutu
        
        //tyhjennetään ensiksi
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        
        GL20.glUseProgram(0); //shaderit mäkeen, ei blurria enään kiitos
        
        renderFBO(FBOTemp); //ja piiretään Scene
        
        
         //Additive blend cinematic bloomille
        GL11.glEnable(GL11.GL_BLEND); 
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE);
        
        //piirretään cinematic bloom scenen päälle
        renderFBO(FBOBlur2Tex); 
        
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
    

    /** Piirtää openGL jutut
     *
     */
    public static void render()
    {
        //poistetaan poistettavat
        for (Renderable removeRen : toBeDeletedRenderables)
        {
            if (renderableList.contains(removeRen))
            {
                renderableList.remove(removeRen);
                continue;
            }
            if (intermediateRenderableList.contains(removeRen))
            {
                intermediateRenderableList.remove(removeRen);
                continue;
            }
            if (menuRenderableList.contains(removeRen))
            {
                menuRenderableList.remove(removeRen);
                continue;
            }
            
        }
        toBeDeletedRenderables.clear();
        
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
        
        //piirretään
        renderRenderables(renderableList);
        
        GL11.glColor3f(1.0f,1.0f,1.0f);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        renderRenderables(intermediateRenderableList);
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

            if (enableShaders) //piirretään cinematic bloom, jos asetukset on kohdillaan
                renderBlur();
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
        boolean fboWasEnabled= enableFBO;
        enableFBO = false;
        
        renderRenderables(menuRenderableList);
        enableFBO = fboWasEnabled;

        
        getFont().renderTextCool("RenderableCount: "+Integer.toString(getRenderableCount()),new Vector2f(32,32),2.0f);
        
    }

    static Animation getAnimation(String  anim)
    {
        return animationMap.get(anim);
    }
    
    static Texture getTexture(String tex)
    {
        return textureMap.get(tex);
    }

    /** Palauttaa onko glow päällä
     *
     * @return tosi, jos glow on päällä
     */
    public static boolean isGlowEnabled()
    {
        if (!enableFBO)
            return false;
        
        return enableShaders;
    }

    private static void loadData()
    {
        generateTexture("default",loadImageData("./data/tekstuuri.png",true),loadImageData("./data/tekstuuriglow.png",true));
        generateTexture("explosion1",loadImageData("./data/fire/explosion.png",false));
        
        Animation anim = new Animation("fieryFlames");
        anim.addFrame(generateSelfGlowingTexture("fire1",loadImageData("./data/fire/f1.png",false)));
        anim.addFrame(generateSelfGlowingTexture("fire2",loadImageData("./data/fire/f2.png",false)));
        anim.addFrame(generateSelfGlowingTexture("fire3",loadImageData("./data/fire/f3.png",false)));
        anim.addFrame(generateSelfGlowingTexture("fire4",loadImageData("./data/fire/f4.png",false)));
        anim.setRandomized(true);
        
        animationMap.put(anim.getName(),anim);
        
        anim = new Animation("flameOut");
        anim.addFrame(generateSelfGlowingTexture("fireout1",loadImageData("./data/fire/fo1.png",false)));
        anim.addFrame(generateSelfGlowingTexture("fireout2",loadImageData("./data/fire/fo2.png",false)));
        anim.addFrame(generateSelfGlowingTexture("fireout3",loadImageData("./data/fire/fo3.png",false)));
        
        animationMap.put(anim.getName(),anim);
    }
    
    public static int getIntermediateRenderableCount()
    {
        return intermediateRenderableList.size();
    }
    
    public static int getMenuRenderableCount()
    {
        return menuRenderableList.size();
    }
    public static int getBaseRenderableCount()
    {
        return renderableList.size();
    }
    public static int getRenderableCount()
    {
        return getIntermediateRenderableCount()+getMenuRenderableCount()+getBaseRenderableCount();
    }
}
