/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game2d.graphics;

import game2d.Main;
import org.lwjgl.util.vector.Vector2f;

/** Partikkeliefekti apumetodit sisältävä luokka
 *
 * @author MacodiusMaximus
 */
public class ParticleEffects
{

    /** Suorittaa räjähdyksen ruudulla
     *
     * @param pos räjähdyksen sijainti
     */
    public static void explode(Vector2f pos)
    {
        for (int i = 0; i < 25; i++)
        {
            FireParticle part = new FireParticle();
            Vector2f p = new Vector2f(pos.x+10*Main.randomFloat()-5,pos.y+10*Main.randomFloat()-5);
            part.setPos(p);
            
        }

        ExplosionParticle part = new ExplosionParticle(0.6f);
        part.setPos(new Vector2f(pos));
        
        Decal decal = new Decal(Graphics.getTexture("explosiondecal"));
        decal.setPos(new Vector2f(pos));
        decal.setScale(new Vector2f(0.3f,0.3f));
        
        Graphics.explode(new Vector2f(pos), 1);
    }
}
