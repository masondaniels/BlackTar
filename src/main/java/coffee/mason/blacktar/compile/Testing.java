package coffee.mason.blacktar.compile;

import javax.naming.OperationNotSupportedException;

import coffee.mason.blacktar.canvas.Canvas2D;
import coffee.mason.blacktar.canvas.controls.TouchControls;
import coffee.mason.blacktar.canvas.controls.impl.Camera;
import coffee.mason.blacktar.canvas.impl.CanvasGLImpl2;
import coffee.mason.blacktar.canvas.webgl.Obj;
import coffee.mason.blacktar.canvas.webgl.ObjStatic;
import coffee.mason.blacktar.html.ErrorBox;
import coffee.mason.blacktar.util.JavaScriptUtil;
import coffee.mason.blacktar.web.files.FileLoader;

public class Testing {

	public static void main(String[] args) {

		new ErrorBox();

		new FileLoader("teapot.obj", "cube.obj") {

			@Override
			public void onComplete(String[] content) {

				ObjStatic.TEAPOT = new Obj(content[0].split("\n"));
				ObjStatic.TEAPOT.computeNormals(Camera.up);

				ObjStatic.CUBE = new Obj(content[1].split("\n"));
				ObjStatic.CUBE.computeNormals(Camera.up);

				init();
			}
		};

	}

	private static void init() {
		CanvasGLImpl2 glImpl = new CanvasGLImpl2(true);
		Canvas2D ui = new Canvas2D(true) {

			@Override
			public void update() {

			}

			@Override
			public void onCanvasResize() {

			}

			@Override
			public void loadBeforeAnimation() {

			}

			@Override
			public void loadAfterAnimation() {

			}

			@Override
			public void draw() {
				getCtx().clearRect(0, 0, getWidth(), 100 * getDPI());
				getCtx().setFont(10 * getDPI() + "px sans-serif");
				getCtx().setFillStyle("white");

				Camera camera = glImpl.getScene().getCamera();

				String x = JavaScriptUtil.numToFixed(camera.getPosX(), 2);
				String y = JavaScriptUtil.numToFixed(camera.getPosY(), 2);
				String z = JavaScriptUtil.numToFixed(camera.getPosZ(), 2);
				getCtx().fillText("FPS = (" + glImpl.getFps() + ")", 10 * getDPI(), 20 * getDPI());
				getCtx().fillText("P = (" + x + ", " + y + ", " + z + ")", 10 * getDPI(), 32 * getDPI());
				getCtx().fillText(
						"L = (" + JavaScriptUtil.numToFixed(camera.getViewDirection().getValue(0), 2) + ","
								+ JavaScriptUtil.numToFixed(camera.getViewDirection().getValue(1), 2) + ","
								+ JavaScriptUtil.numToFixed(camera.getViewDirection().getValue(2), 2) + ")",
						10 * getDPI(), 44 * getDPI());
				getCtx().fillText("Yaw = (" + JavaScriptUtil.numToFixed(camera.getYaw(), 2) + ")", 10 * getDPI(),
						56 * getDPI());
				getCtx().fillText("Pitch = (" + JavaScriptUtil.numToFixed(camera.getPitch(), 2) + ")", 10 * getDPI(),
						68 * getDPI());
			}
		};
		glImpl.setZIndex(1);
		ui.setZIndex(2);

		TouchControls.register(glImpl.getTouchControls(), ui);
	}

}
