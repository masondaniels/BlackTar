package coffee.mason.blacktar.canvas.webgl.impl;

import coffee.mason.blacktar.linear.Vec3;

public class MeshInstance {

	public MeshInstance(Vec3 location) {
		this.location = location;
	}

	private Vec3 location;

	public Vec3 getLocation() {
		return location;
	}

	public void setLocation(Vec3 location) {
		this.location = location;
	}

}
