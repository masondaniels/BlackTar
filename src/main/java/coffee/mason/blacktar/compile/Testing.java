package coffee.mason.blacktar.compile;

import org.teavm.jso.dom.html.HTMLDocument;

import coffee.mason.blacktar.canvas.Canvas2D;
import coffee.mason.blacktar.canvas.controls.impl.Camera;
import coffee.mason.blacktar.canvas.controls.impl.FpsKeyboardControls;
import coffee.mason.blacktar.canvas.impl.CanvasGLImpl2;
import coffee.mason.blacktar.linear.MatWxH;
import coffee.mason.blacktar.linear.Vec3;
import coffee.mason.blacktar.util.JavaScriptUtil;

public class Testing {

	private static HTMLDocument document;

	static {
		document = HTMLDocument.current();

	}

	public static void main(String[] args) {
		canvasGLImplTest(); // 3d

//		CanvasTouchImpl timpl = new CanvasTouchImpl(true); // touch

		testMatrices();
		testVecCross();
//		testCubeWinding();
	}

	private static void testVecCross() {
		Vec3 v = new Vec3(1, 0, 0);
		Vec3 w = new Vec3(0, 1, 0);

		System.out.println("[1, 0, 0] cross [0, 1, 0] = " + Vec3.cross(v, w));
	}

	private static void testMatrices() {
		MatWxH a = new MatWxH(3, 1);
		a.setValue(0, 0, 1);
		a.setValue(1, 0, 2);
		a.setValue(2, 0, 3);

		MatWxH b = new MatWxH(1, 3);
		b.setValue(0, 0, 1);
		b.setValue(0, 1, 2);
		b.setValue(0, 2, 1);

		System.out.println(a);
		System.out.println(b);

		MatWxH p = a.product(b);
		System.out.println("Product of a . b:\n" + p);

		Vec3 c = new Vec3(11, 12, -2);
		Vec3 d = new Vec3(-0.3f, -0.4f, 0.3f);

		System.out.println(c);
		System.out.println(d);

		System.out.println(c.add(d));

		MatWxH t = new MatWxH(2, 2);
		t.fill(3);

		System.out.println(t.product(t));

	}

	private static void canvasGLImplTest() {

		CanvasGLImpl2 glImpl = new CanvasGLImpl2(true);
		glImpl.setZIndex(1);

		Canvas2D view = new Canvas2D(true) {

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
				getCtx().clearRect(0, 0, getWidth(), 100);
				getCtx().setFillStyle("white");

				Camera camera = ((FpsKeyboardControls) glImpl.getKeyboardControls()).getCamera();

				String x = JavaScriptUtil.numToFixed(camera.getPosX(), 2);
				String y = JavaScriptUtil.numToFixed(camera.getPosY(), 2);
				String z = JavaScriptUtil.numToFixed(camera.getPosZ(), 2);
				getCtx().fillText("P = (" + x + ", " + y + ", " + z + ")", 10, 20);
				getCtx().fillText(
						"L = (" + JavaScriptUtil.numToFixed(camera.getViewDirection().getValue(0), 2) + ","
								+ JavaScriptUtil.numToFixed(camera.getViewDirection().getValue(1), 2) + ","
								+ JavaScriptUtil.numToFixed(camera.getViewDirection().getValue(2), 2) + ")",
						10, 30); // getRefreshCount(), getHeight() looked weird! like a loading bar, prefilled
			}
		};
		view.setZIndex(2);

	}

}
