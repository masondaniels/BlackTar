package coffee.mason.blacktar.canvas.webgl.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import coffee.mason.blacktar.canvas.controls.impl.Camera;
import coffee.mason.blacktar.canvas.webgl.GL;
import coffee.mason.blacktar.canvas.webgl.Shader;
import coffee.mason.blacktar.canvas.webgl.WebGLContext;
import coffee.mason.blacktar.component.Drawable;
import coffee.mason.blacktar.component.Updatable;
import coffee.mason.blacktar.linear.Mat4x4;

public class Scene implements Drawable, Updatable {

	private WebGLContext gl;
	private DrawableShader shader;

	private Camera camera;

	private Mat4x4 proj;

	private float width, height;

	public Scene(WebGLContext gl, float width, float height) {
		this.gl = gl;
		this.shader = new DrawableShader(gl);
		this.width = width;
		this.height = height;
		camera = new Camera();
		camera.setPitch(-16f);
		camera.setYaw(272.4f);
		camera.setPosX(0.45f);
		camera.setPosY(7.5f);
		camera.setPosZ(12f);
		camera.updateViewDirection();
		updateUniforms();
	}

	private HashMap<String, Mesh> meshes = new HashMap<String, Mesh>();

	public void putMesh(String name, Mesh mesh) {
		this.meshes.put(name, mesh);
	}

	public Mesh getMesh(String name) {
		return this.meshes.get(name);
	}

	public void createInstance(String name, MeshInstance instance) {
		getMesh(name).addInstance(instance);
	}

	@Override
	public void draw() {
		gl.clearColor(0.7f, 0.7f, 0.7f, 1f);
		gl.clear(GL.COLOR_BUFFER_BIT | GL.DEPTH_BUFFER_BIT);
		
		Iterator<Entry<String, Mesh>> itr = meshes.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<String, Mesh> entry = itr.next();
			Mesh mesh = entry.getValue();
			mesh.drawInstances(shader);
		}
	}

	@Override
	public void update() {

	}

	public Shader getShader() {
		return shader;
	}

	public void resize(float width, float height) {
		this.width = width;
		this.height = height;

		gl.viewport(0, 0, width, height);
		updateUniforms();
	}

	private void updateUniforms() {
		proj = Mat4x4.perspective((float) Math.toRadians(45), width / height, 0.1f, 1000f);
		gl.uniformMatrix4fv(getShader().getUniformLocation("mProj"), false, proj.getArray());
		gl.uniformMatrix4fv(getShader().getUniformLocation("mView"), false, camera.getViewMatrix().getArray());
		System.out.println("Updated projection matrix:\n" + proj.toString());
	}

	public Camera getCamera() {
		return camera;
	}

}
