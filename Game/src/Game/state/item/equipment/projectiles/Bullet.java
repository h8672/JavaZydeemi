/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.state.item.equipment.projectiles;

import Game.graphics.Drawing;
import Game.graphics.FireParticle;
import Game.graphics.Graphics;
import Game.state.GameState;
import Game.state.event.events.Explosion;
import Game.state.item.Projectile;
import org.lwjgl.util.vector.Vector2f;

/**
 * Basic Particle
 * @author h8672
 */
public class Bullet extends Projectile {
    
    public Bullet(Vector2f position, float height, float rotation, float attackspeed, float attackrange, float DMG){
        this.setName("fire1");
        this.setPos(new Vector2f(position));
        this.setVel(attackspeed, rotation);
        this.setHeight(height);
        this.setAttackrange(attackrange);
        Graphics.registerRenderable(this,Graphics.IntermediateLayer);
    }
    
    @Override
    public boolean isVisible() {
        return true;
    }
    @Override
    public float hit(){ // I hit something!
        this.setAttackrange(0);
        GameState.addEvent(new Explosion(this.getPos(), 3,4));
        return this.getDMG();
    }
    @Override
    public void render() {
        float size = 2.0f;
        float[] color = { 1.0f, 1.0f, 1.0f, 0.5f };
        Drawing.drawThing(this.getPos(), size);
        //Drawing.drawSpriteCenteredAdditive(Graphics.getTexture(this.getName()), this.getPos(),5f,new Vector2f(size,size), color);
    }

    @Override
    public void update() {
        FireParticle f = new FireParticle();
        f.setPos(new Vector2f(this.getPos()));
    }
    
}
