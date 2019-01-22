package game.android.com.catstacks.engine.core;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Choreographer;
import android.view.MotionEvent;

import java.util.concurrent.ArrayBlockingQueue;

import game.android.com.catstacks.engine.gameloop.Save;
import game.android.com.catstacks.engine.input.InputBuffer;
import game.android.com.catstacks.engine.physics.Pair;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.rendering.Configuration;
import game.android.com.catstacks.engine.rendering.GameRenderer;
import game.android.com.catstacks.engine.sound.SoundManager;
import game.android.com.catstacks.engine.states.Menu;

public class MainSurfaceView extends GLSurfaceView
{
    private Context context;
    private GameThread gameThread = null;
    private GameRenderer renderer;
    private final ConditionVariable syncObject = new ConditionVariable();

    private SoundPool soundPool;
    private SoundPool.Builder soundPoolBuilder;
    private AudioAttributes attributes;
    private AudioAttributes.Builder attributesBuilder;
    private boolean soundPoolLoaded;
    private static final int MAX_STREAMS = 100;

    private ArrayBlockingQueue<InputBuffer> inputBuffers;
    private InputBuffer input;
    public static final int INPUT_QUEUE_SIZE = 30;

    public static int framesPassed;

    public MainSurfaceView(Context context, final Handler handler)
    {
        super(context);
        this.context = context;

        framesPassed = 30;

        initGL();

        initSound();

        createInputBuffer();

        //setOnTouchListener(new InputBuffer());
        gameThread = new GameThread(handler);
        gameThread.setPlaying(true);
        gameThread.start();
    }

    private void initGL()
    {
        renderer = new GameRenderer(context, this, new Configuration());

        setEGLContextClientVersion(2);
        setEGLConfigChooser(true);
        setPreserveEGLContextOnPause(true);
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        //Choreographer.getInstance().postFrameCallback(this);
    }

    private void createInputBuffer()
    {
        inputBuffers = new ArrayBlockingQueue<InputBuffer>(INPUT_QUEUE_SIZE);
        for(int i = 0; i < INPUT_QUEUE_SIZE; i++)
        {
            inputBuffers.add(new InputBuffer(inputBuffers));
        }
        /*inputBuffers = new ArrayBlockingQueue<InputBuffer>(INPUT_QUEUE_SIZE);
        input = new InputBuffer(inputBuffers);
        Log.println(Log.ERROR, "init", "input");*/
    }

    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        try
        {
            int hist = e.getHistorySize();
            if(hist > 0)
            {
                for(int i = 0; i < hist; i++)
                {
                    InputBuffer input = inputBuffers.take();
                    input.useEventHistory(e, i);
                    gameThread.feedInput(input);
                }
            }
            InputBuffer input = inputBuffers.take();
            input.useEvent(e);
            gameThread.feedInput(input);
        }
        catch(InterruptedException ex)
        {

        }
        /*int hist = e.getHistorySize();
        if(hist > 0)
        {
            for(int i = 0; i < hist; i++)
            {
                input.useEventHistory(e, i);
                gameThread.feedInput(input);
            }
        }
        else
        {
            input.useEvent(e);
            gameThread.feedInput(input);
        }*/

        try
        {
            Thread.sleep(16);
        }
        catch(InterruptedException ex)
        {

        }
        return true;
    }

    public void initMenu()
    {
        initScreen();
        initScoreSession();
        Menu.menu = new Menu(context);
        initSoundManager();
    }

    public void initScreen()
    {
        gameThread.initScreen(context, renderer.getWidth(), renderer.getHeight());
    }

    public void initScoreSession()
    {
        gameThread.initScoreSession(context);
    }

    public void initSoundManager()
    {
        gameThread.initSoundManager(context, soundPool);
    }

    private void initSound()
    {
        if(Build.VERSION.SDK_INT >= 21)
        {
            attributesBuilder = new AudioAttributes.Builder();
            attributesBuilder.setUsage(AudioAttributes.USAGE_GAME);
            attributesBuilder.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION);
            attributes = attributesBuilder.build();

            soundPoolBuilder = new SoundPool.Builder();
            soundPoolBuilder.setAudioAttributes(attributes);
            soundPoolBuilder.setMaxStreams(MAX_STREAMS);
            soundPool = soundPoolBuilder.build();
        }
        else
        {
            soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        gameThread.onPause();
        //soundPool.autoPause();
        if(SoundManager.soundManager != null)
        {
            SoundManager.soundManager.pause();
        }

        syncObject.close();
        queueEvent(new Runnable()
        {
            @Override
            public void run()
            {
                renderer.save();
                Save.save();
                syncObject.open();
            }
        });
        syncObject.block();

    }

    public void pause()
    {
        gameThread.onPause();
    }

    public void resume()
    {
        super.onResume();
        Save.restore();
        //soundPool.autoResume();
        if(SoundManager.soundManager != null)
        {
            SoundManager.soundManager.resume();
        }

        gameThread.onResume();
        renderer.restore();
    }

    public void destroy()
    {
        gameThread.setPlaying(false);
    }
}
