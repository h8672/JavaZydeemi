/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state;

import Game.Main;
import Game.state.object.GameObject;
import Game.state.object.actor.Actor;
import Game.state.object.actor.actors.Human;
import game.state.AI;
import game.state.Bullet;
import game.state.object.actor.actors.Player;
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
        player = new Player(new Vector2f(0,0), 20);
        AIlist = new ArrayList();
        actors = new ArrayList();
        map = new Map(20,12);
        for (int i = 0; i < 10; i++)
         actors.add(new Human(new Vector2f(Main.randomFloat()*800,Main.randomFloat()*600), 20));
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
            
            Actor act = actor;
            //argumentit: checkCircleCollisionWithMap(pallon keskipiste, pallon säde, kartta)
            CollisionDetectionResult cdr = CollisionDetection.checkCircleCollisionWithMap(act.getPosition(), 18, map);
            
            if (cdr.found)
            {
                //cdr.fix sisältää tarvitun muutoksen actorin sijaintiin
                Vector2f d = act.getPosition();
                act.setPosition(new Vector2f(d.x+cdr.fix.x,d.y+cdr.fix.y));
            }

        }
        
        
    }
    
}
