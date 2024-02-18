package coffee.mason.blacktar.canvas.controls.impl;

import java.util.HashMap;

import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.KeyboardEvent;

import coffee.mason.blacktar.canvas.controls.KeyboardControls;
import coffee.mason.blacktar.canvas.webgl.WebGLContext;
import coffee.mason.blacktar.component.Updatable;

public class FpsKeyboardControls implements KeyboardControls, Updatable {

	private WebGLContext gl;
	private int viewUniformLocation;

	private Camera camera;

	public FpsKeyboardControls(Camera camera, WebGLContext gl, int viewUniformLocation) {
		this.gl = gl;
		this.viewUniformLocation = viewUniformLocation;
		this.camera = camera;
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

	private float speed = 0.3f;
	private float angleSpeed = 0.8f;

	private boolean movedPosition;
	private boolean movedEye;

	@Override
	public void update() {

		if (keysDown.containsKey("w")) {
			movedPosition = true;
			camera.moveForward(speed);
		}
		if (keysDown.containsKey("a")) {
			movedPosition = true;
			camera.strafeLeft(speed);
		}
		if (keysDown.containsKey("s")) {
			movedPosition = true;
			camera.moveBackwards(speed);
		}
		if (keysDown.containsKey("d")) {
			movedPosition = true;
			camera.strafeRight(speed);
		}
		if (keysDown.containsKey(" ")) {
			movedPosition = true;
			camera.moveUp(speed);
		}
		if (keysDown.containsKey("shift")) {
			movedPosition = true;
			camera.moveDown(speed);
		}

		if (keysDown.containsKey("arrowright")) {
			movedEye = true;
			camera.setYaw(camera.getYaw() - angleSpeed);
		}

		if (keysDown.containsKey("arrowleft")) {
			movedEye = true;
			camera.setYaw(camera.getYaw() + angleSpeed);
		}

		if (keysDown.containsKey("arrowdown")) {
			movedEye = true;
			camera.setPitch(camera.getPitch() - angleSpeed);
		}

		if (keysDown.containsKey("arrowup")) {
			movedEye = true;
			camera.setPitch(camera.getPitch() + angleSpeed);
		}

		if (movedEye) {
			camera.updateViewDirection();
		}

		if (movedEye || movedPosition) {
			gl.uniformMatrix4fv(viewUniformLocation, false, camera.getViewMatrix().getArray());
			if (movedPosition) {
				movedPosition = false;
			}
			if (movedEye) {
				movedEye = false;
			}
		}
	}
	
}
