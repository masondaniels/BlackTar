package coffee.mason.blacktar.canvas.webgl;

import java.util.Arrays;
import org.teavm.jso.typedarrays.Float32Array;

import coffee.mason.blacktar.linear.Vec3;
import coffee.mason.blacktar.util.Float32ArrayUtil;

public class Obj {
	private String name = "";
	private float[][] v; // vert coordinates in space
	private float[][] vn; // vert normals
	private float[][] vt; // vert textures
	private Float32Array triangles;
	private Float32Array normals;

	// This Obj class can't triangulate models with faces more than 3 arguments.
	// Make sure the face only has 3 arguments.

	public Obj(String... toParse) {
		parseStringToObj(toParse);
	}

	private void parseStringToObj(String[] toParse) {

		// Count
		int vc = 0;
		int vnc = 0;
		int vtc = 0;
		int fc = 0;

		for (int i = 0; i < toParse.length; i++) {
			String line = toParse[i].replaceAll("\n", "");
			switch (line.split(" ")[0]) {
			case "o":
				name = line.split(" ")[1];
				System.out.println("Name: " + name);
				break;
			case "v":
				vc++;
				break;
			case "vt":
				vtc++;
				break;
			case "vn":
				vnc++;
				break;

			case "f":
				fc++;
				break;

			default:
				break;
			}
		}

		v = new float[vc][3];
		vt = new float[vtc][2];
		vn = new float[vnc][3];

		// TODO: Ensure faces have 3 arguments.
		triangles = Float32Array.create(fc * 9);
		normals = Float32Array.create(fc * 9);

		vc = 0;
		vtc = 0;
		vnc = 0;

		// Set v, vn, vt
		for (int i = 0; i < toParse.length; i++) {
			String line = toParse[i].replaceAll("\n", "");
			switch (line.split(" ")[0]) {
			case "v":
				v[vc][0] = Float.parseFloat(line.split(" ")[1]);
				v[vc][1] = Float.parseFloat(line.split(" ")[2]);
				v[vc][2] = Float.parseFloat(line.split(" ")[3]);
//				System.out.println("v " + vc + " = " + v[vc][0] + " " + v[vc][1] + " " + v[vc][2]);
				vc++;

				break;
			case "vt":
				vt[vtc][0] = Float.parseFloat(line.split(" ")[1]);
				vt[vtc][1] = Float.parseFloat(line.split(" ")[2]);
//				System.out.println("vt " + vtc + " = " + vt[vtc][0] + " " + vt[vtc][1]);
				vtc++;
				break;
			case "vn":
				vn[vnc][0] = Float.parseFloat(line.split(" ")[1]);
				vn[vnc][1] = Float.parseFloat(line.split(" ")[2]);
				vn[vnc][2] = Float.parseFloat(line.split(" ")[3]);
//				System.out.println("vn " + vnc + " = " + vn[vnc][0] + " " + vn[vnc][1] + " " + vn[vnc][2]);
				vnc++;
				break;
			default:
				break;
			}
		}

		float[] trianglesTemp = new float[triangles.getLength()];
		//TODO: Load normals
		float[] normalsTemp = new float[triangles.getLength()];

		fc = 0;

		for (int i = 0; i < toParse.length; i++) {
			String line = toParse[i].replaceAll("\n", "");
			switch (line.split(" ")[0]) {
			case "f":
				// TODO: Check which face type is being used.
				// Currently using v/vt/vn
				float a = Float.parseFloat(line.split(" ")[1].split("/")[0] + "") - 1;
				float b = Float.parseFloat(line.split(" ")[2].split("/")[0] + "") - 1;
				float c = Float.parseFloat(line.split(" ")[3].split("/")[0] + "") - 1;

//				System.out.println("a = " + a);

				trianglesTemp[(fc * 9) + 0] = v[(int) a][0];
				trianglesTemp[(fc * 9) + 1] = v[(int) a][1];
				trianglesTemp[(fc * 9) + 2] = v[(int) a][2];

//				System.out.println("b = " + b);

				trianglesTemp[(fc * 9) + 3] = v[(int) b][0];
				trianglesTemp[(fc * 9) + 4] = v[(int) b][1];
				trianglesTemp[(fc * 9) + 5] = v[(int) b][2];

//				System.out.println("c = " + c);

				trianglesTemp[(fc * 9) + 6] = v[(int) c][0];
				trianglesTemp[(fc * 9) + 7] = v[(int) c][1];
				trianglesTemp[(fc * 9) + 8] = v[(int) c][2];

//				trianglesTemp[(fc * 9) + 2] = v[(int)c][2];

//				float d = Float.parseFloat(line.split(" ")[1].split("/")[2] + "");
//				float e = Float.parseFloat(line.split(" ")[2].split("/")[2] + "");
//				float f = Float.parseFloat(line.split(" ")[3].split("/")[2] + "");
//				
//				normalsTemp[(fc * 3) + 0] = vn[(int)d][0];
//				normalsTemp[(fc * 3) + 1] = vn[(int)e][1];
//				normalsTemp[(fc * 3) + 2] = vn[(int)f][2];
				fc++;
				break;
			default:
				break;
			}
		}

		triangles = (Float32Array) Float32ArrayUtil.of(trianglesTemp);
		normals = (Float32Array) Float32ArrayUtil.of(normalsTemp);

		if (trianglesTemp.length < 420) {
			System.out.println(name + " -> " + Arrays.toString(trianglesTemp));
		}

	}

	public Float32Array getTriangleFloats() {
		return triangles;
	}

	public Float32Array getNormalFloats() {
		return normals;
	}
	
	// If no normals are computed, compute normals
	public void computeNormals(Vec3 up) {
		normals = Float32ArrayUtil.create(triangles.getLength());
		float[] tempNormals = new float[triangles.getLength()];
		
//		Float32Array triangles = getTriangleFloats();
		for (int i = 0; i < triangles.getLength()/9; i++) {
			Vec3 v0 = new Vec3(triangles.get(i*9), triangles.get(i*9 + 1), triangles.get(i*9 + 2));
			Vec3 v1 = new Vec3(triangles.get(i*9 + 3), triangles.get(i*9 + 4), triangles.get(i*9 + 5));
			Vec3 v2 = new Vec3(triangles.get(i*9 + 6), triangles.get(i*9 + 7), triangles.get(i*9 + 8));
//			System.out.println("Triangle " + i + ":\n" + triangle.toString());
			
			//edge1 = v1 - v0
			//edge2 = v2 - v0
			//normal = cross(edge1, edge2)
			
			Vec3 edge0 = (Vec3) Vec3.sub(v1, v0);
			Vec3 edge1 = (Vec3) Vec3.sub(v2, v0);
			
			Vec3 normal = Vec3.cross(edge0, edge1).normalize();
//			System.out.println("Normal " + i + ":\n" + normal.toString());
			for (int j = 0; j < 9; j++) {
				tempNormals[i*9 + j] = normal.getValue(j%3);
			}
		}
		normals = Float32ArrayUtil.of(tempNormals);
		
	}

}
