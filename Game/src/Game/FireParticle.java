/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import org.lwjgl.util.vector.Vector2f;

/** Tuliefekti
 *
 * @author MacodiusMaximus
 */
public class FireParticle extends ParticleFX
{
    private Vector2f pos;
    private Vector2f scale;
    float rotation = 0;

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
    int timer;

    /** FireParticle constructor
     * <p>
     * Älä kutsu renderablen render() metodista
     * <p>
     * lisää partikkelin Graphics.IntermediateLayer Renderable listaan
     *
     */
    public FireParticle ()
    {
        pos = new Vector2f();
        scale = new Vector2f(1,1);
        anim = new Animator(Graphics.getAnimation("fieryFlames"));
        Graphics.registerRenderable(this,Graphics.IntermediateLayer);
        timer = Math.abs(Main.randomInt())%16+4;
        rotation = Main.randomFloat()*360;
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
        
       
            Graphics.drawSpriteCenteredAdditive(tex, pos,rotation,scale);
        
        anim.advance();
        timer--;
        if (timer == 0)
        {
            anim = new Animator(Graphics.getAnimation("flameOut"));
            
        }
        
        pos.x += Main.randomFloat()*4-2;
        pos.y += Main.randomFloat()*4-2;
        
        scale.x *= 0.96+0.05*Main.randomFloat();
        scale.y *= 0.96+0.05*Main.randomFloat();
        
        rotation += Main.randomFloat()*80-40;
        if (anim.hasReachedEnd())
            Graphics.removeRenderable(this);
        
    }
    
}
