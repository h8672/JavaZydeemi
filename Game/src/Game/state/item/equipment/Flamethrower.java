/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Game.state.item.equipment;

import Game.state.GameState;
import Game.state.item.Weapon;
import Game.state.item.equipment.projectiles.Flame;
import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author h8672
 */
public class Flamethrower extends Weapon {
    public Flamethrower(){
        this.setName("Flamethrower");
        this.setDMG(25);
        this.setAttackrange(150);
        this.setAttackspeed(10);
        this.setSpeed(10);
        this.setCone(20);
        this.setClipsize(50);
        this.setAmmunation(50);
        this.setReloadtime(20);
    }

    @Override
    public void projectile(Vector2f position, float height, float rotation) {
        GameState.addAttack(new Flame(position, height, rotation, this.getSpeed(), this.getAttackrange(), this.getDMG()));
    }
    
}
