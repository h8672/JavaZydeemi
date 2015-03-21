/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import java.util.ArrayList;
import org.lwjgl.util.vector.Vector2f;

/**
 * Lataa menun, sirtää valintaa, tulostaa valinnan korostettuna ja
 * 2 ylempää ja alempaa valintaa näkyy erilaisena.
 * Näkyy vähintään 3 valintaa menussa.
 * 
 * 
 * @author Juha-Matti
 */
public class Menu extends Valikko implements Renderable {
    private int x, max;
    private ArrayList valinnat;
    
    public Menu(String otsikko){
        x = 0;
        valinnat = new ArrayList();
        valinnat.add(otsikko);
        Graphics.registerRenderable(this);
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
    
    @Override
    public boolean isVisible()
    {
        return true;
    }
    
    @Override
    public float getDepth()
    {
        return 100.0f; // Menu on kaikista päällimmäisenä
    }
    
    @Override
    public void render()
    {
        
        Graphics.getFont().renderText("Menutekstia!!!!!", new Vector2f(120,90));
        
        float[] color = {1.0f,0.9f,0.6f};
        
        if (Main.getTime() % 90 > 45)
            Graphics.getFont().renderTextExt("Lisaa menutekstia!!!!!", new Vector2f(((float)Math.sin(Main.getTime()/60)*100+200),190),2.0f,color);
        
        
        String xy = "MSAA: ";
        if (Graphics.getMSAAEnabled())
            xy = xy+"Enabled x"+Graphics.getMSAASamples();
        else
            xy = xy+"Disabled";
        Graphics.getFont().renderTextCool(xy, new Vector2f(77,390),3.0f);
    }
}
