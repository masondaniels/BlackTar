package coffee.mason.blacktar.canvas.controls;

import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.html.HTMLElement;

public interface TouchMethodsInterface {

	public abstract void handleTouchStart(Event e);
	public abstract void handleTouchMove(Event e);
	public abstract void handleTouchEnd(Event e);
	public abstract void handleTouchCancel(Event e);
	public abstract void handleTouchOut(Event e);
	
	public default void registerTouchControls(HTMLElement element) {
		System.out.println("Registered Touch Events");
		element.getStyle().setProperty("touch-action", "none"); // Cancels early touchcancel firing.
		element.getStyle().setProperty("user-select", "none"); // no copy text!!!
		element.getStyle().setProperty("-webkit-touch-callout", "none");
		element.getStyle().setProperty("-webkit-user-select", "none");
		element.getStyle().setProperty("-khtml-user-select", "none");
		element.getStyle().setProperty("-moz-user-select", "none");
		element.getStyle().setProperty("-ms-user-select", "none");
		element.getStyle().setProperty("-webkit-tap-highlight-color", "rgba(0,0,0,0)");
		
		
		element.addEventListener("pointerdown", (e) -> {
			handleTouchStart(e);
		}, false);
		element.addEventListener("pointermove", (e) -> {
			handleTouchMove(e);
		}, false);
		element.addEventListener("pointerup", (e) -> {
			handleTouchEnd(e);
		}, false);
		element.addEventListener("pointercancel", (e) -> {
			handleTouchCancel(e);
		}, false);
		element.addEventListener("pointerout", (e) -> {
			handleTouchOut(e);
		}, false);
	}
	
}
