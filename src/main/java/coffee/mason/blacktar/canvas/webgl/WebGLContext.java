package coffee.mason.blacktar.canvas.webgl;

import org.teavm.jso.JSObject;

import coffee.mason.blacktar.javascript.Any;

// Must be interface to interact with properties
public interface WebGLContext extends Any {

	// Missing getCanvas on purpose. Should never use this method.

	void clearColor(float r, float g, float b, float a);

	void clear(int value);

	void viewport(float x, float y, float width, float height);

	JSObject getParameter(int value);

	JSObject createShader(int value);

	void shaderSource(JSObject shader, String shaderSource);

	void compileShader(JSObject shader);

	JSObject getShaderParameter(JSObject shader, int value);

	JSObject getProgramParameter(JSObject program, int value);

	String getShaderInfoLog(JSObject shader);

	JSObject createProgram(WebGLContext gl);

	void attachShader(JSObject program, JSObject shader);

	void linkProgram(JSObject program);

	JSObject createBuffer(WebGLContext gl);

	JSObject bindBuffer(int value, JSObject buffer);

	void bufferData(int arrayValue, JSObject array, int value);

	int getAttribLocation(JSObject program, String string);

	void vertexAttribPointer(int index, int size, int type, boolean normalized, int stride,
			int offset);

	void enableVertexAttribArray(int index);

	void drawArrays(int mode, int first, int count);

	void useProgram(JSObject program);

	void validateProgram(JSObject program);

	int getUniformLocation(JSObject program, String identifier);

	void uniformMatrix4fv(int location, boolean transpose, JSObject value);

	void cullFace(int mode);

	void frontFace(int mode);

	void enable(int mode);
}
