package coffee.mason.blacktar.canvas;

import coffee.mason.blacktar.canvas.webgl.WebGLContext;

public abstract class CanvasGL extends Canvas {

	private WebGLContext ctx;

	public CanvasGL(boolean fullscreen) {
		super("webgl", fullscreen);
		ctx = (WebGLContext) getContext();
		setup();
	}

	public WebGLContext getCtx() {
		return ctx;
	}
	
	// Same method but stylistically better.
	// getCtx is kept to make it clear what gl is in this.
	public WebGLContext gl() {
		return getCtx();
	}

}