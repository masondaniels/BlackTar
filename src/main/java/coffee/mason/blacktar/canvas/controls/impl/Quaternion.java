package coffee.mason.blacktar.canvas.controls.impl;

import java.util.Iterator;

import coffee.mason.blacktar.linear.Mat4x4;
import coffee.mason.blacktar.linear.Vec3;

public class Quaternion {
	private float x, y, z, w;

	public Quaternion() {
		this(0, 0, 0, 1);
	}

	public Quaternion(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public static Quaternion fromEulerAngles(float pitch, float yaw, float roll) {
		float cy = (float) Math.cos(yaw * 0.5);
		float sy = (float) Math.sin(yaw * 0.5);
		float cp = (float) Math.cos(pitch * 0.5);
		float sp = (float) Math.sin(pitch * 0.5);
		float cr = (float) Math.cos(roll * 0.5);
		float sr = (float) Math.sin(roll * 0.5);

		float qw = cy * cp * cr + sy * sp * sr;
		float qx = cy * cp * sr - sy * sp * cr;
		float qy = sy * cp * sr + cy * sp * cr;
		float qz = sy * cp * cr - cy * sp * sr;

		return new Quaternion(qx, qy, qz, qw);
	}

	public Quaternion multiply(Quaternion other) {
		float nx = w * other.x + x * other.w + y * other.z - z * other.y;
		float ny = w * other.y + y * other.w + z * other.x - x * other.z;
		float nz = w * other.z + z * other.w + x * other.y - y * other.x;
		float nw = w * other.w - x * other.x - y * other.y - z * other.z;

		return new Quaternion(nx, ny, nz, nw);
	}

	public Mat4x4 toMatrix() {
		Mat4x4 returnable = new Mat4x4();
		
		float[] matrix = new float[16];

		float xx = x * x;
		float xy = x * y;
		float xz = x * z;
		float xw = x * w;
		float yy = y * y;
		float yz = y * z;
		float yw = y * w;
		float zz = z * z;
		float zw = z * w;

		matrix[0] = 1 - 2 * (yy + zz);
		matrix[1] = 2 * (xy - zw);
		matrix[2] = 2 * (xz + yw);

		matrix[4] = 2 * (xy + zw);
		matrix[5] = 1 - 2 * (xx + zz);
		matrix[6] = 2 * (yz - xw);

		matrix[8] = 2 * (xz - yw);
		matrix[9] = 2 * (yz + xw);
		matrix[10] = 1 - 2 * (xx + yy);

		matrix[15] = 1;
		
		
		for (int i = 0; i < 16; i++) {
			returnable.setValue(i, matrix[i]);
		}

		return returnable;
	}

	public Quaternion conjugate() {
		return new Quaternion(-x, -y, -z, w);
	}

	public Vec3 toEulerAngles() {
		float[] angles = new float[3];

		float sinr_cosp = 2 * (w * x + y * z);
		float cosr_cosp = 1 - 2 * (x * x + y * y);
		angles[0] = (float) Math.atan2(sinr_cosp, cosr_cosp);

		float sinp = 2 * (w * y - z * x);
		if (Math.abs(sinp) >= 1)
			angles[1] = (float) Math.copySign(Math.PI / 2, sinp); // use 90 degrees if out of range
		else
			angles[1] = (float) Math.asin(sinp);

		float siny_cosp = 2 * (w * z + x * y);
		float cosy_cosp = 1 - 2 * (y * y + z * z);
		angles[2] = (float) Math.atan2(siny_cosp, cosy_cosp);

		return new Vec3(angles[0], angles[1], angles[2]);
	}
	
    public static Quaternion toQuaternion(Vec3 v) {
        return new Quaternion(v.getValue(0), v.getValue(1),v.getValue(2), 0);
    }
	
    public Vec3 rotateVector(Vec3 vector) {
        Quaternion conjugate = conjugate();
        Quaternion wxyz = this.multiply(Quaternion.toQuaternion(vector)).multiply(conjugate);
        return new Vec3(wxyz.x, wxyz.y, wxyz.z);
    }
    
    public Quaternion normalize() {
        float magnitude = (float) Math.sqrt(x * x + y * y + z * z + w * w);
        return new Quaternion(x / magnitude, y / magnitude, z / magnitude, w / magnitude);
    }
}
