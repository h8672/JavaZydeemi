/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.graphics;

import Game.Main;
import org.lwjgl.util.vector.Vector2f;

/** Räjähdysefektin välähdys
 *
 * @author MacodiusMaximus
 */
public class ExplosionParticle extends ParticleFX
{
    private Vector2f pos;
    private Vector2f scale;
    private Texture tex;
    float rotation = 0;
    int timer =1;

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
    /**Palauttaa sijainnin
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
    
    /** ExplosionParticle constructor
     * <p>
     * Älä kutsu renderablen render() metodista
     * <p>
     * lisää partikkelin Graphics.IntermediateLayer Renderable listaan
     *
     * @param size partikkelin koko
     */
    public ExplosionParticle (float size)
    {
        pos = new Vector2f();
        scale = new Vector2f(size,size);
        tex = Graphics.getTexture("explosion1");
        Graphics.registerRenderable(this,Graphics.IntermediateLayer);
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
        Drawing.drawSpriteCenteredAdditive(tex, pos,rotation,scale, new float[]{1.0f,1.0f,1.0f,0.5f});
        if (timer == 0)
        Graphics.removeRenderable(this);
        timer--;
        
        
    }
    
}

