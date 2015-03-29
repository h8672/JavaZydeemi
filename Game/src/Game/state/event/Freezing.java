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
public class Freezing extends Event {
    public Freezing(){
        this.setName("Freezing");
        this.setRadius(100.0f);
        this.setDamage(1000.0f);
        this.setContagious(false);//ellei...
        Graphics.registerRenderable(this, Graphics.IntermediateLayer);
    }

    @Override
    public boolean isVisible() { //beware black ice tho
        return true;
    }

    @Override
    public float getDepth() { //strong coldness can be seen as snow
        return this.getDamage()/this.getRadius();
    }

    @Override
    public void render() {
        // jääpinta, lumisade tai muuta
    }
    
}
