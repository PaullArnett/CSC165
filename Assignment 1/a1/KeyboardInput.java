package a1;

import tage.*;

import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;

import a2.MyGame;
import a2.Player;
import net.java.games.input.Event;
import net.java.games.input.Component;

public class KeyboardInput extends AbstractInputAction
{ 
    private MyGame game;
    private Player av;

    public KeyboardInput(MyGame g)
    { 
        game = g;
    }   

    @Override
    public void performAction(float time, Event e)
    { 
        av = game.getAvatar();
        float scalar;
        char key = e.getComponent().getIdentifier().getName().charAt(0); 

        //All keyboard inputs
        if (!game.gameOver){
            if (key == 'w' || key =='W'){
                scalar = 0.05f * game.timeScale;
                av.move(scalar, game);
            }
            else if (e.getComponent().getIdentifier() == Component.Identifier.Key.S){ //Key 'S' and SPACE both produce char S
                scalar = -0.05f * game.timeScale;
                av.move(scalar, game);
            }
            else if (key == 'a' || key =='A'){
                scalar = 0.02f * game.timeScale;
                if(game.rideMode) { av.yaw(scalar, game); }
                else{ game.cam.yaw(scalar, game); }
            }
            else if (e.getComponent().getIdentifier() == Component.Identifier.Key.D){    //key 'D' and DOWN both produce char D
                scalar = -0.02f * game.timeScale;
                if(game.rideMode){av.yaw(scalar, game);}
                else{game.cam.yaw(scalar, game);}
            }
            else if (e.getComponent().getIdentifier() == Component.Identifier.Key.DOWN){
                scalar = 0.02f * game.timeScale;
                if(game.rideMode){av.pitch(scalar, game);}
                else{game.cam.pitch(scalar, game);}
            }
            else if (key == 'u' || key =='U'){
                scalar = -0.02f * game.timeScale;
                if(game.rideMode){av.pitch(scalar, game);}
                else{game.cam.pitch(scalar, game);}
            }
            else if (e.getComponent().getIdentifier() == Component.Identifier.Key.SPACE){
                game.rideMode = !game.rideMode;
                if (!game.rideMode){
                    game.cam = (game.engine.getRenderSystem().getViewport("MAIN").getCamera());
                    Vector3f loc = game.cam.getLocation();
                    Vector3f right = game.cam.getU(); 
                    game.cam.setLocation(loc.add(right.mul(1)));
                }
            }
        }
        if (key == 'r' || key =='R'){
            game.restartGame();
        }
    }
}


