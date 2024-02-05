package coffee.mason.blacktar.canvas.impl;

import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.typedarrays.Float32Array;

import coffee.mason.blacktar.canvas.CanvasGL;
import coffee.mason.blacktar.canvas.webgl.GL;
import coffee.mason.blacktar.glmatrix.GLMatrix;
import coffee.mason.blacktar.javascript.Float32ArrayUtil;
import coffee.mason.blacktar.util.JavaScriptUtil;
import coffee.mason.blacktar.util.StringUtil;

public class CanvasGLImpl extends CanvasGL {

	private static final String[] VERTEX = {
			"precision mediump float;",
			"attribute vec3 vertPosition;",
			"varying vec3 vertColor;",
			"uniform mat4 mProj;",
			"uniform mat4 mView;",
			"uniform mat4 mWorld;",
			"void main()",
			"{",
				"vertColor = vec3(1, 1, 1);",
				"gl_Position = mProj * mView * mWorld * vec4(vertPosition, 1.0);",
			"}"
	};
	
	private static final String[] FRAG = {
			"precision mediump float;",
			"varying vec3 vertColor;",
			"void main()",
			"{",
				"gl_FragColor = vec4(vertColor, 1.0);",
			"}"
	};
	
	public CanvasGLImpl(boolean fullscreen) {
		super(fullscreen);
	}

//	private MatProj proj;
//	private Mat4F world;
//	private MatView view;
	private Float32Array proj;
	private int projUniformLocation;

	@Override
	public void loadBeforeAnimation() {

		proj = (Float32Array) Float32Array.create(16);
//		proj = new MatProj();
//		world = new Mat4F();
//		view = new MatView();

		GLMatrix.init();

		setupShader();

	}

	private void setupShader() {
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

		// Validate program (very important, literally spent an hour or two debugging
		// because I missed this)
		GL.validateProgram(gl(), program);

		JSObject programValidStatus = GL.getProgramParameter(gl(), program, GL.VALIDATE_STATUS);
		if (!JavaScriptUtil.not(programValidStatus)) {
			System.err.println("Validate status error with program.");
		}

		JSObject programLinkStatus = GL.getProgramParameter(gl(), program, GL.LINK_STATUS);
		if (!JavaScriptUtil.not(programLinkStatus)) {
			System.err.println("Link error with program.");
		}

		Float32Array array = (Float32Array) Float32ArrayUtil.of(0f, 0.5f, 0f,
				-0.5f, -0.5f, 0f,
				0.5f, -0.5f, 0f);

		JSObject floatBuffer = GL.createBuffer(gl());

		// Set as active buffer.
		GL.bindBuffer(gl(), GL.ARRAY_BUFFER, floatBuffer);

		// Put data in active buffer.
		GL.bufferData(gl(), GL.ARRAY_BUFFER, array, GL.STATIC_DRAW);

		int attribLocation = GL.getAttribLocation(gl(), program, "vertPosition");

		// Setting up attrib
		// index, size (# of elements per attrib), type, normalized, stride, offset
		GL.vertexAttribPointer(gl(), attribLocation, 3, GL.FLOAT, false, 3 * 4, 0);
		GL.enableVertexAttribArray(gl(), attribLocation);

		JSObject colorBuffer = GL.createBuffer(gl());

		GL.bindBuffer(gl(), GL.ARRAY_BUFFER, colorBuffer);

		int colorLocation = 1; // GL.getAttribLocation(gl(), program, "vertColor");

		GL.vertexAttribPointer(gl(), colorLocation, 3, GL.FLOAT, false, 3 * 4, 3 * 4);
		GL.enableVertexAttribArray(gl(), colorLocation);

		GL.useProgram(gl(), program);

		projUniformLocation = GL.getUniformLocation(gl(), program, "mProj");
		int viewUniformLocation = GL.getUniformLocation(gl(), program, "mView");
		int worldUniformLocation = GL.getUniformLocation(gl(), program, "mWorld");

//		view.lookAt(new Vec3(0, 0, -5), new Vec3(0, 0, 1), new Vec3(0, 1, 0));
		Float32Array world = (Float32Array) Float32Array.create(16);
		Float32Array view = (Float32Array) Float32Array.create(16);

//		float[] p = {0f,0f,-5f};
//		float[] t = {0f,0f,0f};
//		float[] u = {0f,1f,0f};

		GLMatrix.lookAt(view, 0, 0, -5, 0, 0, 0, 0, 1, 0);
		GLMatrix.perspective(proj, (float) Math.toRadians(45), (float) (getWidth() / getHeight()), 0.1f, 1000f);
		GLMatrix.identity(world);

		System.out.println(world.get(0) + " SHOULD BE 1 - IF IT IS GLMATRIX IS WORKING.");

		GL.uniformMatrix4fv(gl(), projUniformLocation, false, proj); // .getMatrixAsFloat32Array()
		GL.uniformMatrix4fv(gl(), viewUniformLocation, false, view);
		GL.uniformMatrix4fv(gl(), worldUniformLocation, false, world);

	}

	@Override
	public void loadAfterAnimation() {

	}

	private double dpi = -1;

	private void onResize() {
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

		GL.viewport(gl(), 0, 0, (float) getWidth(), (float) getHeight());

//		proj.setAspectRatio((float)getWidth()/(float)getHeight());
		// TODO: UPDATE PROJ

		GLMatrix.perspective(proj, (float) Math.toRadians(45), (float) (getWidth() / getHeight()), 0.1f, 1000f);
		GL.uniformMatrix4fv(gl(), projUniformLocation, false, proj);
//		System.out.println("Updated projection matrix: " + proj.toString());
		System.out.println("Updated projection matrix: [" + proj.get(0) + ", " + proj.get(1) + ", " + proj.get(2) + ", "
				+ proj.get(3) + ", " + proj.get(4) + ", " + proj.get(5) + ", " + proj.get(6) + ", " + proj.get(7) + ", "
				+ proj.get(8) + ", " + proj.get(9) + ", " + proj.get(10) + ", " + proj.get(11) + ", " + proj.get(12)
				+ ", " + proj.get(13) + ", " + proj.get(14) + ", " + proj.get(15) + "]");
	}

	@Override
	public void update() {
		// Resize if init
		if (dpi == -1) {
			onResize();
		}

	}

	@Override
	public void draw() {

		GL.clearColor(gl(), 0f, 0f, 0f, 1f);
		GL.clear(gl(), GL.COLOR_BUFFER_BIT | GL.DEPTH_BUFFER_BIT);

		GL.drawArrays(gl(), GL.TRIANGLES, 0, 3);

	}

	@Override
	public void onCanvasResize() {
		onResize();
	}

}