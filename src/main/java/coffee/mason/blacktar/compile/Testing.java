package coffee.mason.blacktar.compile;

import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.dom.html.HTMLDocument;

import coffee.mason.blacktar.canvas.Canvas2D;
import coffee.mason.blacktar.canvas.CanvasGL;
import coffee.mason.blacktar.canvas.webgl.GL;
import coffee.mason.blacktar.canvas.webgl.WebGLContext;
import coffee.mason.blacktar.util.JavaScriptUtil;

public class Testing {

	private static HTMLDocument document;

	static {
		document = HTMLDocument.current();

	}

	public static void main(String[] args) {
		canvasGlTest();
	}

	private static void canvasGlTest() {
		CanvasGL gl = new CanvasGL(true) {

			@Override
			public void update() {
				// TODO Auto-generated method stub

			}

			@Override
			public void loadBeforeAnimation() {
				// TODO Auto-generated method stub

			}

			@Override
			public void loadAfterAnimation() {
				// TODO Auto-generated method stub

			}

			boolean flipped;
			@Override
			public void draw() {

				System.out.println(getCtx().getDrawingBufferWidth().floatValue() + " "
						+ getCtx().getDrawingBufferHeight().floatValue());

				GL.viewport(gl(), 0, 0, (float) getWidth(), (float) getHeight());
				
				if (getRefreshCount() % 60 == 0) {
					flipped = !flipped;
				}
				float f = flipped ? 0f : 1f;
				GL.clearColor(gl(), f, f, f, 1f);
				GL.clear(gl(), GL.COLOR_BUFFER_BIT);
				
			}

			@Override
			public void onCanvasResize() {
				// TODO Auto-generated method stub

			}
		};
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
				// TODO Auto-generated method stub

			}

			@Override
			public void loadAfterAnimation() {
				// TODO Auto-generated method stub

			}

			@Override
			public void draw() {
				getCtx().setFont("30px Arial");
				getCtx().fillText("Refresh count: " + getRefreshCount(), 10, 30);
				getCtx().fillText("Refresh count / 60: " + getRefreshCount() / 60f, 10, 60);
			}

			@Override
			public void onCanvasResize() {
				// TODO Auto-generated method stub

			}
		};

	}

}
