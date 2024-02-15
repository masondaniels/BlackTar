package coffee.mason.blacktar.linear;

import org.teavm.jso.typedarrays.Float32Array;

// Matrix W x H
public class MatWxH {

	protected Float32Array array;
	protected int w;
	protected int h;

	public MatWxH(int w, int h) {
		this.w = w;
		this.h = h;
		array = Float32Array.create(w * h);
	}
	
	public Float32Array getArray() {
		return array;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		String vecString = (w == 1 ? " (Vec" + h + ")" : "");

		sb.append("Mat" + w + "x" + h + "" + vecString + ":\n");
		for (int iy = 0; iy < h; iy++) {

			sb.append("[");
			for (int ix = 0; ix < w; ix++) {
				if (ix == 0) {
					sb.append(getValue(ix, iy));
				} else {
					sb.append(", " + getValue(ix, iy));
				}
			}
			sb.append("]\n");
		}

		return sb.toString();
	}

	public float getValue(int x, int y) {
		return getValue(y * w + x);
	}

	public float getValue(int index) {
		return array.get(index);
	}

	public void setValue(int x, int y, float v) {
		setValue(y * w + x, v);
	}

	public void setValue(int index, float v) {
		array.set(index, v);
	}

	public static boolean multiplicationValid(MatWxH a, MatWxH b) {
		return a.h == b.w;
	}

	// Computes matrix product
	public MatWxH product(MatWxH other) {
		return MatWxH.matrixProduct(this, other);
	}

	public static MatWxH matrixProduct(MatWxH a, MatWxH b) {
		if (!multiplicationValid(a, b)) {
			System.err.println("a.h != b.w (" + a.h + " != " + b.w + "): Matrix multiplication not valid.");
			throw new UnsupportedOperationException("Matrix multiplication not valid.");
		}

		int m = a.w;
		int n = a.h;
		int p = b.h;

		MatWxH returnable = new MatWxH(m, p);

		// iterate over height of matrix
		for (int ih = 0; ih < m; ih++) {

			// r = to the right
			for (int r = 0; r < p; r++) {
				float sum = 0; // sum

				// iterate over n
				for (int in = 0; in < n; in++) {
					sum += a.getValue(ih, in) * b.getValue(in, r); // a0n bnp
				}
				returnable.setValue(r, ih, sum);
			}
		}
		return returnable;
	}

	public MatWxH add(MatWxH other) {
		return MatWxH.add(this, other);
	}

	public static MatWxH add(MatWxH a, MatWxH b) {
		if (!(a.w == b.w && a.h == b.h)) {
			System.err.println("!(a.w == b.w && a.h == b.h): Matrix widths and heights do not match.");
			throw new UnsupportedOperationException("Matrix dimensions not valid.");
		}

		MatWxH returnable = new MatWxH(a.w, a.h);

		// flat indexing
		for (int i = 0; i < a.w * a.h; i++) {
			returnable.setValue(i, a.getValue(i) + b.getValue(i));
		}

		return returnable;
	}

	public MatWxH sub(MatWxH other) {
		return MatWxH.sub(this, other);
	}

	public static MatWxH sub(MatWxH a, MatWxH b) {
		if (!(a.w == b.w && a.h == b.h)) {
			System.err.println("!(a.w == b.w && a.h == b.h): Matrix widths and heights do not match.");
			throw new UnsupportedOperationException("Matrix dimensions not valid.");
		}

		MatWxH returnable = new MatWxH(a.w, a.h);

		// flat indexing
		for (int i = 0; i < a.w * a.h; i++) {
			returnable.setValue(i, a.getValue(i) - b.getValue(i));
		}

		return returnable;
	}

	public void fill(int v) {
		// flat indexing
		for (int i = 0; i < w * h; i++) {
			setValue(i, v);
		}
	}

	//TODO: Implement the Leibniz formula for determinants.
	
}
