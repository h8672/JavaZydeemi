/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import java.util.ArrayList;

/**
 * Lataa menun, sirtää valintaa, tulostaa valinnan korostettuna ja
 * 2 ylempää ja alempaa valintaa näkyy erilaisena.
 * Näkyy vähintään 3 valintaa menussa.
 * 
 * 
 * @author Juha-Matti
 */
public class Menu extends Valikko {
    private int x, max;
    private ArrayList valinnat;
    
    public Menu(String otsikko){
        x = 0;
        valinnat = new ArrayList();
        valinnat.add(otsikko);
    }
    
    public void addChoice(String valinta){
        this.valinnat.add(valinta);
        this.max++;
    }
    
    public void moveUP(){
        if(1 < x){
            x--;
        } else;
    }
    
    public void moveDOWN(){
        if(x < max){
            x++;
        } else;
    }
    
    public int chosenone(){
        return x;
    }
    
    public ArrayList chosenlist(){
        return valinnat;
    }
}
