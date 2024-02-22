package coffee.mason.blacktar.util;

import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.Float32Array;

import coffee.mason.blacktar.canvas.webgl.BufferInformation;
import coffee.mason.blacktar.canvas.webgl.GL;
import coffee.mason.blacktar.canvas.webgl.WebGLContext;

public class WebGLUtil {

	/**
	 * 
	 * Create float attribute.
	 * 
	 * @param attribLocation The location of the attribute in GPU memory/layout.
	 * @param elements       The number of elements per attrib. Eg 3 if Vec3.
	 * @param input          The float 32 array which will be written to the buffer.
	 * @return
	 */
	public static BufferInformation createFloatBuffer(WebGLContext gl, int attribLocation, int elements, Float32Array input) {
		JSObject buffer = createFloatVBO(gl, input);
		setupFloatAttrib(gl, attribLocation, elements);
		return new BufferInformation(buffer, attribLocation, elements);
	}

	private static void setupFloatAttrib(WebGLContext gl, int attribLocation, int elements) {
		gl.vertexAttribPointer(attribLocation, elements, GL.FLOAT, false, elements * 4, 0);
		gl.enableVertexAttribArray(attribLocation);
	}

	private static JSObject createFloatVBO(WebGLContext gl,  Float32Array input) {
		JSObject buffer = gl.createBuffer();
		writeFloatVBO(gl, buffer, input);
		return buffer;
	}

	private static void writeFloatVBO(WebGLContext gl, JSObject buffer, Float32Array input) {
		gl.bindBuffer(GL.ARRAY_BUFFER, buffer);
		gl.bufferData(GL.ARRAY_BUFFER, input, GL.STATIC_DRAW);
	}
	
}
