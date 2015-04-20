/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game2d.input;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.*;

class KeyState
{
    private boolean down;
    private boolean stateChanged;
    public boolean isDown()
    {
        return down;
    }
    public boolean isPressed()
    {
        return down&&stateChanged;
    }
    public void update()
    {
        stateChanged = false;
    }
    public void press()
    {
        stateChanged = true;
        down = true;
    }  
    public void release()
    {
        stateChanged = true;
        down = false;
    }
            
}
/**
 *
 * @author Juha-Matti
 */
public class Input
{
    static private LinkedList<Integer> keys;
    static private TreeMap<Integer,KeyState> keyStates;
    public static void init()
    {
        try {
            Keyboard.create();
            Mouse.create();
        } catch (LWJGLException ex) {
            System.out.println("Ohjainten alustuksessa vikaa");
        }
        keyStates = new TreeMap<>();
        
        keys = new LinkedList<>();
        
        keyStates.put(Keyboard.KEY_UP,new KeyState());
        keyStates.put(Keyboard.KEY_DOWN,new KeyState());
        keyStates.put(Keyboard.KEY_LEFT,new KeyState());
        keyStates.put(Keyboard.KEY_RIGHT,new KeyState());
        keyStates.put(Keyboard.KEY_Z,new KeyState());
        keyStates.put(Keyboard.KEY_RETURN,new KeyState());
        keyStates.put(Keyboard.KEY_ESCAPE,new KeyState());
        keyStates.put(Keyboard.KEY_SPACE,new KeyState());
        keyStates.put(Keyboard.KEY_X,new KeyState());

        
       
    }
    /*
    Onko nappain alhaalla?
    Onko sama nappain ollut alhaalla?
    
    */
    
    public static void update()
    {
        for (Map.Entry<Integer,KeyState> k : keyStates.entrySet())
        {
            k.getValue().update();
        }
         
        while (Keyboard.next())
        {
            int key = Keyboard.getEventKey();
            if (!keyStates.containsKey(key)) continue;
            
            if (Keyboard.getEventKeyState())
                keyStates.get(key).press();
            else
                keyStates.get(key).release();
        }
        
       
    }
    public static boolean getKeyPressed(int key){
        return keyStates.get(key).isPressed();
    }
    public static boolean getKeyDown(int key){
        return keyStates.get(key).isDown();
    }

    public void destroy(){
        Keyboard.destroy();
        Mouse.destroy();
    }
}
