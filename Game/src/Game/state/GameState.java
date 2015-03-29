/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state;

import Game.state.object.GameObject;
import Game.state.object.actor.Actor;
import game.state.AI;
import game.state.Bullet;
import game.state.Player;
import java.util.ArrayList;
import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author Juha-Matti
 */
public class GameState {
    private ArrayList<GameObject> objects;
    private ArrayList<Actor> actors;
    private Player player;
    private ArrayList<AI> AIlist;
    private ArrayList<Bullet> bullets;
    private Vector2f vector1, vector2;
    public GameState(){
        objects = new ArrayList();
        player = new Player();
        AIlist = new ArrayList();
    }

    public void addGameObject(GameObject object){
        objects.add(object);
    }
    public void delGameObject(GameObject object){
        objects.remove(object);
    }
    
    public void addActor(Actor actor){
        actors.add(actor);
    }
    public void delActor(Actor actor){
        actors.remove(actor);
    }
    
    public void update(){
        
        for(Actor actor : actors){
            
            
        }
        
        
    }
    
}
