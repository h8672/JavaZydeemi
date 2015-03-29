/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state.object;

import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author Juha-Matti
 */
public interface GameObject {

    /**
     * GameObjects base methods
     */
    public Vector2f getPosition();
    public Vector2f getSize();
    public float getRotation();
    public void setPosition(Vector2f position);
    public void setSize(Vector2f size);
    public void setRotation(float rotation);
    public void update();
}