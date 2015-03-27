/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state;

import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author MacodiusMaximus
 */

public class CollisionDetection
{
    public static CollisionDetectionResult checkCircleCollisionWithMap(Vector2f center, float radius, Vector2f velocity, Map map)
    {
        CollisionDetectionResult cdr = new CollisionDetectionResult();
        float mSize = (float)map.getTileSize();
        
        Vector2f tPos = new Vector2f();
        tPos.x = center.x%mSize;
        tPos.y = center.y%mSize;
        
        int cx,cy;
        cx = (int)(center.x/mSize);
        cy = (int)(center.y/mSize);
        
        if (map.getTileCollision(cx,cy))
        {
            cdr.fix = new Vector2f(velocity);
            cdr.fix.x = -cdr.fix.x;
            cdr.fix.y = -cdr.fix.y;
            cdr.found = true;
            return cdr;
        }
        
        if (tPos.x > radius && tPos.y > radius)
            if (tPos.x < mSize-radius && tPos.y < mSize-radius)
            {
                cdr.found = false;
                return cdr;
            }
        int dcx, dcy;
        CollisionDetectionResult clcr;
        Vector2f nc = new Vector2f(center);
        
        float xdist = tPos.x-radius;
        float ydist = tPos.y-radius;
        float drad = mSize-radius*2;
        
        int[] dcxList = new int[]{-1,-1,0,1,1,1,0,-1};
        int[] dcyList = new int[]{0,-1,-1,-1,0,1,1,1};
        boolean found = false;
        for (int i = 0; i < dcxList.length; i++)
        {
            
            dcx = dcxList[i];
            dcy = dcyList[i];
            
            
            if (dcx < 0 && xdist > 0)
                continue;
            
            if (dcx > 0 && xdist < drad)
                continue;
            
            if (dcy < 0 && ydist > 0)
                continue;
            
            if (dcy > 0 && ydist < drad)
                continue;
            
            dcx = cx+dcx;
            dcy = cy+dcy;
            
            
            if (map.getTileCollision(dcx,dcy))
            {
                clcr = circleBoxCollision(nc,radius,new Vector2f(dcx*mSize,dcy*mSize),new Vector2f(dcx*mSize+mSize,dcy*mSize+mSize));
                if (clcr.found)
                {
                    found = true;
                    nc.x  += clcr.fix.x;
                    nc.y  += clcr.fix.y;
                }
            }
        }
        cdr.found = found;
        cdr.fix = new Vector2f(nc.x-center.x,nc.y-center.y);
        return cdr;
    }
    
    private static CollisionDetectionResult circleBoxCollision(Vector2f center, float radius, Vector2f p1, Vector2f p2)
    {
        Vector2f nc = new Vector2f(center);
        CollisionDetectionResult cdr = new CollisionDetectionResult();
        CollisionDetectionResult cdr2;
        
        boolean found = false;
        cdr2 = circleLineCollision(nc,radius,p1,new Vector2f(p1.x,p2.y));
        if (cdr2.found)
        {
            found = true;
            nc.x+= cdr2.fix.x;
            nc.y+= cdr2.fix.y;
        }
        
        cdr2 = circleLineCollision(nc,radius,p1,new Vector2f(p2.x,p1.y));
        if (cdr2.found)
        {
            found = true;
            nc.x+= cdr2.fix.x;
            nc.y+= cdr2.fix.y;
        }
        
        cdr2 = circleLineCollision(nc,radius,p2,new Vector2f(p1.x,p2.y));
        if (cdr2.found)
        {
            found = true;
            nc.x+= cdr2.fix.x;
            nc.y+= cdr2.fix.y;
        }
        
        cdr2 = circleLineCollision(nc,radius,p2,new Vector2f(p2.x,p1.y));
        if (cdr2.found)
        {
            found = true;
            nc.x+= cdr2.fix.x;
            nc.y+= cdr2.fix.y;
        }
        cdr.found = found;
        cdr.fix = new Vector2f(nc.x-center.x,nc.y-center.y);
        return cdr;
    }
    
    private static CollisionDetectionResult circleLineCollision(Vector2f center, float radius, Vector2f lineP1, Vector2f lineP2)
    {
        //en jaksanut koodata koko roskaa uudestaan jotenka nyysin koodin
        //vanhasta c++ projusta
        //kyseinen c++ koodi on todennäköisesti ryöstetty netistä
        
        //koska ei jumalauta mitä helvetin vektorilaskuja, toisen asteen yhtälöitä
        //ja muuta kivaa
        
        CollisionDetectionResult clcr = new CollisionDetectionResult();
        clcr.found = false;
        Vector2f d = new Vector2f();
        Vector2f f = new Vector2f();
        d.x = lineP1.x-lineP2.x;
        d.y = lineP1.y-lineP2.y;
        f.x = lineP1.x-center.x;
        f.y = lineP1.y-center.y;
        
        float a = d.x*d.x+d.y*d.y; //linjan pituus neliössä
        float b = 2*(f.x*d.x+f.y*d.y); //pistetulot
        float c = f.x*f.x+f.y*f.y-radius*radius; //etäisyys neliössä
        
        float ds = b*b-4*a*c; //toisen asteen yhtälön discriminantti
        if (ds <= 0)
        {
            return clcr;
        }
        else
        {
            ds = (float)Math.sqrt(ds);
            
            /*
            ympyrän osuminen linjaan voi aiheuttaa maksimissaan
            kaksi leikkauspistettä
            
            Seuraavat suhteet osumakohtien pisteitä seuraavalla kaavalla
            OsumaP1 = LineP1+(LineP2-LineP1)*p1
            (mikäli muistan oikein)
            */
            
            float p1 = 0-(-b+ds)/(2*a);//Osumakohta 1
            float p2 = 0-(-b-ds)/(2*a);//Osumakohta 2
            
            
            float dist; //etäisyys
            float ct; //joku
            float pmean = (p1+p2)/2.0f; //sama kuin b/(2*a);
            
            if (pmean < 0 && p2 >= 0)
            {
                dist = (float) Math.hypot(lineP1.x-center.x, lineP1.y-center.y);
                float d2 = radius-dist;
                ct = -d2/dist;
                clcr.fix = new Vector2f();
                clcr.fix.x = (lineP1.x-center.x)*ct;
                clcr.fix.y = (lineP1.y-center.y)*ct;
                clcr.found = true;
                return clcr;
            }
            if (pmean > 1 && p1 <= 1)
            {
                dist = (float) Math.hypot(lineP2.x-center.x, lineP2.y-center.y);
                float d2 = radius-dist;
                ct = -d2/dist;
                clcr.fix = new Vector2f();
                clcr.fix.x = (lineP2.x-center.x)*ct;
                clcr.fix.y = (lineP2.y-center.y)*ct;
                clcr.found = true;
                return clcr;
            }
            if (pmean >= 0 && pmean <= 1)
            {
                float tx,ty;
                tx = lineP1.x+d.x*(-pmean);
                ty = lineP1.y+d.y*(-pmean);
                
                dist = (float) Math.hypot(tx-center.x,ty-center.y);
                float d2 = radius-dist;
                
                ct = -d2/dist;
                
                clcr.fix = new Vector2f();
                clcr.fix.x = (tx-center.x)*ct;
                clcr.fix.y = (ty-center.y)*ct;
                clcr.found = true;
                return clcr;
            }
        }
        return clcr;
    }
    
}
