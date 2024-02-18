package coffee.mason.blacktar.canvas.controls.impl;

import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.MouseEvent;

import coffee.mason.blacktar.canvas.controls.TouchControls;
import coffee.mason.blacktar.canvas.webgl.WebGLContext;
import coffee.mason.blacktar.component.Updatable;
import coffee.mason.blacktar.html.StyledButton;
import coffee.mason.blacktar.html.StyledGrid;
import coffee.mason.blacktar.html.impl.StyledButtonHeld;
import coffee.mason.blacktar.linear.Vec3;

public class MCPETouchControls implements TouchControls, Updatable {

	private Camera camera;
	private int viewUniformLocation;
	private WebGLContext gl;

	private float speed = 0.3f;

	private StyledButtonHeld wb;
	private StyledButtonHeld ab;
	private StyledButtonHeld sb;
	private StyledButtonHeld db;

	public MCPETouchControls(WebGLContext gl, Camera camera, int viewUniformLocation) {
		this.camera = camera;
		this.gl = gl;
		this.viewUniformLocation = viewUniformLocation;

		wb = new StyledButtonHeld("W") {

			@Override
			protected void onHold() {
				camera.moveForward(speed);
				updateCamera();
			}

		};

		ab = new StyledButtonHeld("A") {

			@Override
			protected void onHold() {
				camera.strafeLeft(speed);
				updateCamera();
			}
		};

		sb = new StyledButtonHeld("S") {

			@Override
			protected void onHold() {
				camera.moveBackwards(speed);
				updateCamera();
			}
		};

		db = new StyledButtonHeld("D") {

			@Override
			protected void onHold() {
				camera.strafeRight(speed);
				updateCamera();
			}
		};

		StyledGrid grid = new StyledGrid(null, wb.getButton(), null, ab.getButton(), sb.getButton(), db.getButton());

		grid.addToDocument();

//		wb.setZIndex(3);
//		ab.setZIndex(3);
//		sb.setZIndex(3);
//		db.setZIndex(3);

	}

	private void updateCamera() {
		camera.updateViewDirection();
		gl.uniformMatrix4fv(viewUniformLocation, false, camera.getViewMatrix().getArray());
	}

	private int xLast = -1;
	private int yLast = -1;

	private boolean touching; // is touching

	@Override
	public void handleTouchStart(Event e) {
		MouseEvent me = (MouseEvent) e;
		touching = true;

	}

	@Override
	public void handleTouchMove(Event e) {
		MouseEvent me = (MouseEvent) e;

		if (touching) {
			if (!(xLast == -1 && yLast == -1)) {
				float xD = (me.getClientX() - xLast);
				float yD = (me.getClientY() - yLast);

				Vec3 n = new Vec3(xD, yD, 0f).normalize();
				camera.setYaw(camera.getYaw() - n.getValue(0)); // xD
				camera.setPitch(camera.getPitch() - n.getValue(1)); // yD
				updateCamera();

				// calculate delta
			}

			xLast = me.getClientX();
			yLast = me.getClientY();
		}
	}

	@Override
	public void handleTouchEnd(Event e) {
		MouseEvent me = (MouseEvent) e;
		touching = false;
	}

	@Override
	public void handleTouchCancel(Event e) {
		MouseEvent me = (MouseEvent) e;
		touching = false;
	}

	@Override
	public void handleTouchOut(Event e) {
		MouseEvent me = (MouseEvent) e;
		touching = false;
	}

	@Override
	public void update() {
		wb.update();
		ab.update();
		sb.update();
		db.update();

	}

}
