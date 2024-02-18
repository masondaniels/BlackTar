package coffee.mason.blacktar.html;

import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

public class StyledGrid {

//	display: grid;
//    grid-template-columns: repeat(3, 100px);
//    grid-gap: 10px;

	private HTMLElement[] elements;
	private HTMLElement div; // This is the styled wrapper

	public StyledGrid(HTMLElement... elements) {
		this.elements = elements;
		div = HTMLDocument.current().createElement("div");
		setZIndex(99);

		int size = 80;

		for (int i = 0; i < elements.length; i++) {
			if (elements[i] == null) {
				div.appendChild(HTMLDocument.current().createElement("div")); // append empty div
			} else {
				elements[i].getStyle().setProperty("height", size + "px");
				div.appendChild(elements[i]);
			}
		}
		div.getStyle().setProperty("display", "grid");
		div.getStyle().setProperty("grid-template-columns", "repeat(3, " + size + "px)");
		div.getStyle().setProperty("grid-gap", "10px");

		// set to bottom left
		div.getStyle().setProperty("bottom", "0");
		div.getStyle().setProperty("left", "0");

		div.getStyle().setProperty("padding", "15px");
	}

	// TODO: Abstract setZIndex in this class, Canvas and StyledButton
	public void setZIndex(int value) {
		div.getStyle().setProperty("position", "absolute");
		div.getStyle().setProperty("z-index", value + "");
	}

	public void addToDocument() {
		HTMLDocument.current().getBody().appendChild(div);
	}

}
