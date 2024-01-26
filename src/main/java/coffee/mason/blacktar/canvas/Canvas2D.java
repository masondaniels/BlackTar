package coffee.mason.blacktar.canvas;

import org.teavm.jso.canvas.CanvasRenderingContext2D;

public abstract class Canvas2D extends Canvas {
	
	private CanvasRenderingContext2D ctx;

	public Canvas2D(boolean fullscreen) {
		super("2d", fullscreen);
		ctx = (CanvasRenderingContext2D) getContext();
		setup();
	}

	public CanvasRenderingContext2D getCtx() {
		return ctx;
	}

}