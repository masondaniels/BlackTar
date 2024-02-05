package coffee.mason.blacktar.javascript;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

public class Float32ArrayUtil implements Any {

    @JSBody(params = "elements", script = "return new Float32Array(elements);")
    public static native JSObject of(float... elements);
    
    @JSBody(params = "size", script = "return new Float32Array(size);")
    public static native JSObject create(int size);
	
}
