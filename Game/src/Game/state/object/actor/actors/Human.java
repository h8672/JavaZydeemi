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
import Game.graphics.ParticleEffects;
import Game.state.item.equipment.Clothes;
import Game.state.item.equipment.Pistol;
import Game.state.object.actor.Actors;
import org.lwjgl.util.vector.Vector2f;

/**
 * Human actor
 * @author Juha-Matti
 */
public class Human extends Actors
{
    
    private float[] colorHead;
    private float[] colorTorso;
    private float[] colorArms;

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
        
        this.colorHead = new float[]{0.7f,   0.7f,   0.7f,   1.0f};
        this.colorTorso = new float[]{0.7f,   0.1f,   0.1f,   1.0f}; 
        this.colorArms = new float[]{1f, 0.925f, 0.765f, 1.0f};
    }
    
    /** Muuttaa hahmon pään väriä
     *
     * @param colorHead float taulukko missä on 4 elementtiä (Red,Green,Blue,Alpha)
    */
    public void setColorHead(float[] colorHead) {
        this.colorHead = colorHead;
    }

    /** Muuttaa hahmon ruumiin väriä
     *
     * @param colorTorso float taulukko missä on 4 elementtiä (Red,Green,Blue,Alpha)
    */
    public void setColorTorso(float[] colorTorso) {
        this.colorTorso = colorTorso;
    }

    /** Muuttaa hahmon käsien väriä
     *
     * @param colorArms float taulukko missä on 4 elementtiä (Red,Green,Blue,Alpha)
    */
    public void setColorArms(float[] colorArms) {
        this.colorArms = colorArms;
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
        Drawing.enableColorizer(colorTorso, colorArms, colorHead);
        Drawing.drawSpriteCentered(Graphics.getTexture("tyyppi1"), this.getPosition(),this.getRotation());
        Drawing.disableColorizer();
    }

}
