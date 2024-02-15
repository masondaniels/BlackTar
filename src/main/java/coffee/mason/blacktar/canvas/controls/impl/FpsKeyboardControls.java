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

	private Camera camera = new Camera();

	public FpsKeyboardControls(WebGLContext gl, int viewUniformLocation) {
		this.gl = gl;
		this.viewUniformLocation = viewUniformLocation;
		camera.setPosZ(3f);
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

	@Override
	public void update() {

		camera.setLookX(camera.getPosX());
		camera.setLookY(camera.getPosY());
		camera.setLookZ(camera.getLookZ() - 1f);

		if (keysDown.containsKey("w")) {
			camera.setPosZ(camera.getPosZ() - 0.3f);
		}
		if (keysDown.containsKey("a")) {
			camera.setPosX(camera.getPosX() + 0.3f);
		}
		if (keysDown.containsKey("s")) {
			camera.setPosZ(camera.getPosZ() + 0.3f);
		}
		if (keysDown.containsKey("d")) {
			camera.setPosX(camera.getPosX() - 0.3f);
		}
		if (keysDown.containsKey(" ")) {
			camera.setPosY(camera.getPosY() + 0.3f);
		}
		if (keysDown.containsKey("shift")) {
			camera.setPosY(camera.getPosY() - 0.3f);
		}

		gl.uniformMatrix4fv(viewUniformLocation, false, camera.getViewMatrix().getArray());
	}

}
