package coffee.mason.blacktar.util;

import java.util.Random;

public class RandomUtil {

	private static Random r = new Random();

	public static int nextInt(int bounds) {
		return r.nextInt(bounds);
	}

}