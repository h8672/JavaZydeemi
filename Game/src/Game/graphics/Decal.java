/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.graphics;

import Game.Main;
import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author MacodiusMaximus
 */
public class Decal extends ParticleFX
{
    private Vector2f pos;
    private Vector2f scale;
    private int disappearTimer;
    private static int decalDisappearTime = 300;
    private static int maxDecals = 100;
    private static int totalDecalsCreated = 0;
    private int decalID = 0;
    private boolean toBeDeleted = false;
    /** Palauttaa skaalauksen
     *
     * @return skaalaus
     */
    public Vector2f getScale() {
        return scale;
    }

    /** Asettaa skaalauksen
     *
     * @param scale asetettava skaalaus
     */
    public void setScale(Vector2f scale) {
        this.scale = scale;
    }
    private Texture texture;
    float rotation = 0;
    /** Palauttaa sijainnin
     *
     * @return sijainti
     */
    public Vector2f getPos() {
        return pos;
    }

    /** Asettaa sijainnin
     *
     * @param pos
     */
    public void setPos(Vector2f pos) {
        this.pos = pos;
    }
    
    /** Decal luokan konstruktori. Luo uuden Decal olion tietyllä tekstuurilla.
     *
     * @param tex tekstuuri
     */
    public Decal (Texture tex)
    {
        disappearTimer = 0;
        pos = new Vector2f();
        scale = new Vector2f(1f,1f);
        texture = tex;
        
        
        rotation = Main.randomFloat()*360;
        Graphics.registerRenderable(this,Graphics.IntermediateAlphaLayer);
        
        decalID = totalDecalsCreated;
        totalDecalsCreated++;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public float getDepth() {
        return -50;
    }

    @Override
    public void render()
    {
        float alpha = 1f-((float)disappearTimer/decalDisappearTime);
        alpha = alpha*alpha*alpha;
        float[] color = new float[]{1.0f,1.0f,1.0f,alpha};
        Drawing.drawSpriteCentered(texture, pos,rotation,scale,color);
        
        
        if (toBeDeleted)
        {
            disappearTimer++;
            if (disappearTimer > decalDisappearTime)
                Graphics.removeRenderable(this);
        }
        else
        {
            //onko decalID yli maxDecals jäljessä totalDecalsCreatedia
            //eli ollanko tehty yli maxDecals määrä decaleja tämän decalin luonnin jälkeen
            //jos niin, niin tämä decali poistoon
            if ((decalID) < (totalDecalsCreated-maxDecals))
            {
                toBeDeleted = true;
            }
        }
        
        

    }
    
}
