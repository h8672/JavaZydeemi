/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game2d.state.item;

/**
 * Base abstract class for Equipment interface
 * @author Juha-Matti
 */
public abstract class Armor implements Equipment {
    private String name;
    private float amount;
    private boolean equip = false;
    
    public void setName(String name){
        this.name = name;
    }
    public void setAmount(float amount){
        this.amount = amount;
    }
    
    public String getName(){
        return this.name;
    }
    public float getAmount(){
        return this.amount;
    }
    
    @Override
    public void pick(float amount){
        this.amount += amount;
    }
    @Override
    public float drop(){
        return this.amount;
    }
    @Override
    public float drop(float amount){
        if(this.amount >= amount) this.amount -= amount;
        else{ //if dont have that many items to drop
            amount = this.amount;
            this.amount = 0;
        }
        return amount;
    }
    @Override
    public void destroy(){
        // ei voi tuhota muuten kuin kuluttamalla
    }
    @Override
    public void destroy(float amount){
        // ei voi tuhota muuten kuin kuluttamalla
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
