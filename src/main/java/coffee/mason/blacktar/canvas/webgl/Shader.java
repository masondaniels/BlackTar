package coffee.mason.blacktar.canvas.webgl;

import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.Float32Array;

import coffee.mason.blacktar.util.JavaScriptUtil;
import coffee.mason.blacktar.util.StringUtil;

public class Shader {

	private WebGLContext gl;

	private String vertSource;
	private String fragSource;

	private JSObject program; // gl shader program

	public Shader(WebGLContext gl, String[] vertSource, String[] fragSource) {
		this.gl = gl;
		this.vertSource = StringUtil.join("\n", vertSource);
		this.fragSource = StringUtil.join("\n", fragSource);
		createShaderProgram();
	}

	private void createShaderProgram() {
		JSObject vertexShader = gl.createShader(GL.VERTEX_SHADER);
		JSObject fragShader = gl.createShader(GL.FRAGMENT_SHADER);

		gl.shaderSource(vertexShader, vertSource);
		gl.shaderSource(fragShader, fragSource);

		gl.compileShader(vertexShader);
		gl.compileShader(fragShader);

		// Compiling error checks - fragment
		JSObject compileStatusFrag = gl.getShaderParameter(fragShader, GL.COMPILE_STATUS);
		if (!JavaScriptUtil.not(compileStatusFrag)) {
			System.err.println("Compiling error with fragment shader.");
			System.err.println(gl.getShaderInfoLog(fragShader) + "");
		}

		// Compiling error checks - vertex
		JSObject compileStatusVertex = gl.getShaderParameter(vertexShader, GL.COMPILE_STATUS);
		if (!JavaScriptUtil.not(compileStatusVertex)) {
			System.err.println("Compiling error with vertex shader.");
			System.err.println(gl.getShaderInfoLog(vertexShader) + "");
		}

		program = gl.createProgram();

		gl.attachShader(program, vertexShader);
		gl.attachShader(program, fragShader);

		// Link program
		gl.linkProgram(program);

		// Validate program
		gl.validateProgram(program);

		JSObject programValidStatus = gl.getProgramParameter(program, GL.VALIDATE_STATUS);
		if (!JavaScriptUtil.not(programValidStatus)) {
			System.err.println("Validate status error with program.");
		}

		JSObject programLinkStatus = gl.getProgramParameter(program, GL.LINK_STATUS);
		if (!JavaScriptUtil.not(programLinkStatus)) {
			System.err.println("Link error with program.");
		}

	}

	public void useProgram() {
		gl.useProgram(program);
	}

	public JSObject getProgram() {
		return program;
	}

	/**
	 * 
	 * Create float attribute.
	 * 
	 * @param attribLocation The location of the attribute in GPU memory/layout.
	 * @param elements       The number of elements per attrib. Eg 3 if Vec3.
	 * @param input          The float 32 array which will be written to the buffer.
	 * @return
	 */
	public BufferInformation createFloatBuffer(int attribLocation, int elements, Float32Array input) {
		JSObject buffer = createFloatVBO(input);
		setupFloatAttrib(attribLocation, elements);
		return new BufferInformation(buffer, attribLocation, elements);
	}

	private void setupFloatAttrib(int attribLocation, int elements) {
		gl.vertexAttribPointer(attribLocation, elements, GL.FLOAT, false, elements * 4, 0);
		gl.enableVertexAttribArray(attribLocation);
	}

	private JSObject createFloatVBO(Float32Array input) {
		JSObject buffer = gl.createBuffer();
		writeFloatVBO(buffer, input);
		return buffer;
	}

	private void writeFloatVBO(JSObject buffer, Float32Array input) {
		gl.bindBuffer(GL.ARRAY_BUFFER, buffer);
		gl.bufferData(GL.ARRAY_BUFFER, input, GL.STATIC_DRAW);
	}
	
	public void useBuffer(WebGLContext gl, BufferInformation buffer) {
		gl.disableVertexAttribArray(buffer.getLocation());
		gl.bindBuffer(GL.ARRAY_BUFFER, buffer.getBuffer());
		gl.vertexAttribPointer(buffer.getLocation(), buffer.getElements(), GL.FLOAT, false, buffer.getElements() * 4, 0);
		gl.enableVertexAttribArray(buffer.getLocation());
	}

	public int getUniformLocation(String string) {
		return gl.getUniformLocation(getProgram(), string);
	}

	// TODO: Implement dynamic arrays instanced calls
	public void drawObj(Obj obj, BufferInformation... buffers) {

		gl.useProgram(program);
		
		// TODO: Implement usage of uniforms
		
		for (int i = 0; i < buffers.length; i++) {
			useBuffer(gl, buffers[i]);
		}

		gl.drawArrays(GL.TRIANGLES, 0, obj.getTriangleFloats().getLength()/3);
	}

}
