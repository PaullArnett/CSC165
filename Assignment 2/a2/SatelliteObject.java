package a2;

import tage.*;

import org.joml.*;

import a2.MyGame;

import static java.lang.Math.min;
import tage.networking.client.GameConnectionClient;

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
            if (avDistance < 6.0f){
                this.setTextureImage(game.detonation);
                IsDetonated = true;
                game.gameOver();
            }
            //Close enough to disarm
            else if (avDistance < 11.0f && avDistance >= 6.0f) { 
                
                this.setTextureImage(disarmable);
                game.gameMessage = "Close Enough, Disarm the satellite!";
            }	
            //else remain idle
            else{
                this.setTextureImage(idle);
                game.gameMessage = "Disarm the Satellites!";
            }  
        }
    }  

    public void disarm(MyGame game, TextureImage disarmed){
            //disarm the satellite
            if (avDistance < 11.0f && avDistance > 6.0f && !IsDetonated && !IsDisarmed && avDistance == closestDistance){
                this.setTextureImage(disarmed);
                IsDisarmed = true;
                game.score = game.score + 100;                
                game.gameMessage = "Satellite Disarmed!";
                
                //show an addition core for each satellite disarmed
                if(game.coreCount == 0){game.satCore1.setLocalScale(new Matrix4f().scaling(.15f, .15f, .15f));}
                if(game.coreCount == 1){game.satCore2.setLocalScale(new Matrix4f().scaling(.15f, .15f, .15f));}
                if(game.coreCount == 2){game.satCore3.setLocalScale(new Matrix4f().scaling(.15f, .15f, .15f));}
                game.coreCount++;

                if(game.coreCount == 3){
                    game.gameOver();
                }
                //satellite effects once disarmed
                if(this.equals(game.satellite1)){
                    game.shakeController.addTarget(game.satellite1);
                }
                if(this.equals(game.satellite2)){
                    game.rotationController.addTarget(game.satellite2);
                }
                if(this.equals(game.satellite3)){
                    game.rotationController.addTarget(game.satellite3);
                }
            }
            //disarming too soon is a point penatly
            else if(avDistance == closestDistance){
                game.score = game.score - 50; 
                game.gameMessage = "Not Close Enough! -50 Points";
            }
    }
    //used to restart the game
    public void resetTexture(MyGame game, TextureImage idle){
        this.setTextureImage(idle);
        IsDisarmed = false;
        IsDetonated = false;
        game.coreCount = 0;
    }    
}
