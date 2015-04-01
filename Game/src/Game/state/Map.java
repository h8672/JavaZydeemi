/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state;

import Game.Main;
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
public class Map implements Renderable
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
    
    public boolean gridLineOfSight(Vector2f p1, Vector2f p2)
    {
        return gridLineOfSight((int)(p1.x/TileSize),(int)(p1.y/TileSize),(int)(p2.x/TileSize),(int)(p2.y/TileSize));
    }
    
    public boolean gridLineOfSight(int x1, int y1, int x2, int y2)
    {
        
        //Bresenheim
        /*int x = x1;
        int y = y1;
        int dx = x2-x1;
        int dy = y2-y1;
        
        //if dx < 0: gx = -1 else: gx = 1
        int gx,gy;
        if (dx < 0)
            gx = -1;
        else
            gx = 1;
        
        if (dy < 0)
            gy = -1;
        else
            gy = 1;
        
        //dx = abs(dx)
        dx = dx*gx;
        dy = dy*gy;
        
        int f = 0;
        
        if (dx >= dy)
        {
            while (x != x2)
            {
                f += dy;
                if (f >= dx)
                {
                    if (getTileCollision(x+((gx-1)/2),y+((gy-1)/2)))
                        return false;
                    y+= gy;
                    f -= dx;
                }
                if (f != 0 && getTileCollision(x+((gx-1)/2),y+((gy-1)/2)))
                    return false;
                if (dy == 0 && getTileCollision(x+((gx-1)/2),y) && getTileCollision(x+((gx-1)/2),y - 1))
                    return false;
                x+=gx;
            }
        }
        else
        {
            while (y != y2)
            {
                f += dx;
                if (f >= dx)
                {
                    if (getTileCollision(x+((gx-1)/2),y+((gy-1)/2)))
                        return false;
                    x+= gx;
                    f-= dy;
                }
                if (f != 0 && getTileCollision(x+((gx-1)/2),y+((gy-1)/2)))
                    return false;
                if (dx == 0 && getTileCollision(x,y+((gy-1)/2)) && getTileCollision(x-1,y +((gy-1)/2)))
                    return false;
                y+=gy;
            }
        }
        
        return true;*/
        //Supercover
        
        int i;               // loop counter
        int ystep, xstep;    // the step on y and x axis
        int error;           // the error accumulated during the increment
        int errorprev;       // *vision the previous value of the error variable
        int y = y1, x = x1;  // the line points

        int ddy, ddx;        // compulsory variables: the double values of dy and dx
        int dx = x2 - x1;
        int dy = y2 - y1;
        if (getTileCollision(x1, y1)) return false;
        // NB the last point can't be here, because of its previous point (which has to be verified)
        if (dy < 0)
        {
            ystep = -1;
            dy = -dy;
        }
        else
            ystep = 1;
        if (dx < 0)
        {
            xstep = -1;
            dx = -dx;
        }
        else
            xstep = 1;
        
        ddy = 2 * dy;  // work with double values for full precision
        ddx = 2 * dx;
        if (ddx >= ddy)
        {  // first octant (0 <= slope <= 1)
            // compulsory initialization (even for errorprev, needed when dx==dy)
            errorprev = error = dx;  // start in the middle of the square
            for (i=0 ; i < dx ; i++)
            {  // do not use the first point (already done)
                x += xstep;
                error += ddy;
                if (error > ddx)
                {  // increment y if AFTER the middle ( > )
                    y += ystep;
                    error -= ddx;
                    // three cases (octant == right->right-top for directions below):
                    if (error + errorprev < ddx)  // bottom square also
                        if (getTileCollision(x,y-ystep))return false;
                    else if (error + errorprev > ddx)  // left square also
                        if (getTileCollision(x-xstep,y))return false;
                    else
                    {  // corner: bottom and left squares also
                        if (getTileCollision(x,y-ystep))return false;
                        if (getTileCollision(x-xstep,y))return false;
                    } 
                }
                if (getTileCollision(x,y))return false;
                errorprev = error;
            }
        }
        else
        {  // the same as above
            errorprev = error = dy;
            for (i=0 ; i < dy ; i++)
            {
                y += ystep;
                error += ddx;
                if (error > ddy)
                {
                    x += xstep;
                    error -= ddy;
                    if (error + errorprev < ddy)
                        if (getTileCollision(x-xstep,y))return false;
                    else if (error + errorprev > ddy)
                        if (getTileCollision(x,y-ystep))return false;
                    else
                    {
                        if (getTileCollision(x-xstep,y))return false;
                        if (getTileCollision(x,y-ystep))return false;
                    } 
                }
                if (getTileCollision(x,y))return false;
                errorprev = error;
            }
        }
        return true;
    }
    
    
    /** Palauttaa kartan ruudun koon
     *
     * @return yksittäisen ruudun koko
     */
    public int getTileSize() {
        return TileSize;
    }
    
    /** Lataa kartan tarvitsemat tekstuurit
     *
     */
    public static void loadMapData()
    {
        textures = new ArrayList<>();
        textures.add(Graphics.loadTexture("./data/map/tile1.png","mapTile1",true));
        textures.add(Graphics.loadTexture("./data/map/tile2.png","mapTile2",true));
        textures.add(Graphics.loadTexture("./data/map/tile3.png","mapTile3",true));
        isDataLoaded = true;
    }

    /** Map luokan konstruktori. Luo uuden Map olion tietyllä koolla.
     * 
     *
     * @param width kartan leveys
     * @param height kartan korkeus
     */
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
                {
                    col.add((i*11411*i2+1)%4);
                    

                }
                
            }
            tileMap.add(col);
        }
    }
    
    /** Palauttaa mikäli sijainnissa oleva karttaruutu on kiinteä seinä
     *
     * @param pos sijainti
     * @return true, mikäli sijainti aiheuttaa törmäyksiä
     */
    public boolean getTileCollision(Vector2f pos)
    {
        return getTileCollision((int)(pos.x/TileSize),(int)(pos.y/TileSize));
    }
    
    /** Palauttaa, onko karttaruutu kiinteä seinä
     * <p>
     * Koordinaattien yksikköinä karttaruudut.
     *
     * @param x karttaruudun x-koordinaatti
     * @param y karttaruudun y-koordinaatti
     * @return true, mikäli ruutu aiheuttaa törmäyksiä
     */
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
        return -99.0f;
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

    int getWidth()
    {
        return width;
    }

    int getHeight()
    {
        return height;
    }
    
}