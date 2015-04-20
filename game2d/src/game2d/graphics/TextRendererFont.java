/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game2d.graphics;

import game2d.Main;
import java.awt.Color;
import java.io.File;
import java.util.TreeMap;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author MacodiusMaximus
 */

public class TextRendererFont
{
    private static TreeMap<String, Character> filenameToGlyphMap;

    /** alustaa fontti asetukset
     * määrittelee eri merkeille oikeat tiedostonimet
     */
    public static void init()
    {
        filenameToGlyphMap = new TreeMap<>();
        filenameToGlyphMap.put("question", '?');
        filenameToGlyphMap.put("comma", ',');
        filenameToGlyphMap.put("colon", ':');
        filenameToGlyphMap.put("apostrophe", '\'');
        filenameToGlyphMap.put("dash", '-');
        filenameToGlyphMap.put("period", '.');
        filenameToGlyphMap.put("exclamation", '!');
        filenameToGlyphMap.put("par1", '(');
        filenameToGlyphMap.put("quote", '"');
        filenameToGlyphMap.put("semicolon", ';');
        filenameToGlyphMap.put("par2", ')');
        filenameToGlyphMap.put("plus", '+');
        filenameToGlyphMap.put("equals", '=');
        filenameToGlyphMap.put("slash", '/');
    }
    
    
    private TreeMap<Character,Texture> glyphMap;
    TextRendererFont()
    {
        glyphMap = new TreeMap<>();
    }
    
    /** Lataa fontin kansiosta
     *
     * @param folder kansion nimi josta fontti ladataan
     */
    public void load(String folder)
    {
        
        try
        {

            File dir = new File(folder);
            for (File file : dir.listFiles())
            {
                if (file.getName().endsWith((".png")))
                {
                    String filename = file.getName();
                    filename = filename.split("\\.")[0];
                    char glyph = 0;
                    if (filename.length() == 1)
                    {
                        glyph = filename.charAt(0);
                    }
                    else
                    {
                        if (filenameToGlyphMap.containsKey(filename))
                            glyph = filenameToGlyphMap.get(filename);
                        else
                            return;
                    }

                    Texture t = Graphics.loadTexture(file.toString(),filename,false);
                    if (t.isLoaded())
                    {
                        glyphMap.put(glyph, t);
                    }
                }
            }

        }
        catch (Exception e)
        {
            System.out.println("Failed to load font '"+folder+"' :" + e);
        }
        
        System.out.println("Loaded "+glyphMap.size()+" glyphs.");
        
    }
    
    abstract class RenderTextCharacterHandler
    {
        abstract float getOffX(int CharNum);
        abstract float getOffY(int CharNum);
        abstract float[] getColor(int CharNum);
    }
    
    private void renderTextFunc(String str, Vector2f pos, float size, RenderTextCharacterHandler func)
    {
        float spacing = 1;
        float whiteSpace = 7;
        float curX = pos.x;
        float curY = pos.y;
        int currentChar = 0;
        boolean hasCases = false;
        
        
        GL11.glPushMatrix();
        
        for (char c : str.toCharArray())
        {
            
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            
            if (c == ' ')
            {
                curX += whiteSpace*size;
            }
            if (!hasCases)
                c = Character.toLowerCase(c);
            if (glyphMap.containsKey(c))
            {
                float offx = 0.0f;
                float offy = 0.0f;
                if (func != null)
                {
                    offx = func.getOffX(currentChar)*size;
                    offy = func.getOffY(currentChar)*size;
                    float[] color = func.getColor(currentChar);
                    GL11.glColor3f(color[0],color[1],color[2]);
                }

                Texture tex = glyphMap.get(c);
                
                GL11.glTranslatef(curX+offx,curY+offy,0.0f);
                GL11.glScalef(size,size,1.0f);
                
                Drawing.bindAndPrintTexture(tex.getBaseImage());
                curX += tex.getBaseImage().getWidth()*size;
            }
            
            curX += spacing*size;
            currentChar++;
        }
        GL11.glColor4f(1.0f,1.0f,1.0f,1.0f);
        GL11.glPopMatrix();
    }
    
    
    
    
    class RenderTextNormal extends RenderTextCharacterHandler
    {
        float[] color;
        RenderTextNormal(float[] color)
        {
            this.color = color;
        }
        float getOffX(int CharNum)
        {
            return 0.0f;
        }
        float getOffY(int CharNum)
        {
            return 0.0f;
        }
        float[] getColor(int CharNum)
        {
            return color;
        }
    }
    
    /** Piirtää tekstiä laajennetuilla parametreilla
     *
     * @param str piirrettävä teksti
     * @param pos sijainti
     * @param size tekstin koko, 1.0f on normaali, 0.5f puolet, 2.0f kaksinkertainen
     * @param color tekstin väri taulukkona jossa 3 (RGB) tai 4 (RGBA) floattia
     */
    public void renderTextExt(String str, Vector2f pos, float size, float[] color)
    {
        
        if (color.length < 3 || color.length > 4)
            throw new IllegalArgumentException("color[] must be 3 or 4 elements long");
        if (color.length == 4)
            GL11.glColor4f(color[0],color[1],color[2],color[3]);
        else
            GL11.glColor3f(color[0],color[1],color[2]);
        
        RenderTextNormal func = new RenderTextNormal(color);
        
        renderTextFunc(str,pos,size,func);
        
    }
        
            

    
    /** Piirtää tekstiä
     *
     * @param str piirrettävä teksti
     * @param pos sijainti
     */
    
    public void renderText(String str, Vector2f pos)
    {
        
        float[] color = {1.0f,1.0f,1.0f};
        renderTextExt(str,pos,1.0f,color);
    }
    
    class RenderTextCool extends RenderTextCharacterHandler
    {
        final private static float timeDivOff = 14;
        final private static float timeDivColor = 90;
        final private static float offShiftDiv = 1.0f;
        final private static float hueShiftDiv = 20.0f;
        
        float getOffX(int CharNum)
        {
            return (float) Math.sin(CharNum/offShiftDiv+Main.getTime()/timeDivOff);
        }
        float getOffY(int CharNum)
        {
            return (float) -Math.abs(Math.cos(CharNum/offShiftDiv+Main.getTime()/timeDivOff))*2;
        }
        float[] getColor(int CharNum)
        {
            float hue = (float) (Main.getTime()/timeDivColor+CharNum/hueShiftDiv);
            
            
            Color d = Color.getHSBColor(hue, 0.3f, 0.9f);
          
            return d.getRGBColorComponents(null);
        }
    }
    
    /** Piirtää koolia tekstiä
     *
     * @param str teksti
     * @param pos sijainti
     * @param size koko
     */
    
    
    public void renderTextCool(String str, Vector2f pos,float size)
    {
        
        RenderTextCool func = new RenderTextCool();
        
        renderTextFunc(str,pos,size,func);
    }
    
    
    
}
