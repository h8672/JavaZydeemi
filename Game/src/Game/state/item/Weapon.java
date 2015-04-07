/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state.item;

import Game.Main;
import org.lwjgl.util.vector.Vector2f;

/**
 * Base abstract class for Equipment interface
 * @author Juha-Matti
 */
public abstract class Weapon implements Equipment { // implements event (burning, explosion, freezing and so on...)
    private String name;
    private float DMG, attackrange, attackspeed, cone; // cone like part of circle, piece of cake and so on...
    private float reloadtime, ammunation, clipsize, speed; // ammunation stuff
    private float time;
    boolean equip;

    public float getSpeed() {
        return speed;
    }
    public String getName() {
        return name;
    }
    public float getDMG() {
        return DMG;
    }
    public float getAttackrange() {
        return attackrange;
    }
    public float getAttackspeed() {
        return attackspeed;
    }
    public float getCone() {
        return cone;
    }
    public float getReloadtime() {
        return reloadtime;
    }
    public float getAmmunation() {
        return ammunation;
    }
    public float getClipsize() {
        return clipsize;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDMG(float DMG) {
        this.DMG = DMG;
    }
    public void setAttackrange(float attackrange) {
        this.attackrange = attackrange;
    }
    public void setAttackspeed(float attackspeed) {
        this.attackspeed = attackspeed;
    }
    public void setCone(float cone) {
        this.cone = cone;
    }
    public void setReloadtime(float reloadtime) {
        this.reloadtime = reloadtime;
    }
    public void setAmmunation(float ammunation) {
        this.ammunation = ammunation;
    }
    public void setClipsize(float clipsize) {
        this.clipsize = clipsize;
    }
    
    public abstract void projectile(Vector2f position, float height, float rotation);
    
    public void attack(Vector2f position, float height, float rotation){
        if(ammunation > 0 && Main.getTime()  >= time + this.attackspeed){
            ammunation--;
            this.projectile(position, height, rotation - cone/2 + (float)((Math.random() * cone)));
            time = Main.getTime();
        }
        if(Main.getTime() >= time + this.reloadtime && ammunation == 0) this.ammunation = this.clipsize;
    }
    
    
    @Override
    public void pick(float amount){
        this.equip();
        this.ammunation += amount;
    }
    @Override
    public float drop(){
        return this.ammunation;
    }
    @Override
    public float drop(float amount){
        if(this.ammunation >= amount) this.ammunation -= amount;
        else{ //if dont have that many items to drop
            amount = this.ammunation;
            this.ammunation = 0;
        }
        return amount;
    }
    @Override
    public void destroy(){
        this.ammunation = 0;
    }
    @Override
    public void destroy(float amount){
        if(this.ammunation >= amount) this.ammunation -= amount;
        else this.ammunation = 0;
    }
    
    public void equip(){
        this.equip = true;
    }
    public void unequip(){
        this.equip = false;
    }
    @Override
    public boolean isEquipped(){
        return this.equip;
    }
}
