/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author MacodiusMaximus
 */
public class Decal extends ParticleFX
{
    private Vector2f pos;
    private Vector2f scale;

    public Vector2f getScale() {
        return scale;
    }

    public void setScale(Vector2f scale) {
        this.scale = scale;
    }
    private Texture texture;
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
    
    public Decal (Texture tex)
    {
        pos = new Vector2f();
        scale = new Vector2f(1f,1f);
        texture = tex;
        
        rotation = Main.randomFloat()*360;
        Graphics.registerRenderable(this,Graphics.BaseLayer);
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
       
        Drawing.drawSpriteCentered(texture, pos,rotation,scale);
        

    }
    
}
