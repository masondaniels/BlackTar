package coffee.mason.blacktar.linear;

public class Vec3 extends VecN {

	public Vec3(float x, float y, float z) {
		super(x, y, z);
	}

	public Vec3() {
		super(3);
	}

	// for some reason teavm can't compile this, but it can compile the static
	// version
//	public Vec3 cross(Vec3 other) {
//		return Vec3.cross(this, other);
//	}

	public static Vec3 cross(Vec3 v, Vec3 w) {
		// 2 3 2 3
		// 3 1 3 1
		// 1 2 1 2

		// 1 2 1 2
		// 2 0 2 0
		// 0 1 0 1
		return new Vec3(v.getValue(1) * w.getValue(2) - w.getValue(1) * v.getValue(2),
				v.getValue(2) * w.getValue(0) - w.getValue(2) * v.getValue(0),
				v.getValue(0) * w.getValue(1) - w.getValue(0) * v.getValue(1));
	}

}
