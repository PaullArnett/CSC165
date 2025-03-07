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

public class MinimapController 
{
    private Engine engine;
    private Camera camera;
    private float x, z, y, panX, panZ;
    private MyGame game;

    public MinimapController(Camera cam, Engine e, MyGame g){
        engine = e;
        camera = cam;
        game = g;

        //initial postion of minimap camera
        x = 10.0f;
        y = 25.0f;
        z = -10.0f;

        setupInputs();
        updateCameraPosition();
    }

    //mapping more keys
    public void setupInputs()
    { 
        PanMapAction panMapAction = new PanMapAction();
        ZoomMapAction zoomMapAction = new ZoomMapAction();
        InputManager im = engine.getInputManager();
        
        //keyboard inputs
		im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.UP, panMapAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.DOWN, panMapAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.LEFT, panMapAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.RIGHT, panMapAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.PAGEUP, zoomMapAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.PAGEDOWN, zoomMapAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
    
    }

    public void updateCameraPosition()
    { 
        camera.setLocation(new Vector3f(x,y,z));
    }

    //pans the camera but adjusting the x and z of camera's location
    private class PanMapAction extends AbstractInputAction
    { public void performAction(float time, Event event)
        { 
            String btnName = event.getComponent().getIdentifier().getName();
            panX=0.0f;
            panZ=0.0f;

            if (btnName.equals("Right"))
                { panX=0.3f; }
            else if (btnName.equals("Left"))
                { panX=-0.3f; }
            else if (btnName.equals("Up"))
                { panZ=-0.3f; }
            else if (btnName.equals("Down"))
                { panZ=0.3f; }

            x += panX * game.timeScale;
            z += panZ * game.timeScale;
            updateCameraPosition();
        }
    }

    //zooms the camera but adjusting the y of camera's location
    private class ZoomMapAction extends AbstractInputAction
    { public void performAction(float time, Event event)
        { 
            float zoomAmount;
            String btnName = event.getComponent().getIdentifier().getName();

            if (btnName.equals("Pg Up"))
                { zoomAmount=-0.25f; }
            else if (btnName.equals("Pg Down"))
                { zoomAmount=0.25f; }
            else
                { zoomAmount=0.0f; }

            y += zoomAmount * game.timeScale;
            updateCameraPosition();
        }
    }
}
