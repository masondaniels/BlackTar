package coffee.mason.blacktar.canvas;

import coffee.mason.blacktar.canvas.webgl.WebGLContext;

public abstract class CanvasGL extends Canvas {

	protected WebGLContext gl;

	public CanvasGL(boolean fullscreen) {
		super("webgl", fullscreen);
		gl = (WebGLContext) getContext();
		setup();
	}

	public WebGLContext gl() {
		return gl;
	}

}