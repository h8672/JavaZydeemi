/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.*;
import static org.lwjgl.input.Keyboard.*;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

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
    private static Menu menu;
    
    public static void main(String[] args) {
        init();
        menu();
    }
    
    private static void menu(){
        menu = new Menu("otsikko");
        while(true){
            if(Keyboard.isKeyDown(KEY_UP)){
                menu.moveUP();
            }
            if(Keyboard.isKeyDown(KEY_DOWN)){
                menu.moveDOWN();
            }
            if(Keyboard.isKeyDown(KEY_RETURN)){
                switch(menu.chosenone()){
                    case 1:
                        System.out.println("Ensimmäinen valinta");
                        break;
                    default:
                        break;
                }
            }
            
        }
    }
    
    private static void init(){
        Input(); //lataa ohjaimet
        
        try {
            Display.setDisplayMode(new DisplayMode(500,400));
            Display.setTitle("Hieno!");
            Display.create();
        } catch (LWJGLException ex) {
            System.out.println("Test");
        }
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
            //if(input.isKeyDown(59)){
            //    System.out.println("Se on!");
            //}
            
            if(Keyboard.isKeyDown(KEY_UP)){
                Display.setTitle("Ylös");
            } else Display.setTitle("testi");
            
            Display.update();
        }
    }
    
    private static void cleanUp(){
        Display.destroy();
        //input.destroy();
        Keyboard.destroy();
        Mouse.destroy();
    }
    
}
