package coffee.mason.blacktar.linear;

public class Mat4x4 extends MatWxH {

	public Mat4x4() {
		super(4, 4);
	}
	
	// fov in radians
	public static Mat4x4 perspective(float fov, float aspect, float near, float far) {
		
		Mat4x4 returnable = new Mat4x4();
		float a = (float) Math.tan(fov/2);
		returnable.setValue(0, 0, 1f/(aspect*a));
		returnable.setValue(1, 1, 1f/a);
		returnable.setValue(2, 2, -((far+near)/(far-near)));
		returnable.setValue(3, 2, -1f); // -2f*((far+near)/(far-near))
		returnable.setValue(2, 3, -0.2f);
		
		return returnable;
		
	}
	
	// Derminant for only the 3x3 dimensions in the 4x4.
	public float det3x3() {
		float a = getValue(0);
		float b = getValue(1);
		float c = getValue(2);
		float d = getValue(4);
		float e = getValue(5);
		float f = getValue(6);
		float g = getValue(8);
		float h = getValue(9);
		float i = getValue(10);
		return (a*e*i - a*f*h - b*d*c + b*f*g + c*d*f - c*e*g);
	}


	public static Mat4x4 identity() {
		Mat4x4 returnable = new Mat4x4();
		for (int i = 0; i < 4; i++) {
			returnable.setValue(i, i, 1f);
		}
		return returnable;
	}

}
