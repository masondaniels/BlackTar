package coffee.mason.blacktar.canvas.impl;

import coffee.mason.blacktar.canvas.CanvasGL2;
import coffee.mason.blacktar.canvas.controls.TouchControls;
import coffee.mason.blacktar.canvas.controls.impl.Camera;
import coffee.mason.blacktar.canvas.controls.impl.FpsKeyboardControls;
import coffee.mason.blacktar.canvas.controls.impl.MCPETouchControls;
import coffee.mason.blacktar.canvas.webgl.GL;
import coffee.mason.blacktar.canvas.webgl.ObjStatic;
import coffee.mason.blacktar.canvas.webgl.Shader;
import coffee.mason.blacktar.linear.Mat4x4;
import coffee.mason.blacktar.util.JavaScriptUtil;

public class CanvasGLImpl2 extends CanvasGL2 {

	private static final String[] VERTEX = {
			"# version 300 es",
			"precision highp float;",
			"layout (location=0) in vec3 triPosition;",
			"layout (location=1) in vec3 triNormal;",
			"out vec3 triColor;",
			"uniform mat4 mProj;",
			"uniform mat4 mView;",
			"uniform float time;",
			"void main()",
			"{",
			    "vec3 ambientColor = vec3(0.7, 0.7, 0.7);",
			    "float ambientIntensity = max(dot(triNormal, vec3(0.0, 1.0, 0.0)), 0.0);", // Lambertian reflection model
			    "vec3 ambient = ambientColor * ambientIntensity;",
			    "vec3 rColor = vec3(min(tan(float(gl_InstanceID)), 0.2), min(cos(float(gl_InstanceID)), 0.3), min(sin(float(gl_InstanceID)), 0.2));",
			    "triColor = vec3(0.1, 0.1, 0.1) + ambient + rColor;",
			    "gl_Position = mProj * mView * vec4(triPosition.x + cos(float(gl_InstanceID)*time), triPosition.y, triPosition.z - float(gl_InstanceID)*5.0, 1.0);",
			"}"
	};
	
	private static final String[] FRAG = {
			"# version 300 es",
			"precision highp float;",
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

	private int projUniformLocation;
	private int viewUniformLocation;
	
	private int timeUniformLocation;

	private Camera camera;
	private FpsKeyboardControls keyboardControls;
	
	private MCPETouchControls touchControls;
	
	@Override
	public void loadBeforeAnimation() {
		
		setupShader();
		
		gl.uniform1f(timeUniformLocation, JavaScriptUtil.getElapsed().floatValue()/1000f);
		proj = Mat4x4.perspective((float) Math.toRadians(45), (float) (getWidth() / getHeight()), 0.1f, 1000f);
		
		camera = new Camera();
		
		keyboardControls = new FpsKeyboardControls(camera, gl, viewUniformLocation);
		camera.setPitch(-16f);
		camera.setYaw(272.4f);
		camera.setPosX(0.45f);
		camera.setPosY(7.5f);
		camera.setPosZ(12f);
		camera.updateViewDirection();
		
		uniformMatrix4fv();
		
		touchControls = new MCPETouchControls(gl, camera, viewUniformLocation);

	}

	private Shader shader;
	
	private void setupShader() {

		shader = new Shader(gl, VERTEX, FRAG);
		shader.useProgram();
		
		shader.createFloatAttrib(0, 3, ObjStatic.TEAPOT.getTriangleFloats());
		shader.createFloatAttrib(1, 3, ObjStatic.TEAPOT.getNormalFloats());

		// Enable depth testing & culling
		gl.frontFace(GL.CCW);
		gl.cullFace(GL.BACK);
		gl.enable(GL.DEPTH_TEST);

		projUniformLocation = shader.getUniformLocation("mProj");
		viewUniformLocation = shader.getUniformLocation("mView");
		timeUniformLocation = shader.getUniformLocation("time");
		
	}

	private void uniformMatrix4fv() {
		gl.uniformMatrix4fv(projUniformLocation, false, proj.getArray());
		gl.uniformMatrix4fv(viewUniformLocation, false, camera.getViewMatrix().getArray());
	}

	@Override
	public void loadAfterAnimation() {

	}
	
	private void onResize() {
		gl.viewport(0, 0, (float) getWidth(), (float) getHeight());
		proj = Mat4x4.perspective((float) Math.toRadians(45), (float) (getWidth() / getHeight()), 0.1f, 1000f);
		uniformMatrix4fv();
		System.out.println("Updated projection matrix:\n" + proj.toString());
	}

	@Override
	public void update() {
		gl.uniform1f(timeUniformLocation, JavaScriptUtil.getElapsed().floatValue()/10000f);
		keyboardControls.update();
		touchControls.update();
	}

	@Override
	public void draw() {
		gl.clearColor(0f, 0f, 0f, 1f);
		gl.clear(GL.COLOR_BUFFER_BIT | GL.DEPTH_BUFFER_BIT);

		gl.drawArraysInstanced(GL.TRIANGLES, 0, ObjStatic.TEAPOT.getTriangleFloats().getLength()/3, 50);

	}

	@Override
	public void onCanvasResize() {
		onResize();
	}

	public Camera getCamera() {
		return camera;
	}

	public TouchControls getTouchControls() {
		return touchControls;
	}

}