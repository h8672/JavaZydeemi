/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game2d.graphics;

import de.matthiasmann.twl.utils.PNGDecoder;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

/** Kuvadataa ja openGL tekstuuriobjektin sisältävä luokka
 *
 * @author MacodiusMaximus
 */
public class ImageData
{
    private int width = 0;
    private int height = 0;
    private int glID = 0;
    private boolean loaded = false;
    private boolean wrap = false;

    /** palautta kuvan leveyden
     *
     * @return
     */
    public int getWidth() {
        return width;
    }

    /** palauttaa kuvan korkeuden
     *
     * @return
     */
    public int getHeight() {
        return height;
    }

    /** palauttaa kuvan openGL nimen
     *
     * @return openGL nimi
     */
    public int getGLName() {
        return glID;
    }

    /** palauttaa, jos kuvan lataus on onnistunut
     *
     * @return
     */
    public boolean isLoaded() {
        return loaded;
    }
    
    /**
     *
     * @param file
     * @param wrap
     */
    public void load(String file, boolean wrap)
    {
        this.wrap = wrap;
        InputStream inFile;
        
        try
        {
            inFile = new FileInputStream(file);
            try 
            {
                //Dekoodataan PNG filu 
                PNGDecoder decoder = new PNGDecoder(inFile);
                
                //Varataan RGBA kuvalle tila (yksi pikseli vaatii 4 tavua)
                ByteBuffer buffer = ByteBuffer.allocateDirect(decoder.getWidth()*decoder.getHeight()*4); 
                decoder.decode(buffer, decoder.getWidth()*4, PNGDecoder.Format.RGBA);
                
                //buf lukutilaan
                buffer.flip(); 
                
                int newTexID = GL11.glGenTextures(); //uusi openGL tekstuuri
                
                this.glID = newTexID;
                this.width = decoder.getWidth();
                this.height = decoder.getHeight();

                //ladataan tekstuuri puskurista 
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, newTexID);
                GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
                
                //textuurin skaalaus
                //GL11.GL_NEAREST nearest neighbor; pikselit erottuu
                //GL11.GL_LINEAR lineaarinen blendaus; toimii hyvin ei pikseligrafiikalla
                
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MIN_FILTER,GL11.GL_NEAREST); 
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MAG_FILTER,GL11.GL_NEAREST);

                //tekstuuri jatkuu äärettömiin
                if (this.wrap)
                {
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
                }
                else //tekstuurilla ei jatku äärettömiin, tekstuurialueen ulkopuolella tasaista väriä
                {
                    //ja tasainen väri tässä tapauksessa on täysin läpinäkyvä musta
                    
                    float color[] = { 0.0f, 0.0f, 0.0f, 0.0f };
                    
                    //puskuriin tarpeeksi tilaa, LWJGL jostain syystä haluaa kaksinkertaisen tilan
                    ByteBuffer bbuf = ByteBuffer.allocateDirect(8*4);
                    FloatBuffer fbuf = bbuf.asFloatBuffer();
                    fbuf.put(color);


                    GL11.glTexParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_BORDER_COLOR, fbuf);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL13.GL_CLAMP_TO_BORDER);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL13.GL_CLAMP_TO_BORDER);

                }
                
                this.loaded = true;
            }
            finally
            {
                inFile.close();
            }
            
        }
        catch (Exception e)
        {
            System.out.println("Error while loading texture '" + file + "' :"+e.toString());
        }
    }
}