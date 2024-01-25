package coffee.mason.blacktar.javascript;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

public class UInt32Array implements Any {

    @JSBody(params = "elements", script = "return Uint32Array.of(elements);")
    public static native JSObject of(int... elements);
	
}
