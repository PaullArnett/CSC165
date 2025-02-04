package myGame;

import tage.*;
import tage.shapes.*;

import java.lang.Math;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import org.joml.*; //only thing vscode doesnt like

public class MyGame extends VariableFrameRateGame
{
	private static Engine engine;

	private boolean paused=true;
	private int counter=0;
	private double lastFrameTime, currFrameTime, elapsTime;

	private GameObject dol, cub;
	private ObjShape dolS, cubS;
	private TextureImage doltx, brick;
	private Light light1;

	public MyGame() { super(); }

	public static void main(String[] args)
	{	MyGame game = new MyGame();
		engine = new Engine(game);
		game.initializeSystem();
		game.game_loop();
	}

	@Override
	public void loadShapes()
	{	dolS = new ImportedModel("dolphinHighPoly.obj");
		cubS = new Cube();
	}

	@Override
	public void loadTextures()
	{	doltx = new TextureImage("Dolphin_HighPolyUV.png");
		brick= new TextureImage("Dolphin_HighPolyUV.png");
	}

	@Override
	public void buildObjects()
	{	Matrix4f initialTranslation, initialScale;

		// build dolphin in the center of the window
		dol = new GameObject(GameObject.root(), dolS, doltx);
		initialTranslation = (new Matrix4f()).translation(0,0,0);
		initialScale = (new Matrix4f()).scaling(3.0f);
		dol.setLocalTranslation(initialTranslation);
		dol.setLocalScale(initialScale);
		
		// build cube at the right of the window
		cub = new GameObject(GameObject.root(), cubS, brick);
		initialTranslation = (new Matrix4f()).translation(3,0,0);
		initialScale = (new Matrix4f()).scaling(0.5f);
		cub.setLocalTranslation(initialTranslation);
		cub.setLocalScale(initialScale);
		
	}

	@Override
	public void initializeLights()
	{	Light.setGlobalAmbient(0.5f, 0.5f, 0.5f);
		light1 = new Light();
		light1.setLocation(new Vector3f(5.0f, 4.0f, 2.0f));
		(engine.getSceneGraph()).addLight(light1);
	}

	@Override
	public void initializeGame()
	{	lastFrameTime = System.currentTimeMillis();
		currFrameTime = System.currentTimeMillis();
		elapsTime = 0.0;
		(engine.getRenderSystem()).setWindowDimensions(1900,1000);

		// ------------- positioning the camera -------------
		(engine.getRenderSystem().getViewport("MAIN").getCamera()).setLocation(new Vector3f(0,0,5));
	}

	@Override
	public void update()
	{	
		Vector3f loc, fwd, up, right, newLocation;
		Camera cam;
		
		// rotate dolphin if not paused
		lastFrameTime = currFrameTime;
		currFrameTime = System.currentTimeMillis();
		if (!paused) elapsTime += (currFrameTime - lastFrameTime) / 1000.0;
		dol.setLocalRotation((new Matrix4f()).rotation((float)elapsTime, 0, 1, 0));

		// build and set HUD
		int elapsTimeSec = Math.round((float)elapsTime);
		String elapsTimeStr = Integer.toString(elapsTimeSec);
		String counterStr = Integer.toString(counter);
		String dispStr1 = "Time = " + elapsTimeStr;
		String dispStr2 = "Keyboard hits = " + counterStr;
		Vector3f hud1Color = new Vector3f(1,0,0);
		Vector3f hud2Color = new Vector3f(0,0,1);
		(engine.getHUDmanager()).setHUD1(dispStr1, hud1Color, 15, 15);
		(engine.getHUDmanager()).setHUD2(dispStr2, hud2Color, 500, 15);

		//rides dolphin
		cam = (engine.getRenderSystem()
		.getViewport("MAIN").getCamera());
		loc = dol.getWorldLocation();
		fwd = dol.getWorldForwardVector();
		up = dol.getWorldUpVector();
		right = dol.getWorldRightVector();
		cam.setU(right);
		cam.setV(up);
		cam.setN(fwd);
		cam.setLocation(loc.add(up.mul(1.3f)).add(fwd.mul(-2.5f)));
	}

@Override
public void keyPressed(KeyEvent e)
	{ 
	Vector3f loc, fwd, up, right, newLocation;

	switch (e.getKeyCode())
		{ 
		case KeyEvent.VK_1:
			paused = !paused;
			break;
		case KeyEvent.VK_2: // move dolphin forward
			fwd = dol.getWorldForwardVector();
			loc = dol.getWorldLocation();
			newLocation = loc.add(fwd.mul(.2f));
			dol.setLocalLocation(newLocation);
			break;
		case KeyEvent.VK_3: // move dolphin backward
			fwd = dol.getWorldForwardVector();
			loc = dol.getWorldLocation();
			newLocation = loc.add(fwd.mul(-.2f));
			dol.setLocalLocation(newLocation);
		break;
		case KeyEvent.VK_4: // view from dolphin
			// to ride the dolphin, copy this code to update()
			break;
		}
		super.keyPressed(e);
	}
}