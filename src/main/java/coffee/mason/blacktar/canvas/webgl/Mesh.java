package coffee.mason.blacktar.canvas.webgl;

import org.teavm.jso.typedarrays.Float32Array;

public class Mesh {

	private Float32Array triangles;

	public void setTriangles(Float32Array triangles) {
		this.triangles = triangles;
	}

	public void checkWinding() {
		//	TODO: Implement & understand linear algebra using 3b1b.
		//	https://stackoverflow.com/questions/9120032/determine-winding-of-a-2d-triangles-after-triangulation
		//	For a triangle A B C, you can find the winding by computing the cross product (B - A) x (C - A). For 2d tri's, with z=0, it will only have a z component.
		//	To give all the same winding, swap vertices C and B if this z component is negative.

	}

}
