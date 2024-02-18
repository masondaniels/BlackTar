package coffee.mason.blacktar.html;

import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.html.HTMLButtonElement;
import org.teavm.jso.dom.html.HTMLDocument;

import coffee.mason.blacktar.canvas.controls.KeyControls;
import coffee.mason.blacktar.canvas.controls.MouseControls;
import coffee.mason.blacktar.canvas.controls.TouchMethodsInterface;

public abstract class StyledButton implements TouchMethodsInterface {

	private HTMLButtonElement button;
	private String renderString; // String to be rendered

	public StyledButton(String renderString) {
		this.renderString = renderString;

		HTMLDocument document = HTMLDocument.current();
		button = (HTMLButtonElement) document.createElement("button");

		button.getStyle().setProperty("background-color", "#34495e");
		button.getStyle().setProperty("border-radius", "10px");

		button.getStyle().setProperty("color", "#fff");
		button.getStyle().setProperty("border", "none");
		
		button.setInnerText(renderString);
		getButton().addEventListener("click", (e) -> {
			onClick(e);
		}, false);

		registerTouchControls(getButton());
	}

	public void addToDocument() {
		HTMLDocument.current().getBody().appendChild(getButton());
	}

	public HTMLButtonElement getButton() {
		return button;
	}

	// TODO: Abstract setZIndex in Canvas and StyledButton
	public void setZIndex(int value) {
		button.getStyle().setProperty("position", "absolute");
		button.getStyle().setProperty("z-index", value + "");
	}

	public abstract void onClick(Event e);

}