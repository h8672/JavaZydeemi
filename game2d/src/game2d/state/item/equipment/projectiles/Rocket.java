/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game2d.state.item.equipment.projectiles;

import game2d.Main;
import game2d.graphics.Drawing;
import game2d.graphics.FireParticle;
import game2d.graphics.Graphics;
import game2d.state.GameState;
import game2d.state.event.events.Explosion;
import game2d.state.item.Projectile;
import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author MacodiusMaximus
 */
public class Rocket extends Projectile
{
    private int flameSpawnTimer;
    public Rocket(Vector2f position, float height, float rotation, float attackspeed, float attackrange, float DMG){
        this.setName("fire1");
        this.setPos(new Vector2f(position));
        this.setVel(attackspeed, rotation);
        this.setHeight(height);
        this.setAttackrange(attackrange);
        this.setDMG(DMG);
        Graphics.registerRenderable(this,Graphics.IntermediateAlphaLayer);
        flameSpawnTimer = 0;
    }
    
    @Override
    public boolean isVisible() {
        return true;
    }
    @Override
    public float hit(boolean stop){ // I hit something!
        this.setAttackrange(0);
        GameState.addEvent(new Explosion(this.getPos(), 70, 140));
        return this.getDMG();
    }
    @Override
    public void render() {
        Drawing.drawLine(this.getPos(), new Vector2f(this.getPos().getX() + this.getVel().getX(), this.getPos().getY() + this.getVel().getY()));
    }

    @Override
    public void update() {
        if (flameSpawnTimer > 3)
        {
            if (Main.randomInt()%12 > 4)
            {
                FireParticle f = new FireParticle();
                f.setPos(new Vector2f(this.getPos()));
            }
        }
        else
            flameSpawnTimer++;
    }
    
}

