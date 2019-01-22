package game.android.com.catstacks.engine.rendering;

import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;

import game.android.com.catstacks.engine.physics.Vec;

public class TextSprite extends TexturedSprite
{
    private FloatBuffer colourBuffer;

    private static int positionHandle = -1;
    private static int colorHandle = -1;
    private static int texcoordHandle = -1;
    private static int mvpMatrixHandle = -1;
    private static int samplerLocation = -1;

    private static boolean shaderLoaded = false;

    public TextSprite(float[] vertices, Texture texture)
    {
        loadTexture(texture);
        initTextureBuffers(vertices);
        initTextShader();
    }

    public TextSprite(float[] vertices, Texture texture, float[] texCoords, float[] colours, short[] indices)
    {
        this.texture = texCoords;
        this.colour = colours;
        this.indices = indices;
        loadTexture(texture);
        initColourTextureBuffers(vertices);
        initTextShader();
    }

    private void initColourTextureBuffers(float[] vertices)
    {
        initTextureBuffers(vertices);
        initColourBuffer();
    }

    private void initTextShader()
    {
        if(!shaderLoaded)
        {
            int vertexShaderCode = Shaders.loadShader(GLES20.GL_VERTEX_SHADER, Shaders.textVertexShader);
            int fragmentShaderCode = Shaders.loadShader(GLES20.GL_FRAGMENT_SHADER, Shaders.textFragmentShader);

            Shaders.textProgram = GLES20.glCreateProgram();
            GLES20.glAttachShader(Shaders.textProgram, vertexShaderCode);
            GLES20.glAttachShader(Shaders.textProgram, fragmentShaderCode);
            GLES20.glLinkProgram(Shaders.textProgram);

            positionHandle = GLES20.glGetAttribLocation(Shaders.imageProgram, "vPosition");
            colorHandle = GLES20.glGetAttribLocation(Shaders.imageProgram, "a_Color");
            texcoordHandle = GLES20.glGetAttribLocation(Shaders.imageProgram, "a_texCoord");
            mvpMatrixHandle = GLES20.glGetUniformLocation(Shaders.imageProgram, "uMVPMatrix");
            samplerLocation = GLES20.glGetUniformLocation(Shaders.imageProgram, "s_texture");

            GLES20.glUseProgram(Shaders.imageProgram);
            GLES20.glUniform1i(samplerLocation, 0);
            GLES20.glUseProgram(0);

            shaderLoaded = true;
        }
    }

    public void setIndices(short[] indices)
    {
        this.indices = indices;
    }

    public void setColours(float[] colours)
    {
        this.colour = colours;
    }

    protected void loadTexture(Texture texture)
    {
        GLES20.glGenTextures(1, textures, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, texture.getBitmap(), 0);
        texture.getBitmap().recycle();

        if(textures[0] == 0)
        {
            Log.println(Log.ERROR, "texturing", "Error loading texture: " + texture.getBitmap().getGenerationId());
        }
    }

    @Override
    public void render(float[] vpMatrix, Vec pos, float rot, Vec center)
    {
        float[] scratchMatrix = vpMatrix.clone();
        float[] scratchMatrix2 = vpMatrix.clone();

        Matrix.setIdentityM(translationMatrix, 0);
        Matrix.translateM(translationMatrix, 0, pos.x, pos.y, 0);
        Matrix.setRotateM(rotationMatrix, 0, rot, 0, 0, 1);

        Matrix.multiplyMM(scratchMatrix2, 0, scratchMatrix, 0, translationMatrix, 0);
        Matrix.multiplyMM(scratchMatrix, 0, scratchMatrix2, 0, rotationMatrix, 0);

        GLES20.glUseProgram(Shaders.imageProgram);

        int positionHandle = GLES20.glGetAttribLocation(Shaders.imageProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        int texcoordHandle = GLES20.glGetAttribLocation(Shaders.imageProgram, "a_texCoord");
        GLES20.glVertexAttribPointer(texcoordHandle, 2, GLES20.GL_FLOAT, false, 0, textureBuffer);
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glEnableVertexAttribArray(texcoordHandle);

        int colourHandle = GLES20.glGetAttribLocation(Shaders.textProgram, "a_Color");
        GLES20.glEnableVertexAttribArray(colourHandle);
        GLES20.glVertexAttribPointer(colourHandle, 4, GLES20.GL_FLOAT, false, 0, colourBuffer);

        int mvpMatrixHandle = GLES20.glGetUniformLocation(Shaders.imageProgram, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, scratchMatrix, 0);

        int samplerLocation = GLES20.glGetUniformLocation(Shaders.imageProgram, "s_texture");
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        GLES20.glUniform1i(samplerLocation, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);

        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(colourHandle);
        GLES20.glDisableVertexAttribArray(texcoordHandle);
    }
}
