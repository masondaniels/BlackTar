package coffee.mason.blacktar.util;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

public class JavaScriptUtil {
	@JSBody(params = { "object" }, script = "console.log(\"Logged object: \" + object); console.log(object);")
	public static native void log(JSObject object);
	
	@JSBody(params = { "object" }, script = "return object;")
	public static native JSObject get(String object);
	
	@JSBody(params = { "object" }, script = "return eval(object);")	
	public static native JSObject eval(String object);
}
