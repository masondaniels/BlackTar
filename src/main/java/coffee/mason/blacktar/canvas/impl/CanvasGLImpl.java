package coffee.mason.blacktar.canvas.impl;

import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSNumber;

import coffee.mason.blacktar.canvas.CanvasGL;
import coffee.mason.blacktar.canvas.webgl.GL;
import coffee.mason.blacktar.javascript.Float32Array;
import coffee.mason.blacktar.util.JavaScriptUtil;
import coffee.mason.blacktar.util.StringUtil;

public class CanvasGLImpl extends CanvasGL {

	private static final String[] VERTEX = {
			"precision mediump float;",
			"attribute vec2 vertPosition;",
			"void main()",
			"{",
			"  gl_Position = vec4(vertPosition, 0.0, 1.0);",
			"}"
	};
	
	private static final String[] FRAG = {
			"precision mediump float;",
			"void main()",
			"{",
			"  gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);",
			"}"
	};
	
	
	public CanvasGLImpl(boolean fullscreen) {
		super(fullscreen);
	}

	@Override
	public void loadBeforeAnimation() {
		
		JSObject vertexShader = GL.createShader(gl(), GL.VERTEX_SHADER);
		JSObject fragShader = GL.createShader(gl(), GL.FRAGMENT_SHADER);
		
		GL.shaderSource(gl(), vertexShader, StringUtil.join("\n", VERTEX));
		GL.shaderSource(gl(), fragShader, StringUtil.join("\n", FRAG));
		
		GL.compileShader(gl(), vertexShader);
		GL.compileShader(gl(), fragShader);
		
		// Compiling error checks - fragment
		JSObject compileStatusFrag = GL.getShaderParameter(gl(), fragShader, GL.COMPILE_STATUS);
		if (!JavaScriptUtil.not(compileStatusFrag)) {
			System.err.println("Compiling error with fragment shader.");
			System.err.println(GL.getShaderInfoLog(gl(), fragShader) + "");
		}
		
		// Compiling error checks - vertex
		JSObject compileStatusVertex = GL.getShaderParameter(gl(), vertexShader, GL.COMPILE_STATUS);
		if (!JavaScriptUtil.not(compileStatusVertex)) {
			System.err.println("Compiling error with vertex shader.");
			System.err.println(GL.getShaderInfoLog(gl(), vertexShader) + "");
		}
		
		// Need to create graphics program (graphics pipeline)
		JSObject program = GL.createProgram(gl());
		
		// Attach shaders to program
		GL.attachShader(gl(), program, vertexShader);
		GL.attachShader(gl(), program, fragShader);
		
		// Link program
		GL.linkProgram(gl(), program);
		
		// Validate program (very important, literally spent an hour or two debugging because I missed this)
		GL.validateProgram(gl(), program);
		
		JSObject programValidStatus = GL.getProgramParameter(gl(), program, GL.VALIDATE_STATUS);
		if (!JavaScriptUtil.not(programValidStatus)) {
			System.err.println("Validate status error with program.");
		}
		
		JSObject programLinkStatus = GL.getProgramParameter(gl(), program, GL.LINK_STATUS);
		if (!JavaScriptUtil.not(programLinkStatus)) {
			System.err.println("Link error with program.");
		}
		
		Float32Array array = (Float32Array) Float32Array.of(
				0f, 0.5f,
				-0.5f, -0.5f,
				0.5f, -0.5f);
		
		JSObject floatBuffer = GL.createBuffer(gl());

		// Set as active buffer.
		GL.bindBuffer(gl(), GL.ARRAY_BUFFER, floatBuffer);
		
		// Put data in active buffer.
		GL.bufferData(gl(), GL.ARRAY_BUFFER, array, GL.STATIC_DRAW);
		
		int attribLocation = GL.getAttribLocation(gl(), program, "vertPosition");
		
		// Setting up attrib
		// index, size (# of elements per attrib), type, normalized, stride, offset
		GL.vertexAttribPointer(gl(), attribLocation, 2, GL.FLOAT, false, 8, 0); 
		GL.enableVertexAttribArray(gl(), attribLocation);
		
		GL.useProgram(gl(), program);
		
	}

	@Override
	public void loadAfterAnimation() {
		
	}

	private double dpi = -1;

	@Override
	public void update() {
		if (dpi == -1) {
			dpi = ((JSNumber) JavaScriptUtil.eval("window.devicePixelRatio || 1")).doubleValue();
		}

		if (isFullscreen()) {
			canvas.setWidth((int) (Window.current().getInnerWidth() * dpi));
			canvas.setHeight((int) (Window.current().getInnerHeight() * dpi));
			setWidth(Window.current().getInnerWidth() * dpi);
			setHeight(Window.current().getInnerHeight() * dpi);
			canvas.getStyle().setProperty("width", Window.current().getInnerWidth() + "px");
			canvas.getStyle().setProperty("height", Window.current().getInnerHeight() + "px");
		}
	}

	@Override
	public void draw() {
		
		GL.viewport(gl(), 0, 0, (float) getWidth(), (float) getHeight());
		GL.clearColor(gl(), 0f, 0f, 0f, 1f);
		GL.clear(gl(), GL.COLOR_BUFFER_BIT);

		GL.drawArrays(gl(), GL.TRIANGLES, 0, 3);
		
	}

	@Override
	public void onCanvasResize() {
		update();
		draw();
	}

}