/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state;

import Game.graphics.Graphics;
import Game.graphics.Renderable;

/**
 *
 * @author Juha-Matti
 */
public class Map  implements Renderable{

    public Map(){
        Graphics.registerRenderable(this, Graphics.IntermediateLayer);
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
    public void render() {
        //lukee luetun png kartan, ja liittää sen grafiikoihin
        //ja maailmaan.
    }
    
}
