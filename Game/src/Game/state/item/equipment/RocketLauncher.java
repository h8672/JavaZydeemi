/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state.item.equipment;

import Game.state.GameState;
import Game.state.item.Weapon;
import Game.state.item.equipment.projectiles.Flame;
import Game.state.item.equipment.projectiles.Rocket;
import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author MacodiusMaximus
 */
public class RocketLauncher  extends Weapon{

    public RocketLauncher(){
        this.setName("rocket launcher");
        this.setDMG(165);
        this.setAttackrange(2150);
        this.setAttackspeed(22);
        this.setSpeed(12);
        this.setCone(2);
        this.setClipsize(10);
        this.setAmmunation(10);
        this.setReloadtime(100);
    }

    @Override
    public void projectile(Vector2f position, float height, float rotation) {
        GameState.addAttack(new Rocket(position, height, rotation, this.getSpeed(), this.getAttackrange(), this.getDMG()));
    }
    
}

