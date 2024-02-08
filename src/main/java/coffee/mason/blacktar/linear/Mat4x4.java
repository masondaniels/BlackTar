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

}
