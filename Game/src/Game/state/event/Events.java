/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state.event;

/**
 * Base abstract class for Event interface
 * @author Juha-Matti
 */
public abstract class Events implements Event{
    String name; // Event name
    float radius; // PII radius^2 = area mostly
    float damage; // Event damage
    boolean contagious;
    
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
