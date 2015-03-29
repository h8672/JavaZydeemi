/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state;

/**
 *
 * @author Juha-Matti
 */
public interface Mortality {
    //shows health after taking damage
    //if negative, deletes object from list
    //or displays player death message
    public float damage(float damage);
    
}
