package coffee.mason.blacktar.canvas.webgl;

import coffee.mason.blacktar.javascript.Any;

// Must be interface to interact with properties
public interface WebGLContext2 extends Any, WebGLContext {

	void drawArraysInstanced(int mode, int first, int count, int instanceCount);
}
