package coffee.mason.blacktar.canvas.impl;

import coffee.mason.blacktar.canvas.CanvasGL2;
import coffee.mason.blacktar.canvas.controls.TouchControls;
import coffee.mason.blacktar.canvas.controls.impl.FpsKeyboardControls;
import coffee.mason.blacktar.canvas.controls.impl.MCPETouchControls;
import coffee.mason.blacktar.canvas.webgl.GL;
import coffee.mason.blacktar.canvas.webgl.ObjStatic;
import coffee.mason.blacktar.canvas.webgl.impl.Mesh;
import coffee.mason.blacktar.canvas.webgl.impl.MeshInstance;
import coffee.mason.blacktar.canvas.webgl.impl.Scene;
import coffee.mason.blacktar.linear.Vec3;
import coffee.mason.blacktar.util.JavaScriptUtil;

public class CanvasGLImpl2 extends CanvasGL2 {

	private Scene scene;

	public CanvasGLImpl2(boolean fullscreen) {
		super(fullscreen);

	}

	@Override
	public void update() {
		scene.update();
		keyboardControls.update();
		touchControls.update();

		moveInstances();
	}

	private void moveInstances() {
		MeshInstance instance = scene.getMesh("teapot").getInstances().get(0);
		instance.setLocation(
				(Vec3) (unitCircle(JavaScriptUtil.getElapsed().floatValue() / 100f).add(new Vec3(10f, 3f, 10f))));

		MeshInstance instance1 = scene.getMesh("teapot").getInstances().get(1);
		instance1.setLocation((Vec3) new Vec3(1.5f, 0, 0)
				.add(new Vec3((float) Math.cos(JavaScriptUtil.getElapsed().floatValue() / 200f) * 3, 0, 0)));

		MeshInstance instance2 = scene.getMesh("teapot").getInstances().get(2);
		instance2.setLocation((Vec3) new Vec3(1.2f, 1.4f, 0.3f)
				.add(new Vec3(0, 0, (float) Math.cos(JavaScriptUtil.getElapsed().floatValue() / 400f) * 5)));

		int i = 0;
		for (int x = 0; x < 50; x++) {
			for (int y = 0; y < 10; y++) {
				for (int z = 0; z < 10; z++) {
					scene.getMesh("cube").getInstances().get(i).setLocation(new Vec3(
							(float) (x * 5 + Math.cos((float) x * JavaScriptUtil.getElapsed().floatValue() / 1000f)),
							(float) (y * 5
									+ Math.sin((float) x * JavaScriptUtil.getElapsed().floatValue() / (900f * i))),
							(float) (z * 5
									+ Math.sin((float) (z) * JavaScriptUtil.getElapsed().floatValue() / 1200f))));
					i++;
				}
			}
		}

	}

	private Vec3 unitCircle(float f) {
		return new Vec3((float) Math.cos(f), (float) Math.sin(f), (float) Math.sin(f));
	}

	@Override
	public void draw() {
		gl.clearColor(0.7f, 0.7f, 0.7f, 1f);
		gl.clear(GL.COLOR_BUFFER_BIT | GL.DEPTH_BUFFER_BIT);
		scene.draw();
	}

	private FpsKeyboardControls keyboardControls;
	private MCPETouchControls touchControls;

	@Override
	public void loadBeforeAnimation() {

		scene = new Scene(gl, (float) getWidth(), (float) getHeight());

		Mesh cubeMesh = new Mesh(gl, ObjStatic.CUBE);
		scene.putMesh("cube", cubeMesh);

		Mesh teapotMesh = new Mesh(gl, ObjStatic.TEAPOT);
		scene.putMesh("teapot", teapotMesh);

		scene.createInstance("teapot", new MeshInstance(new Vec3(0, 0, 0)));

		scene.createInstance("teapot", new MeshInstance(new Vec3(1.5f, 0, 0)));

		scene.createInstance("teapot", new MeshInstance(new Vec3(1.2f, 1.4f, 0.3f)));
		
		for (int x = 0; x < 50; x++) {
			for (int y = 0; y < 10; y++) {
				for (int z = 0; z < 10; z++) {
					scene.createInstance("cube",
							new MeshInstance(new Vec3((float) (x * 5) + 20, (float) y * 5, (float) (z * 5) + 20)));
				}
			}
		}

		scene.resize((float) getWidth(), (float) getHeight());

		keyboardControls = new FpsKeyboardControls(gl, scene.getCamera(),
				scene.getShader().getUniformLocation("mView"));
		touchControls = new MCPETouchControls(gl, scene.getCamera(), scene.getShader().getUniformLocation("mView"));

	}

	@Override
	public void loadAfterAnimation() {
		scene.resize((float) getWidth(), (float) getHeight());

	}

	@Override
	public void onCanvasResize() {
		scene.resize((float) getWidth(), (float) getHeight());
	}

	public Scene getScene() {
		return scene;
	}

	public TouchControls getTouchControls() {
		return touchControls;
	}

}
