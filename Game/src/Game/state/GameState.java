/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state;

import Game.graphics.Graphics;
import Game.state.event.Event;
import Game.state.item.equipment.Pistol;
import Game.state.object.GameObject;
import Game.state.object.actor.Actors;
import Game.state.object.actor.actors.Human;
import game.state.object.actor.actors.Player;
import java.util.ArrayList;
import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author Juha-Matti
 */
public class GameState {
    private static ArrayList<GameObject> objects;
    private static ArrayList<Actors> actors;
    private static ArrayList<Attack> attacks;
    private static ArrayList<Event> events;
    private Player player;
    private Map map;
    
    private static Vector2f path;
    
    private GameManager manager;
    
    
    
    public GameState(){
        objects = new ArrayList();
        actors = new ArrayList();
        attacks = new ArrayList();
        events = new ArrayList();
        path = new Vector2f(0,0);
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
    
    public static void addActor(Actors actor){
        actors.add(actor);
    }
    public static void delActor(Actors actor){
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
    
    public static void setPath(Vector2f paths){
        path = paths;
    }
    
    public void update(){
        
        manager.manage();
        
        ArrayList<Actors> list = new ArrayList(actors);
        
        for(Actors actor : list)
        {
            actor.update();
            actor.move();
            
            /*
            if(Main.getTime()%60 == 4)
            actor.attack();
            */
            
            Actors act = actor;
            //argumentit: checkCircleCollisionWithMap(pallon keskipiste, pallon säde, kartta)
            CollisionDetectionResult cdr = CollisionDetection.checkCircleCollisionWithMap(act.getPosition(), 18, map);
            
            if (cdr.found)
            {
                //cdr.fix sisältää tarvitun muutoksen actorin sijaintiin
                Vector2f d = act.getPosition();
                act.setPosition(new Vector2f(d.x+cdr.fix.x,d.y+cdr.fix.y));
            }
            
            if(actor != player)
            {
                if(Pathfind.pathfindInMap(player.getPosition(), actor.getPosition(), map)){
                    float rot = getRotFromVectors(new Vector2f(path), new Vector2f(actor.getPosition()));
                    actor.setRotation(rot);
                    actor.addVelocity(2f, actor.getRotation());
                }
                
                cdr = CollisionDetection.checkCircleCollision(new Vector2f(actor.getPosition()), actor.getWeapon().getAttackrange()/5, player.getPosition(), 1f);
                
                if(cdr.found){
                    actor.attack();
                }
            }
        }
        
        
        ArrayList<Attack> list2 = new ArrayList(attacks);
        for(Attack attack : list2){
            attack.update();
            attack.move();
            
            //argumentit: checkCircleCollisionWithMap(pallon keskipiste, pallon säde, kartta)
            CollisionDetectionResult cdr = CollisionDetection.checkCircleCollisionWithMap(attack.getPos(), 2, map);
            
            if (cdr.found)
            {
                attack.hit(true);
                cdr.found = false;
            }
            
            for(Actors actor : list){
                Actors act = actor;
                
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

    /**
     * Returns the angle in degrees where vector is pointing
     * @param vector
     * @return (float)rotation
     */
    private float getRotFromVectors(Vector2f vec1, Vector2f vec2){
        float rota, length, x, y;
        
        rota = 270;//fix to angle
        // 0 ylös
        // 90  ja -270 vasen
        // 180 ja -180 alas
        // 270 ja -90 oikea
        
        rota -= Math.toDegrees(Math.atan2(vec1.y - vec2.y,vec1.x - vec2.x));
        return rota;
        
    }
    
    /**
     *
     */
    public void newGame() {
        
        for (Actors a : actors)
        {
            a.kill();
        }
        
        for (Attack a : attacks)
        {
            a.kill();
        }
        
        objects.clear();
        actors.clear();
        attacks.clear();
        events.clear();
        path = null;
        player = null;
        
        if (map != null)
            Graphics.removeRenderable(map);
        map = new Map(20,12);
        Graphics.registerRenderable(map, Graphics.BaseLayer);
        
        manager = new GameManager(this,map);
    }
    
}
