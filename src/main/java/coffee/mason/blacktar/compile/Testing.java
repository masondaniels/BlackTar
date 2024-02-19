package coffee.mason.blacktar.compile;

import coffee.mason.blacktar.canvas.Canvas2D;
import coffee.mason.blacktar.canvas.controls.TouchControls;
import coffee.mason.blacktar.canvas.controls.impl.Camera;
import coffee.mason.blacktar.canvas.impl.CanvasGLImpl2;
import coffee.mason.blacktar.util.JavaScriptUtil;

public class Testing {

	public static void main(String[] args) {
		CanvasGLImpl2 glImpl = new CanvasGLImpl2(true);
		Canvas2D ui = new Canvas2D(true) {

			@Override
			public void update() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCanvasResize() {
				// TODO Auto-generated method stub

			}

			@Override
			public void loadBeforeAnimation() {
				// TODO Auto-generated method stub
			}

			@Override
			public void loadAfterAnimation() {
				// TODO Auto-generated method stub

			}

			@Override
			public void draw() {
				getCtx().clearRect(0, 0, getWidth(), 100*getDPI());
				getCtx().setFont(10 * getDPI() + "px sans-serif");
				getCtx().setFillStyle("white");

				Camera camera = glImpl.getCamera();

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
