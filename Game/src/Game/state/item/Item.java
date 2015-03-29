/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state.item;

/**
 * Item interface
 * @author Juha-Matti
 */
public interface Item {
    public void pick(float amount);
    public float drop();
    public float drop(float amount);
    public void destroy();
    public void destroy(float amount);
}
