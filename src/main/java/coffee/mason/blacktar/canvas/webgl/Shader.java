package coffee.mason.blacktar.canvas.webgl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSNumber;

import coffee.mason.blacktar.util.JavaScriptUtil;
import coffee.mason.blacktar.util.StringUtil;

public class Shader {

	private WebGLContext gl;

	private static WebGLContext glStatic; // only used for magic replace

	private String vertSource;
	private String fragSource;

	private JSObject program; // gl shader program

	public Shader(WebGLContext gl, String[] vertSource, String[] fragSource) {
		this.gl = gl;
		Shader.glStatic = gl; // bad but whatever
		this.vertSource = StringUtil.join("\n", vertSource);
		this.fragSource = StringUtil.join("\n", fragSource);

		this.vertSource = replaceSourceWithMagic(this.vertSource);
		this.fragSource = replaceSourceWithMagic(this.fragSource);

		createShaderProgram();
	}

	private static Pattern magic;

	static {
		String regex = "(.*?)`(.*?)`";
		magic = Pattern.compile(regex);
	}

	public static String replaceSourceWithMagic(String source) {
		Matcher matcher = magic.matcher(source);
		StringBuffer sb = new StringBuffer();

		while (matcher.find()) {
			int replacement = processMagicReplacement(matcher.group(2));
			matcher.appendReplacement(sb, "$1" + replacement);
		}

		matcher.appendTail(sb);

		System.out.println(sb.toString());

		return sb.toString();
	}

	private static int processMagicReplacement(String content) {

		int value = Integer.parseInt(content);

		return ((JSNumber) glStatic.getParameter(value)).intValue();
	}

	private void createShaderProgram() {
		JSObject vertexShader = gl.createShader(GL.VERTEX_SHADER);
		JSObject fragShader = gl.createShader(GL.FRAGMENT_SHADER);

		gl.shaderSource(vertexShader, vertSource);
		gl.shaderSource(fragShader, fragSource);

		gl.compileShader(vertexShader);
		gl.compileShader(fragShader);

		// Compiling error checks - fragment
		JSObject compileStatusFrag = gl.getShaderParameter(fragShader, GL.COMPILE_STATUS);
		if (!JavaScriptUtil.not(compileStatusFrag)) {
			System.err.println("Compiling error with fragment shader.");
			System.err.println(gl.getShaderInfoLog(fragShader) + "");
		}

		// Compiling error checks - vertex
		JSObject compileStatusVertex = gl.getShaderParameter(vertexShader, GL.COMPILE_STATUS);
		if (!JavaScriptUtil.not(compileStatusVertex)) {
			System.err.println("Compiling error with vertex shader.");
			System.err.println(gl.getShaderInfoLog(vertexShader) + "");
		}

		program = gl.createProgram();

		gl.attachShader(program, vertexShader);
		gl.attachShader(program, fragShader);

		// Link program
		gl.linkProgram(program);

		// Validate program
		gl.validateProgram(program);

		JSObject programValidStatus = gl.getProgramParameter(program, GL.VALIDATE_STATUS);
		if (!JavaScriptUtil.not(programValidStatus)) {
			System.err.println("Validate status error with program.");
		}

		JSObject programLinkStatus = gl.getProgramParameter(program, GL.LINK_STATUS);
		if (!JavaScriptUtil.not(programLinkStatus)) {
			System.err.println("Link error with program.");
		}

	}

	public void useProgram() {
		gl.useProgram(program);
	}

	public JSObject getProgram() {
		return program;
	}

	public void useBuffer(BufferInformation buffer) {
		gl.disableVertexAttribArray(buffer.getLocation());
		gl.bindBuffer(GL.ARRAY_BUFFER, buffer.getBuffer());
		gl.vertexAttribPointer(buffer.getLocation(), buffer.getElements(), GL.FLOAT, false, buffer.getElements() * 4,
				0);
		gl.enableVertexAttribArray(buffer.getLocation());
	}

	public int getUniformLocation(String string) {
		return gl.getUniformLocation(getProgram(), string);
	}

	public void drawObj(Obj obj, BufferInformation[] buffers, UniformInformation[] uniforms, int instances) {

		gl.useProgram(program);

		for (int i = 0; i < buffers.length; i++) {
			useBuffer(buffers[i]);
		}

		for (int i = 0; i < uniforms.length; i++) {
			useUniform(uniforms[i]);
		}

		((WebGLContext2) gl).drawArraysInstanced(GL.TRIANGLES, 0, obj.getTriangleFloats().getLength() / 3, instances);
	}

	public void useUniform(UniformInformation uniform) {
		// TODO: Implement more than v3/parse types
		gl.uniform3fv(getUniformLocation(uniform.getUniformName()), uniform.getData());
	}

}
