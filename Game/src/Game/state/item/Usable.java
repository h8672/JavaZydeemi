/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state.item;

/**
 * Base abstract class for Item interface
 * @author Juha-Matti
 */
public abstract class Usable implements Item {
    String name;
    float amount;
    
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
        this.amount = 0;
    }
    @Override
    public void destroy(float amount){
        if(this.amount >= amount) this.amount -= amount;
        else this.amount = 0;
    }
}
