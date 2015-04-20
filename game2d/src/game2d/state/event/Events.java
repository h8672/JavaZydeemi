/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game2d.state.event;

import org.lwjgl.util.vector.Vector2f;

/**
 * Base abstract class for Event interface
 * @author Juha-Matti
 */
public abstract class Events implements Event{
    String name; // Event name
    float radius; // PII radius^2 = area mostly
    float damage; // Event damage
    boolean contagious;
    Vector2f position;

    
    @Override
    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }
    
    public void setName(String name){
        this.name = name;
    }
    public void setRadius(float radius){
        this.radius = radius;
    }
    public void setDamage(float damage){
        this.damage = damage;
    }
    public void setContagious(boolean contagious){
        this.contagious = contagious;
    }
    
    @Override
    public String getName(){
        return this.name;
    }
    @Override
    public float getRadius(){
        return this.radius;
    }
    @Override
    public float getDamage(){
        return this.damage;
    }
    @Override
    public boolean getContagious(){
        return this.contagious;
    }
}
