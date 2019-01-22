package game.android.com.catstacks.engine.rendering;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.pool.ByteBufferObjectPool;
import game.android.com.catstacks.engine.pool.GenericObjectPool;

public abstract class Sprite
{
    protected static final int COORDS_PER_VERTEX = 3;

    protected short[] indices = {0, 1, 2, 0, 2, 3};
    protected float colour[] = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

    protected FloatBuffer vertexBuffer, colourBuffer;
    protected ShortBuffer indexBuffer;

    protected final float[] rotationMatrix = new float[16];
    protected final float[] translationMatrix = new float[16];

    private static int positionHandle = -1;
    private static int colorHandle = -1;
    private static int mvpMatrixHandle = -1;

    private static boolean shaderLoaded = false;

    public static final GenericObjectPool<Sprite> pool = new GenericObjectPool<Sprite>(Sprite.class);
    protected static final ByteBufferObjectPool byteBufferPool = new ByteBufferObjectPool();

    protected void initBasicBuffers(float[] vertices)
    {
        initVertexBuffer(vertices);
        initIndexBuffer();
    }

    protected void initVertexBuffer(float[] vertices)
    {
        ByteBuffer vertexByteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        vertexByteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = vertexByteBuffer.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
    }

    protected void initColourBuffer()
    {
        ByteBuffer colourByeBuffer = ByteBuffer.allocateDirect(colour.length * 4);
        colourByeBuffer.order(ByteOrder.nativeOrder());
        colourBuffer = colourByeBuffer.asFloatBuffer();
        colourBuffer.put(colour);
        colourBuffer.position(0);
    }

    protected void initIndexBuffer()
    {
        ByteBuffer indexByteBuffer = ByteBuffer.allocateDirect(indices.length * 2);
        indexByteBuffer.order(ByteOrder.nativeOrder());
        indexBuffer = indexByteBuffer.asShortBuffer();
        indexBuffer.put(indices);
        indexBuffer.position(0);
    }

    protected void initSolidShader()
    {
        if(!shaderLoaded)
        {
            int vertexShaderCode = Shaders.loadShader(GLES20.GL_VERTEX_SHADER, Shaders.solidVertexShader);
            int fragmentShaderCode = Shaders.loadShader(GLES20.GL_FRAGMENT_SHADER, Shaders.solidFragmentShader);

            Shaders.solidProgram = GLES20.glCreateProgram();
            GLES20.glAttachShader(Shaders.solidProgram, vertexShaderCode);
            GLES20.glAttachShader(Shaders.solidProgram, fragmentShaderCode);
            GLES20.glLinkProgram(Shaders.solidProgram);

            positionHandle = GLES20.glGetAttribLocation(Shaders.imageProgram, "vPosition");
            colorHandle = GLES20.glGetAttribLocation(Shaders.imageProgram, "a_Color");
            mvpMatrixHandle = GLES20.glGetUniformLocation(Shaders.imageProgram, "uMVPMatrix");

            shaderLoaded = true;
        }
    }

    protected void setVertices(float[] vertices)
    {
        initVertexBuffer(vertices);
    }

    protected void setVertices(float[] vertices, short[] indices)
    {
        initVertexBuffer(vertices);

        this.indices = indices;
        initIndexBuffer();
    }

    protected void setColours(float[] colours)
    {
        this.colour = colours;
        initColourBuffer();
    }

    public void render(float[] vpMatrix, Vec pos, float rot, Vec center)
    {
        //scratch matrices need to swap order in subsequent matrix multiply calls to avoid weird on the fly calculation issues
        //so I need two of them to do the swap, both cloning the projection view matrix (which becomes the model-view-projection matrix by the end of all the calculations)
        float[] scratchMatrix = vpMatrix.clone();
        float[] scratchMatrix2 = vpMatrix.clone();

        Matrix.setIdentityM(translationMatrix, 0);
        Matrix.translateM(translationMatrix, 0, pos.x, pos.y, 0);
        Matrix.setRotateM(rotationMatrix, 0, rot, 0, 0, 1);

        Matrix.multiplyMM(scratchMatrix2, 0, scratchMatrix, 0, translationMatrix, 0);
        Matrix.multiplyMM(scratchMatrix, 0, scratchMatrix2, 0, rotationMatrix, 0);

        GLES20.glUseProgram(Shaders.solidProgram);

        int positionHandle = GLES20.glGetAttribLocation(Shaders.solidProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        int colourHandle = GLES20.glGetUniformLocation(Shaders.solidProgram, "vColor");
        GLES20.glUniform4fv(colourHandle, 1, colour, 0);

        int mvpMatrixHandle = GLES20.glGetUniformLocation(Shaders.solidProgram, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, scratchMatrix, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);

        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(colourHandle);
    }

    public void destruct()
    {
        indices[0] = 0;
        indices[1] = 1;
        indices[2] = 2;
        indices[3] = 0;
        indices[4] = 2;
        indices[5] = 3;
        ArrayPool.shortMaster.recycle(indices, indices.length);

        for(int i = 0; i < colour.length; i++)
        {
            colour[i] = 1;
        }
        ArrayPool.floatMaster.recycle(colour, colour.length);

        ArrayPool.floatMaster.recycle(rotationMatrix, rotationMatrix.length);
        ArrayPool.floatMaster.recycle(translationMatrix, translationMatrix.length);
    }
}
