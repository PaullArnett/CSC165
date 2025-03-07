package tage;

import java.lang.Math;
import tage.*;
import tage.input.action.AbstractInputAction;
import tage.input.*;
import tage.input.action.*;
import net.java.games.input.*;
import net.java.games.input.Component.Identifier.*;
import net.java.games.input.Event;
import org.joml.*;

import a2.MyGame;
import net.java.games.input.Event;
import net.java.games.input.Component;

/**
* CameraOrbit3D is an abstract class used for creating a camera that can orbit around a gameobject.
*/
public abstract class CameraOrbit3D
{ 
    private Engine engine;
    private Camera camera; 
    private GameObject avatar;
    private float cameraAzimuth; 
    private float cameraElevation; 
    private float cameraRadius; 
    private float initialA, initialE, initialR;
    
    /** Creates an orbit camera with the given Azimuth, Elevation, and Radius */
    public CameraOrbit3D(Camera cam, GameObject av, Engine e, float ca, float ce, float cr)
    { 
        engine = e;
        camera = cam;
        avatar = av;

        initialA = ca;
        initialE = ce;
        initialR = cr;

        cameraAzimuth = ca;
        cameraElevation = ce; 
        cameraRadius = cr; 
        updateCameraPosition(cameraAzimuth, cameraElevation, cameraRadius);
    }

    /** add custome inputs/controls here */
    protected abstract void setupInputs();

    /**
    * Compute the camera's azimuth, elevation, and distance, relative to
    * the target in spherical coordinates, then convert to world Cartesian
    * coordinates and set the camera position from that.
    */
    public void updateCameraPosition(float cameraAzimuth, float cameraElevation, float cameraRadius)
    { 
        float totalAz = cameraAzimuth;
        double theta = Math.toRadians(totalAz);
        double phi = Math.toRadians(cameraElevation);
        float x = cameraRadius * (float)(Math.cos(phi) * Math.sin(theta));
        float y = cameraRadius * (float)(Math.sin(phi));
        float z = cameraRadius * (float)(Math.cos(phi) * Math.cos(theta));
        camera.setLocation(new Vector3f(x,y,z).add(avatar.getWorldLocation()));
        camera.lookAt(avatar);
    }

    /** reset camera to inital position */
    public void resetPosition(){
        
        cameraAzimuth = initialA; 
        cameraElevation = initialE;
        cameraRadius = initialR;
        updateCameraPosition(cameraAzimuth, cameraElevation, cameraRadius);

    }
}