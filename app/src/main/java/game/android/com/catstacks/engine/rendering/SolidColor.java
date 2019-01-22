package game.android.com.catstacks.engine.rendering;

import android.opengl.GLES20;

import game.android.com.catstacks.engine.objects.WorldObject;

public abstract class SolidColor extends WorldObject
{
    private float uniformScale;

    protected void draw()
    {
        GLES20.glUseProgram(Shaders.solidProgram);
    }

    protected float[] scaleVerticesToScreen(float[] vertices)
    {
        for(int i = 0; i < vertices.length; i++)
        {
            vertices[i] *= uniformScale;
        }
        return vertices;
    }

    protected float getUniformScale()
    {
        return uniformScale;
    }

    protected void setUniformScale(float uniformScale)
    {
        this.uniformScale = uniformScale;
    }
}
