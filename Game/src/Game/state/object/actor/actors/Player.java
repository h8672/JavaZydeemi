/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.state.object.actor.actors;

import Game.graphics.Graphics;
import Game.state.item.equipment.Clothes;
import Game.state.item.equipment.Pistol;
import Game.state.object.actor.Actors;
import org.lwjgl.util.vector.Vector2f;



/**
 *
 * @author h8672
 */
public class Player extends Actors {
    
    public Player(Vector2f position, float height) {
        Graphics.registerRenderable(this, Graphics.BaseLayer);
        this.setImage("Human");
        this.setPosition(position); // Position in map
        this.setHeight(height); // How high in the screen
        this.setRotation(0); // 2 PI rot / 360 degree
        this.setVelocity(new Vector2f(0,0)); // x / t
        this.setWeight(60); // G = m g
        this.setFriction(0.6f); // N = G µ  = m g µ// µ = friction
        this.setHP(100); // How much dmg can take
        this.setVisible(true);
        this.setArmor(new Clothes());
        this.setWeapon(new Pistol());
    }
    
    @Override
    public void update() {
        
    }

    @Override
    public void render() {
        
    }

}
