package coffee.mason.blacktar.canvas;

import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;

import coffee.mason.blacktar.util.RandomUtil;

public abstract class Canvas {

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

	public void requestAnimationFrame() {
		refreshCount = getRefreshCount() + 1;
		Window.requestAnimationFrame(timestamp -> {
			update();
			draw();
			requestAnimationFrame();
		});
	}

	public void resizeCanvas(HTMLCanvasElement canvas) {
		if (fullscreen) {
			canvas.setWidth(Window.current().getInnerWidth());
			canvas.setHeight(Window.current().getInnerHeight());
			setWidth(Window.current().getInnerWidth());
			setHeight(Window.current().getInnerHeight());
		}
		draw();
	}

	public boolean isFullscreen() {
		return fullscreen;
	}

	public abstract void loadBeforeAnimation();

	public abstract void loadAfterAnimation();

	public abstract void update();

	public abstract void draw();

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

}