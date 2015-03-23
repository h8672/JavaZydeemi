/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

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
        for (int i = 0; i < 5; i++)
        {
            FireParticle part = new FireParticle();
            Vector2f p = new Vector2f(pos.x+20*Main.randomFloat()-10,pos.y+20*Main.randomFloat()-10);
            part.setPos(p);
        }

        ExplosionParticle part = new ExplosionParticle();
        part.setPos(pos);
    }
}
