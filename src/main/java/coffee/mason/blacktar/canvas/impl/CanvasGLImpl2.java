package coffee.mason.blacktar.canvas.impl;

import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.MouseEvent;
import org.teavm.jso.typedarrays.Float32Array;

import coffee.mason.blacktar.canvas.CanvasGL2;
import coffee.mason.blacktar.canvas.controls.KeyboardControls;
import coffee.mason.blacktar.canvas.controls.TouchControls;
import coffee.mason.blacktar.canvas.controls.impl.FpsKeyboardControls;
import coffee.mason.blacktar.canvas.webgl.GL;
import coffee.mason.blacktar.canvas.webgl.ObjStatic;
import coffee.mason.blacktar.linear.Mat4x4;
import coffee.mason.blacktar.util.JavaScriptUtil;
import coffee.mason.blacktar.util.StringUtil;

public class CanvasGLImpl2 extends CanvasGL2 implements TouchControls {

	private static final String[] VERTEX = {
			"# version 300 es",
			"precision mediump float;",
			"layout (location=0) in vec3 triPosition;",
			"layout (location=1) in vec3 triNormal;",
			"out vec3 triColor;",
			"uniform mat4 mProj;",
			"uniform mat4 mView;",
			"uniform mat4 mWorld;",
			"uniform float time;",
			"void main()",
			"{",
			    "vec3 ambientColor = vec3(0.7, 0.7, 0.7);",
			    "float ambientIntensity = max(dot(triNormal, vec3(0.0, 1.0, 0.0)), 0.0);", // Lambertian reflection model
			    "vec3 ambient = ambientColor * ambientIntensity;",
			    "vec3 rColor = vec3(min(tan(float(gl_InstanceID)), 0.2), min(cos(float(gl_InstanceID)), 0.3), min(sin(float(gl_InstanceID)), 0.2));",
			    "triColor = vec3(0.1, 0.1, 0.1) + ambient + rColor;",
				"gl_Position = mProj * mView * mWorld * vec4(triPosition.x + cos(float(gl_InstanceID)*time), triPosition.y, triPosition.z - float(gl_InstanceID)*5.0, 1.0);",
			"}"
	};
	
	private static final String[] FRAG = {
			"# version 300 es",
			"precision mediump float;",
			"in vec3 triColor;",
			"out vec4 fragCoord;",
			"void main()",
			"{",
				"fragCoord = vec4(triColor, 1.0);",
			"}"
	};
	
	public CanvasGLImpl2(boolean fullscreen) {
		super(fullscreen);
	}

	private Mat4x4 proj;
	private Float32Array world;
//	private Float32Array view;
//	private Mat4x4 view;

	private Mat4x4 identity;

	private int projUniformLocation;
	private int viewUniformLocation;
	private int worldUniformLocation;
	
	private int timeUniformLocation;

	private FpsKeyboardControls keyboardControls;
	
	@Override
	public void loadBeforeAnimation() {

		TouchControls.register(this, this);
//		KeyboardControls.register(this);


		world = Float32Array.create(16);
//		view = (Float32Array) Float32Array.create(16);
//		view = Mat4x4.identity();
		identity = Mat4x4.identity();

		setupShader();
		
		keyboardControls = new FpsKeyboardControls(gl, viewUniformLocation);
		keyboardControls.getCamera().setPitch(-16f);
		keyboardControls.getCamera().setYaw(272.4f);
		keyboardControls.getCamera().setPosX(0.45f);
		keyboardControls.getCamera().setPosY(7.5f);
		keyboardControls.getCamera().setPosZ(12f);
		keyboardControls.getCamera().updateViewDirection();
		gl.uniformMatrix4fv(viewUniformLocation, false, keyboardControls.getCamera().getViewMatrix().getArray());
		
//		position = new Vec3(0.45f, 7.50f, 12f);
//		yaw = 272.4f;
//		pitch = -16f;
//		viewDirection = new Vec3(0.04f, -0.28f, -0.96f);
	}

	private void setupShader() {

		JSObject vertexShader = gl.createShader(GL.VERTEX_SHADER);
		JSObject fragShader = gl.createShader(GL.FRAGMENT_SHADER);

		gl.shaderSource(vertexShader, StringUtil.join("\n", VERTEX));
		gl.shaderSource(fragShader, StringUtil.join("\n", FRAG));

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

		// Need to create graphics program (graphics pipeline)
		JSObject program = gl.createProgram();

		gl.attachShader(program, vertexShader);
		gl.attachShader(program, fragShader);

		// Link program
		gl.linkProgram(program);

		// Validate program (very important, literally spent an hour or two debugging
		// because I missed this)
		gl.validateProgram(program);

		JSObject programValidStatus = gl.getProgramParameter(program, GL.VALIDATE_STATUS);
		if (!JavaScriptUtil.not(programValidStatus)) {
			System.err.println("Validate status error with program.");
		}

		JSObject programLinkStatus = gl.getProgramParameter(program, GL.LINK_STATUS);
		if (!JavaScriptUtil.not(programLinkStatus)) {
			System.err.println("Link error with program.");
		}

		Float32Array array = ObjStatic.TEAPOT.getTriangleFloats();

		JSObject floatBuffer = gl.createBuffer();

		// Set as active buffer.
		gl.bindBuffer(GL.ARRAY_BUFFER, floatBuffer);

		// Put data in active buffer.
		gl.bufferData(GL.ARRAY_BUFFER, array, GL.STATIC_DRAW);

		int triPositionAttrib = 0; //  gl.getAttribLocation(program, "triPosition");

		// Setting up attrib
		// index, size (# of elements per attrib), type, normalized, stride, offset
		gl.vertexAttribPointer(triPositionAttrib, 3, GL.FLOAT, false, 3* 4, 0);
		gl.enableVertexAttribArray(triPositionAttrib);

		// Normals
		Float32Array normalArray = ObjStatic.TEAPOT.getNormalFloats();

		System.out.println(array.getLength() + " =? " + normalArray.getLength());
		
		// Create a new buffer for normals
		JSObject normalBuffer = gl.createBuffer();

		// Set as active buffer.
		gl.bindBuffer(GL.ARRAY_BUFFER, normalBuffer);

		// Put data in the active buffer for normals.
		gl.bufferData(GL.ARRAY_BUFFER, normalArray, GL.STATIC_DRAW);

		// Attribute index for normals
		int triNormalAttrib = 1;

		// Setting up the attribute for normals
		// the 3 * 4 offset is used to allow 3 vertices to access the same normal
		gl.vertexAttribPointer(triNormalAttrib, 3, GL.FLOAT, false, (3) * 4, 0);
		gl.enableVertexAttribArray(triNormalAttrib);
		
		
//		Float32Array array1 = ObjStatic.TEAPOT.getNormalFloats();
//
//		JSObject floatBuffer1 = gl.createBuffer();
//
//		gl.bindBuffer(GL.ARRAY_BUFFER, floatBuffer1);
//
//		gl.bufferData(GL.ARRAY_BUFFER, array1, GL.STATIC_DRAW);
//		
//		// TODO: Create normals
//
//		int triNormalAttrib = 1;
//		
//		gl.vertexAttribPointer(triNormalAttrib, 3, GL.FLOAT, false, 3*4, 4*ObjStatic.TEAPOT.getTriangleFloats().getLength());
//		gl.enableVertexAttribArray(triNormalAttrib);
		
		JSObject colorBuffer = gl.createBuffer();

		gl.bindBuffer(GL.ARRAY_BUFFER, colorBuffer);

		gl.useProgram(program);

		// Enable depth testing & culling
		gl.frontFace(GL.CCW);
		gl.cullFace(GL.BACK);
		gl.enable(GL.DEPTH_TEST);

		projUniformLocation = gl.getUniformLocation(program, "mProj");
		viewUniformLocation = gl.getUniformLocation(program, "mView");
		worldUniformLocation = gl.getUniformLocation(program, "mWorld");
		timeUniformLocation = gl.getUniformLocation(program, "time");

		gl.uniform1f(timeUniformLocation, JavaScriptUtil.getElapsed().floatValue()/1000f);
		
//		view.setValue(2, 3, -3.5f);

		proj = Mat4x4.perspective((float) Math.toRadians(45), (float) (getWidth() / getHeight()), 0.1f, 1000f);

		world = identity.getArray();

		uniformMatrix4fv();

	}

	private void uniformMatrix4fv() {
		gl.uniformMatrix4fv(projUniformLocation, false, proj.getArray());
		gl.uniformMatrix4fv(worldUniformLocation, false, world);
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

		gl.viewport(0, 0, (float) getWidth(), (float) getHeight());

		proj = Mat4x4.perspective((float) Math.toRadians(45), (float) (getWidth() / getHeight()), 0.1f, 1000f);

		uniformMatrix4fv();

		System.out.println("Updated projection matrix:\n" + proj.toString());
	}

	@Override
	public void update() {
		// Resize if init
		if (dpi == -1) {
			onResize();
		}
		
		gl.uniform1f(timeUniformLocation, JavaScriptUtil.getElapsed().floatValue()/10000f);
		
		keyboardControls.update();

	}

	@Override
	public void draw() {
		gl.clearColor(0f, 0f, 0f, 1f);
		gl.clear(GL.COLOR_BUFFER_BIT | GL.DEPTH_BUFFER_BIT);

		
//		gl.drawArrays(GL.TRIANGLES, 0, Mesh.CUBE.getLength()/3);
		gl.drawArraysInstanced(GL.TRIANGLES, 0, ObjStatic.TEAPOT.getTriangleFloats().getLength()/3, 100);

	}

	@Override
	public void onCanvasResize() {
		onResize();
	}

	private boolean isTouched; // is screen touched?

	@Override
	public void handleTouchStart(Event e) {
		System.out.println("Touch start");
		isTouched = true;
	}

	@Override
	public void handleTouchMove(Event e) {
		MouseEvent me = (MouseEvent) e;
		if (isTouched) {
			System.out.println("Touch move " + me.getClientX() + ", " + me.getClientY());

		}
	}

	@Override
	public void handleTouchEnd(Event e) {
		isTouched = false;
		System.out.println("Touch end");
	}

	@Override
	public void handleTouchCancel(Event e) {
		isTouched = false;
		System.out.println("Touch cancel");

	}

	@Override
	public void handleTouchOut(Event e) {
		isTouched = false;
		System.out.println("Touch out");
	}


	public KeyboardControls getKeyboardControls() {
		return keyboardControls;
	}

}