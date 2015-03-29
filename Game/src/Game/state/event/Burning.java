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
public class Burning extends Event {
    public Burning(){
        this.setName("Burning");
        this.setRadius(100.0f);
        this.setDamage(1000.0f);
        this.setContagious(false);//ellei...
        Graphics.registerRenderable(this, Graphics.IntermediateLayer);
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public float getDepth() { //more dmg, bigger flame
        return this.getDamage()/this.getRadius();
    }

    @Override
    public void render() {
        //hohtava maa, liekkejä tai muuta
    }
}
