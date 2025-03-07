package a2;

import tage.*;


import org.joml.*;

import a2.MyGame;

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

    //moves the player forward or backwords 
    public void move(float scalar, MyGame game){
            oldPosition = this.getWorldLocation();
            fwdDirection = new Vector4f(0f,0f,1f,1f);
            fwdDirection.mul(this.getWorldRotation());
            fwdDirection.mul(scalar);
            newPosition = oldPosition.add(fwdDirection.x(), fwdDirection.y(), fwdDirection.z());
            this.setLocalLocation(newPosition);

            game.cam = (game.engine.getRenderSystem().getViewport("LEFT").getCamera());
            Vector3f camLoc = game.cam.getLocation();
            Vector3f newLoc = camLoc.add(fwdDirection.x(), fwdDirection.y(), fwdDirection.z());
            game.cam.setLocation(newLoc);
    }

}