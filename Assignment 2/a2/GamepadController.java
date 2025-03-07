package a2;

import tage.*;
import tage.input.InputManager;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;

import a2.MyGame;
import a2.Player;
import net.java.games.input.Event;
import net.java.games.input.Component;

public class GamepadController
{ 
    private MyGame game;
    private Player av;

    public GamepadController(MyGame g)
    { 
        game = g;
        setupInputs();
    }

    private void setupInputs() {
        InputManager im = game.engine.getInputManager();
        ControllerInput controllerInput = new ControllerInput();

		net.java.games.input.Component.Identifier.Axis[] movementAxes = {
			net.java.games.input.Component.Identifier.Axis.X, // Left stick X axis
			net.java.games.input.Component.Identifier.Axis.Y, // Left stick Y axis
			net.java.games.input.Component.Identifier.Axis.RX, // Right stick X axis
			net.java.games.input.Component.Identifier.Axis.RY, // Right stick Y axis
		};
		for (net.java.games.input.Component.Identifier.Axis axis : movementAxes) {
			im.associateActionWithAllGamepads(axis, controllerInput, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		}
        im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Button._0, controllerInput, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Button._2, controllerInput, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Button._3, controllerInput, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
    }     
        
    private class ControllerInput extends AbstractInputAction{
        public void performAction(float time, Event e)
        { 
            av = game.getAvatar();
            float scalar;
            String componentName = e.getComponent().getIdentifier().getName();

            //if (e.getValue() > -.2 && e.getValue() < .2) return; // deadzone

            if (!game.gameOver){
                // Left stick Y-axis for moving forward and backward
                if (componentName.equals(Component.Identifier.Button._0.getName())) {
                    scalar = 0.05f * game.timeScale; 
                    if(!game.cameraMode) { 
                        av.move(scalar, game);
                    }
                }
                // Right stick X-axis for yaw
                else if (componentName.equals(Component.Identifier.Axis.X.getName())) {
                    scalar = -e.getValue() * 0.02f * game.timeScale; 
                    if(!game.cameraMode) { 
                        av.globalYaw(scalar, game);
                    }
                }
                // Button X disarm
                else if (componentName.equals(Component.Identifier.Button._2.getName())) {  
                    game.satellite1.disarm(game, game.metalDisarmed);
                    game.satellite2.disarm(game, game.metalDisarmed2);
                    game.satellite3.disarm(game, game.metalDisarmed3);
                }

                // Button Y for camerMode
                else if (componentName.equals(Component.Identifier.Button._3.getName())) {  
                    game.cameraMode = !game.cameraMode;
                    if (game.cameraMode){
                        game.cameraOrbit3D.setupInputs();
                    }
                    else{
                        game.gamepadController.setupInputs();
                    }
                }
            }
        }
    }
}


