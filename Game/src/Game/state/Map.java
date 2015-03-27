/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state;

import Game.graphics.Drawing;
import Game.graphics.Graphics;
import Game.graphics.Renderable;
import Game.graphics.Texture;
import java.util.ArrayList;
import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author Juha-Matti
 */
public class Map  implements Renderable
{
    private static final int TileWall = 0;
    private static final int TileFloor1 = 1;
    private static final int TileFloor2 = 2;
    private static final int TileFloor3 = 3;
    private static final int TileSize = 64;
    private static ArrayList<Texture> textures;

    private ArrayList<ArrayList<Integer>> tileMap;
    private int width;
    private int height;
    private static boolean isDataLoaded = false;
    
    public int getTileSize() {
        return TileSize;
    }
    
    public static void loadMapData()
    {
        textures = new ArrayList<>();
        textures.add(Graphics.loadTexture("./data/map/tile1.png","mapTile1",true));
        textures.add(Graphics.loadTexture("./data/map/tile2.png","mapTile2",true));
        textures.add(Graphics.loadTexture("./data/map/tile3.png","mapTile3",true));
        isDataLoaded = true;
    }
    public Map(int width, int height)
    {
        if (isDataLoaded == false)
            loadMapData();
        
        Graphics.registerRenderable(this, Graphics.BaseLayer);
        this.width = width;
        this.height = height;
        tileMap = new ArrayList<>();
        for (int i = 0; i < width; i++)
        {
            ArrayList<Integer> col = new ArrayList<>();
            for (int i2 = 0; i2 < height; i2++)
            {
                if (i == 0 || i == width-1 || i2 == 0 || i2 == height-1)
                    col.add(TileWall);
                else
                    col.add((i+i2)%4);
                
            }
            tileMap.add(col);
        }
    }
    
    
    public boolean getTileCollision(Vector2f pos)
    {
        return getTileCollision((int)(pos.x/TileSize),(int)(pos.y/TileSize));
    }
    
    public boolean getTileCollision(int x, int y)
    {
        try 
        {
            return (tileMap.get(x).get(y) == 0);
        }
        catch (Exception e)
        {
            return true;
        }
    }
    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public float getDepth() {
        // Kartta kaikista alin, kaikki juoksee päällä
        return 1.0f;
    }

    @Override
    public void render()
    {
        int x = 0;
        int y = 0;
        for (ArrayList<Integer> col : tileMap)
        {
            y = 0;
            for (int tile : col)
            {
                if (tile > 0)
                Drawing.drawSpriteSized(textures.get(tile-1),new Vector2f(x*TileSize,y*TileSize), new Vector2f(TileSize,TileSize));
                y++;
            }
            x++;
        }
    }
    
}
