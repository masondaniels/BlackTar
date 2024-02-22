package coffee.mason.blacktar.canvas.webgl.impl;

import coffee.mason.blacktar.canvas.webgl.GL;
import coffee.mason.blacktar.canvas.webgl.Shader;
import coffee.mason.blacktar.canvas.webgl.WebGLContext;

public class DrawableShader extends Shader {
	
	private static final String[] VERTEX = {
			"# version 300 es",
			"precision highp float;",
			"layout (location=0) in vec3 triPosition;",
			"layout (location=1) in vec3 triNormal;",
			"out vec3 triColor;",
			"uniform mat4 mProj;",
			"uniform mat4 mView;",
			"uniform float time;",
			"uniform vec3 modelPosition[ `36347` - 8];",
			"void main()",
			"{",
			    "vec3 ambientColor = vec3(0.7, 0.7, 0.7);",
			    "float ambientIntensity = max(dot(triNormal, vec3(0.0, 1.0, 0.0)), 0.0);", // Lambertian reflection model
			    "vec3 ambient = ambientColor * ambientIntensity;",
			    "vec3 rColor = vec3(min(tan(float(gl_InstanceID)), 0.2), min(cos(float(gl_InstanceID)), 0.3), min(sin(float(gl_InstanceID)), 0.2));",
			    "triColor = vec3(0.1, 0.1, 0.1) + ambient + rColor;",
			    "gl_Position = mProj * mView * vec4(triPosition.x + modelPosition[gl_InstanceID].x, triPosition.y + modelPosition[gl_InstanceID].y, triPosition.z + modelPosition[gl_InstanceID].z, 1.0);",
			"}"
	};
	
	private static final String[] FRAG = {
			"# version 300 es",
			"precision highp float;",
			"in vec3 triColor;",
			"out vec4 fragCoord;",
			"void main()",
			"{",
				"fragCoord = vec4(triColor, 1.0);",
			"}"
	};
	
	public DrawableShader(WebGLContext gl) {
		super(gl, VERTEX, FRAG);
		gl.frontFace(GL.CCW);
		gl.cullFace(GL.BACK);
		gl.enable(GL.DEPTH_TEST);
	}
	
}
