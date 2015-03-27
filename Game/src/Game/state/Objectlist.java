/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state;

import java.util.ArrayList;

/**
 *
 * @author Juha-Matti
 */
public class Objectlist {
    ArrayList<GameObject> map;
    ArrayList<GameObject> actors;
    
    public Objectlist(){
        map = new ArrayList();
        actors = new ArrayList();
    }
}
