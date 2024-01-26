package coffee.mason.blacktar.canvas.webgl;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSNumber;

import coffee.mason.blacktar.javascript.Float32Array;
import coffee.mason.blacktar.javascript.Int32Array;

public class GL {
	// Static values used by WebGL
	public static final int COLOR_BUFFER_BIT = 16384;
	public static final int COLOR_CLEAR_VALUE = 3106;
	public static final int DEPTH_BUFFER_BIT = 256;
	public static final int STENCIL_BUFFER_BIT = 1024;
	
	@JSBody(params = {"gl", "r", "g", "b", "a"}, script = "gl.clearColor(r, g, b, a);")
	public static native void clearColor(WebGLContext gl, float r, float g, float b, float a);
	
//	@JSBody(params = {"gl"}, script = "gl.clearColor(0, 0, 0, 1); gl.clear(16384);")
//	public static native void test(WebGLContext gl);
	
	@JSBody(params = {"gl", "value"}, script = "gl.clear(value);")
	public native static void clear(WebGLContext gl, int value);

	@JSBody(params = {"gl", "x", "y", "width", "height"}, script = "gl.viewport(x, y, width, height);")
	public native static void viewport(WebGLContext gl, float x, float y, float width, float height);
	
//    @JSBody(params = "gl", script = "return gl.getParameter(gl.COLOR_CLEAR_VALUE);")
//	public static native JSObject getClearColor(WebGLContext gl);
	
}
