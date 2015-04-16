/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.state.item.equipment.projectiles;

import Game.graphics.Drawing;
import Game.graphics.Graphics;
import Game.state.item.Projectile;
import org.lwjgl.util.vector.Vector2f;

/**
 * Basic Particle
 * @author h8672
 */
public class Bullet extends Projectile {
    
    public Bullet(Vector2f position, float height, float rotation, float attackspeed, float attackrange, float DMG){
        this.setName("bullet");
        this.setPos(new Vector2f(position));
        this.setVel(attackspeed, rotation);
        this.setHeight(height);
        this.setAttackrange(attackrange);
        this.setDMG(DMG);
        Graphics.registerRenderable(this,Graphics.IntermediateAlphaLayer);
    }
    
    @Override
    public boolean isVisible() {
        return true;
    }
    @Override
    public void render() {
        Drawing.drawLine(this.getPos(), new Vector2f(this.getPos().getX() + this.getVel().getX(), this.getPos().getY() + this.getVel().getY()));
        //float[] color = { 1.0f, 1.0f, 1.0f, 0.5f };
        //Drawing.drawSpriteCenteredAdditive(Graphics.getTexture(this.getName()), this.getPos(),5f,new Vector2f(size,size), color);
    }

    @Override
    public void update() {
        
    }
    
}
