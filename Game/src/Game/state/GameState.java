/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state;

import Game.Main;
import Game.state.object.GameObject;
import Game.state.object.actor.Actor;
import Game.state.object.actor.Actors;
import Game.state.object.actor.actors.Human;
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
    private Map map;
    public GameState(){
        objects = new ArrayList();
        player = new Player();
        AIlist = new ArrayList();
        actors = new ArrayList();
        map = new Map(14,14);
        for (int i = 0; i < 40; i++)
         actors.add(new Human(new Vector2f(Main.randomFloat()*400,Main.randomFloat()*400),0));
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
        
        for(Actor actor : actors)
        {
            actor.update();
            if (actor instanceof Human)
            {
                Actors act = (Actors)actor;
                CollisionDetectionResult cdr = CollisionDetection.checkCircleCollisionWithMap(act.getPosition(), 18, map);
                if (cdr.found)
                {
                    Vector2f d = act.getPosition();
                    act.setPosition(new Vector2f(d.x+cdr.fix.x,d.y+cdr.fix.y));
                }
            }
        }
        
        
    }
    
}
