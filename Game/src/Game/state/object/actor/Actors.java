/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state.object.actor;

import Game.graphics.Graphics;
import Game.graphics.Renderable;
import Game.state.Attack;
import Game.state.GameState;
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
    private Vector2f barrel;
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
    public void setVelocity(float speed, float rotation){
        velocity = new Vector2f(0,0);
        this.velocity.setX(- speed * (float)Math.sin(Math.toRadians(rotation)));
        this.velocity.setY(- speed * (float)Math.cos(Math.toRadians(rotation)));
    }
    public void addVelocity(Vector2f velocity){
        this.velocity.setX(velocity.getX() + this.velocity.getX());
        this.velocity.setY(velocity.getY() + this.velocity.getY());
    }
    public void addVelocity(float speed, float rotation){
        this.velocity.setX(this.velocity.getX() - speed * (float)Math.sin(Math.toRadians(rotation)));
        this.velocity.setY(this.velocity.getY() - speed * (float)Math.cos(Math.toRadians(rotation)));
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
    public Vector2f barrelpos(){
        barrel = new Vector2f();
        barrel.setX( -this.size.length()*(float)Math.sin(Math.toRadians(rotation)) + position.getX());
        barrel.setY( -this.size.length()*(float)Math.cos(Math.toRadians(rotation)) + position.getY());
        return barrel;
    }
    
    
    //Actor methods
    @Override
    public void attack(){
        this.weapon.attack(this.barrelpos(), this.height, this.rotation);
    }
    @Override
    public float defend(Attack attack) {
        float dmg = attack.hit(false) - armor.getAmount();
        System.out.println("dealt damage: " + dmg);
        if(dmg > 0) this.HP -= dmg;
        return this.HP;
    }
    @Override
    public float defend(Usable usable) {
        System.out.println("Testest");
        return this.HP += usable.getAmount() - armor.getAmount();
    }
    @Override
    public Vector2f move() {
        this.position.x += this.velocity.x;
        this.position.y += this.velocity.y;
        this.velocity.set(0,0);
        return position;
    }
    
    
    
    @Override
    public void update()
    {   
        if(this.HP <= 0){
            Graphics.removeRenderable(this);
            GameState.delActor(this);
        }
    }
    
    @Override
    public void kill() {
        Graphics.removeRenderable(this);
    }
}
