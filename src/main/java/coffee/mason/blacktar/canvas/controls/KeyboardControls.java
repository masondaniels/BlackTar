package coffee.mason.blacktar.canvas.controls;

import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.events.Event;

public abstract interface KeyboardControls {

	
	public abstract void handleKeyDown(Event e);
	public abstract void handleKeyUp(Event e);
	public abstract void handleKeyPress(Event e);
	
	public static void register(KeyboardControls controls) {
		System.out.println("Registered Keyboard Events");
		// Key down and key up are for ctrl keys such as shift.
		Window.current().addEventListener("keydown", (e) -> {
			controls.handleKeyDown(e);
		}, false);
		Window.current().addEventListener("keyup", (e) -> {
			controls.handleKeyUp(e);
		}, false);
		Window.current().addEventListener("keypress", (e) -> {
			controls.handleKeyPress(e);
		}, false);
	}
	
}
