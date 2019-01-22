package game.android.com.catstacks.engine.rendering;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.ConditionVariable;
import android.os.SystemClock;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import game.android.com.catstacks.engine.core.MainSurfaceView;
import game.android.com.catstacks.engine.core.Screen;
import game.android.com.catstacks.engine.gameloop.Render;

public class GameRenderer implements GLSurfaceView.Renderer
{
    public final RenderSave save = new RenderSave();

    private Configuration configuration;
    private Context context;
    private MainSurfaceView parent;
    private int width, height;
    private static boolean rendered;

    private long lastTime;
    private long startTime;
    private long totalTime;

    private final float[] projectionMatrix = new float[16];
    private final float[] projectionViewMatrix = new float[16];
    private final float[] viewMatrix = new float[16];

    public GameRenderer(Context context, MainSurfaceView parent, Configuration configuration)
    {
        this.context = context;
        this.parent = parent;
        this.configuration = configuration;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig)
    {
        setupScaling();

        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        //GLES20.glDepthFunc(GLES20.GL_LESS);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        GLES20.glDepthMask(true);
        GLES20.glClearDepthf(1f);

        width = parent.getWidth();
        height = parent.getHeight();
        parent.initMenu();

        startTime = System.nanoTime();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height)
    {
        configuration.setScreenWidth(width);
        configuration.setScreenHeight(height);

        setupScaling();

        this.width = width;
        this.height = height;

        //GLES20.glViewport(0, 100, (int)configuration.getScreenWidth(), (int)configuration.getScreenHeight());
        //GLES20.glViewport(0, 0, Screen.screen.getWidth(), Screen.screen.getHeight());
        //GLES20.glViewport(0, 0, width, Screen.screen.newheight);
        GLES20.glViewport(0, 0, width, height);
        //GLES20.glViewport(0, 0, Screen.screen.getWidth() / 2, (int)configuration.getScreenHeight());

        for(int i = 0; i < 16; i++)
        {
            projectionMatrix[i] = 0;
            viewMatrix[i] = 0;
            projectionViewMatrix[i] = 0;
        }

        //Matrix.orthoM(projectionMatrix, 0, 0, configuration.getScreenWidth(), 0, configuration.getScreenHeight(), 0, 1);
        Matrix.orthoM(projectionMatrix, 0, 0, width, 0, height, 0, 50);
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0);
        Matrix.multiplyMM(projectionViewMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
    }

    private float frames = 0;
    private float framesPassed = 0;
    @Override
    public void onDrawFrame(GL10 gl10)
    {
        long now = System.nanoTime();
        long passed = now - lastTime;
        lastTime = now;
        totalTime += passed;
        if(totalTime >= 1000000000)
        {
            framesPassed = frames;
            //Log.println(Log.ERROR, "loop", "render fps: " + frames);
            totalTime = 0;
            frames = 0;
        }
        frames++;

        /*long elapsed = now - lastTime;
        //if(elapsed < 33)
        if(elapsed < 16)
        {
            try
            {
                //Thread.sleep(33 - elapsed);
                Thread.sleep(16 - elapsed);
            } catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }*/
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        lastTime = now;

        //need some sort of screen pixel scaling on objects here (graph does it locally, but not good in game)
        Render.render(projectionViewMatrix);

        //GLES20.glFinish();
        GLES20.glFlush();

        setBufferReady(true);
    }

    public void setupScaling()
    {
        configuration.recalculateScreenPixels(context);
        configuration.recalculateScreenSize();

        if(configuration.getSSX() > configuration.getSSY())
        {
            configuration.setScreenUnits(configuration.getSSY());
            return;
        }
        configuration.setScreenUnits(configuration.getSSX());
    }

    public void save()
    {
        synchronized(save)
        {
            save.configuration = configuration;
            save.context = context;
            save.parent = parent;
            save.width = width;
            save.height = height;

            save.lastTime = lastTime;
            save.startTime = startTime;
            save.totalTime = totalTime;

            for(int i = 0; i < projectionMatrix.length; i++)
            {
                save.projectionMatrix[i] = projectionMatrix[i];
            }
            for(int i = 0; i < projectionViewMatrix.length; i++)
            {
                save.projectionViewMatrix[i] = projectionViewMatrix[i];
            }
            for(int i = 0; i < viewMatrix.length; i++)
            {
                save.viewMatrix[i] = viewMatrix[i];
            }

            save.isValid = true;
        }
    }

    public void restore()
    {
        synchronized(save)
        {
            RenderSave save = this.save;
            if(!save.isValid)
            {
                return;
            }

            configuration = save.configuration;
            context = save.context;
            parent = save.parent;
            width = save.width;
            height = save.height;

            lastTime = SystemClock.uptimeMillis();
            startTime = save.startTime;
            totalTime = save.totalTime;

            for(int i = 0; i < save.projectionMatrix.length; i++)
            {
                projectionMatrix[i] = save.projectionMatrix[i];
            }
            for(int i = 0; i < save.projectionViewMatrix.length; i++)
            {
                projectionViewMatrix[i] = save.projectionViewMatrix[i];
            }
            for(int i = 0; i < save.viewMatrix.length; i++)
            {
                viewMatrix[i] = save.viewMatrix[i];
            }
        }
    }

    public static class RenderSave
    {
        public Configuration configuration;
        public Context context;
        public MainSurfaceView parent;
        public int width, height;

        public long lastTime;
        public long startTime;
        public long totalTime;

        public final float[] projectionMatrix = new float[16];
        public final float[] projectionViewMatrix = new float[16];
        public final float[] viewMatrix = new float[16];

        public boolean isValid;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public Configuration getConfiguration()
    {
        return configuration;
    }

    public static synchronized boolean isBufferReady()
    {
        return rendered;
    }

    public static synchronized void setBufferReady(boolean newRenderState)
    {
        rendered = newRenderState;
    }
}
