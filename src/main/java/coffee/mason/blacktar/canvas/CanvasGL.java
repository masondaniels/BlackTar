package coffee.mason.blacktar.canvas;

import org.teavm.jso.canvas.CanvasRenderingContext2D;

import coffee.mason.blacktar.canvas.webgl.WebGLContext;

public abstract class CanvasGL extends Canvas {

	private WebGLContext ctx;
	
	public CanvasGL(boolean fullscreen) {
		super("webgl", fullscreen);
		ctx = (WebGLContext) getContext();
		update();
	}

	public WebGLContext getCtx() {
		return ctx;
	}

}
