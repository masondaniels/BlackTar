package coffee.mason.blacktar.canvas.controls.impl;

import coffee.mason.blacktar.linear.Mat4x4;
import coffee.mason.blacktar.linear.Vec3;

public class Camera {

//	private Vec3 lookingAt = new Vec3();
	private Vec3 position = new Vec3();
	private Vec3 viewDirection;

	private static Vec3 up = new Vec3();

	static {
		up.setValue(1, 1); // y is up
	}
	
	public Camera() {
		viewDirection = new Vec3(0, 0, -1f);
	}

	public Vec3 getPosition() {
		return position;
	}

	public Vec3 getViewDirection() {
		return viewDirection;
	}
	
	public void moveForward(float distance) {
		position = (Vec3) position.add(viewDirection.scale(distance));
	}
	
	public void moveBackwards(float distance) {
		position = (Vec3) position.sub(viewDirection.scale(distance));
	}
	
	public void strafeLeft(float distance) {
		Vec3 strafeDirection = Vec3.cross(viewDirection, Camera.up);
		position = (Vec3) position.add(strafeDirection.scale(distance));
	}
	
	public void strafeRight(float distance) {
		Vec3 strafeDirection = Vec3.cross(viewDirection, Camera.up);
		position = (Vec3) position.sub(strafeDirection.scale(distance));
	}
	
	public void moveUp(float distance) {
		position = (Vec3) position.add(Camera.up.scale(distance));
	}
	
	public void moveDown(float distance) {
		position = (Vec3) position.sub(Camera.up.scale(distance));
	}
	
	private float yaw; // deg
	private float pitch; // deg

	public void updateViewDirection() {
	    // Convert yaw and pitch to radians
	    float yawRad = (float) Math.toRadians(yaw);
	    float pitchRad = (float) Math.toRadians(pitch);

	    // Calculate the new view direction using spherical coordinates
	    float x = (float) (Math.cos(yawRad) * Math.cos(pitchRad));
	    float y = (float) (Math.sin(pitchRad));
	    float z = (float) (Math.sin(yawRad) * Math.cos(pitchRad));

	    // Update the view direction
	    viewDirection = new Vec3(x, y, z).normalize();
	}

	
	public Mat4x4 getViewMatrix() {
		// Using lookingAt and position, return new view matrix
		Vec3 forward = Vec3.normalize((Vec3) viewDirection);
		Vec3 right = Vec3.normalize(Vec3.cross(Camera.up, forward));
		Vec3 up = Vec3.normalize(Vec3.cross(forward, right));

		Mat4x4 viewMatrix = new Mat4x4();
		viewMatrix.setValue(0, 0, right.getValue(0)); // right.x
		viewMatrix.setValue(1, 0, up.getValue(0)); // up.x
		viewMatrix.setValue(2, 0, -1*(forward.getValue(0))); // forward.x

		viewMatrix.setValue(0, 1, right.getValue(1)); // right.y
		viewMatrix.setValue(1, 1, up.getValue(1)); // up.y
		viewMatrix.setValue(2, 1, -1*(forward.getValue(1))); // forward.y

		viewMatrix.setValue(0, 2, right.getValue(2)); // right.z
		viewMatrix.setValue(1, 2, up.getValue(2)); // up.z
		viewMatrix.setValue(2, 2, -1*(forward.getValue(2))); // forward.z

		

		viewMatrix.setValue(0, 3, -1*Vec3.dot(right, position));
		viewMatrix.setValue(1, 3, -1*Vec3.dot(up, position));
		viewMatrix.setValue(2, 3, Vec3.dot(forward, position));
		
		viewMatrix.setValue(3, 3, 1); // w
		
		System.out.println("View matrix:\n" + viewMatrix);

		return viewMatrix;

	}

	public void setPosX(float v) {
		position.setValue(0, v);
	}

	public void setPosY(float v) {
		position.setValue(1, v);
	}

	public void setPosZ(float v) {
		position.setValue(2, v);
	}

//	public void setLookX(float v) {
//		lookingAt.setValue(0, v);
//	}
//
//	public void setLookY(float v) {
//		lookingAt.setValue(1, v);
//	}
//
//	public void setLookZ(float v) {
//		lookingAt.setValue(2, v);
//	}

	public float getPosX() {
		return position.getValue(0);
	}

	public float getPosY() {
		return position.getValue(1);
	}

	public float getPosZ() {
		return position.getValue(2);
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	
//	public float getLookX() {
//		return lookingAt.getValue(0);
//	}
//	
//	public float getLookY() {
//		return lookingAt.getValue(1);
//	}
//	
//	public float getLookZ() {
//		return lookingAt.getValue(2);
//	}

}
