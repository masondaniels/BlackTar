package coffee.mason.blacktar.canvas.controls.impl;

import coffee.mason.blacktar.linear.Mat4x4;
import coffee.mason.blacktar.linear.Vec3;

public class Camera {

	private Quaternion rotation;
	private Vec3 position = new Vec3();
	private float pitch; // deg
	private float yaw; // deg
	private float roll; // deg

	public static Vec3 up = new Vec3();

	static {
		up.setValue(1, 1); // y is up
	}

	public Camera() {
		rotation = Quaternion.fromEulerAngles(pitch, yaw, roll);
	}

	public Camera(float posX, float posY, float posZ, float pitch, float yaw, float roll) {
		this.position = new Vec3(posX, posY, posZ);
		this.rotation = Quaternion.fromEulerAngles(pitch, yaw, roll);
	}

	public void rotate(float pitch, float yaw, float roll) {
		Quaternion rotationDelta = Quaternion.fromEulerAngles(pitch, yaw, roll);
		rotation = rotation.multiply(rotationDelta).normalize();
	}

	public void move(float dx, float dy, float dz) {
		Vec3 movement = rotation.rotateVector(new Vec3(dx, dy, dz));
		position = (Vec3) position.add(movement);
	}

	public void moveForward(float distance) {
		Vec3 forward = rotation.rotateVector(new Vec3(0, 0, -1)).normalize();
		forward = forward.multiply(distance);
		position = (Vec3) position.add(forward);
	}

	public void moveBackward(float distance) {
		Vec3 forward = rotation.rotateVector(new Vec3(0, 0, 1)).normalize();
		forward = forward.multiply(distance);
		position = (Vec3) position.add(forward);
	}

	public void moveLeft(float distance) {
		Vec3 right = rotation.rotateVector(new Vec3(1, 0, 0)).normalize();
		right = right.multiply(-distance);
		position = (Vec3) position.add(right);
	}

	public void moveRight(float distance) {
		Vec3 right = rotation.rotateVector(new Vec3(1, 0, 0)).normalize();
		right = right.multiply(distance);
		position = (Vec3) position.add(right);
	}

	public Vec3 getPosition() {
		return position;
	}

	public Mat4x4 getViewMatrix() {

		Mat4x4 translationMatrix = Mat4x4.identity();
		translationMatrix.setValue(3, 0, -getPosX());
		translationMatrix.setValue(3, 1, -getPosY());
		translationMatrix.setValue(3, 2, -getPosZ());

		Mat4x4 rotationMatrix = rotation.toMatrix();

		return (Mat4x4) Mat4x4.matrixProduct(translationMatrix, rotationMatrix);

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

	public float getPosX() {
		return position.getValue(0);
	}

	public float getPosY() {
		return position.getValue(1);
	}

	public float getPosZ() {
		return position.getValue(2);
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getRoll() {
		return roll;
	}

	public void setRoll(float roll) {

		this.roll = roll;
	}

	public Quaternion getRotation() {
		return rotation;
	}

}
