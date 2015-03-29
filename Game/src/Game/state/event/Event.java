/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state.event;

import Game.graphics.Renderable;

/**
 *
 * @author Juha-Matti
 */
public abstract class Event implements Renderable{
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
    
    public String getName(){
        return this.name;
    }
    public float getRadius(){
        return this.radius;
    }
    public float getDamage(){
        return this.damage;
    }
    public boolean getContagious(){
        return this.contagious;
    }
}
