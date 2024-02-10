package coffee.mason.blacktar.canvas.impl;

import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.MouseEvent;

import coffee.mason.blacktar.canvas.Canvas2D;
import coffee.mason.blacktar.canvas.controls.PointerEvent;
import coffee.mason.blacktar.canvas.controls.TouchControls;
import coffee.mason.blacktar.util.JavaScriptUtil;

// Class which tests touch controls
public class CanvasTouchImpl extends Canvas2D implements TouchControls {

	public CanvasTouchImpl(boolean fullscreen) {
		super(fullscreen);
		setZIndex(5);
		TouchControls.register(this, this);
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
	public void update() {
		if (dpi == -1) {
			onResize();
		}
	}

	private double pressure;
	private double lPressure = 99999; // lowest pressure
	private double hPressure = -99999; // highest pressure

	@Override
	public void draw() {
		getCtx().setFillStyle("white");
		getCtx().clearRect(0, 0, 40, 60);
		getCtx().fillText(((int) (pressure)) + "", 10, 20);
		getCtx().fillText(((int) (tapCount)) + "", 10, 35);
	}

	private double dpi = -1;

	@Override
	public void onCanvasResize() {
		onResize();
	}

	private void onResize() {
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

	private boolean isTouched;
	private boolean mode; // mode false = pen, mode true = erase
	private int tapCount = 0;
	private int moves = 0;

	@Override
	public void handleTouchStart(Event e) {
		if (tapCount == 2) {
			mode = !mode;
		}
		// Two touches
//		if (((TouchEvent) e).getTouches().getLength() == 1) {
//			mode = !mode;
//		}

		System.out.println("Touch start");
		isTouched = true;
	}

	private float lastX;
	private float lastY;

	private double lpn; // last pressure normalized
	private double pn; // pressure normalized

	@Override
	public void handleTouchMove(Event e) {
		if (isTouched) {
			moves++;
			if (moves > 10) {
				tapCount = 0;
			}
			MouseEvent me = (MouseEvent) e;
			System.out.println("Touch move " + me.getClientX() + ", " + me.getClientY());

			pressure = ((PointerEvent) e).getPressure() * 100d;
			lPressure = Math.min(lPressure, pressure);
			hPressure = Math.max(hPressure, pressure);

			if (!(lastX == 0 && lastY == 0)) {
				getCtx().setStrokeStyle("white");
				lpn = pn;

				pn = (pressure - lPressure) / (hPressure - lPressure); // pressure normalized
				if (lpn != 0) {
					getCtx().setLineWidth(((pn + lpn) / 2d * 10d * dpi));
				} else {
					getCtx().setLineWidth((pn * 10d * dpi));
				}

				if (!mode) {
					getCtx().beginPath();
					getCtx().moveTo(lastX * dpi, lastY * dpi);
					getCtx().lineTo(me.getClientX() * dpi, me.getClientY() * dpi);
					getCtx().closePath();
					getCtx().stroke();
				} else {
					float r = (float) (10 * dpi);
					getCtx().clearRect(((me.getClientX() - r)) * dpi, (me.getClientY() - r) * dpi, (r * 2 * dpi),
							(r * 2 * dpi));
				}
			}
			lastX = me.getClientX();
			lastY = me.getClientY();
		}

	}

	@Override
	public void handleTouchEnd(Event e) {
		if (moves < 10) {
			tapCount++;
		}
		moves = 0;
		isTouched = false;
		lastX = 0;
		lastY = 0;
		System.out.println("Touch end");
	}

	@Override
	public void handleTouchCancel(Event e) {
		if (moves < 10) {
			tapCount++;
		}
		moves = 0;
		isTouched = false;
		lastX = 0;
		lastY = 0;
		System.out.println("Touch cancel");

	}

	@Override
	public void handleTouchOut(Event e) {
		isTouched = false;
		System.out.println("Touch out");
	}

}
