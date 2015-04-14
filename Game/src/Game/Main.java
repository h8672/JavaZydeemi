/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import Game.graphics.Graphics;
import static Game.graphics.Graphics.loadTexture;
import Game.graphics.GraphicsSettings;
import Game.input.Input;
import Game.menu.Menu;
import Game.state.GameState;
import java.util.Random;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.*;
import static org.lwjgl.input.Keyboard.*;
import org.lwjgl.opengl.*;

/**
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Scanner;
 *
 * @author Juha-Matti
 */
public class Main {
    //private static Input input;
    
    
    private static Random randomizer;
    private static int time;
    private static int state;
    private static Menu mainMenu;
    private static Menu mainMenuInGame;
    private static Menu options;
    private static GameState game;
    private static boolean gameInProgress = false;
    
    /** Palauttaa satunnaisen kokonaisluvun
     * 
     * @return satunnainen kokonaisluku
     */
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
        game = new GameState();
        state = 1;
        while (state != 0)
        {
            switch (state)
            {
                case 1:
                case 2:
                    menu();
                    break;

                case 3:
                    gameInProgress = true;
                    gameLoop();
                    break;
                default:
                    break;
            }
            if (Input.getKeyPressed(Keyboard.KEY_ESCAPE))
            {
                if (state == 3)
                    state = 1;
            }
            
            render();
            
            
            if (Display.isCloseRequested())
                state = 0;
        }
        cleanUp();
    }
    
    private static void initMenu()
    {
        // Menun alustus
        mainMenu = new Menu("Woot gaming!");
        // Menun vaihtoehtojen lisäys
        mainMenu.addChoice("NEW GAME");
        mainMenu.addChoice("SETTINGS");
        mainMenu.addChoice("QUIT");
        
        mainMenu.setVisible(false);   
        
        
        mainMenuInGame = new Menu("PAUSED!!!");
        // Menun vaihtoehtojen lisäys
        mainMenuInGame.addChoice("CONTINUE GAME");
        mainMenuInGame.addChoice("NEW GAME");
        mainMenuInGame.addChoice("SETTINGS");
        mainMenuInGame.addChoice("QUIT");
        
        mainMenuInGame.setVisible(false);
        
        
        
        options = new Menu("Options");
        
        
        options.addChoice("AA");
        options.addChoice("SW");
        options.addChoice("BACK");
        
        updateOptionsMenuStrings();

        options.setVisible(false);    
    }
    
    private static void updateOptionsMenuStrings()
    {
        String s = new String("Anti-aliasing:");
        if (Graphics.getMSAAEnabled())
            s = s + " Enabled x "+Graphics.getMSAASamples();
        else
            s = s + " Disabled";
        options.chosenlist().set(1, s);
        
        s = new String("Shockwaves:");
        if (Graphics.getShockWavesEnabled())
            s = s + " Enabled";
        else
            s = s + " Disabled";
        
        options.chosenlist().set(2, s);
        
    }
    
    
    private static void menu()
    {
        Menu menu;
        switch (state)
        {
            case 1:
                if (gameInProgress)
                    menu = mainMenuInGame;
                else
                    menu = mainMenu;
                    
                break;
            case 2:
                menu = options;
                break;
            default:
                state = 0;
                return;
        }
        menu.setVisible(true);

        if(Input.getKeyPressed(Keyboard.KEY_UP)){
            menu.moveUP();
        }
        else if(Input.getKeyPressed(KEY_DOWN)){
            menu.moveDOWN();
        }
        //Valinta ehto
        
        if(Input.getKeyPressed(KEY_RETURN))
        {
            switch (state)
            {
                case 1:
                    if (gameInProgress)
                        updateMainMenuInGame();
                    else
                        updateMainMenu();
                    break;
                case 2:
                    updateOptions();
                    break;
                default:
                    state = 0;
                    return;
            }
        }
    }
    
    private static void init(){
        
        randomizer = new Random();
        
        try {
            GraphicsSettings settings = Graphics.loadSettings();

            Display.setDisplayMode
        (new DisplayMode(settings.windowWidth,settings.windowHeight));
            
            Display.create();
            Display.setVSyncEnabled(true);
            
            Graphics.init(settings);
            
            loadTexture("./data/tyyppi.png","tyyppi1",false);
            
        } catch (LWJGLException ex) {
            System.out.println("Test");
        }
        Input.init();
        initMenu();
    }

    
    private static void gameLoop(){
        game.update(); 
    }
    
    private static void render(){
        Input.update();
        Graphics.render();
        Display.update();
        Display.sync(60);
        time++;
    }
    
    private static void cleanUp(){
        Display.destroy();
        //input.destroy();
        Keyboard.destroy();
        Mouse.destroy();
        Graphics.saveSettings();
    }
    
    private static void updateMainMenuInGame() {
        
        switch (mainMenuInGame.chosenone())
        {
            case 1:
                state = 3;
                break;
            case 2:
                game.newGame();
                state = 3;
                break;
            case 3:
                state = 2;
                break;
            default:
                state = 0;
                break;
                
        }
        
        mainMenuInGame.setVisible(false);
    }

    private static void updateMainMenu() {
        
        switch (mainMenu.chosenone())
        {
            case 1:
                state = 3;
                break;
            case 2:
                state = 2;
                break;
            default:
                state = 0;
                break;
                
        }
        
        mainMenu.setVisible(false);
    }

    private static void updateOptions() {
        
        switch (options.chosenone())
        {
            case 1:
                
                if (Graphics.getMSAAEnabled())
                {
                    int d = Graphics.getMSAASamples();
                    if (d == Graphics.getMSAAMaxSamples())
                    {
                        Graphics.setMSAAEnabled(false);
                    }
                    else
                    {
                        if (d == 0)
                            d = 1;
                        Graphics.setMSAASamples(d*2);
                    }
                }
                else
                {
                    Graphics.setMSAASamples(2);
                    Graphics.setMSAAEnabled(true);
                }
                break;
            case 2:
                Graphics.setShockWavesEnabled(!Graphics.getShockWavesEnabled());
                break;
            default:
                options.setVisible(false);
                state = 1;
                break;

        }
        updateOptionsMenuStrings();
        
    }
}
