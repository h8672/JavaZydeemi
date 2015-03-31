/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Game.state.item.equipment;

import Game.state.GameState;
import Game.state.item.Weapon;
import game.state.item.equipment.projectiles.Bullet;
import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author h8672
 */
public class Flamethrower extends Weapon {
    public Flamethrower(){
        this.setName("Flamethrower");
        this.setDMG(20);
        this.setAttackrange(600);
        this.setAttackspeed(60);
        this.setCone(10);
        this.setClipsize(12);
        this.setAmmunation(36);
        this.setReloadtime(4);
    }
    
    @Override
    public void attack(Vector2f position, float height, float rotation) {
        GameState.addAttack(new Bullet(position, height, rotation, this.getAttackspeed(), this.getAttackrange(), this.getDMG()));
    }
    
}
