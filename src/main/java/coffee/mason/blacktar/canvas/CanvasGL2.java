package coffee.mason.blacktar.canvas;

import coffee.mason.blacktar.canvas.webgl.WebGLContext2;

public abstract class CanvasGL2 extends Canvas {

	protected WebGLContext2 gl;

	public CanvasGL2(boolean fullscreen) {
		super("webgl2", fullscreen);
		gl = (WebGLContext2) getContext();
		setup();
	}

	public WebGLContext2 gl() {
		return gl;
	}

}