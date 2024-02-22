package coffee.mason.blacktar.canvas.webgl;

import org.teavm.jso.JSObject;

public class UniformInformation {

	private String uniformName; // name of uniform
	private JSObject data; // data to update uniform with
	
	public UniformInformation(String uniformName, JSObject data) {
		this.uniformName = uniformName;
		setData(data);
	}
	
	public String getUniformName() {
		return uniformName;
	}
	
	public JSObject getData() {
		return data;
	}
	public void setData(JSObject data) {
		this.data = data;
	}
	
}
