/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game2d.state;

import game2d.graphics.Drawing;
import game2d.graphics.Renderable;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import org.lwjgl.util.vector.Vector2f;

class IntPos implements Comparable<IntPos>
{
    int x;
    int y; 

    IntPos(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    IntPos()
    {
        this.x = 0;
        this.y = 0;
    }
    public int compareTo(IntPos t)
    {
        if (x > t.x)
            return 1;
        if (x < t.x)
            return -1;

        if (y > t.y)
            return 1;
        if (y < t.y)
            return -1;

        return 0;
    }
    static int dist(IntPos a, IntPos b)
    {
        return (int) (Math.sqrt((a.x-b.x)*(a.x-b.x)+(a.y-b.y)*(a.y-b.y))*10);
    }

}

    
class PathCell 
{
    int length = 0;
    boolean closed = false;
    boolean open = false;
    IntPos parent = null;
}

class PathCellGoalComparator implements Comparator<IntPos>
{
    IntPos goal = new IntPos();
    PathCell[][] map;
    @Override
    public int compare(IntPos t, IntPos t1)
    {
        return ((map[t.x][t.y].length+IntPos.dist(t,goal))-(map[t1.x][t1.y].length+IntPos.dist(t1,goal)));
    }
}
    
/**
 *
 * @author MacodiusMaximus
 */
public class Pathfind
{
    private static LinkedList<Vector2f> points;
    private static boolean found;
    
    
    static public boolean pathfindInMap(Vector2f pos1, Vector2f pos2, Map map)
    {
        found = false;
        if (map.getTileCollision(pos1))
            return false;
        if (map.getTileCollision(pos2))
            return false;
        
        PathCell[][] pmap = new PathCell[map.getWidth()][map.getHeight()]; 
        for (int i = 0; i < map.getWidth(); i++)
        {
            for (int i2 = 0; i2 < map.getHeight(); i2++)
            {
                pmap[i][i2] = new PathCell();
            }
        }
      
        int startx,starty,goalx,goaly;
        startx = (int) (pos1.x/map.getTileSize());
        starty = (int) (pos1.y/map.getTileSize());
        goalx = (int) (pos2.x/map.getTileSize());
        goaly = (int) (pos2.y/map.getTileSize());
        
        
        if (startx < 0 || startx >= map.getWidth())
            return false;
        if (starty < 0 || starty >= map.getHeight())
            return false;
        if (goalx < 0 || goalx >= map.getWidth())
            return false;
        if (goaly < 0 || goaly >= map.getHeight())
            return false;
        
        
        IntPos goalCell = new IntPos(goalx,goaly);
        
        
        
        PathCellGoalComparator pcgc = new PathCellGoalComparator();
        pcgc.map = pmap;
        
        pcgc.goal.x = goalx;
        pcgc.goal.y = goaly;
        pmap[startx][starty].parent = new IntPos(startx,starty);
        pmap[startx][starty].open = true;
        
        PriorityQueue<IntPos> openCells = new PriorityQueue<>(1,pcgc);
        
        openCells.add(new IntPos(startx,starty));
        
        boolean foundPath = false;
        while (!openCells.isEmpty())
        {
            IntPos cell = openCells.poll();
            if (cell.compareTo(goalCell) == 0)
            {
                foundPath = true;
                //System.out.println(pmap[cell.x][cell.y].parent.x);
                break;
            }
            PathCell c1 = pmap[cell.x][cell.y];
            c1.closed = true;
            
            //neliöristikossa ympäröivien ruutujen suhteelliset koordinaatit
            
            //vain 4 suuntaa
            int[] dcxList = new int[]{-1,0,1,0};
            int[] dcyList = new int[]{0,-1,0,1};
            
            //8 suuntaa
            //int[] dcxList = new int[]{-1,-1,0,1,1,1,0,-1};
            //int[] dcyList = new int[]{0,-1,-1,-1,0,1,1,1};
            for (int i = 0; i < dcxList.length; i++)
            {
                int dcx = dcxList[i];
                int dcy = dcyList[i]; 
                if (map.getTileCollision(cell.x+dcx,cell.y+dcy))
                    continue;
                IntPos cell2 = new IntPos(cell.x+dcx,cell.y+dcy);
                PathCell c2 = pmap[cell2.x][cell2.y];
                if (!c2.closed)
                {
                    if (!c2.open)
                    {
                        //paljon
                        c2.length = 999912399;
                        c2.parent = cell;
                    }
                    
                    int oc = c2.length;
                    
                
                    if (c1.length + IntPos.dist(cell,cell2) < c2.length)
                    {
                        c2.parent = cell;
                        c2.length = c1.length+ IntPos.dist(cell,cell2) ;
                    }
                    
                    if (c2.length < oc)
                    {
                        if (c2.open)
                        {
                            openCells.remove(cell2);
                        }
                        openCells.add(cell2);
                        c2.open = true;
                    }
                }
            }
            
        }
        
        if (foundPath)
        {
            points = new LinkedList<>();
            found = true;
            IntPos d = new IntPos(goalx,goaly);
            IntPos start = new IntPos(startx,starty);
            while (d.compareTo(start) != 0)
            {
                Vector2f pos = new Vector2f();
                pos.x = ((float)d.x+0.5f)*map.getTileSize();
                pos.y = ((float)d.y+0.5f)*map.getTileSize();
                points.addLast(pos);
                
                d = pmap[d.x][d.y].parent;
            }
            
            Vector2f pos = new Vector2f();
            pos.x = ((float)d.x+0.5f)*map.getTileSize();
            pos.y = ((float)d.y+0.5f)*map.getTileSize();

            points.addLast(pos);
        }
        
        if(foundPath){
            if(points.size() > 1){
                GameState.setPath(new Vector2f(points.get(1)));
            }
            else{
                GameState.setPath(new Vector2f(0,0));
            }
        }
        return found;
    }
    
    boolean foundPath()
    {
        return found;
    }
    
    boolean advance()
    {
        if (found == true)
        {
            if (points.size() > 0)
            {
                points.removeFirst();
                if (points.size() > 0)
                    return true;
            }
        }
        return false;
    }
    
    
    Vector2f getPoint()
    {
        if (found == true)
        {
            if (points.size() > 0)
                return points.getFirst();
        }
        return new Vector2f(0,0);
    }    
 
}
