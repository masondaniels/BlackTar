package coffee.mason.blacktar.compile;

import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.typedarrays.Float32Array;

import coffee.mason.blacktar.canvas.impl.CanvasGLImpl;
import coffee.mason.blacktar.canvas.impl.CanvasTouchImpl;
import coffee.mason.blacktar.canvas.webgl.GL;
import coffee.mason.blacktar.canvas.webgl.Mesh;
import coffee.mason.blacktar.javascript.Float32ArrayUtil;
import coffee.mason.blacktar.linear.MatWxH;
import coffee.mason.blacktar.linear.Vec3;

public class Testing {

	private static HTMLDocument document;

	static {
		document = HTMLDocument.current();

	}

	public static void main(String[] args) {
		canvasGLImplTest(); // 3d
		
		CanvasTouchImpl timpl = new CanvasTouchImpl(true); // touch
		
		testMatrices();
		testVecCross();
//		testCubeWinding();
	}
	
	private static void testVecCross() {
		Vec3 v = new Vec3(1,0,0);
		Vec3 w = new Vec3(0,1,0);
		
		System.out.println("[1, 0, 0] cross [0, 1, 0] = " + Vec3.cross(v, w));
	}
	
	private static void testCubeWinding() {
		Mesh m = new Mesh();
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
		m.setTriangles(array);
//		m.checkWinding();
//		m.fixWinding();
//		m.fixWinding();
//		m.checkWinding();
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

		CanvasGLImpl glImpl = new CanvasGLImpl(true);
		glImpl.setZIndex(1);

	}

}
