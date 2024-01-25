package coffee.mason.blacktar.javascript;


import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

import java.io.Serializable;

/**
 * 
 * Any taken from the author listed below.
 * 
 * @author https://github.com/fluorumlabs/disconnect-project/blob/master/disconnect-classlib/src/main/java/js/lang/Any.java
 *
 */

public interface Any extends JSObject, Serializable {

	@JSBody(script = "return {};")
    static <T extends Any> T empty() {
        throw new UnsupportedOperationException("Available only in JavaScript");
    }
}
