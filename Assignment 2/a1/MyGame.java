package a1;

import tage.*;
import tage.shapes.*;

import java.lang.Math;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import org.joml.*;

import tage.input.*;
import tage.input.action.*;
import net.java.games.input.*;
import net.java.games.input.Component.Identifier.*;
import tage.Light.LightType;
import tage.nodeControllers.*;

public class MyGame extends VariableFrameRateGame
{
	public static Engine engine;
	private InputManager im;
	public Camera cam, cam2;
	private Viewport leftVp, rightVp;
	public MyCameraOrbit3D cameraOrbit3D;
	public GamepadController gamepadController;
	public KeyboardController keyboardController;
	public MinimapController minimapController;
	public RotationController rotationController;
	public ShakeController shakeController;

	public boolean cameraMode, gameOver;
	public int score;
	public String gameMessage;
	public float timeScale;
	public double startTime, lastFrameTime, currFrameTime, elapsTime;
	public int coreCount;

	public Player avatar;
	public GameObject x, y, z, manualTunnel, groundPlane, satCore1, satCore2, satCore3;
	public SatelliteObject satellite1, satellite2, satellite3;
	private ObjShape dolS, linxS, linyS, linzS, sphS, cubS, torS, tunS, groundPlaneS;
	public TextureImage doltx, metalIdle, metalDisarmable, metalDisarmed, metalIdle2, metalDisarmable2, metalDisarmed2, metalIdle3, metalDisarmable3, metalDisarmed3, detonation, tuntx, ground;
	private Light light1, light2, light3;

	public MyGame() { super(); }

	public static void main(String[] args)
	{	MyGame game = new MyGame();
		engine = new Engine(game);
		game.initializeSystem();
		game.game_loop();
	}

	@Override
	public void createViewports()
	{ 
		(engine.getRenderSystem()).addViewport("LEFT",0,0,1f,1f);
		(engine.getRenderSystem()).addViewport("RIGHT",.75f,.75f,.25f,.25f);

		leftVp = (engine.getRenderSystem()).getViewport("LEFT");
		rightVp = (engine.getRenderSystem()).getViewport("RIGHT");

		Camera leftCamera = leftVp.getCamera();
		Camera rightCamera = rightVp.getCamera();

		rightVp.setHasBorder(true);
		rightVp.setBorderWidth(4);
		rightVp.setBorderColor(0.0f, 0.0f, 1.0f);

		leftCamera.setLocation(new Vector3f(-2,0,2));
		leftCamera.setU(new Vector3f(1,0,0));
		leftCamera.setV(new Vector3f(0,1,0));
		leftCamera.setN(new Vector3f(0,0,-1));

		rightCamera.setLocation(new Vector3f(0,30,0));
		rightCamera.setU(new Vector3f(1,0,0));
		rightCamera.setV(new Vector3f(0,0,-1));
		rightCamera.setN(new Vector3f(0,-1,0));
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
		groundPlaneS = new Plane();

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
		ground = new TextureImage("ground.jpg");

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
		groundPlane = new GameObject(GameObject.root(), groundPlaneS, ground);
		groundPlane.setLocalScale((new Matrix4f()).scaling(1000.0f));
		groundPlane.setLocalTranslation((new Matrix4f()).translation(0,0,0));

		// Enable texture tiling
		groundPlane.getRenderStates().setTiling(1); 
		groundPlane.getRenderStates().setTileFactor(100);

		Matrix4f initialTranslation, initialScale;
		// build dolphin in the center of the window
		avatar = new Player(GameObject.root(), dolS, doltx);
		initialTranslation = (new Matrix4f()).translation(0,1.0f,0);
		initialScale = (new Matrix4f()).scaling(3.0f);
		avatar.setLocalTranslation(initialTranslation);
		avatar.setLocalScale(initialScale);
		//initialRotation 
		avatar.setLocalRotation((new Matrix4f()).rotationY((float)java.lang.Math.toRadians(135.0f)));
		
		//build all 3 satellites
		satellite1 = new SatelliteObject(GameObject.root(), sphS, metalIdle);
		initialTranslation = (new Matrix4f()).translation(24,2.75f,-60);
		initialScale = (new Matrix4f()).scaling(3f);
		satellite1.setLocalTranslation(initialTranslation);
		satellite1.setLocalScale(initialScale);

		satellite2 = new SatelliteObject(GameObject.root(), torS, metalIdle2);
		initialTranslation = (new Matrix4f()).translation(40,2.25f,-20);
		initialScale = (new Matrix4f()).scaling(3.8f);
		satellite2.setLocalTranslation(initialTranslation);
		satellite2.setLocalScale(initialScale);
		satellite2.setLocalRotation((new Matrix4f()).rotateZ((float)java.lang.Math.toRadians(-45.0f)).rotateX((float)java.lang.Math.toRadians(-45.0f)));

		satellite3 = new SatelliteObject(GameObject.root(), cubS, metalIdle3);
		initialTranslation = (new Matrix4f()).translation(53,2.25f,-48);
		initialScale = (new Matrix4f()).scaling(3.3f);
		satellite3.setLocalTranslation(initialTranslation);
		satellite3.setLocalScale(initialScale);
		
		// build manual tunnel
		manualTunnel = new GameObject(GameObject.root(), tunS, tuntx);
		initialTranslation = (new Matrix4f()).translation(8,1.0f,-8);
		manualTunnel.setLocalTranslation(initialTranslation);
		manualTunnel.setLocalRotation((new Matrix4f()).rotateY((float)java.lang.Math.toRadians(-45.0f)).rotateX((float)java.lang.Math.toRadians(90.0f)));
		manualTunnel.setLocalScale(new Matrix4f().scaling(.1f, .5f, .1f));
		manualTunnel.getRenderStates().hasLighting(true);

		satCore1 = new GameObject(GameObject.root(), cubS, detonation);
		initialTranslation = (new Matrix4f()).translation(0,1.2f,0);
		satCore1.setLocalTranslation(initialTranslation);
		satCore1.setLocalScale(new Matrix4f().scaling(0,0,0));
		satCore1.setParent(avatar);
		satCore1.propagateTranslation(true);
		satCore1.propagateScale(false);

		satCore2 = new GameObject(GameObject.root(), cubS, detonation);
		initialTranslation = (new Matrix4f()).translation(0,1.5f,0);
		satCore2.setLocalTranslation(initialTranslation);
		satCore2.setLocalScale(new Matrix4f().scaling(0,0,0));
		satCore2.setParent(avatar);
		satCore2.propagateTranslation(true);
		satCore2.propagateScale(false);

		
		satCore3 = new GameObject(GameObject.root(), cubS, detonation);
		initialTranslation = (new Matrix4f()).translation(0,1.8f,0);
		satCore3.setLocalTranslation(initialTranslation);
		satCore3.setLocalScale(new Matrix4f().scaling(0,0,0));
		satCore3.setParent(avatar);
		satCore3.propagateTranslation(true);
		satCore3.propagateScale(false);


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
		light1.setLocation(satellite1Loc.add((up).mul(15.0f)));
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
		cam = (engine.getRenderSystem().getViewport("LEFT").getCamera());
		cam2 = (engine.getRenderSystem().getViewport("RIGHT").getCamera());

		gameMessage = "";
		startTime = System.currentTimeMillis();

  		im = engine.getInputManager();
		cameraOrbit3D = new MyCameraOrbit3D(cam, avatar,  engine);
		minimapController = new MinimapController(cam2, engine);
		gamepadController = new GamepadController(this);
		keyboardController = new KeyboardController(this);
		rotationController = new RotationController(engine, (new Vector3f(0,1,0)), .001f);
		shakeController = new ShakeController(engine, 0.5f);

		(engine.getSceneGraph()).addNodeController(rotationController);
		(engine.getSceneGraph()).addNodeController(shakeController);

		//constantly rotates satellites
		rotationController.enable();
		shakeController.enable();

	}

	@Override
	public void update()
	{	
		//keeps track of elapsed and current time
		currFrameTime = (System.currentTimeMillis() - startTime)/1000;
		elapsTime = currFrameTime - lastFrameTime;
		timeScale = (float)(elapsTime * (1000/6));
		lastFrameTime = currFrameTime;

		//@@@@ADDED TO JAVADOCS@@@@
		// build and set HUD for score and gameMessage
		float leftVpWidth = leftVp.getActualWidth();
		float leftVpHeight = leftVp.getActualHeight();

		String scoreStr = Integer.toString(score);
		String dispScoreStr = "Score = " + scoreStr;
		String dispPosStr = "Position = (" + (int)(getAvatar().getWorldLocation().x()) + ", "+ (int)(getAvatar().getWorldLocation().y()) + ", " + (int)(getAvatar().getWorldLocation().z()) + ")";

		Vector3f hud1Color = new Vector3f(0,0,1);
		Vector3f hud2Color = new Vector3f(0,1,0);
		Vector3f hud3Color = new Vector3f(1,0,0);

		(engine.getHUDmanager()).setHUD1(gameMessage, hud1Color,(int)(leftVpWidth * 0.42), (int)(leftVpHeight * 0.95));
		(engine.getHUDmanager()).setHUD2(dispScoreStr, hud2Color,(int)(leftVpWidth * 0.05),(int)(leftVpHeight * 0.05));
		(engine.getHUDmanager()).setHUD3(dispPosStr, hud3Color,(int)(leftVpWidth * 0.76), (int)(leftVpHeight * 0.765));

		//end game if score reaches 300 (all satellites disarmed)
		if(score >= 300){
			gameOver();
		}

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
			gameMessage = "Victory: All Satellites Disarmed!";
		}
		else {
			gameMessage = "Game Over: Satellite exploded!";
		}
	}

	public void restartGame() {

		// Reset player position
		Matrix4f initialTranslation = (new Matrix4f()).translation(0, 1, 0);
		avatar.setLocalTranslation(initialTranslation);
		avatar.setLocalRotation((new Matrix4f()).rotationY((float) Math.toRadians(135.0f)));
		
		//reset camera
		cameraOrbit3D.resetPosition();
		
		// Reset score and game state
		score = 0;
		gameOver = false;
		cameraMode = false;
		
		// Reset satellite textures
		satellite1.resetTexture(metalIdle);
		satellite2.resetTexture(metalIdle2);
		satellite3.resetTexture(metalIdle3);
		
		// Reset game timer
		startTime = System.currentTimeMillis();
		lastFrameTime = 0;
		currFrameTime = 0;

		satCore1.setLocalScale(new Matrix4f().scaling(0,0,0));
		satCore2.setLocalScale(new Matrix4f().scaling(0,0,0));
		satCore3.setLocalScale(new Matrix4f().scaling(0,0,0));
	
		System.out.println("Game restarted!");
	}

}