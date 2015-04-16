/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state.object.objects.items;

import Game.Main;
import Game.graphics.SparkParticle;
import Game.state.GameState;
import Game.state.object.GameObjects;
import game.state.object.actor.actors.Player;
import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author MacodiusMaximus
 */
abstract public class ItemGround extends GameObjects 
{
    ItemGround()
    {
        this.setHeight(19);
    }
    
    @Override
    public void update() {
        if (Main.randomInt()%120 > 110)
        {
            SparkParticle t = new SparkParticle();
            t.setPos(new Vector2f(this.getPosition()));
        }
    }
    
    abstract public void addEffect(Player player);
    
}
