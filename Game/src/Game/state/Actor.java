/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Game.state;

import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author h8672
 */
public class Actor extends GameObject {
    private Vector2f vel; //Velocity vector
    private int HP; //Actors hitpoints
    private int weight;
    private float friction; //friction
    
    public Actor(){
        this.setPos(0,0);   //Position (x and y)
        this.setSize(1,2);  //Size (width and lenght)
        this.setRot(0);     //Rotation
        this.HP = 100; //Health
        this.weight = 60;
        this.friction = 1;
    }
    
    public void setHP(int HP){
        this.HP = HP;
    }
    
    public void setWeight(int weight){
        this.weight = weight;
    }
    
    public void setFriction(float friction){
        this.friction = friction;
    }
    
    @Override
    public void update(){
        
    }
}
