/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game2d.state.item.equipment;

import game2d.state.GameState;
import game2d.state.item.Weapon;
import game2d.state.item.equipment.projectiles.Flame;
import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author h8672
 */
public class Flamethrower extends Weapon {
    public Flamethrower(){
        this.setName("flamethrower");
        this.setDMG(15);
        this.setAttackrange(250);
        this.setAttackspeed(2);
        this.setSpeed(8);
        this.setCone(10);
        this.setClipsize(150);
        this.setAmmunation(150);
        this.setReloadtime(20);
    }

    @Override
    public void projectile(Vector2f position, float height, float rotation) {
        GameState.addAttack(new Flame(position, height, rotation, this.getSpeed(), this.getAttackrange(), this.getDMG()));
    }
    
}
