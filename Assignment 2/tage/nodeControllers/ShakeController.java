package tage.nodeControllers;
import tage.*;

import org.joml.*;

/**
* A ShakeController is a node controller that, when enabled, causes any object
* it is attached to to shake in place.
*/
public class ShakeController extends NodeController
{
	private Engine engine;
	private float shakeDistance = 0.1f;
	private Vector3f fwdShake = new Vector3f(.05f,0,0); 
	private Vector3f bckShake = new Vector3f(-.05f,0,0); 
	private int counter;

	/** Creates a shake controller with distance=0.1f*/
	public ShakeController() { super(); }

	/** Creates a shake controller that shake based on the given distance*/
	public ShakeController(Engine e, float d)
	{	
		super();
		shakeDistance = d;
	}

	
	/** sets the distance when the controller is enabled */
	public void setDistance(float d) { shakeDistance = d; }

	/** This is called automatically by the RenderSystem (via SceneGraph) once per frame
	*   during display().  It is for engine use and should not be called by the application.
	*/
	public void apply(GameObject go)
	{
		Vector3f oldPosition = go.getWorldLocation();
		if (counter < 5) {
			go.setLocalLocation(oldPosition.add(fwdShake));  // Move forward
		}
		else {
			go.setLocalLocation(oldPosition.add(bckShake));  // Move backward
		}
		counter++;
		counter = counter % 10;
	}

}