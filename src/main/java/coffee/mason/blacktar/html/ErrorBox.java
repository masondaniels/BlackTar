package coffee.mason.blacktar.html;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLTextAreaElement;

public class ErrorBox {

	private HTMLTextAreaElement textArea;

	public ErrorBox() {
		textArea = (HTMLTextAreaElement) HTMLDocument.current().createElement("textarea");
		textArea.getStyle().setCssText("color: red; width: 100%; height: 200px; position: absolute; z-index: 99;");

		HTMLDocument.current().getBody().appendChild(textArea);
		addConsoleListener("error", new ErrorHandler() {

			@Override
			public void onError(String message) {
				String errorMessage = "Error: " + message + "\n";
				textArea.setValue(textArea.getValue() + errorMessage);
			}

		});
		
	}

	@JSBody(params = { "string", "functor" }, script = "$rt_globals.console.error = functor;")
	static native void addConsoleListener(String message, ErrorHandler functor);

	@JSFunctor
	private interface ErrorHandler extends JSObject {
		void onError(String message);
	}


}
