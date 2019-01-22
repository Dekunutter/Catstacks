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
import game.android.com.catstacks.engine.pool.GenericObjectPool;

public class TexturedSprite extends Sprite
{
    protected float texture[] = {0, 0, 0, 1, 1, 1, 1, 0};
    protected int[] textures = new int[1];
    protected FloatBuffer textureBuffer;
    protected boolean hasAlpha, isTiled, storeBitmap;
    private float[] scratchMatrix, scratchMatrix2;

    private static int positionHandle = -1;
    private static int colorHandle = -1;
    private static int texcoordHandle = -1;
    private static int mvpMatrixHandle = -1;
    private static int samplerLocation = -1;

    private static boolean shaderLoaded = false;

    protected TexturedSprite()
    {

    }

    public TexturedSprite(float[] vertices, float[] colours, Texture texture, boolean hasAlpha, boolean isTiled, boolean storeBitmap)
    {
        if(colours != null)
        {
            this.colour = colours;
        }
        this.hasAlpha = hasAlpha;
        this.isTiled = isTiled;
        this.storeBitmap = storeBitmap;
        loadTexture(texture);
        initTextureBuffers(vertices);
        initImageShader();
    }

    public TexturedSprite(float[] vertices, Texture texture, float[] texCoords, boolean hasAlpha, boolean isTiled, boolean storeBitmap)
    {
        this.texture = texCoords;
        this.hasAlpha = hasAlpha;
        this.isTiled = isTiled;
        this.storeBitmap = storeBitmap;
        loadTexture(texture);
        initTextureBuffers(vertices);
        initImageShader();
    }

    public TexturedSprite(float[] vertices, float[] colours, Texture texture, float[] texCoords, short[] indices, boolean hasAlpha, boolean isTiled, boolean storeBitmap)
    {
        if(colours != null)
        {
            this.colour = colours;
        }
        if(texCoords != null)
        {
            this.texture = texCoords;
        }
        this.indices = indices;
        this.hasAlpha = hasAlpha;
        this.isTiled = isTiled;
        this.storeBitmap = storeBitmap;
        loadTexture(texture);
        initTextureBuffers(vertices);
        initImageShader();
    }

    protected void initTextureBuffers(float[] vertices)
    {
        initVertexBuffer(vertices);
        initTextureBuffer();
        initIndexBuffer();
        initColourBuffer();
    }

    protected void initTextureBuffer()
    {
        ByteBuffer textureByteBuffer = ByteBuffer.allocateDirect(texture.length * 4);
        textureByteBuffer.order(ByteOrder.nativeOrder());
        textureBuffer = textureByteBuffer.asFloatBuffer();
        textureBuffer.put(texture);
        textureBuffer.position(0);
    }

    private void initImageShader()
    {
        if(!shaderLoaded)
        {
            int vertexShaderCode = Shaders.loadShader(GLES20.GL_VERTEX_SHADER, Shaders.imageVertexShader);
            int fragmentShaderCode = Shaders.loadShader(GLES20.GL_FRAGMENT_SHADER, Shaders.imageFragmentShader);

            Shaders.imageProgram = GLES20.glCreateProgram();
            GLES20.glAttachShader(Shaders.imageProgram, vertexShaderCode);
            GLES20.glAttachShader(Shaders.imageProgram, fragmentShaderCode);
            GLES20.glLinkProgram(Shaders.imageProgram);

            positionHandle = GLES20.glGetAttribLocation(Shaders.imageProgram, "vPosition");
            colorHandle = GLES20.glGetAttribLocation(Shaders.imageProgram, "a_Color");
            texcoordHandle = GLES20.glGetAttribLocation(Shaders.imageProgram, "a_texCoord");
            mvpMatrixHandle = GLES20.glGetUniformLocation(Shaders.imageProgram, "uMVPMatrix");
            samplerLocation = GLES20.glGetUniformLocation(Shaders.imageProgram, "s_texture");

            GLES20.glUseProgram(Shaders.imageProgram);
            GLES20.glUniform1i(samplerLocation, 0);
            GLES20.glUseProgram(0);

            GLES20.glEnableVertexAttribArray(positionHandle);
            GLES20.glEnableVertexAttribArray(colorHandle);
            GLES20.glEnableVertexAttribArray(texcoordHandle);
            GLES20.glEnableVertexAttribArray(mvpMatrixHandle);

            shaderLoaded = true;
        }
    }

    protected void loadTexture(Texture texture)
    {
        GLES20.glGenTextures(1, textures, 0);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        if(isTiled)
        {
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
        }
        else
        {
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        }
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, texture.getBitmap(), 0);

        if(!storeBitmap)
        {
            texture.getBitmap().recycle();
        }

        if(textures[0] == 0)
        {
            Log.println(Log.ERROR, "texturing", "Error loading texture: " + texture.getBitmap().getGenerationId());
        }
    }

    public void setTextureCoords(float[] textureCoords)
    {
        this.texture = textureCoords;
        initTextureBuffer();

        /*FloatBuffer buffer = textureBuffer;
        for(int i = 0; i < textureCoords.length; i++)
        {
            buffer.put(textureCoords[i]);
        }
        buffer.position(0);*/
    }

    @Override
    public void render(float[] vpMatrix, Vec pos, float rot, Vec center)
    {
        if(hasAlpha)
        {
            GLES20.glEnable(GLES20.GL_BLEND);
            //GLES20.glDepthMask(false);
            GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        }
        //scratch matrices need to swap order in subsequent matrix multiply calls to avoid weird on the fly calculation issues
        //so I need two of them to do the swap, both cloning the projection view matrix (which becomes the model-view-projection matrix by the end of all the calculations)
        //scratchMatrix = vpMatrix.clone();
        if(scratchMatrix == null)
        {
            scratchMatrix = new float[vpMatrix.length];
        }
        //scratchMatrix2 = vpMatrix.clone();
        if(scratchMatrix2 == null)
        {
            scratchMatrix2 = new float[vpMatrix.length];
        }

        for(int i = 0; i < vpMatrix.length; i++)
        {
            scratchMatrix[i] = vpMatrix[i];
            scratchMatrix2[i] = vpMatrix[i];
        }

        Matrix.setIdentityM(translationMatrix, 0);
        Matrix.translateM(translationMatrix, 0, pos.x, pos.y, 0);
        Matrix.setRotateM(rotationMatrix, 0, rot, 0, 0, 1);

        Matrix.multiplyMM(scratchMatrix2, 0, scratchMatrix, 0, translationMatrix, 0);
        Matrix.multiplyMM(scratchMatrix, 0, scratchMatrix2, 0, rotationMatrix, 0);

        GLES20.glUseProgram(Shaders.imageProgram);

        //GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        //GLES20.glEnableVertexAttribArray(colorHandle);
        GLES20.glVertexAttribPointer(colorHandle, 4, GLES20.GL_FLOAT, false, 0, colourBuffer);


        GLES20.glVertexAttribPointer(texcoordHandle, 2, GLES20.GL_FLOAT, false, 0, textureBuffer);
        //GLES20.glEnableVertexAttribArray(texcoordHandle);

        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, scratchMatrix, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);

        //GLES20.glDisableVertexAttribArray(positionHandle);
        //GLES20.glDisableVertexAttribArray(colorHandle);
        //GLES20.glDisableVertexAttribArray(texcoordHandle);

        if(hasAlpha)
        {
            //GLES20.glDepthMask(true);
            GLES20.glDisable(GLES20.GL_BLEND);
        }
    }
}
