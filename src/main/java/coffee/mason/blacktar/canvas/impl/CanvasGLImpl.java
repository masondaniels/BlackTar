package coffee.mason.blacktar.canvas.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.KeyboardEvent;
import org.teavm.jso.dom.events.MouseEvent;
import org.teavm.jso.typedarrays.Float32Array;

import coffee.mason.blacktar.canvas.CanvasGL;
import coffee.mason.blacktar.canvas.controls.KeyboardControls;
import coffee.mason.blacktar.canvas.controls.TouchControls;
import coffee.mason.blacktar.canvas.webgl.GL;
import coffee.mason.blacktar.javascript.Float32ArrayUtil;
import coffee.mason.blacktar.linear.Mat4x4;
import coffee.mason.blacktar.util.JavaScriptUtil;
import coffee.mason.blacktar.util.StringUtil;

public class CanvasGLImpl extends CanvasGL implements TouchControls, KeyboardControls {

	private static final String[] VERTEX = {
			"precision mediump float;",
			"attribute vec3 triPosition;",
			"varying vec3 triColor;",
//			"attribute vec3 triNormal;",
			"uniform mat4 mProj;",
			"uniform mat4 mView;",
			"uniform mat4 mWorld;",
			"void main()",
			"{",
				"triColor = vec3(triPosition.x + 0.2, triPosition.y + 0.1, triPosition.z + 0.4);",
				"gl_Position = mProj * mView * mWorld * vec4(triPosition, 1.0);",
			"}"
	};
	
	private static final String[] FRAG = {
			"precision mediump float;",
			"varying vec3 triColor;",
			"void main()",
			"{",
				"gl_FragColor = vec4(triColor, 1.0);",
			"}"
	};
	
	public CanvasGLImpl(boolean fullscreen) {
		super(fullscreen);
	}
	
	private Mat4x4 proj;
	private Float32Array world;
//	private Float32Array view;
	private Mat4x4 view;
	
	private Mat4x4 identity;
	
	private int projUniformLocation;
	private int viewUniformLocation;
	private int worldUniformLocation;

	@Override
	public void loadBeforeAnimation() {
		
		TouchControls.register(this, this);
		KeyboardControls.register(this);

		world = (Float32Array) Float32Array.create(16);
//		view = (Float32Array) Float32Array.create(16);
		view = Mat4x4.identity();
		identity = Mat4x4.identity();
		
		setupShader();

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

		Float32Array array = (Float32Array) Float32ArrayUtil.of(
				
				// front face
				0.5f, 0.5f, 0f,
				-0.5f, -0.5f, 0f,
				0.5f, -0.5f, 0f,
				-0.5f, 0.5f, 0f,
				-0.5f, -0.5f, 0f,
				0.5f, 0.5f, 0f,
				
				// back face
				0.5f, 0.5f, 1f,
				-0.5f, -0.5f, 1f,
				0.5f, -0.5f, 1f,
				-0.5f, 0.5f, 1f,
				-0.5f, -0.5f, 1f,
				0.5f, 0.5f, 1f,
				
				// bottom face
				0.5f, -0.5f, 0f,
				-0.5f, -0.5f, 0f,
				-0.5f, -0.5f, 1f,
				0.5f, -0.5f, 0f,
				-0.5f, -0.5f, 1f,
				0.5f, -0.5f, 1f,
				
				// top face
				0.5f, 0.5f, 0f,
				-0.5f, 0.5f, 0f,
				-0.5f, 0.5f, 1f,
				0.5f, 0.5f, 0f,
				-0.5f, 0.5f, 1f,
				0.5f, 0.5f, 1f,
				
				// left face
				-0.5f, -0.5f, 0f,
				-0.5f, 0.5f, 0f,
				-0.5f, 0.5f, 1f,
				
				// right face
				0.5f, 0.5f, 0f,
				0.5f, -0.5f, 0f,
				0.5f, -0.5f, 1f,
				
				0.5f, -0.5f, 1f,
				0.5f, 0.5f, 1f,
				0.5f, 0.5f, 0f
				
				);

		JSObject floatBuffer = gl.createBuffer();

		// Set as active buffer.
		gl.bindBuffer(GL.ARRAY_BUFFER, floatBuffer);

		// Put data in active buffer.
		gl.bufferData(GL.ARRAY_BUFFER, array, GL.STATIC_DRAW);

		int triPositionAttrib = gl.getAttribLocation(program, "triPosition");

		// Setting up attrib
		// index, size (# of elements per attrib), type, normalized, stride, offset
		gl.vertexAttribPointer(triPositionAttrib, 3, GL.FLOAT, false, 3 * 4, 0);
		gl.enableVertexAttribArray(triPositionAttrib);
		
		// TODO: Create normals
		
//		int triNormalAttrib = gl.getAttribLocation(program, "triNormal");
//		
//		gl.vertexAttribPointer(triNormalAttrib, 1, GL.FLOAT, false, 3*4, 0);
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
		
		view.setValue(2, 3, -3.5f);
		
		proj = Mat4x4.perspective((float) Math.toRadians(45), (float) (getWidth() / getHeight()), 0.1f, 1000f);

		world = identity.getArray();

		uniformMatrix4fv();

	}

	private void uniformMatrix4fv() {
		gl.uniformMatrix4fv(projUniformLocation, false, proj.getArray());
		gl.uniformMatrix4fv(viewUniformLocation, false, view.getArray());
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

	}

	
	@Override
	public void draw() {
		gl.clearColor(0f, 0f, 0f, 1f);
		gl.clear(GL.COLOR_BUFFER_BIT | GL.DEPTH_BUFFER_BIT);

		gl.drawArrays(GL.TRIANGLES, 0, 18+6+3+6);
		
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

//	@Override
//	public void handleKeyDown(Event e) {
//		System.out.println("Key down:" + ((KeyboardEvent)e).getCharCode());
//
//		
//	}
//
//	@Override
//	public void handleKeyUp(Event e) {
//		System.out.println("Key up:" + ((KeyboardEvent)e).getCharCode());
//
//	}
	
	private static final int VK_W = 119;
	private static final int VK_A = 97;
	private static final int VK_S = 115;
	private static final int VK_D = 100;
	
	private static final int VK_SPACE = 32;

	@Override
	public void handleKeyPress(Event e) {
		int value = ((KeyboardEvent)e).getCharCode();
		System.out.println("Key pressed: " + value);

		switch (value) {
		case VK_W:
			view.setValue(14, view.getValue(14)+0.1f);
			gl().uniformMatrix4fv(viewUniformLocation, false, view.getArray());
			break;
		case VK_A:
			view.setValue(12, view.getValue(12)+0.1f);
			gl().uniformMatrix4fv(viewUniformLocation, false, view.getArray());
			break;
		case VK_S:
			view.setValue(14, view.getValue(14)-0.1f);
			gl().uniformMatrix4fv(viewUniformLocation, false, view.getArray());
			break;
		case VK_D:
			view.setValue(12, view.getValue(12)-0.1f);
			gl().uniformMatrix4fv(viewUniformLocation, false, view.getArray());
			break;
		case VK_SPACE:
			view.setValue(13, view.getValue(13)-0.1f);
			gl().uniformMatrix4fv(viewUniformLocation, false, view.getArray());
			break;

		default:
			break;
		}
		
	}

	private HashMap<String, Object> keysDown = new HashMap<String, Object>();
	
	@Override
	public void handleKeyDown(Event e) {
		System.out.println("Key down: "+ ((KeyboardEvent) e).getKey());
		keysDown.put(((KeyboardEvent) e).getKey().toLowerCase(), null);
		if (((KeyboardEvent) e).isShiftKey()) {
			view.setValue(13, view.getValue(13)+0.1f);
			gl().uniformMatrix4fv(viewUniformLocation, false, view.getArray());
		}
		System.out.println("Keys down: " + keysDown.size());
	}

	@Override
	public void handleKeyUp(Event e) {
		keysDown.remove(((KeyboardEvent) e).getKey().toLowerCase());
	}

	public Mat4x4 getView() {
		return view;
	}
	
}