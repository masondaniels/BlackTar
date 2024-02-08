package coffee.mason.blacktar.canvas.controls;

import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.events.Event;

public interface PointerEvent extends Event {
	
	@JSProperty
	public float getHeight();
	
	@JSProperty
	public float getWidth();
	
	@JSProperty
	public float getPressure();
	
}
