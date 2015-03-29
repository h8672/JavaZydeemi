/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state.item.equipment;

import Game.state.item.Weapon;

/**
 *
 * @author Juha-Matti
 */
public class Pistol extends Weapon {
    public Pistol(){
        this.setName("Pistol");
        this.setDMG(20);
        this.setAttackrange(50);
        this.setCone(10);
        this.setClipsize(12);
        this.setAmmunation(36);
        this.setReloadtime(4);
    }
}
