package coffee.mason.blacktar.util;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.Float32Array;

public class Float32ArrayUtil {

    @JSBody(params = "elements", script = "return new Float32Array(elements);")
    public static native Float32Array of(float... elements);
    
    public static Float32Array of(double... elements) {
    	float[] a = new float[elements.length];
    	for (int i = 0; i < a.length; i++) {
			a[i] = (float) elements[i];
		}
    	return of(a);
    }
    
    @JSBody(params = "size", script = "return new Float32Array(size);")
    public static native Float32Array create(int size);

    // Print all elements
	public static void print(Float32Array array, int mod) {
		StringBuilder sb = new StringBuilder("[" + array.get(0));
		for (int i = 1; i < array.getLength(); i++) {
			if (i % mod == 0) {
				sb.append("\n"+ array.get(i));
			} else {
			sb.append("," + array.get(i));
			}
		}
		sb.append("]");
		System.out.println(sb.toString());
	}
	
}
