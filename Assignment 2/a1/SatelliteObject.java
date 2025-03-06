package a1;

import tage.*;

import org.joml.*;
import static java.lang.Math.min;

public class SatelliteObject extends GameObject  
{
    public SatelliteObject(GameObject parent, ObjShape shape, TextureImage texture) {
        super(parent, shape, texture);
    }
    public SatelliteObject(GameObject parent, ObjShape shape) {
        super(parent, shape);
    }

    private boolean IsDetonated, IsDisarmed, closest;
    public float avDistance, closestDistance, camDistance;

    public void checkDistances(MyGame game, TextureImage idle, TextureImage disarmable, TextureImage disarmed){
        //distance from satellite to player and camera
		avDistance = game.avatar.getWorldLocation().distance(this.getWorldLocation());
        camDistance = game.cam.getLocation().distance(this.getWorldLocation());
        closestDistance = min(game.satellite1.avDistance, min(game.satellite2.avDistance, game.satellite3.avDistance));

        // only concerned with the closest satellite to player
        if (!IsDetonated && !IsDisarmed && avDistance == closestDistance){

            //too close, detonate satellite
            if (avDistance < 2.0f){
                this.setTextureImage(game.detonation);
                IsDetonated = true;
                game.gameOver();
            }
            //Close enough to disarm
            else if (avDistance < 6.0f && avDistance >= 2.0f) { 
                
                this.setTextureImage(disarmable);
                game.gameMessage = "Close Enough, Disarm the satellite!";
            }	
            //else remain idle
            else{
                this.setTextureImage(idle);
                game.gameMessage = "Disarm the Satellites!";
            }  
        }
        //game message if far away from all satellites
        if (closestDistance > 22.0f){
            game.gameMessage = "Go through the Wormhole!";
        }
    }  

    public void disarm(MyGame game, TextureImage disarmed){
            //disarm the satellite
            if (avDistance < 6.0f && avDistance > 2.0f && !IsDetonated && !IsDisarmed && avDistance == closestDistance){
                this.setTextureImage(disarmed);
                IsDisarmed = true;
                game.score = game.score + 100;                
                game.gameMessage = "Satellite Disarmed!";
                if(game.coreCount == 0){game.satCore1.setLocalScale(new Matrix4f().scaling(.1f, .1f, .1f));}
                if(game.coreCount == 1){game.satCore2.setLocalScale(new Matrix4f().scaling(.1f, .1f, .1f));}
                if(game.coreCount == 2){game.satCore3.setLocalScale(new Matrix4f().scaling(.1f, .1f, .1f));}
                game.coreCount++;

                if(this.equals(game.satellite1)){
                    game.shakeController.disable();
                }
            }
            else if(avDistance == closestDistance){
                game.score = game.score - 50; 
                game.gameMessage = "Not Close Enough! -50 Points";
                System.out.println(avDistance);
            }
    }
    //used to restart the game
    public void resetTexture(TextureImage idle){
        this.setTextureImage(idle);
        IsDisarmed = false;
        IsDetonated = false;
    }    
}
