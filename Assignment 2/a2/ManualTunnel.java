package a2;

import tage.*;
import tage.shapes.*;

public class ManualTunnel extends ManualObject {
    //4 faces of a cube to make tunnel
    private float[] vertices = new float[] {
        -1.0f, -1.0f, 1.0f,   1.0f, -1.0f, 1.0f,   1.0f, 1.0f, 1.0f,  
        -1.0f, -1.0f, 1.0f,   1.0f, 1.0f, 1.0f,   -1.0f, 1.0f, 1.0f,
        
        -1.0f, -1.0f, -1.0f,  1.0f, -1.0f, -1.0f,  1.0f, 1.0f, -1.0f, 
        -1.0f, -1.0f, -1.0f,  1.0f, 1.0f, -1.0f,  -1.0f, 1.0f, -1.0f, 
        
        -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f,  -1.0f, 1.0f, 1.0f,
        -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, 1.0f,  -1.0f, 1.0f, -1.0f,
        
         1.0f, -1.0f, -1.0f,  1.0f, -1.0f, 1.0f,   1.0f, 1.0f, 1.0f,
         1.0f, -1.0f, -1.0f,  1.0f, 1.0f, 1.0f,   1.0f, 1.0f, -1.0f,     
    };
    private float[] texcoords = new float[] {
        0.0f, 0.0f,  1.0f, 0.0f,  1.0f, 1.0f,
        0.0f, 0.0f,  1.0f, 1.0f,  0.0f, 1.0f,
    
        1.0f, 0.0f,  0.0f, 0.0f,  0.0f, 1.0f,
        1.0f, 0.0f,  0.0f, 1.0f,  1.0f, 1.0f,
    
        0.0f, 0.0f,  1.0f, 0.0f,  1.0f, 1.0f,
        0.0f, 0.0f,  1.0f, 1.0f,  0.0f, 1.0f,
    
        0.0f, 0.0f,  1.0f, 0.0f,  1.0f, 1.0f,
        0.0f, 0.0f,  1.0f, 1.0f,  0.0f, 1.0f,
    };
    
    private float[] normals = new float[] {
        0.0f, 0.0f, 1.0f,  0.0f, 0.0f, 1.0f,  0.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f,  0.0f, 0.0f, 1.0f,  0.0f, 0.0f, 1.0f,

        0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f,
        0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f,

        -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,
        -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,

        1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
};
    public ManualTunnel() {
        super();
        setNumVertices(24);
        setVertices(vertices);
        setTexCoords(texcoords);
        setNormals(normals);
        setMatAmb(Utils.goldAmbient());
        setMatDif(Utils.goldDiffuse());
        setMatSpe(Utils.goldSpecular());
        setMatShi(Utils.goldShininess());
    }
}
