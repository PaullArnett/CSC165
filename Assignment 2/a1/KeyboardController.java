package a1;

import tage.*;
import tage.input.InputManager;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;
import net.java.games.input.Event;
import net.java.games.input.Component;

public class KeyboardController
{ 
    private MyGame game;
    private Player av;

    public KeyboardController(MyGame g)
    { 
        game = g;
        setupInputs();
    }   

    private void setupInputs() {
        InputManager im = game.engine.getInputManager();
        KeyboardInput keyboardInput = new KeyboardInput();

		//MOVEMENT FOR KEYBOARD
		net.java.games.input.Component.Identifier.Key[] movementKeys = {
			net.java.games.input.Component.Identifier.Key.W,
			net.java.games.input.Component.Identifier.Key.S,
			net.java.games.input.Component.Identifier.Key.A,
			net.java.games.input.Component.Identifier.Key.D,
		};
		for (net.java.games.input.Component.Identifier.Key key : movementKeys) {
			im.associateActionWithAllKeyboards(key, keyboardInput, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		}
		//Other controls for keyboard
		im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.SPACE, keyboardInput, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.R, keyboardInput, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.C, keyboardInput, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.E, keyboardInput, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
    } 

    private class KeyboardInput extends AbstractInputAction{
        public void performAction(float time, Event e)
        { 
            av = game.getAvatar();
            float scalar;
            char key = e.getComponent().getIdentifier().getName().charAt(0); 


            if (!game.gameOver){
                //Movement keys
                if (key == 'w' || key =='W'){
                    scalar = 0.05f * game.timeScale;
                    if(!game.cameraMode) {av.move(scalar, game);}
                }
                else if (e.getComponent().getIdentifier() == Component.Identifier.Key.S){ //Key 'S' and SPACE both produce char S
                    scalar = -0.05f * game.timeScale;
                    if(!game.cameraMode) {av.move(scalar, game);}
                }
                else if (key == 'a' || key =='A'){
                    scalar = 0.02f * game.timeScale;
                    if(!game.cameraMode) { av.globalYaw(scalar, game); }
                }
                else if (e.getComponent().getIdentifier() == Component.Identifier.Key.D){    //key 'D' and DOWN both produce char D
                    scalar = -0.02f * game.timeScale;
                    if(!game.cameraMode){av.globalYaw(scalar, game);}
                }
                
                //disarm key
                else if (key == 'e' || key =='E'){
                    game.satellite1.disarm(game, game.metalDisarmed);
                    game.satellite2.disarm(game, game.metalDisarmed2);
                    game.satellite3.disarm(game, game.metalDisarmed3);
                }

                //Camermode key
                else if (key == 'c' || key =='C'){
                    game.cameraMode = !game.cameraMode;
                    if (game.cameraMode){
                        game.cameraOrbit3D.setupInputs();
                    }
                    else{
                        game.keyboardController.setupInputs();
                    }
                }
            }
            //Restart game key
            if (key == 'r' || key =='R'){
                game.restartGame();
            }
        }
    }
}


