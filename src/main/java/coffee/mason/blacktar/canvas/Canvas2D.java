package coffee.mason.blacktar.canvas;

import org.teavm.jso.browser.Window;
import org.teavm.jso.canvas.CanvasRenderingContext2D;

public abstract class Canvas2D extends Canvas {
	private CanvasRenderingContext2D ctx;

	public Canvas2D(boolean fullscreen) {
		super("2d", fullscreen);
		ctx = (CanvasRenderingContext2D) getCanvas().getContext(getContextType());
		loadBeforeAnimation();
		resizeCanvas(getCanvas());
		if (fullscreen) {
			Window.current().addEventListener("resize", (e) -> {
				resizeCanvas(getCanvas());
			});
		}
		requestAnimationFrame();
		loadAfterAnimation();
	}

	public CanvasRenderingContext2D getCtx() {
		return ctx;
	}

}