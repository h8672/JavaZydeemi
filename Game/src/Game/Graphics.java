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
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author MacodiusMaximus
 */
public class Graphics
{
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
        
        
        textureArray = new ArrayList<>();
        camera = new Vector2f();
        
        TextRendererFont.init();
        loadTexture("tekstuuri.png");
        
        fontArray = new ArrayList<>();
        
        TextRendererFont font  = new TextRendererFont();
        font.load("data/font");
        
        fontArray.add(font);
        
        renderableList = new HashSet<>();
        return true;
    }
    public static TextRendererFont getFont()
    {
        return fontArray.get(0);
    }
    public static void registerRenderable(Renderable renderable)
    {
        renderableList.add(renderable);
    }
    
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
    
    /** Piirtää openGL jutut
     *
     */
    public static void render()
    {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glLoadIdentity();
        GL11.glTranslatef(-camera.x, -camera.y,-0.0f);
        
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
        
        GL11.glPushMatrix();
        GL11.glTranslatef(0,0,-1.0f);
        float[] color = {0.8f,0.6f,0.5f};
        fontArray.get(0).renderTextCool("TEKSTIIIIIIIIIIII", new Vector2f(16,16),1.0f);
        GL11.glPopMatrix();
        
        GL11.glTranslatef(0,0,0.99f);
        
        TextureData tiili = textureArray.get(0);
        
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
        
        
    }
    
}
