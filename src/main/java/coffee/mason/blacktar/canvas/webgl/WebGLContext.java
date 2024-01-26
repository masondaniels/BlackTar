package coffee.mason.blacktar.canvas.webgl;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSNumber;

import coffee.mason.blacktar.javascript.Any;
import coffee.mason.blacktar.javascript.Float32Array;

// Must be interface to interact with properties
public interface WebGLContext extends Any {

	// Missing getCanvas on purpose. Should never use this method.
	
	@JSProperty
	JSNumber getDrawingBufferHeight();
	
	@JSProperty
	JSNumber getDrawingBufferWidth();
		
//	@JSProperty
//	void clearColor(JSObject array);
	
//	/**
//	 * 
//	 * Clears buffers using value
//	 * 
//	 * @param value
//	 * Use static values such as GL.COLOR_BUFFER_BIT
//	 */
//	@JSProperty
//	void clear(JSObject value);
//	
//	@JSProperty
//	void viewport(JSObject array);

}
