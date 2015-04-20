/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game2d.graphics;

import game2d.Main;
import org.lwjgl.util.vector.Vector2f;

/** Kipinä
 *
 * @author MacodiusMaximus
 */
public class SparkParticle extends ParticleFX
{
    private Vector2f pos;
    private Vector2f vel;

    
    private Vector2f scale;
    private float rotation = 0;
    private int maxTimer;
    private Texture texture;

    
    /** Palauttaa nopeuden
     *
     * @return nopeus
     */
    public Vector2f getVel() {
        return vel;
    }

    /** Asettaa nopeuden
     *
     * @param vel nopeus
     */
    public void setVel(Vector2f vel) {
        this.vel = vel;
    }
    
    /** Palauttaa skaalauksen
     *
     * @return skaalaus
     */
    public Vector2f getScale() {
        return scale;
    }

    /** Asettaa skaalauksen
     *
     * @param scale skaalaus
     */
    public void setScale(Vector2f scale) {
        this.scale = scale;
    }
    
    /** Palauttaa sijainnin
     *
     * @return
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
    
    private int timer;

    /** SparkParticle constructor
     * <p>
     * lisää partikkelin Graphics.IntermediateAdditiveLayer Renderable listaan
     *
     */
    public SparkParticle ()
    {
        vel = new Vector2f();
        pos = new Vector2f();
        
        float fscale = 0.2f-Main.randomFloat()*0.1f ;
        scale = new Vector2f(fscale,fscale);
        texture = Graphics.getTexture("spark");
        Graphics.registerRenderable(this,Graphics.IntermediateAdditiveLayer);
        timer = Math.abs(Main.randomInt())%44+4;
        rotation = Main.randomFloat()*360;
        maxTimer = timer;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public float getDepth() {
        return 0;
    }

    @Override
    public void render()
    {
        
        float alpha = 1.0f;
        alpha = ((float)timer)/maxTimer;
        float[] color = new float[]{1.0f,1.0f,1.0f,alpha};
       
        Drawing.drawSpriteCenteredAdditive(texture, pos,rotation,scale, color);

        float velDif = 0.2f;
        vel.x += (Main.randomFloat()-0.5)*velDif;
        vel.y += (Main.randomFloat()-0.5)*velDif;
        
        
        float maxVel = 1.0f;
        Vector2f d = new Vector2f();
        vel.normalise(d);
        
        float mult = vel.length()*0.94f;
        vel.x = d.x*mult;
        vel.y = d.y*mult;
        
        pos.x += vel.x;
        pos.y += vel.y;
        
        scale.x *= 0.96+0.05*Main.randomFloat();
        scale.y *= 0.96+0.05*Main.randomFloat();
        
        rotation += Main.randomFloat()*80-40;
        
        timer--;
        if (timer < 0)
        {
            Graphics.removeRenderable(this);
        }
        
        
    }
    
}
