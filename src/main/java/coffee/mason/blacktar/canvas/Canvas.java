package coffee.mason.blacktar.canvas;

import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;

import coffee.mason.blacktar.component.Updatable;
import coffee.mason.blacktar.util.JavaScriptUtil;
import coffee.mason.blacktar.util.RandomUtil;

public abstract class Canvas implements Updatable {

	protected HTMLCanvasElement canvas;
	private String contextType;
	private double width;
	private double height;
	private boolean fullscreen;
	private int refreshCount; // incremented each frame

	public Canvas(String contextType, boolean fullscreen) {
		this.fullscreen = fullscreen;
		this.contextType = contextType;
		HTMLDocument document = HTMLDocument.current();

		canvas = (HTMLCanvasElement) document.createElement("canvas");
		canvas.setAttribute("id", contextType + "-" + RandomUtil.nextInt(100000));
		document.getBody().appendChild(getCanvas());

	}

	private long timeLast = -1; // in seconds
	private int fpsLast;
	private int fps;

	public void requestAnimationFrame() {
		refreshCount = getRefreshCount() + 1;
		if (timeLast == -1) {
			fps = 0;
			timeLast = System.currentTimeMillis();
		}
		Window.requestAnimationFrame(timestamp -> {
			if (System.currentTimeMillis()-timeLast > 1000) {
				fpsLast = fps;
				fps = 0;
				timeLast = -1;
			} else {
				fps++;
			}
			if (dpi == -1) {
				onDPI();
			}
			update();
			draw();
			requestAnimationFrame();
		});
	}

	// Already implemented -- called whenever there is a resize
	public void resizeCanvas(HTMLCanvasElement canvas) {
		if (fullscreen) {
			canvas.setWidth(Window.current().getInnerWidth());
			canvas.setHeight(Window.current().getInnerHeight());
			setWidth(Window.current().getInnerWidth());
			setHeight(Window.current().getInnerHeight());
		}
		onDPI();
		onCanvasResize(); // Calls special methods (in implemented classes) on canvas resize if there are
							// any
		draw();
	}

	public boolean isFullscreen() {
		return fullscreen;
	}

	public abstract void loadBeforeAnimation();

	public abstract void loadAfterAnimation();

	public abstract void draw();

	public abstract void onCanvasResize();
	
	private double dpi = -1;
	
	public double getDPI() {
		return dpi;
	}
	
	private void onDPI() {
		if (dpi == -1) {
			dpi = ((JSNumber) JavaScriptUtil.eval("window.devicePixelRatio || 1")).doubleValue();
		}

		if (isFullscreen()) {
			canvas.setWidth((int) (Window.current().getInnerWidth() * dpi));
			canvas.setHeight((int) (Window.current().getInnerHeight() * dpi));
			setWidth(Window.current().getInnerWidth() * dpi);
			setHeight(Window.current().getInnerHeight() * dpi);
			canvas.getStyle().setProperty("width", Window.current().getInnerWidth() + "px");
			canvas.getStyle().setProperty("height", Window.current().getInnerHeight() + "px");
		}
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public String getContextType() {
		return contextType;
	}

	public HTMLCanvasElement getCanvas() {
		return canvas;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public void setZIndex(int value) {
		canvas.getStyle().setProperty("position", "absolute");
		canvas.getStyle().setProperty("z-index", value + "");
	}

	// Get how many times the canvas has been drawn
	public int getRefreshCount() {
		return refreshCount;
	}

	// Returns context object
	public JSObject getContext() {
		return getCanvas().getContext(getContextType());
	}

	// Call to set up canvas logic. Includes fullscreen logic.
	public void setup() {
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
	
	public int getFps() {
		return fpsLast;//
	}

}