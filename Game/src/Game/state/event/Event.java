/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state.event;

/**
 * Event interface
 * @author Juha-Matti
 */
public interface Event {
    
    public String getName();
    public float getRadius();
    public float getDamage();
    public boolean getContagious();
}
