/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game2d.state.event.events;

import game2d.state.event.Events;

/**
 *
 * @author Juha-Matti
 */
public class Burning extends Events {
    public Burning(){
        this.setName("Burning");
        this.setRadius(100.0f);
        this.setDamage(1000.0f);
        this.setContagious(false);//ellei...
    }

}
