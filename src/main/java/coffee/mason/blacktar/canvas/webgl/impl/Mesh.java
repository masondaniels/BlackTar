package coffee.mason.blacktar.canvas.webgl.impl;

import java.util.ArrayList;
import java.util.List;

import coffee.mason.blacktar.canvas.controls.impl.Camera;
import coffee.mason.blacktar.canvas.webgl.BufferInformation;
import coffee.mason.blacktar.canvas.webgl.Obj;
import coffee.mason.blacktar.canvas.webgl.Shader;
import coffee.mason.blacktar.canvas.webgl.UniformInformation;
import coffee.mason.blacktar.canvas.webgl.WebGLContext;
import coffee.mason.blacktar.util.Float32ArrayUtil;
import coffee.mason.blacktar.util.WebGLUtil;

public class Mesh {

	// Mesh has buffers, textures, obj, etc.
	private Obj obj;
	private BufferInformation[] buffers;

	private ArrayList<MeshInstance> instances = new ArrayList<MeshInstance>();

	public Mesh(WebGLContext gl, Obj obj) {
		setObj(obj);
		createBuffers(gl);
	}

	private void createBuffers(WebGLContext gl) {
		obj.computeNormals(Camera.up);
		setBuffers(WebGLUtil.createFloatBuffer(gl, 0, 3, obj.getTriangleFloats()),
				WebGLUtil.createFloatBuffer(gl, 1, 3, obj.getNormalFloats()));
	}

	public Obj getObj() {
		return obj;
	}

	public void setObj(Obj obj) {
		this.obj = obj;
	}

	public BufferInformation[] getBuffers() {
		return buffers;
	}

	public void setBuffers(BufferInformation... buffers) {
		this.buffers = buffers;
	}

	public void addInstance(MeshInstance m) {
		instances.add(m);
	}

	public List<MeshInstance> getInstances() {
		return instances;
	}

	// Draws all mesh instances to shader
	public void drawInstances(Shader shader) {
		shader.drawObj(obj, buffers, generateUniforms("modelPosition"), instances.size());
	}

	
	//TODO: Cache f32 array
	
	private int gi;
	private UniformInformation[] generateUniforms(String positionName) {
		float[] pos = new float[instances.size() * 3];
		gi = 0;
		instances.iterator().forEachRemaining(e -> {
			pos[gi*3 + 0] = e.getLocation().getValue(0);
			pos[gi*3 + 1] = e.getLocation().getValue(1);
			pos[gi*3 + 2] = e.getLocation().getValue(2);
			gi++;
		});
		
		UniformInformation position = new UniformInformation(positionName, Float32ArrayUtil.of(pos));
		return new UniformInformation[]{position};
	}

}
