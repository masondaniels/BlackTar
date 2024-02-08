package coffee.mason.blacktar.canvas.controls;

import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.MouseEvent;

import coffee.mason.blacktar.canvas.Canvas;

public abstract interface TouchControls {

	public abstract void handleTouchStart(Event e);
	public abstract void handleTouchMove(Event e);
	public abstract void handleTouchEnd(Event e);
	public abstract void handleTouchCancel(Event e);
	public abstract void handleTouchOut(Event e);
	
	public static void register(TouchControls controls, Canvas canvas) {
		System.out.println("Registered Touch Events");
		canvas.getCanvas().getStyle().setProperty("touch-action", "none"); // Cancels early touchcancel firing.
		
		canvas.getCanvas().addEventListener("pointerdown", (e) -> {
			controls.handleTouchStart(e);
		}, false);
		canvas.getCanvas().addEventListener("pointermove", (e) -> {
			controls.handleTouchMove(e);
		}, false);
		canvas.getCanvas().addEventListener("pointerup", (e) -> {
			controls.handleTouchEnd(e);
		}, false);
		canvas.getCanvas().addEventListener("pointercancel", (e) -> {
			controls.handleTouchCancel(e);
		}, false);
		canvas.getCanvas().addEventListener("pointerout", (e) -> {
			controls.handleTouchOut(e);
		}, false);
	}
	
}
