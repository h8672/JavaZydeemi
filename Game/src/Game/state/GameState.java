/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state;

import Game.Main;
import Game.state.event.Event;
import Game.state.object.GameObject;
import Game.state.object.actor.Actor;
import Game.state.object.actor.actors.Human;
import game.state.AI;
import game.state.object.actor.actors.Player;
import java.util.ArrayList;
import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author Juha-Matti
 */
public class GameState {
    private static ArrayList<GameObject> objects;
    private static ArrayList<Actor> actors;
    private static ArrayList<Attack> attacks;
    private static ArrayList<Event> events;
    private Player player;
    private ArrayList<AI> AIlist;
    private Map map;
    
    public GameState(){
        objects = new ArrayList();
        AIlist = new ArrayList();
        actors = new ArrayList();
        attacks = new ArrayList();
        events = new ArrayList();
        player = new Player(new Vector2f(500,300), 20);
        player.setImage("tyyppi1");
        player.setSize(new Vector2f(20,20));
        player.setRotation(60);
        actors.add(player);
        map = new Map(20,12);
        
        for (int i = 0; i < 1; i++)
        {
            Human h = new Human(new Vector2f(Main.randomFloat()*600+100,Main.randomFloat()*400+100), 20);
            h.setColorHead(new float[]{Main.randomFloat(),Main.randomFloat(),Main.randomFloat(),1.0f});
            h.setColorTorso(new float[]{Main.randomFloat(),Main.randomFloat(),Main.randomFloat(),1.0f});
            h.setColorArms(new float[]{Main.randomFloat(),Main.randomFloat(),Main.randomFloat(),1.0f});
            h.setRotation(Main.randomFloat()*360);
            actors.add(h);
        }
    }

    public static void addGameObject(GameObject object){
        objects.add(object);
    }
    public static void delGameObject(GameObject object){
        objects.remove(object);
    }
    
    public static void addActor(Actor actor){
        actors.add(actor);
    }
    public static void delActor(Actor actor){
        actors.remove(actor);
    }
    
    public static void addAttack(Attack attack){
        attacks.add(attack);
    }
    public static void delAttack(Attack attack){
        attacks.remove(attack);
    }
    
    public static void addEvent(Event event){
        events.add(event);
    }
    public static void delEvent(Event event){
        events.remove(event);
    }
    
    public void update(){
        ArrayList<Actor> list = new ArrayList(actors);
        for(Actor actor : list)
        {
            actor.update();
            actor.move();
            
            /*
            if(Main.getTime()%60 == 4)
            actor.attack();
            */
            
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
        
        ArrayList<Attack> list2 = new ArrayList(attacks);
        for(Attack attack : list2){
            Vector2f pos1, pos2; // ampumalinja
            attack.update();
            pos1 = attack.getPos();
            attack.move();
            pos2 = attack.getPos();
            
            //argumentit: checkCircleCollisionWithMap(pallon keskipiste, pallon säde, kartta)
            CollisionDetectionResult cdr = CollisionDetection.checkCircleCollisionWithMap(attack.getPos(), 2, map);
            
            if (cdr.found)
            {
                attack.hit();
                cdr.found = false;
            }
            
            for(Actor actor : list){
                Actor act = actor;
                /*
                //keskustan sijainti, ympärysmitta r, viivan sijainti1, viivan sijainti2
                cdr = CollisionDetection.circleLineCollision(act.getPosition(), act.getSize().length()/2, pos1, pos2);
                
                if(cdr.found == true){
                    actor.defend(attack);
                }
                */
            }
            
        }
        
        for(Event event : events){
            
        }
        
    }
    
}
