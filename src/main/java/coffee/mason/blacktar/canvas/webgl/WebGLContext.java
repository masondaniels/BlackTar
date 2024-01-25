package coffee.mason.blacktar.canvas.webgl;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSNumber;

import coffee.mason.blacktar.javascript.Any;

// Must be interface to interact with properties
public interface WebGLContext extends Any {

	// Missing getCanvas on purpose. Should never use this method.
	
	@JSProperty
	JSNumber getDrawingBufferHeight();
	
	@JSProperty
	JSNumber getDrawingBufferWidth();
		
	

}
