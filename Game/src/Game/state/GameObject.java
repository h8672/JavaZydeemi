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
public class GameObject {
    private Vector2f pos, size; //x and y vectors for position or size
    private float rot; //rotation 0-359 or smt
    private String ImageID; //image adress or id
    
    GameObject(){
        this.pos.x = 0;
        this.pos.y = 0;
        this.size.x = 1; //lenght in x
        this.size.y = 2; //lenght in y
        this.rot = 0;
    }
    
    public void setPos(float x, float y){
        this.pos.x = x;
        this.pos.y = y;
    }
    public void setSize(float x, float y){
        this.size.x = x;
        this.size.y = y;
    }
    public void setRot(float rotation){
        this.rot = rotation;
    }
    
    public Vector2f getPos(){
        return this.pos;
    }
    
    public Vector2f getSize(){
        return this.size;
    }
    
    public float getRot(){
        return this.rot;
    }
    
    public void update(){
        // miten päivitetään :o
    }
}
