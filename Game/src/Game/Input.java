/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.*;


/**
 *
 * @author Juha-Matti
 */
public class Input {

    /**
     *Tekee jotain lol...
     */
    public Input(){
        
        
        try {
            Keyboard.create();
            Mouse.create();
            
        } catch (LWJGLException ex) {
            System.out.println("Ohjainten alustus epäonnistui.");
            Keyboard.destroy();
        }
    }
    
    public boolean isKeyDown(int key){
        return (Keyboard.isKeyDown(key));
    }
    
    public void destroy(){
        Keyboard.destroy();
        Mouse.destroy();
    }
}
