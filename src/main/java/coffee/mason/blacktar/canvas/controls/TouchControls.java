package coffee.mason.blacktar.canvas.controls;

import org.teavm.jso.browser.Window;
import coffee.mason.blacktar.canvas.Canvas;

public abstract interface TouchControls extends TouchMethodsInterface {

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
	
	public static void register(TouchControls controls) {
		System.out.println("Registered Touch Events");
//		Window.current().getStyle().setProperty("touch-action", "none"); // Cancels early touchcancel firing.
		
		Window.current().addEventListener("pointerdown", (e) -> {
			controls.handleTouchStart(e);
		}, false);
		Window.current().addEventListener("pointermove", (e) -> {
			controls.handleTouchMove(e);
		}, false);
		Window.current().addEventListener("pointerup", (e) -> {
			controls.handleTouchEnd(e);
		}, false);
		Window.current().addEventListener("pointercancel", (e) -> {
			controls.handleTouchCancel(e);
		}, false);
		Window.current().addEventListener("pointerout", (e) -> {
			controls.handleTouchOut(e);
		}, false);
	}
	
}
