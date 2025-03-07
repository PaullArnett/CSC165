package a1;

import tage.*;
import tage.shapes.*;

import java.lang.Math;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import org.joml.*;

import a2.ManualTunnel;
import a2.MyGame;
import a2.Player;
import a2.SatelliteObject;
import tage.input.*;
import tage.input.action.*;
import net.java.games.input.*;
import net.java.games.input.Component.Identifier.*;
import tage.Light.LightType;

public class MyGame extends VariableFrameRateGame
{
	public static Engine engine;
	private InputManager im;
	public Camera cam;

	public boolean rideMode, gameOver;
	public int score;
	public String gameMessage;
	public float timeScale;
	public double startTime, lastFrameTime, currFrameTime, elapsTime;

	public Player avatar;
	public GameObject x, y, z, manualTunnel;
	public SatelliteObject satellite1, satellite2, satellite3;
	private ObjShape dolS, linxS, linyS, linzS, sphS, cubS, torS, tunS;
	public TextureImage doltx, metalIdle, metalDisarmable, metalDisarmed, metalIdle2, metalDisarmable2, metalDisarmed2, metalIdle3, metalDisarmable3, metalDisarmed3, detonation, tuntx;
	private Light light1, light2, light3;

	public MyGame() { super(); }

	public static void main(String[] args)
	{	MyGame game = new MyGame();
		engine = new Engine(game);
		game.initializeSystem();
		game.game_loop();
	}

	@Override
	public void loadShapes()
	{	
		//player, tunnel, and satellites
		dolS = new ImportedModel("dolphinHighPoly.obj");
		sphS = new Sphere();
		cubS= new Cube();
		torS = new Torus();
		tunS = new ManualTunnel();

		//orgin lines
		linxS = new Line(new Vector3f(0f,0f,0f), new Vector3f(3f,0f,0f));
		linyS = new Line(new Vector3f(0f,0f,0f), new Vector3f(0f,3f,0f));
		linzS = new Line(new Vector3f(0f,0f,0f), new Vector3f(0f,0f,-3f));
	}

	@Override
	public void loadTextures()
	{	doltx = new TextureImage("Dolphin_HighPolyUV.png");
		tuntx = new TextureImage("tunnel.jpg");
		detonation = new TextureImage("detonation.jpg");

		metalIdle = new TextureImage("metal_idle.jpg");
		metalDisarmable = new TextureImage("metal_disarmable.jpg");
		metalDisarmed = new TextureImage("metal_disarmed.jpg");

		metalIdle2 = new TextureImage("metal2_idle.jpg");
		metalDisarmable2 = new TextureImage("metal2_disarmable.jpg");
		metalDisarmed2 = new TextureImage("metal2_disarmed.jpg");

		metalIdle3 = new TextureImage("metal3_idle.jpg");
		metalDisarmable3 = new TextureImage("metal3_disarmable.jpg");
		metalDisarmed3 = new TextureImage("metal3_disarmed.jpg");
	}

	@Override
	public void buildObjects()
	{	
		Matrix4f initialTranslation, initialScale;
		// build dolphin in the center of the window
		avatar = new Player(GameObject.root(), dolS, doltx);
		initialTranslation = (new Matrix4f()).translation(0,0,0);
		initialScale = (new Matrix4f()).scaling(3.0f);
		avatar.setLocalTranslation(initialTranslation);
		avatar.setLocalScale(initialScale);
		//initialRotation 
		avatar.setLocalRotation((new Matrix4f()).rotationY((float)java.lang.Math.toRadians(135.0f)));
		
		//build all 3 satellites
		satellite1 = new SatelliteObject(GameObject.root(), sphS, metalIdle);
		initialTranslation = (new Matrix4f()).translation(24,5,-60);
		initialScale = (new Matrix4f()).scaling(1.5f);
		satellite1.setLocalTranslation(initialTranslation);
		satellite1.setLocalScale(initialScale);

		satellite2 = new SatelliteObject(GameObject.root(), torS, metalIdle2);
		initialTranslation = (new Matrix4f()).translation(40,1,-20);
		initialScale = (new Matrix4f()).scaling(1.8f);
		satellite2.setLocalTranslation(initialTranslation);
		satellite2.setLocalScale(initialScale);
		satellite2.setLocalRotation((new Matrix4f()).rotateZ((float)java.lang.Math.toRadians(-45.0f)).rotateX((float)java.lang.Math.toRadians(-45.0f)));

		satellite3 = new SatelliteObject(GameObject.root(), cubS, metalIdle3);
		initialTranslation = (new Matrix4f()).translation(53,-4,-48);
		initialScale = (new Matrix4f()).scaling(1.0f);
		satellite3.setLocalTranslation(initialTranslation);
		satellite3.setLocalScale(initialScale);
		
		// build manual tunnel
		manualTunnel = new GameObject(GameObject.root(), tunS, tuntx);
		initialTranslation = (new Matrix4f()).translation(8,0,-8);
		manualTunnel.setLocalTranslation(initialTranslation);
		manualTunnel.setLocalRotation((new Matrix4f()).rotateY((float)java.lang.Math.toRadians(-45.0f)).rotateX((float)java.lang.Math.toRadians(90.0f)));
		manualTunnel.setLocalScale(new Matrix4f().scaling(2.8f, 14.0f, 2.8f));
		manualTunnel.getRenderStates().hasLighting(true);


		// add X,Y,Z axes
		x = new GameObject(GameObject.root(), linxS);
		y = new GameObject(GameObject.root(), linyS);
		z = new GameObject(GameObject.root(), linzS);
		//set lines color (R,G,B)
		(x.getRenderStates()).setColor(new Vector3f(1f,0f,0f));
		(y.getRenderStates()).setColor(new Vector3f(0f,1f,0f));
		(z.getRenderStates()).setColor(new Vector3f(0f,0f,1f));
		//makes lines longer
		x.setLocalScale(new Matrix4f().scaling(10.0f, 1.0f, 1.0f));
		y.setLocalScale(new Matrix4f().scaling(1.0f, 10.0f, 1.0f));
		z.setLocalScale(new Matrix4f().scaling(1.0f, 1.0f, 10.0f));
	}

	@Override
	public void initializeLights()
	{	
		Light.setGlobalAmbient(0.5f, 0.5f, 0.5f);

		//satellite lights
		Vector3f up = new Vector3f(0,1,0);

		//red light
		light1 = new Light();
		Vector3f satellite1Loc = new Vector3f(satellite1.getWorldLocation());
		light1.setLocation(satellite1Loc.add((up).mul(10.0f)));
		light1.setDiffuse(1.0f, 0.0f, 0.0f); 
		light1.setSpecular(0.2f, 0.0f, 0.0f);
		light1.setType(LightType.SPOTLIGHT);
		light1.setDirection(new Vector3f(0,-1,0));

		//green light
		light2 = new Light();
		Vector3f satellite2Loc = new Vector3f(satellite2.getWorldLocation());
		light2.setLocation(satellite2Loc.add((up).mul(1f)));
		light2.setDiffuse(0.0f, 1.0f, 0.0f); 
		light2.setSpecular(0.0f, 0.2f, 0.0f);
		light2.setType(LightType.SPOTLIGHT);
		light2.setDirection(new Vector3f(0,-1,0));

		//blue light
		light3 = new Light();
		Vector3f satellite3Loc = new Vector3f(satellite3.getWorldLocation());
		light3.setLocation(satellite3Loc.add((up).mul(1f)));
		light3.setDiffuse(0.0f, 0.0f, 1.0f); 
		light3.setSpecular(0.0f, 0.0f, 0.2f);
		light3.setType(LightType.SPOTLIGHT);
		light3.setDirection(new Vector3f(0,-1,0));

		//add lights to scene
		(engine.getSceneGraph()).addLight(light1);
		(engine.getSceneGraph()).addLight(light2);
		(engine.getSceneGraph()).addLight(light3);
	}

	@Override
	public void initializeGame()
	{	
		gameMessage = "";
		startTime = System.currentTimeMillis();
		rideMode = true;

		// Initialize InputManager
		im = engine.getInputManager();
		(engine.getRenderSystem()).setWindowDimensions(1900,1000);

		//INPUTS
		KeyboardInput keyboardInput = new KeyboardInput(this);
		ControllerInput controllerInput = new ControllerInput(this);

		//MOVEMENT FOR KEYBOARD
		net.java.games.input.Component.Identifier.Key[] movementKeys = {
			net.java.games.input.Component.Identifier.Key.W,
			net.java.games.input.Component.Identifier.Key.S,
			net.java.games.input.Component.Identifier.Key.A,
			net.java.games.input.Component.Identifier.Key.D,
			net.java.games.input.Component.Identifier.Key.UP,
			net.java.games.input.Component.Identifier.Key.DOWN
		};
		for (net.java.games.input.Component.Identifier.Key key : movementKeys) {
			im.associateActionWithAllKeyboards(key, keyboardInput, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		}
		//Other controls for keyboard
		im.associateActionWithAllKeyboards(
		net.java.games.input.Component.Identifier.Key.SPACE, keyboardInput,
		InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		im.associateActionWithAllKeyboards(
		net.java.games.input.Component.Identifier.Key.R, keyboardInput,
		InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

		//CONTROLLER inputs
		net.java.games.input.Component.Identifier.Axis[] movementAxes = {
			net.java.games.input.Component.Identifier.Axis.X, // Left stick X axis
			net.java.games.input.Component.Identifier.Axis.Y, // Left stick Y axis
			net.java.games.input.Component.Identifier.Axis.RX, // Right stick X axis
			net.java.games.input.Component.Identifier.Axis.RY, // Right stick Y axis
		};
		for (net.java.games.input.Component.Identifier.Axis axis : movementAxes) {
			im.associateActionWithAllGamepads(axis, controllerInput, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		}
		im.associateActionWithAllGamepads(
			net.java.games.input.Component.Identifier.Button._1, controllerInput,
			InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
	}

	@Override
	public void update()
	{	
		Vector3f loc, fwd, up, right;

		//keeps track of elapsed and current time
		currFrameTime = (System.currentTimeMillis() - startTime)/1000;
		elapsTime = currFrameTime - lastFrameTime;
		timeScale = (float)(elapsTime * (1000/6));
		lastFrameTime = currFrameTime;

		// build and set HUD for score and gameMessage
		String scoreStr = Integer.toString(score);
		String dispScoreStr = "Score = " + scoreStr;
		Vector3f hud1Color = new Vector3f(1,0,0);
		Vector3f hud2Color = new Vector3f(0,0,1);
		(engine.getHUDmanager()).setHUD1(gameMessage, hud1Color, 650, 910);
		(engine.getHUDmanager()).setHUD2(dispScoreStr, hud2Color, 100, 15);

		//camera follows, "rides" dolphin
		if(rideMode){
			cam = (engine.getRenderSystem().getViewport("MAIN").getCamera());
			loc = avatar.getWorldLocation();
			fwd = avatar.getWorldForwardVector();
			up = avatar.getWorldUpVector();
			right = avatar.getWorldRightVector();
			cam.setU(right);
			cam.setV(up);
			cam.setN(fwd);
			cam.setLocation(loc.add(up.mul(1.3f)).add(fwd.mul(-2.5f)));
		}

		//end game if score reaches 300 (all satellites disarmed)
		if(score >= 300){
			gameOver();
		}

		//constantly rotates satellites
		manualTunnel.yaw(0.015f * timeScale, this);
		satellite1.yaw(0.01f * timeScale, this);
		satellite2.yaw(0.005f * timeScale, this);
		satellite3.yaw(0.008f * timeScale, this);
		satellite3.pitch(0.002f * timeScale, this);

		satellite1.checkDistances(this, metalIdle, metalDisarmable, metalDisarmed);
		satellite2.checkDistances(this, metalIdle2, metalDisarmable2, metalDisarmed2);
		satellite3.checkDistances(this, metalIdle3, metalDisarmable3, metalDisarmed3);

		//updates inputs
		im.update((float)elapsTime);
	}

	// Returns the avatar object
	public Player getAvatar() {
		return avatar;
	}

	public void gameOver() {
		gameOver = true;
		if(score >= 300){
			gameMessage = "Victory: All Satellites Disarmed! Press R to Play Again!";
		}
		else {
			gameMessage = "Game Over: Satellite exploded! Press R to Play Again!";
		}
	}

	public void restartGame() {

		// Reset player position
		Matrix4f initialTranslation = (new Matrix4f()).translation(0, 0, 0);
		avatar.setLocalTranslation(initialTranslation);
		avatar.setLocalRotation((new Matrix4f()).rotationY((float) Math.toRadians(135.0f)));
		
		// Reset score and game state
		score = 0;
		gameOver = false;
		rideMode = true;
		
		// Reset satellite textures
		satellite1.resetTexture(metalIdle);
		satellite2.resetTexture(metalIdle2);
		satellite3.resetTexture(metalIdle3);
		
		// Reset game timer
		startTime = System.currentTimeMillis();
		lastFrameTime = 0;
		currFrameTime = 0;
	
		System.out.println("Game restarted!");
	}
}