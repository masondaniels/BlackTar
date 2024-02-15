package coffee.mason.blacktar.canvas.controls.impl;

import java.util.HashMap;

import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.KeyboardEvent;

import coffee.mason.blacktar.canvas.controls.KeyboardControls;
import coffee.mason.blacktar.canvas.webgl.WebGLContext;
import coffee.mason.blacktar.component.Updatable;
import coffee.mason.blacktar.linear.Vec3;

public class FpsKeyboardControls implements KeyboardControls, Updatable {

	private WebGLContext gl;
	private int viewUniformLocation;

	private Camera camera = new Camera();

	public FpsKeyboardControls(WebGLContext gl, int viewUniformLocation) {
		this.gl = gl;
		this.viewUniformLocation = viewUniformLocation;
		camera.setPosZ(3f);
//		camera.setLookX(camera.getPosX());
//		camera.setLookY(camera.getPosY());
//		camera.setLookZ(camera.getPosZ() - 1f);
		gl.uniformMatrix4fv(viewUniformLocation, false, camera.getViewMatrix().getArray());
		KeyboardControls.register(this);
	}

	@Override
	public void handleKeyPress(Event e) {
		int value = ((KeyboardEvent) e).getCharCode();
		System.out.println("Key pressed: " + value);

	}

	private static HashMap<String, Object> keysDown = new HashMap<String, Object>();

	@Override
	public void handleKeyDown(Event e) {
		System.out.println("Key down: " + ((KeyboardEvent) e).getKey());
		keysDown.put(((KeyboardEvent) e).getKey().toLowerCase(), null);
		System.out.println("Keys down: " + keysDown.size());
	}

	@Override
	public void handleKeyUp(Event e) {
		keysDown.remove(((KeyboardEvent) e).getKey().toLowerCase());
	}

	private static float moveSpeed = 0.3f;

	private boolean didMove;

	@Override
	public void update() {

		boolean didMove = false;

		// Implement this so it moves pitch relative to direction
		if (keysDown.containsKey("arrowleft")) {
			didMove = true;
			camera.rotate(0, moveSpeed, 0);
		}
		if (keysDown.containsKey("arrowright")) {
			didMove = true;
			camera.rotate(0, -moveSpeed, 0);
		}
		if (keysDown.containsKey("arrowup")) {
			didMove = true;
			camera.rotate(moveSpeed, 0, 0);
		}
		if (keysDown.containsKey("arrowdown")) {
			didMove = true;
			camera.rotate(-moveSpeed, 0, 0);
		}

		// Assuming your Camera class has rotate and move methods

		if (keysDown.containsKey("w")) {
		    didMove = true;
		    camera.move(forward.getValue(0) * moveSpeed, forward.getValue(1) * moveSpeed, forward.getValue(2) * moveSpeed);
		}
		if (keysDown.containsKey("a")) {
		    didMove = true;
		    camera.move(-right.getValue(0) * moveSpeed, -right.getValue(1) * moveSpeed, -right.getValue(2) * moveSpeed);
		}
		if (keysDown.containsKey("s")) {
		    didMove = true;
		    camera.move(-forward.getValue(0) * moveSpeed, -forward.getValue(1) * moveSpeed, -forward.getValue(2) * moveSpeed);
		}
		if (keysDown.containsKey("d")) {
		    didMove = true;
		    camera.move(right.getValue(0) * moveSpeed, right.getValue(1) * moveSpeed, right.getValue(2) * moveSpeed);
		}
		if (keysDown.containsKey(" ")) {
		    didMove = true;
		    camera.move(0, moveSpeed, 0);
		}
		if (keysDown.containsKey("shift")) {
		    didMove = true;
		    camera.move(0, -moveSpeed, 0);
		}


		if (didMove) {
			gl.uniformMatrix4fv(viewUniformLocation, false, camera.getViewMatrix().getArray());
		}
	}

}
