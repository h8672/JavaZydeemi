/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.state.object.actor.actors;

import Game.graphics.Drawing;
import Game.graphics.Graphics;
import Game.state.GameState;
import Game.state.item.Weapon;
import Game.state.item.equipment.AssaultRifle;
import Game.state.item.equipment.Clothes;
import Game.state.item.equipment.Flamethrower;
import Game.state.item.equipment.Pistol;
import Game.state.object.actor.Actors;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;



/**
 *
 * @author h8672
 */
public class Player extends Actors {
    
    private boolean isSpecialWeapon = false;
    public Player(Vector2f position, float height) {
        this.setPosition(position); // Position in map
        this.setHeight(height); // How high in the screen
        this.setRotation(0); // 2 PI rot / 360 degree
        this.setVelocity(new Vector2f(0,0)); // x / t
        this.setWeight(60); // G = m g
        this.setFriction(0.6f); // N = G µ  = m g µ// µ = friction
        this.setHP(200); // How much dmg can take
        this.setVisible(true);
        this.setArmor(new Clothes());
        Graphics.registerRenderable(this, Graphics.BaseLayer);
    }
    
    @Override
    public void update() {
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
            this.setRotation(this.getRotation() + 4f);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
            this.setRotation(this.getRotation() - 4f);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_Z)){
            this.setVelocity(3f, this.getRotation() + 90);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_X)){
            this.setVelocity(3f, this.getRotation() - 90);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)){
            this.addVelocity(4f, this.getRotation());
            
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
             this.addVelocity(-2f, this.getRotation());
             
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            this.attack();
        }
        
        if(this.getHP() <= 0){
            Graphics.removeRenderable(this);
            GameState.playerDied();
            GameState.delActor(this);
        }
        if (isSpecialWeapon)
            if (this.getWeapon().getAmmunation() <= 0)
            {
                this.setWeapon(new AssaultRifle());
                isSpecialWeapon = false;
            }
        
    }

    @Override
    public void render() {
        
        Vector2f rt = new Vector2f();
        rt.x = (float) -Math.sin(Math.toRadians(this.getRotation()));
        rt.y = (float) -Math.cos(Math.toRadians(this.getRotation()));
        Vector2f rt2 = new Vector2f(rt);
        rt2.x*=12;
        rt2.y*=12;
        rt.x*=222;
        rt.y*=222;
        
        rt2.x+=this.getPosition().x;
        rt2.y+=this.getPosition().y;
        rt.x+=this.getPosition().x;
        rt.y+=this.getPosition().y;
        
        Drawing.drawLine(rt,rt2,new float[]{0.9f,0,0,1});
        
        Drawing.drawBar(new Vector2f(800,10), 150, new float[] {0.5f, 0.1f, 0.1f}, new float[] {0.6f, 1.5f, 0f}, this.getHP(), 200,16);
        float[] colorHead = new float[]{0.2f,   0.2f,   0.2f,   1.0f};
        float[] colorTorso = new float[]{0.7f,   0.1f,   0.1f,   1.0f}; 
        float[] colorArms = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
        Drawing.enableColorizer(colorTorso, colorArms, colorHead);
        Drawing.drawSpriteCentered(Graphics.getTexture(this.getImage()), this.getPosition(),this.getRotation());
        Drawing.disableColorizer();
        
        Weapon wappet = this.getWeapon();
        Graphics.getFont().renderText(wappet.getName(), new Vector2f(1000,10));
        Graphics.getFont().renderTextExt(((int)wappet.getAmmunation())+"/"+((int)wappet.getClipsize()), new Vector2f(1000,25),2,new float[]{1,1,1,1});
        
        
        
    }

    public void setSpecialWeapon(Weapon weapon) {
        this.setWeapon(weapon);
        isSpecialWeapon = true;
    }

}
