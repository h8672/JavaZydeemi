/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state.object.actor;

import Game.graphics.Renderable;
import Game.state.item.Armor;
import Game.state.item.Usable;
import Game.state.item.Weapon;
import org.lwjgl.util.vector.Vector2f;

/**
 * Base abstract class for Actor interface
 * @author Juha-Matti
 */
public abstract class Actors implements Actor, Renderable {
    private Vector2f position, size, velocity;
    private float rotation, height, friction, weight, HP;
    private String Image;
    private boolean visible;
    private Armor armor;
    private Weapon weapon;

    public Armor getArmor(){
        return this.armor;
    }
    public Weapon getWeapon(){
        return this.weapon;
    }
    public void setArmor(Armor armor){
        this.armor = armor;
    }
    public void setWeapon(Weapon weapon){
        this.weapon = weapon;
    }
    
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
    public Vector2f getVelocity() {
        return velocity;
    }
    public float getHeight() {
        return height;
    }
    public float getFriction() {
        return friction;
    }
    public float getWeight() {
        return weight;
    }
    public float getHP() {
        return HP;
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
    public void setVelocity(Vector2f velocity) {
        this.velocity = velocity;
    }
    public void setHeight(float height) {
        this.height = height;
    }
    public void setFriction(float friction) {
        this.friction = friction;
    }
    public void setWeight(float weight) {
        this.weight = weight;
    }
    public void setHP(float HP) {
        this.HP = HP;
    }
    public void setImage(String Image) {
        this.Image = Image;
    }
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    //renderable methods
    @Override
    public boolean isVisible() {
        return visible;
    }
    @Override
    public float getDepth(){
        return this.height;
    }
    
    //Actor methods
    @Override
    public Weapon attack() {
        return weapon;
    }
    @Override
    public float defend(Weapon weapon) {
        return this.HP += armor.getAmount() - weapon.getDMG();
    }
    @Override
    public float defend(Usable usable) {
        return this.HP += armor.getAmount() - usable.getAmount();
    }
    @Override
    public Vector2f move() {
        return velocity.negate(position);
    }
    
}
