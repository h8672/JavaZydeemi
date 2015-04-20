/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game2d.graphics;

import game2d.Main;
import org.lwjgl.util.vector.Vector2f;

/** Savuefekti
 *
 * @author MacodiusMaximus
 */
public class SmokeParticle extends ParticleFX
{
    private Vector2f pos;
    private Vector2f vel;

    
    private Vector2f scale;
    private float rotation = 0;
    private int maxTimer;
    private int delay = 0;
    
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
    
    private Animator anim;
    private int timer;

    /** SmokeParticle constructor
     * <p>
     * Älä kutsu renderablen render() metodista
     * <p>
     * lisää partikkelin Graphics.IntermediateAlphaLayer Renderable listaan
     *
     */
    public SmokeParticle ()
    {
        vel = new Vector2f();
        pos = new Vector2f();
        
        float fscale = 2.7f-Main.randomFloat()*1.4f ;
        scale = new Vector2f(fscale,fscale);
        
        anim = new Animator(Graphics.getAnimation("fieryFlames"));
        Graphics.registerRenderable(this,Graphics.IntermediateAlphaLayer);
        timer = Math.abs(Main.randomInt())%44+34;
        rotation = Main.randomFloat()*360;
        maxTimer = timer;
        //mahdollinen muutaman sekunnin kymmenyksen viive
        delay = Math.abs(Main.randomInt()%10);
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public float getDepth() {
        return 50;
    }

    @Override
    public void render()
    {
        if (delay > 0)
        {
            delay--;
            return;
        }
        Texture tex = anim.getTexture();
        
        float smokeMul = (float)timer/maxTimer;
        
        if (smokeMul > 0.8)
            smokeMul = 1.0f-(smokeMul-0.8f)*5;
        if (smokeMul < 0.0)
            smokeMul = 0.0f;
        
        smokeMul/=2;
        
        float[] color = new float[]{0.05f,0.075f,0.1f,smokeMul};
        
        Drawing.drawSpriteCentered(tex, pos,rotation,scale, color);
        
        
        anim.advance();
        timer--;
        if (timer == 0)
        {
            anim = new Animator(Graphics.getAnimation("flameOut"));
        }
        
        float velDif = 0.1f;
        vel.x += (Main.randomFloat()-0.5)*velDif;
        vel.y += (Main.randomFloat()-0.5)*velDif;
        
        
        float maxVel = 0.4f;
        Vector2f d = new Vector2f();
        vel.normalise(d);
        
        float mult = vel.length()*0.94f;
        vel.x = d.x*mult;
        vel.y = d.y*mult;
        
        pos.x += vel.x;
        pos.y += vel.y;
        
        scale.x *= 0.98;
        scale.y *= 0.98;
        
        rotation += Main.randomFloat()*10-5;
        if (anim.hasReachedEnd())
            Graphics.removeRenderable(this);
        
    }
    
}
