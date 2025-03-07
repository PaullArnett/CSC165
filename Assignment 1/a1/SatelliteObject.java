package a1;

import tage.*;
import org.joml.*;

import a2.MyGame;

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
            //disarm the satellite
            if (camDistance < 3.0f && camDistance > 1.5f && !IsDetonated && !game.rideMode){
                this.setTextureImage(disarmed);
                IsDisarmed = true;
                game.score = game.score + 100;                
                game.gameMessage = "Satellite Disarmed!";
            }
            //too close, detonate satellite
            else if (avDistance < 2.0f){
                this.setTextureImage(game.detonation);
                IsDetonated = true;
                game.gameOver();
            }
            //Close enough to disarm
            else if (avDistance < 6.0f && avDistance >= 2.0f) { 
                
                this.setTextureImage(disarmable);
                game.gameMessage = "Close Enough, Dismount and Disarm!";
            }	
            //else remain idle
            else{
                this.setTextureImage(idle);
                game.gameMessage = "Disarm the Satellites!";
            }  
        }
        //game message if far away from all satellites
        if (closestDistance > 22.0f){
            game.gameMessage = "Go through the Wormhole and Disarm the Satellites!";
        }
    }  
    //used to restart the game
    public void resetTexture(TextureImage idle){
        this.setTextureImage(idle);
        IsDisarmed = false;
        IsDetonated = false;
    }    
}
