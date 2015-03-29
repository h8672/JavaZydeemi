/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state.object;

import Game.graphics.Renderable;
import org.lwjgl.util.vector.Vector2f;

/**
 * Base abstract class for GameObject interface
 * @author Juha-Matti
 */
public abstract class GameObjects implements GameObject, Renderable {
    Vector2f position, size;
    float rotation, height;
    String Image;
    boolean Visible;

    @Override
    public Vector2f getPosition() {
        return position;
    }
    @Override
    public Vector2f getSize() {
        return size;
    }
    @Override
    public float getRotation() {
        return rotation;
    }
    public float getHeight() {
        return height;
    }
    public String getImage() {
        return Image;
    }

    @Override
    public void setPosition(Vector2f position) {
        this.position = position;
    }
    @Override
    public void setSize(Vector2f size) {
        this.size = size;
    }
    @Override
    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
    public void setHeight(float height) {
        this.height = height;
    }
    public void setImage(String Image) {
        this.Image = Image;
    }
    public void setVisible(boolean Visible) {
        this.Visible = Visible;
    }
    
    //Renderable methods
    @Override
    public boolean isVisible() {
        return Visible;
    }
    @Override
    public float getDepth(){
        return height;
    }
}
