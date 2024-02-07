package coffee.mason.blacktar.compile;

import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.dom.html.HTMLDocument;

import coffee.mason.blacktar.canvas.Canvas2D;
import coffee.mason.blacktar.canvas.CanvasGL;
import coffee.mason.blacktar.canvas.impl.CanvasGLImpl;
import coffee.mason.blacktar.canvas.webgl.GL;
import coffee.mason.blacktar.canvas.webgl.WebGLContext;
import coffee.mason.blacktar.util.JavaScriptUtil;

public class Testing {

	private static HTMLDocument document;

	static {
		document = HTMLDocument.current();

	}

	public static void main(String[] args) {
//		canvasGlTest();
		canvasGLImplTest();

	}

	private static void canvasGLImplTest() {

		CanvasGLImpl glImpl = new CanvasGLImpl(true);
		glImpl.setZIndex(1);

	}

	private static void canvas2dTest() {
		Canvas2D c = new Canvas2D(true) {

			private double dpi = -1;

			@Override
			public void update() {
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

			@Override
			public void loadBeforeAnimation() {
				setZIndex(3);
			}

			@Override
			public void loadAfterAnimation() {
				// TODO Auto-generated method stub

			}

			@Override
			public void draw() {
				getCtx().setFillStyle("white");
				getCtx().setFont("23px Arial");
				getCtx().fillText("Spinning Triangle", 10, 30);
				getCtx().fillText("Refresh count: " + getRefreshCount(), 10, 55);
				getCtx().fillText("Refresh count / 60: " + getRefreshCount() / 60f, 10, 80);
				getCtx().fillText("Elapsed time: " + (JavaScriptUtil.getElapsed().floatValue() / 1000f), 10, 105);
			}

			@Override
			public void onCanvasResize() {
				// TODO Auto-generated method stub

			}
		};

	}

}
