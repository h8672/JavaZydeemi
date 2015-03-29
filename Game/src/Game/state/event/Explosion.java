/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state.event;

import Game.graphics.Graphics;

/**
 *
 * @author Juha-Matti
 */
public class Explosion extends Event {
    public Explosion(){
        this.setName("Explosion");
        this.setRadius(100.0f);
        this.setDamage(1000.0f);
        this.setContagious(false);//ellei...
        Graphics.registerRenderable(this, Graphics.IntermediateLayer);
    }
    public Explosion(float radius, float damage){
        this.setName("Explosion");
        this.setRadius(radius);
        this.setDamage(damage);
        this.setContagious(false);//ellei...
        Graphics.registerRenderable(this, Graphics.IntermediateLayer);
    }

    @Override
    public boolean isVisible() { //Explosions can be seen usually
        return true;
    }
    @Override
    public float getDepth() { //Explosions are usually quite visual and not many thing can be in its way
        return 100f;
    }

    @Override
    public void render() { //Renders the explosion to fit how it happens
        //räjähdys...
    }
}
