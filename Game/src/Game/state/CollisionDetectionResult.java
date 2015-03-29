/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.state;

import org.lwjgl.util.vector.Vector2f;

/**Törmäystarkistuksien tulokset
 * käytetään pääosin CollisionDetection luokan metodeissa
 * 
 * @author MacodiusMaximus
 */
public class CollisionDetectionResult
{

    /** Törmäyksentarkistuksen tulos. true, jos törmäys on löytynyt.
     *
     */
    public boolean found = false;

    /** Törmäystarkistuksen mahdollinen korjausvektori. 
     * <p>
     * Kertoo, mihinkä törmäävän objektin on liikuttava
     * ettei törmäystä enään ole.
     *
     */
    public Vector2f fix = new Vector2f();   
}
