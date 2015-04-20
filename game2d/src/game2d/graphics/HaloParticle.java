/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game2d.graphics;

import game2d.Main;
import org.lwjgl.util.vector.Vector2f;

/** Hieno rinkula
 *
 * @author MacodiusMaximus
 */
public class HaloParticle extends ParticleFX
{
    private Vector2f pos;

    private Vector2f scale;
    private float rotation = 0;
    private int maxTimer;
    private Texture texture;

    
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
    public HaloParticle ()
    {
        pos = new Vector2f();
        
        float fscale = 0.5f ;
        scale = new Vector2f(fscale,fscale);
        texture = Graphics.getTexture("halo");
        Graphics.registerRenderable(this,Graphics.IntermediateAlphaLayer);
        timer = 60;
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
        if (timer < 10)
        {
            alpha = ((float)timer*6)/maxTimer;
        }
        float[] color = new float[]{1.0f,1.0f,1.0f,alpha};
       
        Drawing.drawSpriteCentered(texture, pos,rotation,scale,color);
        
        
        rotation += 1;
        
        timer--;
        if (timer < 0)
        {
            Graphics.removeRenderable(this);
        }
        
        
    }
    
}
