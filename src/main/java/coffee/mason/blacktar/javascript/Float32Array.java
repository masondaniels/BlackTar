package coffee.mason.blacktar.javascript;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

public class Float32Array implements Any {

    @JSBody(params = "elements", script = "return new Float32Array(elements);")
    public static native JSObject of(float... elements);
	
}
