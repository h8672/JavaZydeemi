/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game2d.state.object.actor;

import game2d.state.Attack;
import game2d.state.item.Usable;
import game2d.state.object.GameObject;
import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author Juha-Matti
 */
public interface Actor extends GameObject { // movable and acting game object
    
    /**
     * Actor base methods
     * @return Weapon
     */
    
    public void attack();                 //hyökkää aseella
    public float defend(Attack attack);     //puolustautuu hyökkäykseltä
    public float defend(Usable usable);     //puolustautuu tavaralta
    public Vector2f move();                 //liikkuu kohtaan
    public void kill();                     //suorittaa poistooperaatiot
}
