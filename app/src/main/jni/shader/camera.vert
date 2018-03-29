const char* camera_play_vert = STRINGIFY(

attribute vec3    a_Position;
attribute vec2    a_TextCoord;
uniform   mat4    u_MvpMatrix;
varying   vec2    v_TextCoord;

void main()
{
	gl_Position = u_MvpMatrix * vec4(a_Position, 1.0);
	v_TextCoord = a_TextCoord;
}
);