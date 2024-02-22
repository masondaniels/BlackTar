package coffee.mason.blacktar.canvas.webgl;

import org.teavm.jso.JSObject;

public class BufferInformation {

	private JSObject buffer;
	private int location; // Attrib location
	private int elements; // Elements in buffer
	
	public BufferInformation(JSObject buffer, int location, int elements) {
		setBuffer(buffer);
		setLocation(location);
		setElements(elements);
	}

	public JSObject getBuffer() {
		return buffer;
	}

	public void setBuffer(JSObject buffer) {
		this.buffer = buffer;
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public int getElements() {
		return elements;
	}

	public void setElements(int elements) {
		this.elements = elements;
	}
	
}
