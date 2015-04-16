/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state;

import Game.Main;
import Game.graphics.Graphics;
import Game.state.event.Event;
import Game.state.item.equipment.AssaultRifle;
import Game.state.item.equipment.Flamethrower;
import Game.state.item.equipment.Pistol;
import Game.state.item.equipment.RocketLauncher;
import Game.state.object.GameObject;
import Game.state.object.actor.Actors;
import Game.state.object.actor.actors.Human;
import Game.state.object.objects.items.ItemGround;
import Game.state.object.objects.items.WeaponGround;
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
    private static ArrayList<ItemGround> items;
    private static int score;

    public static int getScore() {
        return score;
    }
    
    public static void addScore(int s) {
        score+=s;
    }
    
    
    private static Player player;

    public static void playerDied()
    {
        manager.playerDied();
    }


   
    private Map map;
    
    private static Vector2f path;
    
    private static GameManager manager;
    
    
    
    public GameState(){
        objects = new ArrayList();
        actors = new ArrayList();
        attacks = new ArrayList();
        events = new ArrayList();
        items = new ArrayList();
        path = new Vector2f(0,0);
        map = null;
        score = 0;
        
        WeaponGround.initializeTextures();
        
       
        newGame();

    }
    
    
    static void spawnRandomItem(Vector2f spawnPos)
    {
        WeaponGround wappet;
        int ch = Math.abs(Main.randomInt()%2);
        if (ch == 0)
            wappet = new WeaponGround(new RocketLauncher());
        else
            wappet = new WeaponGround(new Flamethrower());
        wappet.setPosition(spawnPos);
        addItem(wappet);
        
    }
    
    
    static public void spawnPlayer(Vector2f pos)
    {
        player = new Player(new Vector2f(pos), 20);
        player.setImage("tyyppi1");
        player.setSize(new Vector2f(20,20));
        player.setRotation(60);
        player.setWeapon(new AssaultRifle());
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
    
    public static void addItem(ItemGround a) {
        items.add(a);
    }
    
    public static void delItem(ItemGround a) {
        items.remove(a);
    }
    
    public void update(){
        
        manager.manage();
        
        ArrayList<Actors> list = new ArrayList(actors);
        
        for(Actors actor : list)
        {
            if (actor.getHP() <= 0)
                score+= 1000;
            
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
                if (player.getHP() <= 0)
                {
                    continue;
                }
                float forward = 2f;
                if(Pathfind.pathfindInMap(player.getPosition(), actor.getPosition(), map)){
                    Vector2f vec = new Vector2f(actor.getPosition().x - player.getPosition().x, actor.getPosition().y - player.getPosition().y);
                    if(vec.length() > 200)
                        actor.setRotation(getRotFromVectors(new Vector2f(path), new Vector2f(actor.getPosition())));
                    else if(vec.length() > 50)
                        actor.setRotation(getRotFromVectors(new Vector2f(player.getPosition()), new Vector2f(actor.getPosition())));
                    else{
                        actor.setRotation(getRotFromVectors(new Vector2f(player.getPosition()), new Vector2f(actor.getPosition())));
                        forward = 0;
                    }
                    actor.addVelocity(forward, actor.getRotation());
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
        ArrayList<ItemGround> list3 = new ArrayList(items);
        for(ItemGround item : list3)
        {
            item.update();
            
            if (player != null)
            {
                CollisionDetectionResult cdr = CollisionDetection.checkCircleCollision(new Vector2f(player.getPosition()), player.getSize().length()/2, item.getPosition(), 16f);
                
                if(cdr.found == true){
                    item.addEffect(player);
                    delItem(item);
                    
                }
            }
        }
        ArrayList<Event> list4 = new ArrayList(events);
        for(Event event : list4){
            
            for(Actors actor : list)
            {
                Vector2f p1 = actor.getPosition();
                Vector2f p2 = event.getPosition();
                
                float dist = (float) Math.hypot(p1.x-p2.x, p1.y-p2.y);
                if (dist < event.getRadius())
                {
                    float factor = dist/event.getRadius();
                    actor.setHP(actor.getHP()-event.getDamage()*factor);
                }
                
            }
            delEvent(event);
        }
        
        
    }

    /**
     * Returns the angle in degrees where vector is pointing
     * @param vector
     * @return (float)rotation
     */
    private float getRotFromVectors(Vector2f vec1, Vector2f vec2){
        float rota = 270;
        
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
        
        manager = new GameManager(map);
    }
    
}
