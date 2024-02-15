package coffee.mason.blacktar.canvas.webgl;

import org.teavm.jso.typedarrays.Float32Array;

import coffee.mason.blacktar.javascript.Float32ArrayUtil;
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

	public static final Float32Array CUBE = (Float32Array) Float32ArrayUtil.of(
			
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
			
			-0.5f, 0.5f, 1f,
			-0.5f, -0.5f, 1f,
			-0.5f, -0.5f, 0f,
			
			// right face
			0.5f, 0.5f, 0f,
			0.5f, -0.5f, 0f,
			0.5f, -0.5f, 1f,
			
			0.5f, -0.5f, 1f,
			0.5f, 0.5f, 1f,
			0.5f, 0.5f, 0f
			
			);
	
	
}
