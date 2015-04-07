/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Game.state.item.equipment.projectiles;

import Game.graphics.Drawing;
import Game.graphics.FireParticle;
import Game.graphics.Graphics;
import Game.state.item.Projectile;
import org.lwjgl.util.vector.Vector2f;

/**
 * Basic Particle
 * @author h8672
 */
public class Flame extends Projectile {
    
    public Flame(Vector2f position, float height, float rotation, float attackspeed, float attackrange, float DMG){
        this.setName("fire1");
        this.setPos(new Vector2f(position));
        this.setVel(attackspeed, rotation);
        this.setHeight(height);
        this.setAttackrange(attackrange);
        Graphics.registerRenderable(this,Graphics.IntermediateAlphaLayer);
    }
    
    @Override
    public boolean isVisible() {
        return true;
    }
    @Override
    public float hit(boolean stop){ // I hit something!
        if(stop) this.setAttackrange(0);
        //GameState.addEvent(new Explosion(this.getPos(), 3,4));
        return this.getDMG();
    }
    @Override
    public void render() {
        //float[] color = { 1.0f, 1.0f, 1.0f, 0.5f };
        //Drawing.drawSpriteCenteredAdditive(Graphics.getTexture(this.getName()), this.getPos(),5f,new Vector2f(size,size), color);
    }

    @Override
    public void update() {
        FireParticle f = new FireParticle();
        f.setPos(new Vector2f(this.getPos()));
    }
    
}
