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


public class MyCameraOrbit3D extends CameraOrbit3D
{
    private Engine engine;
    private Camera camera; // the camera being controlled
    private GameObject avatar; // the target avatar the camera looks at
    private float cameraAzimuth; // rotation around target Y axis
    private float cameraElevation; // elevation of camera above target
    private float cameraRadius; // distance between camera and target
    
    public MyCameraOrbit3D(Camera cam, GameObject av, Engine e)
    { 
        // Call the superclass constructor with default azimuth, elevation, and radius values
        super(cam, av, e, -45.0f, 30.0f, 15.0f);
        engine = e;
        cameraAzimuth = -45.0f;
        cameraElevation = 30.0f; 
        cameraRadius = 15.0f; 
        setupInputs();
    }

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
        im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.X, azmAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.Y, elvAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.RY, zoomAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

    }
    
    private class OrbitAzimuthAction extends AbstractInputAction
    { public void performAction(float time, Event event)
        { 
            System.out.println("OrbitAzimuthAction triggered!");  // Debugging print

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
            System.out.println(cameraAzimuth);  // Debugging print

            updateCameraPosition(cameraAzimuth, cameraElevation, cameraRadius);
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
            updateCameraPosition(cameraAzimuth, cameraElevation, cameraRadius);
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
            updateCameraPosition(cameraAzimuth, cameraElevation, cameraRadius);
        } 
    }

}