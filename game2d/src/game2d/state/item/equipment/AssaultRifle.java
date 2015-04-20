/**
 *
 * @author MacodiusMaximus
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game2d.state.item.equipment;

import game2d.state.GameState;
import game2d.state.item.Weapon;
import game2d.state.item.equipment.projectiles.Bullet;
import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author MacodiusMaximus
 */
public class AssaultRifle extends Weapon {
    public AssaultRifle(){
        this.setName("Assault Rifle"); // name
        this.setDMG(35); // how much dmg weapon does
        this.setAttackrange(600); // how far attacks will go
        this.setAttackspeed(8); // how many attacks per second
        this.setSpeed(20); //Visual speed
        this.setCone(10); // how much attacks will go side at best
        this.setClipsize(30);
        this.setAmmunation(30); 
        this.setReloadtime(60); // how many much time reloading takes
    }

    @Override
    public void projectile(Vector2f position, float height, float rotation) {
        GameState.addAttack(new Bullet(position, height, rotation, this.getSpeed(), this.getAttackrange(), this.getDMG()));
    }
    
}
