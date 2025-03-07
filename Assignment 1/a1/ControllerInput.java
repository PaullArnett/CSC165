package a1;

import tage.*;

import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;

import a2.MyGame;
import a2.Player;
import net.java.games.input.Event;
import net.java.games.input.Component;

public class ControllerInput extends AbstractInputAction
{ 
    private MyGame game;
    private Player av;

    public ControllerInput(MyGame g)
    { 
        game = g;
    }   


    @Override
    public void performAction(float time, Event e)
    { 
        av = game.getAvatar();
        float scalar;
        String componentName = e.getComponent().getIdentifier().getName();

        //if (e.getValue() > -.2 && e.getValue() < .2) return; // deadzone

        if (!game.gameOver){
            // Left stick Y-axis for moving forward and backward
            if (componentName.equals(Component.Identifier.Axis.Y.getName())) {
                scalar = -e.getValue() * 0.04f * game.timeScale; 
                av.move(scalar, game);
            }
            // Right stick X-axis for yaw
            if (componentName.equals(Component.Identifier.Axis.RX.getName())) {
                scalar = -e.getValue() * 0.02f * game.timeScale; 
                if(game.rideMode) { av.yaw(scalar, game); }
                else{ game.cam.yaw(scalar, game); }
            }
            // Right stick Y-axis for pitch
            if (componentName.equals(Component.Identifier.Axis.RY.getName())) {
                scalar = e.getValue() * 0.02f * game.timeScale;
                if(game.rideMode) { av.pitch(scalar, game); }
                else{ game.cam.pitch(scalar, game); }
            }
            // Button B for dismount and remount
            else if (componentName.equals(Component.Identifier.Button._1.getName())) {  
                game.rideMode = !game.rideMode;
                if (!game.rideMode){
                    game.cam = (game.engine.getRenderSystem().getViewport("MAIN").getCamera());
                    Vector3f loc = game.cam.getLocation();
                    Vector3f right = game.cam.getU(); 
                    game.cam.setLocation(loc.add(right.mul(1)));
                }
            }
        }
    }
}


