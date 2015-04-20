/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game2d.state.event.events;

import game2d.graphics.ParticleEffects;
import game2d.state.event.Events;
import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author Juha-Matti
 */
public class Explosion extends Events {
    public Explosion(Vector2f position){
        this.setName("Explosion");
        this.setRadius(100.0f);
        this.setDamage(1000.0f);
        this.setContagious(false);//ellei...
        this.setPosition(position);
        ParticleEffects.explode(position);
    }
    public Explosion(Vector2f position, float radius, float damage){
        this.setName("Explosion");
        this.setRadius(radius);
        this.setDamage(damage);
        this.setContagious(false);//ellei...
        this.setPosition(position);
        ParticleEffects.explode(position);
    }
}
