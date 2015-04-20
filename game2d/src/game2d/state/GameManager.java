/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game2d.state;

import game2d.Main;
import game2d.graphics.BouncyText;
import game2d.graphics.FireParticle;
import game2d.graphics.HaloParticle;
import game2d.graphics.SmokeParticle;
import game2d.graphics.SparkParticle;
import game2d.state.event.events.Explosion;
import game2d.state.object.actor.actors.Human;
import java.util.ArrayList;
import org.lwjgl.util.vector.Vector2f;

/** GameManager: pelin hallitsija
 * "pelin kulku", spawnaa vihollisia ja pelaajan
 *
 * @author MacodiusMaximus
 */


public class GameManager {

  

    
    enum GamePhase {
        Beginning, SpawnPlayer, SpawnWalls, SpawnEnemies, Lost
    }
    private ArrayList<Integer> enemySpawnerPhases;
    private ArrayList<Vector2f> enemySpawnerLocations;
    private Map map;
    private GamePhase phase;
    private int gameTimer;
    private float difficulty;
    private int livesLeft;
    private Vector2f playerSpawnPos;
    private Vector2f enemySpawnPos;
    
    private static int finalScore = 0;
    public GameManager(Map map)
    {
        this.map = map;
        this.phase = GamePhase.SpawnWalls;
        difficulty = 5;
        livesLeft = 3;
        enemySpawnerPhases = new ArrayList<>();
        enemySpawnerLocations= new ArrayList<>();
    }
    
    private void switchPhase(GamePhase p)
    {
        phase = p;
        gameTimer = 0;
    }
    
    private int lastItemSpawn = 0;
    
    private void spawnRandomItems()
    {
        lastItemSpawn++;
        if (lastItemSpawn == 500)
        {
            int border = 1;
            float minX = map.getTileSize()*border;
            float minY = map.getTileSize()*border;
            float dX = (map.getWidth()-border*2)*map.getTileSize();
            float dY = (map.getHeight()-border*2)*map.getTileSize();
            
            int oCount= 50;
            boolean collides;
            Vector2f spawnPos;
            do
            {
                collides = false;
                spawnPos = new Vector2f(minX+dX*Main.randomFloat(),minY+dY*Main.randomFloat());
                oCount --;
                
                collides |= map.getTileCollision(spawnPos);
                Vector2f copy = new Vector2f(spawnPos);
                collides |= map.getTileCollision(copy.translate(5,0));
                collides |= map.getTileCollision(copy.translate(-10,0));
                collides |= map.getTileCollision(copy.translate(5,5));
                collides |= map.getTileCollision(copy.translate(0,-10));
                
            }
            while (collides && (oCount > 0));
            
            int sparkCount = 1;
            float sparkArea = 14;
            for (int i = 0; i < sparkCount; i++)
            {
                
                SparkParticle spark = new SparkParticle();
                Vector2f sPos = new Vector2f();
                sPos.x = spawnPos.x-sparkArea+sparkArea*2*Main.randomFloat();
                sPos.y = spawnPos.y-sparkArea+sparkArea*2*Main.randomFloat();
                spark.setPos(sPos);
            }
            
            GameState.spawnRandomItem(spawnPos);
            lastItemSpawn = (-1000)+(int)difficulty*70;
        }
    }
    
    
    public void playerDied() {
        switchPhase(GamePhase.SpawnPlayer);
        
        livesLeft--;
        if (livesLeft > 0)
        {
            BouncyText t;
            if (livesLeft != 1)
                t = new BouncyText("ONLY "+livesLeft+" LIVES LEFT");
            else
                t = new BouncyText("ONLY 1 LIFE LEFT!!!");
            
            t.setPos(new Vector2f(Main.randomInt()%200+300,-30));
            t.setSize(2);
        }
        if (livesLeft == 0)
        {
            BouncyText t =new BouncyText("LAST LIFE, MAKE IT COUNT!!!");
            t.setPos(new Vector2f(Main.randomInt()%200+300,-30));
            t.setSize(2);
        }
        if (livesLeft < 0)
        {
            BouncyText t =new BouncyText(Integer.toString(GameState.getScore()));
            t.setPos(new Vector2f(Main.randomInt()%200+300,-180));
            t.setSize(6);
            t.setGravity(new Vector2f(0,0.03f));
            
            t =new BouncyText("GAME OVER!");
            t.setPos(new Vector2f(Main.randomInt()%200+300,-30));
            t.setSize(6);
            switchPhase(GamePhase.Lost);
            
            finalScore = GameState.getScore(); 
        }
        difficulty -= 1.6;
    }
    
    private void lost()
    {
        if (gameTimer == 25)
        {
            int choice = Math.abs(Main.randomInt())%2;
            BouncyText t;
            
            
            if (choice == 0)
                t =new BouncyText("GAME OVER!");
            else
                t =new BouncyText("FINAL SCORE "+finalScore);
            
            
            t.setSize(2);
            t.setPos(new Vector2f(Main.randomInt()%400+500,-30));
            
            switchPhase(phase);
        }
            
    }

    private void spawnPlayer()
    {
        if (gameTimer == 95)
        {
            int border = 3;
            float minX = map.getTileSize()*border;
            float minY = map.getTileSize()*border;
            float dX = (map.getWidth()-border*2)*map.getTileSize();
            float dY = (map.getHeight()-border*2)*map.getTileSize();
            
            int oCount= 50;
            boolean collides;
            do
            {
                collides = false;
                playerSpawnPos = new Vector2f(minX+dX*Main.randomFloat(),minY+dY*Main.randomFloat());
                oCount --;
                
                collides |= map.getTileCollision(playerSpawnPos);
                Vector2f copy = new Vector2f(playerSpawnPos);
                collides |= map.getTileCollision(copy.translate(5,0));
                collides |= map.getTileCollision(copy.translate(-10,0));
                collides |= map.getTileCollision(copy.translate(5,5));
                collides |= map.getTileCollision(copy.translate(0,-10));
                
            }
            while (collides && (oCount > 0));
            
            GameState.addEvent(new Explosion(playerSpawnPos, 140, 140));
            
        }
        if (gameTimer > 96)
        {
            
            int sparkCount = 1;
            float sparkArea = 14;
            
            for (int i = 0; i < sparkCount; i++)
            {
                
                SparkParticle spark = new SparkParticle();
                Vector2f sPos = new Vector2f();
                sPos.x = playerSpawnPos.x-sparkArea+sparkArea*2*Main.randomFloat();
                sPos.y = playerSpawnPos.y-sparkArea+sparkArea*2*Main.randomFloat();
                spark.setPos(sPos);
            }
            
        }
        
        if (gameTimer > 140)
        {
            GameState.spawnPlayer(playerSpawnPos);
            switchPhase(GamePhase.SpawnEnemies);
        }
        
    }
    
    private void spawnWalls()
    {
        if (gameTimer == 1)
        {
            BouncyText t = new BouncyText("GET READY!!!");
        }
        if (gameTimer > 30)
            if ((gameTimer % 5) == 0)
            {
                //Spawnataan seiniä satunnaisesti
                int posX = Math.abs(Main.randomInt())%(map.getWidth()-4)+2;
                int posY = Math.abs(Main.randomInt())%(map.getHeight()-4)+2;
                map.setTile(posX, posY, Map.TileWall);
                
                //ja hieno tuliefekti
                
                float fx = ((float)posX)*map.getTileSize();
                float fy = ((float)posY)*map.getTileSize();

                float r = map.getTileSize();

                for (int i = 0; i < 50; i++)
                {
                    FireParticle t = new FireParticle();
                    t.setPos(new Vector2f(fx+Main.randomFloat()*r,fy+Main.randomFloat()*r));
                }
                
                
            }
        
        //muutetaan lattioitten väriä
        if (gameTimer > 14)
        {
            int pos = gameTimer-14;
            int posX = pos%(map.getWidth()-2)+1;
            int posY = (pos/(map.getWidth()-2))+1;
            
            if (map.getTile(posX, posY) != Map.TileWall)
            map.setTile(posX, posY, Math.abs(Main.randomInt())%Map.TileFloorCount+Map.TileFloorFirst);
            
            //ollaanko kaikki ruudut käyty läpi?? 
            if (pos >= ((map.getHeight()-2)*(map.getWidth()-2)-1))
                switchPhase(GamePhase.SpawnPlayer);
            
            float fx = ((float)posX)*map.getTileSize();
            float fy = ((float)posY)*map.getTileSize();

            float r = map.getTileSize();
            for (int i = 0; i < 5; i++)
            {
                SparkParticle s = new SparkParticle();
                s.setPos(new Vector2f(fx+Main.randomFloat()*r,fy+Main.randomFloat()*r));
            }
            
        }

        
    }
    
    private void updateEnemySpawner(int index)
    {
        int timer = enemySpawnerPhases.get(index);

        if (timer == 1)
        {
            GameState.addScore(100);
            
            int border = 1;
            float minX = map.getTileSize()*border;
            float minY = map.getTileSize()*border;
            float dX = (map.getWidth()-border*2)*map.getTileSize();
            float dY = (map.getHeight()-border*2)*map.getTileSize();
            Vector2f spawnPos;
            int oCount= 50;
            boolean collides;
            do
            {
                collides = false;
                spawnPos = new Vector2f(minX+dX*Main.randomFloat(),minY+dY*Main.randomFloat());
                oCount --;
                
                collides |= map.getTileCollision(spawnPos);
                Vector2f copy = new Vector2f(spawnPos);
                collides |= map.getTileCollision(copy.translate(5,0));
                collides |= map.getTileCollision(copy.translate(-10,0));
                collides |= map.getTileCollision(copy.translate(5,5));
                collides |= map.getTileCollision(copy.translate(0,-10));
                
                
            }
            while (collides && (oCount > 0));
            
            enemySpawnerLocations.set(index, spawnPos);
        }
        Vector2f location = enemySpawnerLocations.get(index);
        
        if (timer > 20 && timer < 60)
        {
            int smokeCount = 5;
            float smokeArea = 24;
            for (int i = 0; i < smokeCount;i++)
            {
                SmokeParticle s = new SmokeParticle();
                Vector2f sPos = new Vector2f();
                sPos.x = location.x-smokeArea+smokeArea*2*Main.randomFloat();
                sPos.y = location.y-smokeArea+smokeArea*2*Main.randomFloat();
                s.setPos(sPos);
            }
            
        }
        
        if (timer == 1)
        {
            HaloParticle halo = new HaloParticle();
            
            halo.setPos(new Vector2f(location));
            
        }
        if (timer == 60)
        {
            int spawnCount = 1;
         
                   
            for (int i = 0; i < spawnCount; i++)
            {
                Vector2f spawnPos = new Vector2f(location);
                spawnPos.x+=(Main.randomFloat()-0.5)*4;
                spawnPos.y+=(Main.randomFloat()-0.5)*4;
                Human h = new Human(new Vector2f(location), 20);
                h.setColorHead(new float[]{Main.randomFloat(),Main.randomFloat(),Main.randomFloat(),1.0f});
                h.setColorTorso(new float[]{Main.randomFloat(),Main.randomFloat(),Main.randomFloat(),1.0f});
                h.setColorArms(new float[]{Main.randomFloat(),Main.randomFloat(),Main.randomFloat(),1.0f});
                h.setRotation(Main.randomFloat()*360);
                GameState.addActor(h);
            }

        }
        
        enemySpawnerPhases.set(index, timer+1);
        
    }

  
    
    private void spawnEnemies()
    {
        if (gameTimer == 30)
        {
            gameTimer += difficulty*20;
            
            
            int dC = Main.randomInt()%12+(int)difficulty-15;
            
            int iCount = 1;
            if (dC > 0)
                iCount++;
            for (int i = 0; i < iCount; i++)
            {
                enemySpawnerPhases.add(Main.randomInt()%12-12);
                enemySpawnerLocations.add(new Vector2f());
                
            }
        }
        if (gameTimer > 360)
        {
            
            switchPhase(GamePhase.SpawnEnemies);
            
            if (difficulty > 10)
                difficulty += 0.1;
            else
                difficulty += 0.2;
            if (difficulty > 15)
                difficulty = 15;
        }
        
    }
    
    public void manage()
    {
        switch (phase)
        {
            case Beginning:
                phase = GamePhase.SpawnWalls;
            break;
            case SpawnPlayer:
                spawnPlayer();
                break;
            case SpawnWalls:
                spawnWalls();
                break;
            case SpawnEnemies:
                spawnEnemies();                
                spawnRandomItems();
                break;
            case Lost:
                lost();
                break;
        }
        
        int i = 0;
        for (i = 0; i < enemySpawnerPhases.size(); i++)
        {
            updateEnemySpawner(i);
        }
        
        ArrayList listCopy = new ArrayList(enemySpawnerPhases);
        
        for (i = 0; i < enemySpawnerPhases.size(); i++)
        {
            if ((Integer)listCopy.get(i) > 60)
            {
                enemySpawnerPhases.remove(i);
                enemySpawnerLocations.remove(i);
            }
        }
        
        
        
        gameTimer++;
    }
    
}


