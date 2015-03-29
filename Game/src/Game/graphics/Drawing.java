/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
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
        
        GL11.glTranslatef(pos.x,pos.y,0.0f);
        
        GL11.glScalef(size,size,1.0f);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(1.0f,0.0f,1.0f,1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        
        
        GL11.glVertex2f(-1,-1);
        
        GL11.glVertex2f(1,-1);
        
        GL11.glVertex2f(1,1);
        
        GL11.glVertex2f(-1,1);

        GL11.glEnd();
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
    
}
