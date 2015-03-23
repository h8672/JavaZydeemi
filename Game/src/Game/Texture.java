/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

/** Luokka joka pitää sisällään kuvadatoja tekstuurille
 *
 * @author MacodiusMaximus
 */
public class Texture
{
    private String name = "";
    private ImageData baseTex = null;
    private ImageData glowTex = null;
    private boolean hasGlowTex = false;

    /** Palauttaa onko tekstuurilla on glowmap
     *
     * @return
     */
    public boolean hasGlow()
    {
        return hasGlowTex;
    }

    /** Palauttaa tekstuurin nimen
     *
     * @return
     */
    public String getName()
    {
        return name;
    }

    /** Palauttaa tekstuurin peruskuvan
     *
     * @return
     */
    public ImageData getBaseImage()
    {
        return baseTex;
    }
    
    /** Palauttaa tekstuurin glowmap kuvan
     *
     * @return
     */
    public ImageData getGlowImage()
    {
        return glowTex;
    }
    
    /**Texture Constructor
     *
     * @param name uniikki nimi
     * @param baseTex peruskuva
     */
    public Texture(String name, ImageData baseTex)
    {
        this.name = name;
        this.baseTex = baseTex;
    }

    /**Texture Constructor
     *
     * @param name uniikki nimi
     * @param baseTex peruskuva
     * @param glowTex glowmap
     */
    public Texture(String name, ImageData baseTex, ImageData glowTex)
    {
        this.name = name;
        this.baseTex = baseTex;
        this.glowTex = glowTex;
        hasGlowTex= true;
    }
    
    /** Palauttaa onko tekstuuri ladattu onnistuneesti
     *
     * @return
     */
    public boolean isLoaded()
    {
        if (baseTex == null)
            return false;
        return baseTex.isLoaded();
    }
}
