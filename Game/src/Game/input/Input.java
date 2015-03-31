/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.input;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.*;


/**
 *
 * @author Juha-Matti
 */
public class Input {
    int OldKey;
    public Input(){
        OldKey = 0;
    }
    /*
    Onko nappain alhaalla?
    Onko sama nappain ollut alhaalla?
    
    */
    
    public void menuButtons(){
        
    }
    
    public int getKey(){
        return (Keyboard.getEventKey());
    }
    
    public boolean isRepeat(){// toistuuko?
        return Keyboard.isRepeatEvent();
    }
    
    public boolean KeyState(){
        return Keyboard.getEventKeyState();
    }
    
    public void Repeat(boolean allow){// true to allow
        Keyboard.enableRepeatEvents(allow);
    }
    
    public void init(){
        try {
            Keyboard.create();
            Mouse.create();
        } catch (LWJGLException ex) {
            System.out.println("Ohjainten alustuksessa vikaa");
        }
    }
    
    public void destroy(){
        Keyboard.destroy();
        Mouse.destroy();
    }
}
