package a1;

import java.lang.Math;
import tage.*;
import tage.input.action.AbstractInputAction;
import tage.input.*;
import tage.input.action.*;
import net.java.games.input.*;
import net.java.games.input.Component.Identifier.*;

import net.java.games.input.Event;
import org.joml.*;
import net.java.games.input.Event;
import net.java.games.input.Component;


public class CameraOrbit3D
{ 
    private Engine engine;
    private Camera camera; // the camera being controlled
    private GameObject avatar; // the target avatar the camera looks at
    private float cameraAzimuth; // rotation around target Y axis
    private float cameraElevation; // elevation of camera above target
    private float cameraRadius; // distance between camera and target
    
    public CameraOrbit3D(Camera cam, GameObject av, Engine e)
    { 
        engine = e;
        camera = cam;
        avatar = av;
        cameraAzimuth = 0.0f; // start BEHIND and ABOVE the target
        cameraElevation = 30.0f; // elevation is in degrees
        cameraRadius = 10.0f; // distance from camera to avatar
        setupInputs();
        updateCameraPosition();
    }

    public void setupInputs()
    { 
        OrbitAzimuthAction azmAction = new OrbitAzimuthAction();
        ElevationAction elvAction = new ElevationAction();
        ZoomRadiusAction zoomAction = new ZoomRadiusAction();
        InputManager im = engine.getInputManager();
        
        //keyboard inputs
		im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.W, elvAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.A, azmAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.S, elvAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.D, azmAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key._1, zoomAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key._2, zoomAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        


        //controller inputs
        im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.X, azmAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.Y, elvAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.RY, zoomAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

    }

    // Compute the camera’s azimuth, elevation, and distance, relative to
    // the target in spherical coordinates, then convert to world Cartesian
    // coordinates and set the camera position from that.
    public void updateCameraPosition()
    { 
        Vector3f avatarRot = avatar.getWorldForwardVector();
        double avatarAngle = Math.toDegrees((double)
        avatarRot.angleSigned(new Vector3f(0,0,-1), new Vector3f(0,1,0)));
        float totalAz = cameraAzimuth - (float)avatarAngle;
        double theta = Math.toRadians(totalAz);
        double phi = Math.toRadians(cameraElevation);
        float x = cameraRadius * (float)(Math.cos(phi) * Math.sin(theta));
        float y = cameraRadius * (float)(Math.sin(phi));
        float z = cameraRadius * (float)(Math.cos(phi) * Math.cos(theta));
        camera.setLocation(new Vector3f(x,y,z).add(avatar.getWorldLocation()));
        camera.lookAt(avatar);
    }

    public void resetPosition(){
        
        cameraAzimuth = 0.0f; // start BEHIND and ABOVE the target
        cameraElevation = 30.0f; // elevation is in degrees
        cameraRadius = 10.0f; // distance from camera to avatar
        updateCameraPosition();

    }
    
    private class OrbitAzimuthAction extends AbstractInputAction
    { public void performAction(float time, Event event)
        { 
            float rotAmount;
            String btnName = event.getComponent().getIdentifier().getName();

            if (btnName.equals("A"))
                { rotAmount=0.5f; }
            else if (btnName.equals("D"))
                { rotAmount=-0.5f; }
            else if (event.getValue() < -0.2)
                { rotAmount=-0.5f; }
            else if (event.getValue() > 0.2)
                { rotAmount=0.5f; }
            else
                { rotAmount=0.0f; }

            cameraAzimuth += rotAmount;
            cameraAzimuth = cameraAzimuth % 360;
            updateCameraPosition();
        } 
    }
    private class ZoomRadiusAction extends AbstractInputAction
    { public void performAction(float time, Event event)
        { 
            float zoomAmount;
            String btnName = event.getComponent().getIdentifier().getName();

            if (btnName.equals("1"))
                { zoomAmount=-0.025f; }
            else if (btnName.equals("2"))
                { zoomAmount=0.025f; }
            else if (event.getValue() < -0.2)
                { zoomAmount=-0.025f; }
            else if (event.getValue() > 0.2)
                { zoomAmount=0.025f; }
            else
                { zoomAmount=0.0f; }

            cameraRadius += zoomAmount;
            updateCameraPosition();
        } 
    }

    private class ElevationAction extends AbstractInputAction
    { public void performAction(float time, Event event)
        { 
            float elvAmount;
            String btnName = event.getComponent().getIdentifier().getName();

            if (btnName.equals("W"))
                { elvAmount=0.5f; }
            else if (btnName.equals("S"))
                { elvAmount=-0.5f; }
            else if (event.getValue() < -0.2)
                { elvAmount=-0.5f; }
            else if (event.getValue() > 0.2)
                { elvAmount=0.5f; }
            else
                { elvAmount=0.0f; }

            cameraElevation += elvAmount;
            cameraElevation = cameraElevation % 360;
            cameraElevation = Math.max(1.0f, Math.min(89.0f, cameraElevation));
            updateCameraPosition();
        } 
    }

}