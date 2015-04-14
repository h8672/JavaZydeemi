/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state;

import Game.Main;
import Game.graphics.BouncyText;
import Game.graphics.ExplosionParticle;
import Game.graphics.FireParticle;
import Game.graphics.HaloParticle;
import Game.graphics.ParticleEffects;
import Game.graphics.SmokeParticle;
import Game.graphics.SparkParticle;
import Game.state.object.actor.actors.Human;
import game.state.object.actor.actors.Player;
import org.lwjgl.util.vector.Vector2f;

/** GameManager: pelin hallitsija
 * "pelin kulku", spawnaa vihollisia ja pelaajan
 *
 * @author MacodiusMaximus
 */


public class GameManager {
    
    
    
    enum GamePhase {
        Beginning, SpawnPlayer, SpawnWalls, SpawnEnemies
    }
    private GameState state;
    private Map map;
    private GamePhase phase;
    private int gameTimer;
    private int difficulty;
    
    private Vector2f playerSpawnPos;
    private Vector2f enemySpawnPos;
    public GameManager(GameState state, Map map)
    {
        this.state = state;
        this.map = map;
        this.phase = GamePhase.SpawnWalls;
        difficulty = 0;
    }
    
    private void switchPhase(GamePhase p)
    {
        phase = p;
        gameTimer = 0;
    }

    private void spawnPlayer()
    {
        if (gameTimer == 2)
        {
            int border = 3;
            float minX = map.getTileSize()*border;
            float minY = map.getTileSize()*border;
            float dX = (map.getWidth()-border*2)*map.getTileSize();
            float dY = (map.getHeight()-border*2)*map.getTileSize();
            
            int oCount= 50;
            boolean collides = false;
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
            
        }
        if (gameTimer > 3)
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
        
        if (gameTimer > 45)
        {
            state.spawnPlayer(playerSpawnPos);
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
    
    private void spawnEnemies()
    {
        if (gameTimer == 65)
        {
            int border = 1;
            float minX = map.getTileSize()*border;
            float minY = map.getTileSize()*border;
            float dX = (map.getWidth()-border*2)*map.getTileSize();
            float dY = (map.getHeight()-border*2)*map.getTileSize();
            
            int oCount= 50;
            boolean collides = false;
            do
            {
                collides = false;
                enemySpawnPos = new Vector2f(minX+dX*Main.randomFloat(),minY+dY*Main.randomFloat());
                oCount --;
                
                collides |= map.getTileCollision(enemySpawnPos);
                Vector2f copy = new Vector2f(enemySpawnPos);
                collides |= map.getTileCollision(copy.translate(5,0));
                collides |= map.getTileCollision(copy.translate(-10,0));
                collides |= map.getTileCollision(copy.translate(5,5));
                collides |= map.getTileCollision(copy.translate(0,-10));
                
            }
            while (collides && (oCount > 0));
            
        }
        
        if (gameTimer > 76 && gameTimer < 90)
        {
            int smokeCount = 5;
            float smokeArea = 24;
            for (int i = 0; i < smokeCount;i++)
            {
                SmokeParticle s = new SmokeParticle();
                Vector2f sPos = new Vector2f();
                sPos.x = enemySpawnPos.x-smokeArea+smokeArea*2*Main.randomFloat();
                sPos.y = enemySpawnPos.y-smokeArea+smokeArea*2*Main.randomFloat();
                s.setPos(sPos);
            }
            
        }
        
        if (gameTimer == 66)
        {
            HaloParticle halo = new HaloParticle();
            
            halo.setPos(new Vector2f(enemySpawnPos));
            
        }
        if (gameTimer == 90)
        {
            Human h = new Human(new Vector2f(enemySpawnPos), 20);
            h.setColorHead(new float[]{Main.randomFloat(),Main.randomFloat(),Main.randomFloat(),1.0f});
            h.setColorTorso(new float[]{Main.randomFloat(),Main.randomFloat(),Main.randomFloat(),1.0f});
            h.setColorArms(new float[]{Main.randomFloat(),Main.randomFloat(),Main.randomFloat(),1.0f});
            h.setRotation(Main.randomFloat()*360);
            state.addActor(h);
                    
        }
        
        if (gameTimer > 160)
            switchPhase(GamePhase.SpawnEnemies);
        
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
                break;
        }
        
        
        gameTimer++;
    }
    
}
