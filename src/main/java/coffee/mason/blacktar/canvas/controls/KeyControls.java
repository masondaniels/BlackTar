package coffee.mason.blacktar.canvas.controls;

import org.teavm.jso.dom.events.Event;

public interface KeyControls {

	public abstract void handleKeyDown(Event e);
	public abstract void handleKeyUp(Event e);
	public abstract void handleKeyPress(Event e);
	
}
