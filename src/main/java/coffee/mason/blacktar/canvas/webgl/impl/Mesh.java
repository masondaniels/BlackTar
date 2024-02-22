package coffee.mason.blacktar.canvas.webgl.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.teavm.jso.core.JSNumber;

import coffee.mason.blacktar.canvas.controls.impl.Camera;
import coffee.mason.blacktar.canvas.webgl.BufferInformation;
import coffee.mason.blacktar.canvas.webgl.GL;
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

	private WebGLContext gl;

	public Mesh(WebGLContext gl, Obj obj) {
		this.gl = gl;
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

		// Keep in mind vertex uniforms
		// This is a bit hardcoded...
		// TODO: FIX HARDCODING
		int vectors = (((JSNumber) (gl.getParameter(GL.MAX_VERTEX_UNIFORM_VECTORS))).intValue() - 8);

		int drawCalls = (int) Math.ceil(instances.size() / ((double) vectors));

		for (int i = 0; i < drawCalls; i++) {
			int chunk = (i == drawCalls - 1) ? instances.size() % vectors : vectors;
			shader.drawObj(obj, buffers, generateUniforms("modelPosition", chunk, chunk * i), chunk);
		}

	}

	// TODO: Cache f32 array

	private int generateUniformsIteratorIndex;

	private UniformInformation[] generateUniforms(String positionName, int size, int start) {
		float[] pos = new float[size * 3];
		generateUniformsIteratorIndex = 0;

		Iterator<MeshInstance> itr = instances.iterator();

		while (itr.hasNext()) {
			if (generateUniformsIteratorIndex >= start) {
				MeshInstance instance = itr.next();
				int relativeIndex = generateUniformsIteratorIndex - start;
				pos[relativeIndex * 3 + 0] = instance.getLocation().getValue(0);
				pos[relativeIndex * 3 + 1] = instance.getLocation().getValue(1);
				pos[relativeIndex * 3 + 2] = instance.getLocation().getValue(2);
			}

			generateUniformsIteratorIndex++;
			if (generateUniformsIteratorIndex > (size + start)) {
				break;
			}
		}

		UniformInformation position = new UniformInformation(positionName, Float32ArrayUtil.of(pos));
		return new UniformInformation[] { position };
	}

}
