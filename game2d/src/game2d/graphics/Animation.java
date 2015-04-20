/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game2d.graphics;

import java.util.ArrayList;

/** Luokka joka on tarkoitettu animaatiodatan talletukseen
 *
 * @author MacodiusMaximus
 */
public class Animation
{
    private ArrayList<Texture> frames;
    private String name;
    private boolean loop = false;

    /** Palauttaa onko animaatio looppaava
     *
     * @return
     */
    public boolean isLooping() {
        return loop;
    }

    /** Asettaa onko animaatio looppaava
     *
     * @param loop
     */
    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    /** Palauttaa onko animaatio satunnaisjärjestyksessä
     *
     * @return
     */
    public boolean isRandomized() {
        return randomized;
    }

    /** Asettaa onko animaatio satunnaisjärjestyksessä
     *
     * @param randomized
     */
    public void setRandomized(boolean randomized) {
        this.randomized = randomized;
    }
    
    private boolean randomized = false;
    
    /** Animation constructor
     *
     * @param name uniikki nimi
     */
    public Animation(String name)
    {
        frames = new ArrayList<>();
        this.name = name;
    }
    
    /** Palauttaa animaation nimen
     *
     * @return nimi
     */
    public String getName() {
        return name;
    }

    /** Lisää animaation framen
     *
     * @param frame Texture, joka sisältää framen datan
     */
    public void addFrame(Texture frame)
    {
        frames.add(frame);
    }

    /** Palauttaa animaation framen
     *
     * @param frame framen index numero
     * @return Texture joka sisältää framen
     */
    public Texture getFrame(int frame)
    {
        return frames.get(frame);
    }

    /** Palauttaa animaation kokonaisframejen määrän
     *
     * @return määrä
     */
    public int getFrameCount()
    {
        return frames.size();
    }
}
