package a2;

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


public class MyCameraOrbit3D extends CameraOrbit3D
{
    private Engine engine;
    private Camera camera; 
    private GameObject avatar; 
    private float cameraAzimuth; // rotation around target Y axis
    private float cameraElevation; // elevation of camera above target
    private float cameraRadius; // distance between camera and target
    private MyGame game;
    
    public MyCameraOrbit3D(Camera cam, GameObject av, Engine e, MyGame g)
    { 
        super(cam, av, e, -45.0f, 30.0f, 15.0f);
        engine = e;
        game = g;
        cameraAzimuth = -45.0f;
        cameraElevation = 30.0f; 
        cameraRadius = 15.0f; 
        setupInputs();
    }

    //mapping keys for Orbit camera
    @Override
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
        im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.RX, azmAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.RY, elvAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.Y, zoomAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

    }
    
    //this orbits the camera from side to side
    private class OrbitAzimuthAction extends AbstractInputAction
    { public void performAction(float time, Event event)
        { 
            float rotAmount;
            String btnName = event.getComponent().getIdentifier().getName();

            if (btnName.equals("A"))
                { rotAmount=-0.5f; }
            else if (btnName.equals("D"))
                { rotAmount=0.5f; }
            else if (event.getValue() < -0.2)
                { rotAmount=-0.5f; }
            else if (event.getValue() > 0.2)
                { rotAmount=0.5f; }
            else
                { rotAmount=0.0f; }

            cameraAzimuth += rotAmount * game.timeScale;
            cameraAzimuth = cameraAzimuth % 360;

            updateCameraPosition(cameraAzimuth, cameraElevation, cameraRadius);
        } 
    }

    //zooms in and out towards the avatar
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

            cameraRadius += zoomAmount * game.timeScale;
            cameraRadius = Math.max(3.0f, cameraRadius); //radius of 3 is the minimum
            updateCameraPosition(cameraAzimuth, cameraElevation, cameraRadius);
        } 
    }

    //orbits camera up and down
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
                { elvAmount=0.5f; }
            else if (event.getValue() > 0.2)
                { elvAmount=-0.5f; }
            else
                { elvAmount=0.0f; }

            cameraElevation += elvAmount * game.timeScale;
            cameraElevation = cameraElevation % 360;
            cameraElevation = Math.max(1.0f, Math.min(89.0f, cameraElevation)); //range from 1 - 89 
            updateCameraPosition(cameraAzimuth, cameraElevation, cameraRadius);
        } 
    }

}