/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.graphics;

import org.lwjgl.util.vector.Vector2f;

/** pomppiva teksti!!!
 *
 * @author MacodiusMaximus
 */
public class BouncyText extends ParticleFX
{
    private Vector2f pos;
    private Vector2f vel;
    private Vector2f gravity;
    private float bounceY;
    private float removeY;
    private float size;
    private String text;
    private boolean bounced;


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
    
    /** BouncyText constructor
     * <p>
     * lisää partikkelin Graphics.MenuLayer Renderable listaan
     *
     * @param str teksti
     */
    public BouncyText(String str)
    {
        
        pos = new Vector2f(300,-20);
        vel = new Vector2f(0,0);
        gravity = new Vector2f(0,0.2f);
        text = str;
        Graphics.registerRenderable(this,Graphics.MenuLayer);
        size = 6.0f;
        bounced = false;
        bounceY = 300;
        removeY = 900;
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
        vel.x += gravity.x;
        vel.y += gravity.y;
        
        pos.x += vel.x;
        pos.y += vel.y;
        
        Graphics.getFont().renderTextCool(text, pos, size);
        
        if (bounced == false)
            if (pos.y > bounceY)
            {
                vel.y = -vel.y/2;
                bounced = true;
            }
        if (pos.y > removeY)
            Graphics.removeRenderable(this);
        
    }
    
}

