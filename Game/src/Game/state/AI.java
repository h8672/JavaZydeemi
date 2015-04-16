/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.state;

import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author h8672
 */
public class AI {
    Vector2f position, destination;
    Pathfind path = new Pathfind();
    
    public AI(){
        this.position = new Vector2f();
        this.destination = new Vector2f();
    }

    public void setPos(Vector2f position){
        this.position = position;
    }
    public void setDes(Vector2f destination){
        this.destination = destination;
    }
    public void check(){
        
    }
    
}
