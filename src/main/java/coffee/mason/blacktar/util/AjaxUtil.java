package coffee.mason.blacktar.util;

import java.io.IOException;

import org.teavm.interop.Async;
import org.teavm.interop.AsyncCallback;
import org.teavm.jso.ajax.XMLHttpRequest;

public class AjaxUtil {

	@Async
	public static native String get(String url) throws IOException;

	public static void get(String url, AsyncCallback<String> callback) {
		XMLHttpRequest xhr = XMLHttpRequest.create();
		xhr.open("get", url);
		xhr.setOnReadyStateChange(() -> {
			if (xhr.getReadyState() != XMLHttpRequest.DONE) {
				return;
			}

			int statusGroup = xhr.getStatus() / 100;
			if (statusGroup != 2 && statusGroup != 3) {
				callback.error(new IOException("HTTP status: " + xhr.getStatus() + " " + xhr.getStatusText()));
			} else {
				callback.complete(xhr.getResponseText());
			}
		});
		xhr.send();
	}
}