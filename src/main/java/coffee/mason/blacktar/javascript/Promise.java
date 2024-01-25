package coffee.mason.blacktar.javascript;

import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

/**
 * 
 * Promise modified from disconnect-project.
 * 
 * @author https://github.com/fluorumlabs/disconnect-project/blob/master/disconnect-classlib/src/main/java/js/lang/Promise.java
 * @author Mason
 */

abstract class Promise<T extends Any> implements JSObject {
	
	
	@JSFunctor
    @FunctionalInterface
    public interface FullfilledValueVoidCallback<T extends Any> extends JSObject {
		void fullfilled(T value);
    }
	
    public native Promise then(FullfilledValueVoidCallback<T> onfulfilled);
    
}
