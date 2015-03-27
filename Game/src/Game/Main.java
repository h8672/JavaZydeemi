/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import Game.graphics.Graphics;
import Game.menu.Menu;
import java.util.Random;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.*;
import static org.lwjgl.input.Keyboard.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector2f;

/**
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Scanner;
 *
 * @author Juha-Matti
 */
public class Main {
    /*
    private static GameState game;
    private static Graphics graphics;
        Vector2f vector = new Vector2f();
        Vector2f velocity = new Vector2f();
        vector.translate(velocity.x, velocity.y);
    */
    //private static Input input;
    
    /**
     * @param args the command line arguments
     */
    private static Random randomizer;
    private static int time;
    
    public static int randomInt() {
        return randomizer.nextInt();
    }
    
    public static float randomFloat() {
        return randomizer.nextFloat();
    }

    public static float getTime() {
        return time;
    }

    public static void main(String[] args) {
        init();
        menu();
    }
    
    private static void menu(){
        // Menun alustus
        Menu menu = new Menu("Woot gaming!");
        // Menun vaihtoehtojen lisäys
        menu.addChoice("Aloita peli");
        menu.addChoice("Lopeta");
        // Ikkunan otsikon muutos
        Display.setTitle("Main menu");
        
        while(menu.loop()){
            render();
            if(Keyboard.isKeyDown(KEY_UP)){
                menu.moveUP();
            }
            else if(Keyboard.isKeyDown(KEY_DOWN)){
                menu.moveDOWN();
            }
            //Valinta ehto
            else if(Keyboard.isKeyDown(KEY_RETURN)){
                switch(menu.chosenone()){
                    case 1:
                        gameLoop();
                        break;
                    case 2:
                        break;
                    default:
                        break;
                }
                menu.close();
            }
            // Sulkemis ehto
            else if(Keyboard.isKeyDown(
                    Keyboard.KEY_ESCAPE) || Display.isCloseRequested()
                    ) menu.close();
            else;
            time++;
        }
        Graphics.removeRenderable(menu);
    }
    
    private static void init(){
        randomizer = new Random();
        
        try {
            Display.setDisplayMode(new DisplayMode(800,600));
            Display.create();
            Display.setVSyncEnabled(true);
            Graphics.init(800,600); //ikkunan koko oltava sama
            

        } catch (LWJGLException ex) {
            System.out.println("Test");
        }
        Input(); //lataa ohjaimet
    }
    
    private static void Input(){
        try {
            Keyboard.create();
        } catch (LWJGLException ex) {
            System.out.println("Näppäimistöä ei löytynyt");
        }
        try {
            Mouse.create();
        } catch (LWJGLException ex) {
            System.out.println("Hiirtä ei löytynyt");
        }
        
    }
    
    private static void gameLoop(){
        while(!Display.isCloseRequested()){
            
            
            
            render();
            time++;
        }
    }
    
    private static void render(){
            Graphics.render();
            Display.update();
            Display.sync(60);
    }
    
    
    
    private static void cleanUp(){
        Display.destroy();
        //input.destroy();
        Keyboard.destroy();
        Mouse.destroy();
    }
    
}
