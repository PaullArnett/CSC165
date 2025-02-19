package a1;

import tage.*;
import org.joml.*;

public class Player extends GameObject  
{
    public Player(GameObject parent, ObjShape shape, TextureImage texture) {
        super(parent, shape, texture);
    }
    public Player(GameObject parent, ObjShape shape) {
        super(parent, shape);
    }

    private Vector3f oldPosition, newPosition;
    private Vector4f fwdDirection;

    //moves the player forward or backwords if riding the dolphin
    public void move(float scalar, MyGame game){
        if(game.rideMode){
            oldPosition = this.getWorldLocation();
            fwdDirection = new Vector4f(0f,0f,1f,1f);
            fwdDirection.mul(this.getWorldRotation());
            fwdDirection.mul(scalar);
            newPosition = oldPosition.add(fwdDirection.x(), fwdDirection.y(), fwdDirection.z());
            this.setLocalLocation(newPosition);
        }
        //if not riding then moves camera instead
        else{
            game.cam = (game.engine.getRenderSystem().getViewport("MAIN").getCamera());
            Vector3f loc = game.cam.getLocation();
            Vector3f fwd = game.cam.getN(); 
            Vector3f newLoc = loc.add(fwd.mul(scalar));

            if(game.avatar.getWorldLocation().distance(newLoc) < 5.0f){
                game.cam.setLocation(loc.add(fwd.mul(scalar)));
            }
        }
    }

}