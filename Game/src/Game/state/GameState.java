/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state;

import Game.Main;
import Game.graphics.Graphics;
import Game.state.event.Event;
import Game.state.item.equipment.Flamethrower;
import Game.state.item.equipment.Pistol;
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
    
    private GameManager manager;
    
    
    
    public GameState(){
        objects = new ArrayList();
        AIlist = new ArrayList();
        actors = new ArrayList();
        attacks = new ArrayList();
        events = new ArrayList();
        map = null;
        
        newGame();

    }
    
    
    public void spawnPlayer(Vector2f pos)
    {
        player = new Player(new Vector2f(pos), 20);
        player.setImage("tyyppi1");
        player.setSize(new Vector2f(20,20));
        player.setRotation(60);
        player.setWeapon(new Pistol());
        actors.add(player);
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
        
        manager.manage();
        
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
                attack.hit(true);
                cdr.found = false;
            }
            
            for(Actor actor : list){
                Actor act = actor;
                
                //keskustan sijainti, ympärysmitta r, viivan sijainti1, viivan sijainti2
                cdr = CollisionDetection.checkCircleCollision(new Vector2f(act.getPosition()), act.getSize().length()/2, attack.getPos(), 16f);
                
                if(cdr.found == true){
                    System.out.println(actor.defend(attack));
                    
                }
                
            }
            
        }
        
        for(Event event : events){
            
        }
        
        
    }

    public void newGame() {
        
        for (Actor a : actors)
        {
            a.kill();
        }
        
        for (Attack a : attacks)
        {
            a.kill();
        }
        
        objects.clear();
        AIlist.clear();
        actors.clear();
        attacks.clear();
        events.clear();
        player = null;
        
        if (map != null)
            Graphics.removeRenderable(map);
        map = new Map(20,12);
        Graphics.registerRenderable(map, Graphics.BaseLayer);
        
        
        manager = new GameManager(this,map);
        
    }
    
}
