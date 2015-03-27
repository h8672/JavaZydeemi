/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.graphics;

import Game.Main;

/** Luokka jota käytetään yksittäisen objektin animoinnin apuna
 *
 * @author MacodiusMaximus
 */
public class Animator
{
    private Animation animation = null;
    private int currentFrame = 0;
    private int frameDelay = 2;
    private int frameDelayExt = 0;
    private boolean reachedEnd = false; 
    
    /** Palauttaa frameviiveen
     *
     * @return
     */
    public int getFrameDelay() {
        return frameDelay;
    }

    /** Asettaa frameviiveen
     *
     * @param frameDelay
     */
    public void setFrameDelay(int frameDelay) {
        this.frameDelay = frameDelay;
    }
    
    /** Animator constructor
     *
     * @param animation animaation jota Animator käyttää
     */
    public Animator(Animation animation)
    {
        this.animation = animation;
    }

    /** palauttaa nykyisen framen numeron
     *
     * @return
     */
    public int getCurrentFrame()
    {
        return currentFrame;
    }

    /** palauttaa onko animaatio päättynyt
     *
     * @return
     */
    public boolean hasReachedEnd()
    {
        return reachedEnd;
    }

    /** Nollaa animaationpäättymisen lipun
     *
     */
    public void clearReachedEnd()
    {
        this.reachedEnd = false;
    }
    
    Texture getTexture()
    {
        return animation.getFrame(currentFrame);
    }
    
    /** Animoi animaatiota yhden framen verran frameDelayn huomioon ottaen
     *
     */
    public void advance()
    {
        frameDelayExt++;
        if (frameDelayExt >= frameDelay)
        {
            frameDelayExt = 0;
            if (animation.isRandomized())
            {
                int nextFrame = Math.abs(Main.randomInt())%animation.getFrameCount();
                if (nextFrame == currentFrame)
                {
                    if (nextFrame == animation.getFrameCount()-1)
                        currentFrame = nextFrame-1;
                    else
                        currentFrame = nextFrame+1;
                }
                else
                    currentFrame = nextFrame;
                return;
            }
            currentFrame++;
            if (currentFrame == animation.getFrameCount())
            {
                if (animation.isLooping())
                    currentFrame = 0;
                else
                {
                    currentFrame--;
                    reachedEnd = true;
                }
            }
        }
    }
    
}
