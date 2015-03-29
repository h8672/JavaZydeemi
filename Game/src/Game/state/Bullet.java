/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.state;

import Game.graphics.Graphics;
import Game.graphics.ParticleFX;
import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author h8672
 */
public class Bullet extends ParticleFX {
    String type = "Bullet";
    Vector2f position, velocity;
    float height;
    
    public Bullet(Vector2f position, Vector2f velocity, float height){
        this.position = position;
        this.velocity = velocity;
        this.height = height;
        Graphics.registerRenderable(this,Graphics.IntermediateLayer);
    }
    
    @Override
    public boolean isVisible() {
        return true;
    }
    
    @Override
    public float getDepth() {
        return height;
    }
    
    @Override
    public void render() {
        float size = 2.0f;
        float[] color = { 1.0f, 1.0f, 1.0f };
        Graphics.getFont().renderTextExt(type, position, size, color);
    }
    
    public void update(){
        this.position.setX(this.position.getX() - this.velocity.getX());
        this.position.setY(this.position.getY() - this.velocity.getY());
        this.velocity.setX(this.velocity.getX() - 0.08f);
        this.velocity.setY(this.velocity.getY() + 0.05f);
    }
    
    class Basic {
        
        public Basic() {
            
        }
        
    }
    
    
}
