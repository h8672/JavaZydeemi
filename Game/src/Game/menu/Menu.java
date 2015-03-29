/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.menu;
import Game.Main;
import Game.graphics.Renderable;
import Game.graphics.Graphics;
import game.state.Bullet;
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
public class Menu implements Renderable {
    private int x;
    private ArrayList valinnat;
    private float size;
    private boolean wait = true, select = false;
    private boolean visible = true;
    
    Bullet bullet = new Bullet(new Vector2f(400, 300), new Vector2f(5,4), 1.0f);
    
    public Menu(String otsikko){
        x = 1;
        valinnat = new ArrayList();
        valinnat.add(otsikko);
        Graphics.registerRenderable(this,Graphics.MenuLayer);
    }
    
    public boolean loop(){
        return wait;
    }
    
    public void close(){
        this.wait = false;
    }
    
    public void addChoice(String valinta){
        this.valinnat.add(valinta);
    }
    
    public void moveUP(){
        if(1 < x){// ei otsikkoa pysty valitsemaan
            x--;
        } else;
    }
    
    public void moveDOWN(){
        if(x < this.valinnat.size()-1){
            x++;
        } else;
    }
    
    
    public int chosenone(){
        select = true;
        return x;
    }
    
    public ArrayList chosenlist(){
        return valinnat;
    }
    
    @Override
    public boolean isVisible(){
        return visible;
    }
    
    @Override
    public float getDepth(){
        return 100.0f; // Menu on kaikista päällimmäisenä
    }
    
    @Override
    public void render(){
        bullet.update();
        float[] color = {1.0f,0.9f,0.6f};
        for(int i = 0; i < this.valinnat.size(); i++){
            if (i == 0) size = 3.0f;
            else if(i != x) size = 2.0f;
            else {
                size = 2.4f;
            }
            // teksti, sijainti, koko, väri
            Graphics.getFont().renderTextExt(
                    this.valinnat.get(i).toString(),
                    new Vector2f(160, ((float)Math.sin(Main.getTime()/20))*1.5f+90*(i+1)),
                    size,
                    color);
        }
        
        /*
        if (Main.getTime() % 90 > 45)
            Graphics.getFont().renderTextExt("Lisaa menutekstia!!!!!", new Vector2f(((float)Math.sin(Main.getTime()/60)*100+200),190),2.0f,color);
        
        
        String xy = "MSAA: ";
        if (Graphics.getMSAAEnabled())
            xy = xy+"Enabled x"+Graphics.getMSAASamples();
        else
            xy = xy+"Disabled";
        Graphics.getFont().renderTextCool(xy, new Vector2f(77,390),3.0f);
                */
    }
}
