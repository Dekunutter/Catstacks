package game.android.com.catstacks.engine.rendering;

import android.opengl.GLES20;
import android.util.Log;

public class Shaders
{
    public static int solidProgram, imageProgram, textProgram;

    public static final String solidVertexShader =
        "uniform mat4 uMVPMatrix;" +
        "attribute vec4 vPosition;" +
        "void main() {" +
        " gl_Position = uMVPMatrix * vPosition;" +
        "}";
    public static final String solidFragmentShader =
        "precision mediump float;" +
        "uniform vec4 vColor;" +
        "void main() {" +
        " gl_FragColor = vColor;" +
        "}";

    public static final String imageVertexShader =
        "uniform mat4 uMVPMatrix;" +
        "attribute vec4 vPosition;" +
        "attribute vec4 a_Color;" +
        "varying vec4 v_Color;" +
        "attribute vec2 a_texCoord;" +
        "varying vec2 v_texCoord;" +
        "void main() {" +
        " gl_Position = uMVPMatrix * vPosition;" +
        "v_Color = a_Color;" +
        " v_texCoord = a_texCoord;" +
        "}";
    public static final String imageFragmentShader =
        "precision mediump float;" +
        "varying vec4 v_Color;" +
        "varying vec2 v_texCoord;" +
        "uniform sampler2D s_texture;" +
        "void main() {" +
        " vec4 val = (v_Color * texture2D(s_texture, v_texCoord));" +
        " if(val.a > 0.0) {" +
        "  val.rgb *= v_Color.a;" +
        "  gl_FragColor = val;" +
        " } else {" +
        "  discard;" +
        " }" +
        "}";

    public static final String textVertexShader =
        "uniform mat4 uMVPMatrix;" +
        "attribute vec4 vPosition;" +
        "attribute vec4 a_Color;" +
        "attribute vec2 a_texCoord;" +
        "varying vec4 v_Color;" +
        "varying vec2 v_texCoord;" +
        "void main() {" +
        " gl_Position = uMVPMatrix * vPosition;" +
        " v_texCoord = a_texCoord;" +
        " v_Color = a_Color;" +
        "}";
    public static final String textFragmentShader =
        "precision mediump float;" +
        "varying vec4 v_Color;" +
        "varying vec2 v_texCoord;" +
        "uniform sampler2D s_texture;" +
        "void main() {" +
        " gl_FragColor = texture2D(s_texture, v_texCoord) * v_Color;" +
        " gl_FragColor.rgb *= v_Color.a;" +
        "}";

    public static int loadShader(int type, String shaderCode)
    {
        int shader = GLES20.glCreateShader(type);

        if(shader != 0)
        {
            GLES20.glShaderSource(shader, shaderCode);
            GLES20.glCompileShader(shader);

            final int[] status = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, status, 0);
            if(status[0] == 0)
            {
                Log.println(Log.ERROR, "shader", "Error compiling shader: " + GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        else
        {
            Log.println(Log.ERROR, "shader", "Error creating shader");
        }

        return shader;
    }
}
