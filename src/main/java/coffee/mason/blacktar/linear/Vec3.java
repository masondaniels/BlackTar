package coffee.mason.blacktar.linear;

import java.util.Iterator;

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
	
	public static Vec3 normalize(Vec3 v) {
		float magnitude = (float) Math.sqrt(Math.pow(v.getValue(0), 2) + Math.pow(v.getValue(1), 2) + Math.pow(v.getValue(2), 2));
		Vec3 returnable = new Vec3();
		if (magnitude != 0) {
			for (int i = 0; i < 3; i++) {
				returnable.setValue(i, v.getValue(i)/magnitude);
			}
			return returnable;
		} else {
			return v;
		}
	}
	
	public Vec3 normalize() {
		return Vec3.normalize(this);
	}
	
	public static float dot(Vec3 v, Vec3 w) {
		float sum = 0;
		for (int i = 0; i < 3; i++) {
			sum+= v.getValue(i) * w.getValue(i);
		}
		return sum;
	}

}
