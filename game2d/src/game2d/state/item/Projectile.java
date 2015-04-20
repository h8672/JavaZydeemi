/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game2d.state.item;

import game2d.graphics.Graphics;
import game2d.graphics.Renderable;
import game2d.state.Attack;
import game2d.state.GameState;
import game2d.state.event.Event;
import org.lwjgl.util.vector.Vector2f;

/**
 * Base abstract class for Attack interface
 * @author Juha-Matti
 */
public abstract class Projectile implements Attack, Renderable {
    private String name;
    private Vector2f position, velocity;
    private float attackrange, height, DMG;
    private Event event;
    
    public void setName(String name){
        this.name = name;
    }
    public void setAttackrange(float attackrange){
        this.attackrange = attackrange;
    }
    public void setHeight(float height){
        this.height = height;
    }
    public void setPos(Vector2f position){
        this.position = position;
    }
    public void setVel(float attackspeed, float rotation){
        velocity = new Vector2f(0,0);
        this.velocity.setX(- attackspeed * (float)Math.sin(Math.toRadians(rotation)));
        this.velocity.setY(- attackspeed * (float)Math.cos(Math.toRadians(rotation)));
    }
    public void setDMG(float DMG){
        this.DMG = DMG;
    }
    
    public String getName(){
        return this.name;
    }
    public float getDMG(){
        return this.DMG;
    }
    
    //Shooting interface methods
    @Override
    public Vector2f getPos(){
        return this.position;
    }
    @Override
    public Vector2f getVel(){
        return this.velocity;
    }
    @Override
    public float hit(boolean stop){ // I hit something!
        attackrange = 0;
        move();
        return this.DMG;
    }
    public void hitEvent(Event event){
        this.event = event;
    }
    @Override
    public void move(){
        
        if (attackrange >= this.velocity.length()){
            this.position.x += this.velocity.x;
            this.position.y += this.velocity.y;
            this.attackrange -= this.velocity.length();
        }
        else if(attackrange == 0) {
            kill();
            GameState.delAttack(this);
        }
        else{ // Mahollisesti väärä laskutoimitus
            this.position.x += (this.velocity.x * attackrange / this.velocity.length());
            this.position.y += (this.velocity.y * attackrange / this.velocity.length());
            this.attackrange = 0;
        }
    }
    
    //Renderable interface methods
    @Override
    public float getDepth() {
        return height;
    }
    
    @Override
    public void kill() {
        Graphics.removeRenderable(this);
    }
}
