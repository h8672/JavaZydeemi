/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.graphics;

import Game.Main;
import org.lwjgl.util.vector.Vector2f;

/** Tuliefekti
 *
 * @author MacodiusMaximus
 */
public class FireParticle extends ParticleFX
{
    private Vector2f pos;
    private Vector2f vel;

    
    private Vector2f scale;
    private float rotation = 0;
    private int maxTimer;
    private boolean spawnSmoke = true;

    
    private static int smokeAmount = 3;
    
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
    
    /** Asettaa, spawnaako liekki savua
     *
     * @param spawnSmoke
     */
    public void setSpawnSmoke(boolean spawnSmoke) {
        this.spawnSmoke = spawnSmoke;
    }
    
    private Animator anim;
    private int timer;

    /** FireParticle constructor
     * <p>
     * Älä kutsu renderablen render() metodista
     * <p>
     * lisää partikkelin Graphics.IntermediateAdditiveLayer Renderable listaan
     *
     */
    public FireParticle ()
    {
        vel = new Vector2f();
        pos = new Vector2f();
        
        float fscale = 1.7f-Main.randomFloat()*1.4f ;
        scale = new Vector2f(fscale,fscale);
        anim = new Animator(Graphics.getAnimation("fieryFlames"));
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
        
        Texture tex = anim.getTexture();
        
        float smokeMul = (float)timer/maxTimer;
        if (maxTimer < 20)
            smokeMul += ((20-maxTimer)/20);
        if (smokeMul > 1)
            smokeMul = 1.0f;
        if (smokeMul < 0.0)
            smokeMul = 0.0f;
        
        float dSmoked = smokeMul*smokeMul;
        float[] color = new float[]{1.0f*smokeMul,1.0f*dSmoked,1.0f*dSmoked,1.0f - (0.5f*smokeMul)};
        
        
       
        Drawing.drawSpriteCenteredAdditive(tex, pos,rotation,scale, color);
        color[3] = 0.5f-smokeMul/2;
        Drawing.drawSpriteCentered(tex, pos,rotation,scale, color);
        
        anim.advance();
        timer--;
        if (timer == 0)
        {
            anim = new Animator(Graphics.getAnimation("flameOut"));
            if (spawnSmoke)
            {
                int d = Main.randomInt()%smokeAmount; 
                float smokeReach = 32;
                for (int i = 0; i < d; i++)
                {
                    Vector2f p = new Vector2f(pos);
                    p.x += smokeReach*Main.randomFloat()-smokeReach/2;
                    p.y += smokeReach*Main.randomFloat()-smokeReach/2;

                    Vector2f s = new Vector2f(scale);
                    s.x *= 0.96+1.55*Main.randomFloat();
                    s.y *= 0.96+1.55*Main.randomFloat();
                    SmokeParticle part = new SmokeParticle();
                    part.setPos(p);
                    part.setScale(s);

                }
            }
        }
        if (spawnSmoke)
        {
            if (Main.randomInt()%32 == 31)
            {
                Vector2f s = new Vector2f(scale);
                s.x *= 0.96+0.55*Main.randomFloat();
                s.y *= 0.96+0.55*Main.randomFloat();
                SmokeParticle part = new SmokeParticle();
                part.setPos(pos);
                part.setScale(s);
            }
        }
        
        float velDif = 0.6f;
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
        if (anim.hasReachedEnd())
        {
            Graphics.removeRenderable(this);
        }
        
        
    }
    
}
