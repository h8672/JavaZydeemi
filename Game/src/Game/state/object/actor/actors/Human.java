/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state.object.actor.actors;

import Game.Main;
import Game.graphics.Drawing;
import Game.graphics.FireParticle;
import Game.graphics.Graphics;
import Game.state.item.equipment.Clothes;
import Game.state.item.equipment.Pistol;
import Game.state.object.actor.Actors;
import org.lwjgl.util.vector.Vector2f;

/**
 * Human actor
 * @author Juha-Matti
 */
public class Human extends Actors {

    public Human(Vector2f position, float height) {
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
    public void update()
    {
        this.setRotation(this.getRotation()+Main.randomFloat()*2);
        float xd = (float) -Math.sin(Math.toRadians(this.getRotation()))*0.65f;
        float yd = (float) -Math.cos(Math.toRadians(this.getRotation()))*0.65f;
        
        float fx = this.getPosition().x+xd*50;
        float fy = this.getPosition().y+yd*50;
        FireParticle f = new FireParticle();
        f.setPos(new Vector2f(fx,fy));
        f.setVel(new Vector2f(xd*18,yd*18));
        

        xd +=this.getPosition().x;
        yd +=this.getPosition().y;
        this.setPosition(new Vector2f(xd,yd));
        
        
    }

    @Override
    public void render()
    {
        float[] R = new float[]{0.7f,   0.1f,   0.1f,   1.0f};
        float[] G = new float[]{1f, 0.925f, 0.765f, 1.0f};
        float[] B = new float[]{0.7f,   0.7f,   0.7f,   1.0f};
        Drawing.enableColorizer(R, G, B);
        Drawing.drawSpriteCentered(Graphics.getTexture("tyyppi1"), this.getPosition(),this.getRotation());
        Drawing.disableColorizer();
    }

}
