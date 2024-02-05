package coffee.mason.blacktar.glmatrix;

import org.teavm.jso.JSBody;
import org.teavm.jso.typedarrays.Float32Array;


public class GLMatrix {
	
	@JSBody(params = {"out"}, script = "glMatrix.mat4.identity(out);")
	public static native void identity(Float32Array out);
	
	@JSBody(params = {"out", "posa", "posb", "posc", "targeta", "targetb", "targetc", "upa", "upb", "upc"}, script = "glMatrix.mat4.lookAt(out, [posa, posb, posc], [targeta, targetb, targetc], [upa, upb, upc]);")
	public static native void lookAt(Float32Array out, float posa, float posb, float posc, float targeta, float targetb, float targetc, float upa, float upb, float upc);

	@JSBody(params = {"out", "rad", "aspect", "near", "far"}, script = "glMatrix.mat4.perspective(out, rad, aspect, near, far);")
	public static native void perspective(Float32Array out, float radians, float aspect, float near, float far);
	
}
