package coffee.mason.blacktar.canvas.webgl;

import java.util.HashMap;

import org.teavm.jso.typedarrays.Float32Array;

public class Scene {

	// A scene is 3d.
	// A scene can have many meshes/instances.
	// Goal: to make it easy to create a scene.

	private HashMap<String, Float32Array> meshes = new HashMap<String, Float32Array>();

	public void putMesh(String name, Float32Array array) {
		this.meshes.put(name, array);
	}
	
	public Float32Array getMesh(String name) {
		return this.meshes.get(name);
	}
	
	public void getMeshInstances() {
		
	}

}
