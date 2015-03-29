/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state.object.actor;

import Game.state.item.Usable;
import Game.state.item.Weapon;
import Game.state.object.GameObject;
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
    
    public Weapon attack();                 //hyökkää aseella
    public float defend(Weapon weapon);     //puolustautuu hyökkäykseltä
    public float defend(Usable usable);     //puolustautuu tavaralta
    public Vector2f move();                 //liikkuu kohtaan
}
