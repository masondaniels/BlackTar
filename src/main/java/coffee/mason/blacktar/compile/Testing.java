package coffee.mason.blacktar.compile;

import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.dom.html.HTMLDocument;

import coffee.mason.blacktar.canvas.Canvas2D;
import coffee.mason.blacktar.canvas.CanvasGL;
import coffee.mason.blacktar.util.JavaScriptUtil;

public class Testing {

	
	private static HTMLDocument document;
	
	static {
		document = HTMLDocument.current();
		
	}
	
	public static void main(String[] args) {
		engine3dTest();
	}
	

	private static void engine3dTest() {
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
				getCtx().fillText("Refresh count / 60: " + getRefreshCount()/60f, 10, 60);
			}
		};
		
		CanvasGL gl = new CanvasGL(true) {
			
			@Override
			public void update() {
				// TODO Auto-generated method stub
				
				System.out.println(((JSNumber) (getCtx().getDrawingBufferWidth())).floatValue());
				
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
				// TODO Auto-generated method stub
				
				
			}
		};
		
		document.getBody().appendChild(c.getCanvas());
		document.getBody().appendChild(gl.getCanvas());
	}
	
}
