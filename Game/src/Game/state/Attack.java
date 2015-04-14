/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state;

import org.lwjgl.util.vector.Vector2f;

/**
 * Attack interface
 * @author Juha-Matti
 */
public interface Attack {
    public Vector2f getPos(); // Where are you?
    public Vector2f getVel(); // When will you come?
    public float hit(boolean stop); // I hit something!
    public void update(); // I hit something!
    public void move(); // if flies!
    public void kill();
}
