/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state.object.objects.items;

import Game.graphics.Drawing;
import Game.graphics.Graphics;
import Game.graphics.Texture;
import Game.state.item.Weapon;
import game.state.object.actor.actors.Player;
import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author Juha-Matti
 */
public class WeaponGround extends ItemGround
{
    static private Texture flamerTexture;
    static private Texture rlTexture;
    private Weapon weapon;
    private Texture texture;
    
    static public void initializeTextures()
    {
        flamerTexture = Graphics.getTexture("flamer");
        rlTexture = Graphics.getTexture("rawwkit");
        
    }
    
    public WeaponGround(Weapon weapon)
    {
        this.weapon = weapon;
        texture = rlTexture;
        
        
        if (weapon.getName() == "flamethrower")
            texture = flamerTexture;
        
        if (weapon.getName() == "rocket launcher")
            texture = rlTexture;
        
        Graphics.registerRenderable(this);
    }

    public void addEffect(Player player) {
        
        player.setSpecialWeapon(weapon);
        Graphics.removeRenderable(this);
    }

    @Override
    public void render()
    {
        Drawing.drawSpriteCentered(texture, this.getPosition(),0);
    }
    
}

