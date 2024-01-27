package coffee.mason.blacktar.util;

public class StringUtil {

	public static String join(String joinWith, String... lines) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < lines.length; i++) {
			sb.append(lines[i] + joinWith);
		}
		return sb.toString();
	}
	
	
}
