/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state.event.events;

import Game.state.event.Events;

/**
 *
 * @author Juha-Matti
 */
public class Freezing extends Events {
    public Freezing(){
        this.setName("Freezing");
        this.setRadius(100.0f);
        this.setDamage(1000.0f);
        this.setContagious(false);//ellei...
    }
    
}
