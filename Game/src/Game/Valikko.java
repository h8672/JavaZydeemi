/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import java.util.ArrayList;

/**
 *
 * @author Juha-Matti
 */
public class Valikko {
    private int x;
    private ArrayList<String> valinnat = new ArrayList();
    
    public Valikko(){
        this.x = 0;
    }
    
    public void addValinta(String valinta){
        this.valinnat.add(valinta);
    }
}
