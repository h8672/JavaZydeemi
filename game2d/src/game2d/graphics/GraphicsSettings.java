/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game2d.graphics;

import java.io.Serializable;

/**
 *
 * @author H9115
 */
public class GraphicsSettings implements Serializable
{
    public boolean MSAAEnabled = false;
    public int MSAASamples = 2;
    public int windowWidth = 1280;
    public int windowHeight = 800;
    public boolean shadersEnabled = true;
    public boolean FBOEnabled = true;
    public boolean shockwaves = true;
    public float shockwaveDisplaceAmount = -10;
    public float shockwaveSize = 180;
    public float shockwaveSpeed = 5.0f;
}
