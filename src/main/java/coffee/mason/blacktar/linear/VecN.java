package coffee.mason.blacktar.linear;

public class VecN extends MatWxH {

	public VecN(int n) {
		super(1, n);
	}
	
	public VecN(float... f) {
		this(f.length);
		for (int i = 0; i < f.length; i++) {
			setValue(i, f[i]);
		}
	}

}
