/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game2d.graphics;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector2f;

/** Piirtoapumetodit sisältävä luokka
 * 
 * @author MacodiusMaximus
 */
public class Drawing
{

    /** Piirtää spriten 
     * 
     * @param tex TextureData
     * @param pos sijainti
     */
    public static void drawSprite(Texture tex, Vector2f pos)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(pos.x,pos.y,0.0f);
        
        bindAndPrintTexture(tex.getBaseImage());

        GL11.glPopMatrix();
    }
    public static void drawBar(Vector2f pos, float size, float color1[], float color2[],float current, float max)
    {
        drawBar(pos,size,color1,color2,current,max,4);
    }
    
    public static void drawBar(Vector2f pos, float size, float color1[], float color2[],float current, float max,float height)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(pos.x,pos.y,0.0f);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        
        GL11.glScalef(size,height,1);
        
        GL11.glColor4f(color1[0],color1[1],color1[2],1.0f);
        
        GL11.glDrawArrays(GL11.GL_QUADS, 0, 4);
        
        float ratio = current/max;
        GL11.glScalef(ratio,1,1);
        
        GL11.glColor4f(color2[0],color2[1],color2[2],1.0f);
        
        GL11.glDrawArrays(GL11.GL_QUADS, 0, 4);
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(1.0f,1.0f,1.0f,1.0f);
        GL11.glPopMatrix();
    }
    
    public static void drawLine(Vector2f p1, Vector2f p2, float[] f)
    {
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(f[0],f[1],f[2],f[3]);
        
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2f(p1.x,p1.y);
        GL11.glVertex2f(p2.x,p2.y);  
        GL11.glEnd();
        
        GL11.glColor4f(1,1,1,1);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
    }
    
    public static void drawLine(Vector2f p1,Vector2f p2)
    {
        drawLine(p1,p2,new float[]{1,1,1,1});
    }
    
    /** Piirtää spriten määrätyllä koolla
     * 
     * @param tex TextureData
     * @param pos sijainti
     * @param size koko pikseleinä
     */
    public static void drawSpriteSized(Texture tex, Vector2f pos, Vector2f size)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(pos.x,pos.y,0.0f);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D,tex.getBaseImage().getGLName());
        
        GL11.glScalef(size.x,size.y,1.0f);
        
        GL11.glDrawArrays(GL11.GL_QUADS, 0, 4);

        GL11.glPopMatrix();
    }
    
    /** Piirtää jutun
     *
     * @param pos
     * @param size
     */
    public static void drawThing(Vector2f pos, float size)
    {
        GL11.glPushMatrix();
        
        GL11.glTranslatef(pos.x-size,pos.y-size,0.0f);
        
        GL11.glScalef(size*2,size*2,1.0f);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(1.0f,0.0f,1.0f,1.0f);
        
        GL11.glDrawArrays(GL11.GL_QUADS, 0, 4);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(1.0f,1.0f,1.0f,1.0f);
        GL11.glPopMatrix();
    }
    
    
    public static void enableColorizer(float R[],float G[], float B[])
    {
       Graphics.enableColorizer(R,G,B);
    }
    public static void disableColorizer()
    {
       Graphics.disableColorizer();
    }
    

    /** Piirtää keskitetyn spriten
     *
     * @param tex piirrettävä Texture
     * @param pos sijainti
     * @param rot kulma asteina, kasvaa vastapäivään
     * @param scale skaala x ja y suunnissa, 1.0f on normaalikoko
     */
    public static void drawSpriteCentered(Texture tex, Vector2f pos, float rot, Vector2f scale)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(pos.x,pos.y,0.0f);
        GL11.glRotatef(rot,0,0,-1);
        GL11.glScalef(scale.x,scale.y,0.0f);

        bindAndPrintCenteredTexture(tex.getBaseImage());

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
        GL11.glRotatef(rot,0,0,-1);

        bindAndPrintCenteredTexture(tex.getBaseImage());

        GL11.glPopMatrix();
    }
    
    /** Piirtää keskitetyn spriten additiivisella blendauksella
     *
     * @param tex piirrettävä Texture
     * @param pos sijainti
     * @param rot kulma asteina
     * @param scale skaala x ja y suunnissa, 1.0f on normaalikoko
     * @param color väri
     */
    public static void drawSpriteCenteredAdditive(Texture tex, Vector2f pos, float rot, Vector2f scale,float color[])
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(pos.x,pos.y,0.0f);
        GL11.glRotatef(rot,0,0,-1);
        GL11.glScalef(scale.x,scale.y,0.0f);
        
        GL11.glColor4f(color[0],color[1],color[2],color[3]);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND); 
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE);
        
        bindAndPrintCenteredTexture(tex.getBaseImage());
        
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        
        GL11.glColor4f(1.0f,1.0f,1.0f,1.0f);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glPopMatrix();
    }
    
    /** Piirtää keskitetyn spriten alpha blendauksella
     *
     * @param tex piirrettävä Texture
     * @param pos sijainti
     * @param rot kulma asteina
     * @param scale skaala x ja y suunnissa, 1.0f on normaalikoko
     * @param color väri
     */
    public static void drawSpriteCentered(Texture tex, Vector2f pos, float rot, Vector2f scale,float color[])
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(pos.x,pos.y,0.0f);
        GL11.glRotatef(rot,0,0,-1);
        GL11.glScalef(scale.x,scale.y,0.0f);
        GL11.glColor4f(color[0],color[1],color[2],color[3]);
        
        
        GL11.glEnable(GL11.GL_BLEND); 
        
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        bindAndPrintCenteredTexture(tex.getBaseImage());
        
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1.0f,1.0f,1.0f,1.0f);
        
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glPopMatrix();
    }
    
    
    /** Piirtää keskitetyn teksturoidun neliön
     * muokkaa GL matriisia, käytä varoen
     * @param tex TextureData object to use
     */
    public static void bindAndPrintCenteredTexture(ImageData tex)
    {
        if (tex != null)
        {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,tex.getGLName());
            GL11.glScalef(tex.getWidth(), tex.getHeight(), 1.0f);
            GL11.glTranslatef(-0.5f,-0.5f, 0.0f);
            GL11.glDrawArrays(GL11.GL_QUADS, 0, 4);
        }
    }
    
    /** Piirtää teksturoidun neliön
     * muokkaa GL matriisia, käytä varoen
     * @param tex TextureData object to use
     */
    public static void bindAndPrintTexture(ImageData tex)
    {
        if (tex != null)
        {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,tex.getGLName());
            GL11.glScalef(tex.getWidth(), tex.getHeight(), 1.0f);
            GL11.glDrawArrays(GL11.GL_QUADS, 0, 4);
        }
    }

    
}
