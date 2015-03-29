/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state.item.equipment;

import Game.state.item.Armor;

/**
 * Basic armor
 * @author Juha-Matti
 */
public class Clothes extends Armor {
    public Clothes(){
        this.setName("Clothes");
        this.setAmount(10);
    }
}
