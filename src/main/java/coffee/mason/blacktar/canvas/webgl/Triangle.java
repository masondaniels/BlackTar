package coffee.mason.blacktar.canvas.webgl;

import coffee.mason.blacktar.linear.Vec3;

public class Triangle {

	private Vec3 a;
	private Vec3 b;
	private Vec3 c;
	
	public Triangle(Vec3 a, Vec3 b, Vec3 c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	public Vec3 getA() {
		return a;
	}
	
	public Vec3 getB() {
		return b;
	}
	
	public Vec3 getC() {
		return c;
	}

	public void swapBC() {
		Vec3 co = c;
		c = b;
		b = co;
	}
	
}
