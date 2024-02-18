package coffee.mason.blacktar.canvas.controls;

import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.html.HTMLElement;

public interface MouseControls {

	public abstract void handleMouseDown(Event e);
	public abstract void handleMouseUp(Event e);
	public abstract void handleMouseLeave(Event e);
	
	public default void registerMouseControls(HTMLElement element) {

		element.addEventListener("mousedown", (e) -> {
			handleMouseDown(e);
		}, false);
		element.addEventListener("mouseup", (e) -> {
			handleMouseUp(e);
		}, false);
		element.addEventListener("mouseleave", (e) -> {
			handleMouseLeave(e);
		}, false);
	}

}
