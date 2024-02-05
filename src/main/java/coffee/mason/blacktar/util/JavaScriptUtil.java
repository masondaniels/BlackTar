package coffee.mason.blacktar.util;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSNumber;

public class JavaScriptUtil {
	@JSBody(params = { "object" }, script = "console.log(\"Logged object: \" + object); console.log(object);")
	public static native void log(JSObject object);
	
	@JSBody(params = { "object" }, script = "return object;")
	public static native JSObject get(String object);
	
	@JSBody(params = { "object" }, script = "return eval(object);")	
	public static native JSObject eval(String object);
	
	@JSBody(script = "return performance.now();")
	public static native JSNumber getElapsed();
	
	/**
	 * Equivalent to !object in javascript.
	 * 
	 * Can be used in java as:
	 * if (!JavaScriptUtil.not(object)) {
	 * 		System.out.println("Object is null/not");
	 * }
	 * 
	 */
	@JSBody(params = "object", script = "if (!object) { return false; } return true;")
	public static native boolean not(JSObject object);
}
