/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

/** Interface piirrettäville asioille
 *
 * @author MacodiusMaximus
 */
public interface Renderable {

    /** palauttaa onko objekti näkyvä
     *
     * @return 
     */
    public boolean isVisible();
    
    /** palauttaa objektin syvyyden
     * syvyys on millä "tasolla" objekti piiretään, eli onko se muiden objektien alla vai päällä
     * @return Syvyys välillä -99 - 100
     */
    public int getDepth();

    /** piirtää objektin
     *
     */
    public void render();
}
