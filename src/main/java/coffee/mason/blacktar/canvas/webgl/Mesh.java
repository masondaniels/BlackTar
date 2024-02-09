package coffee.mason.blacktar.canvas.webgl;

import java.util.Iterator;

import org.teavm.jso.typedarrays.Float32Array;

import coffee.mason.blacktar.linear.Mat4x4;
import coffee.mason.blacktar.linear.MatWxH;
import coffee.mason.blacktar.linear.Vec3;

public class Mesh {

	private Float32Array floats;
	private Triangle[] triangles;

	public void setTriangles(Float32Array floats) {
		this.floats = floats;
		createTriangles(); // This is done for CPU purposes. Not GPU purposes.
	}

	private void createTriangles() {
		if (floats.getLength() % 9 != 0) {
			throw new UnsupportedOperationException("Could not create triangles. Triangle points are not mod 3 == 0.");
		}
		triangles = new Triangle[floats.getLength() / 9];
		for (int i = 0; i < floats.getLength(); i += 9) {
			triangles[i / 9] = new Triangle(new Vec3(floats.get(i), floats.get(i + 1), floats.get(i + 2)),
					new Vec3(floats.get(i + 3), floats.get(i + 4), floats.get(i + 5)),
					new Vec3(floats.get(i + 6), floats.get(i + 7), floats.get(i + 8)));
		}
	}

//	public void checkWinding() {
//		// TODO: Implement & understand linear algebra using 3b1b.
//		// https://stackoverflow.com/questions/9120032/determine-winding-of-a-2d-triangles-after-triangulation
//		// For a triangle A B C, you can find the winding by computing the cross product
//		// (B - A) x (C - A). For 2d tri's, with z=0, it will only have a z component.
//		// To give all the same winding, swap vertices C and B if this z component is
//		// negative.
//
//		boolean sameWinding = true;
//		boolean orientation = false;
//		for (int i = 0; i < triangles.length; i++) {
//			Triangle t = triangles[i];
//			Vec3 a = (Vec3) t.getB().sub(t.getA());
//			Vec3 b = (Vec3) t.getC().sub(t.getA());
//
//			Vec3 res = Vec3.cross(a, b);
//
//			boolean currentOrientation = res.getValue(2) > 0;
//			if (i == 0) {
//				orientation = currentOrientation;
//			} else {
//				if (currentOrientation != orientation) {
//					sameWinding = false;
//				}
//			}
//			System.out.println("Winding on triangle " + i + ": " + (currentOrientation ? "CCW" : "CW"));
//		}
//
//		System.out.println("Winding on mesh is " + (sameWinding ? "the same!" : "not the same."));
//
//	}
//// int winding
//	public void fixWinding() {
//		System.out.println("Attempting to fix winding.");
//		for (int i = 0; i < triangles.length; i++) {
//			Triangle t = triangles[i];
//			Vec3 a = (Vec3) t.getB().sub(t.getA());
//			Vec3 b = (Vec3) t.getC().sub(t.getA());
//
//			Vec3 res = Vec3.cross(a, b);
//
//			Mat4x4 mat = new Mat4x4();
//			mat.fill(1);
//			mat.setValue(0, 0, a.getValue(0));
//			mat.setValue(0, 1, a.getValue(1));
//			mat.setValue(0, 2, a.getValue(2));
//			mat.setValue(1, 0, b.getValue(0));
//			mat.setValue(1, 1, b.getValue(1));
//			mat.setValue(1, 2, b.getValue(2));
//			
//			if (mat.det3x3() > 0) {
//				System.out.println("CCW");
//			} else {
//				System.out.println("CW");
//			}
//			
////			// Change winding
////			if (winding == GL.CW) {
//				if (!(res.getValue(2) < 0)) {
//					triangles[i].swapBC();
//					System.out.println("Changed winding on triangle " + i);
//				}
////			} else {
////				if (!(res.getValue(2) >= 0)) {
////					triangles[i].swapBC();
////					System.out.println("Changed winding on triangle " + i);
////				}
////			}
//		}
//	}
//
//	// is almost equal
//	private boolean isAlmostEqual(float a, float b, double epsilon) {
//		return Math.abs(a - b) < epsilon;
//	}
}
