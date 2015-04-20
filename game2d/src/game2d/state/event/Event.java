/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game2d.state.event;

import org.lwjgl.util.vector.Vector2f;

/**
 * Event interface
 * @author Juha-Matti
 */
public interface Event {
    
    public String getName();
    public float getRadius();
    public float getDamage();
    public Vector2f getPosition();
    public boolean getContagious();
}
