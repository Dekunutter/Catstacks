package game.android.com.catstacks.engine.core;

import android.content.Context;
import android.media.SoundPool;
import android.os.Handler;
import android.util.Log;
import java.util.concurrent.ArrayBlockingQueue;

import game.android.com.catstacks.engine.gameloop.Flag;
import game.android.com.catstacks.engine.input.InputBuffer;
import game.android.com.catstacks.engine.rendering.GameRenderer;
import game.android.com.catstacks.engine.sound.SoundManager;
import game.android.com.catstacks.engine.gameloop.Buffer;
import game.android.com.catstacks.engine.gameloop.Input;
import game.android.com.catstacks.engine.gameloop.Update;
import game.android.com.catstacks.engine.states.Menu;

public class GameThread extends Thread
{
    public static GameState state;

    private ArrayBlockingQueue<InputBuffer> inputQueue = new ArrayBlockingQueue<InputBuffer>(MainSurfaceView.INPUT_QUEUE_SIZE);
    private Object inputQueueMutex = new Object();
    private InputBuffer input;
    private Object pauseLock;
    private long unpauseTime;
    private boolean isAdReady, isInstitialAdReady;
    private Handler handler;

    public static int framesPassed;

    private Object playingMutex = new Object();
    public static boolean playing, paused;

    public GameThread(final Handler handler)
    {
        super();
        pauseLock = new Object();
        unpauseTime = 0;
        isAdReady = false;
        isInstitialAdReady = false;
        this.handler = handler;

        state = GameState.BOOT;
    }

    public void initScreen(Context context, int width, int height)
    {
        Screen.screen = new Screen(context, width, height);
    }

    public void initScoreSession(Context context)
    {
        ScoreSession.session = new ScoreSession(context);
    }

    public void initSoundManager(Context context, SoundPool soundPool)
    {
        SoundManager.soundManager = new SoundManager(context, soundPool);
    }

    public Object getPauseLock()
    {
        return pauseLock;
    }

    @Override
    public void run()
    {
        while(Menu.menu == null)
        {
            try
            {
                Thread.sleep(1000);
            }
            catch(InterruptedException ex)
            {
                Log.println(Log.ERROR, "game loop", "initial sleep interrupted");
            }
        }

        Menu.menu.initMenu();
        GameThread.nextState();

        framesPassed = 30;

        Time.init();

        int frames = 0;
        int ups = 0;
        long lastTime = System.nanoTime();
        long totalTime = 0;
        long updateTime = 0;
        unpauseTime = 0;

        String framerateOutput = "fps ";
        while(playing)
        {
            long now = System.nanoTime();
            long passed = 0;


            /*long elapsed = now - lastTime;
            Log.println(Log.ERROR, "time", elapsed + "");
            //if(elapsed < 33)
            if(elapsed > 16666666.6667f)
            {
                try
                {
                    Log.println(Log.ERROR, "sleep", "plaze " + (elapsed - 16666667));
                    //Thread.sleep(33 - elapsed);
                    Thread.sleep(16);
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
            }*/

            if(unpauseTime > 0)
            {
                passed = now - unpauseTime;
            }
            else
            {
                passed = now - lastTime;
            }
            unpauseTime = 0;

            lastTime = now;
            totalTime += passed;
            updateTime += passed;

            while(updateTime >= 16666666.6667f)
            {
                Update.update();
                updateTime -= 16666666.6667f;
                ups++;
                Buffer.buffer();
            }

            //Buffer.buffer();
            processInput();
            processFlags();

            if(totalTime >= 1000000000)
            {
                framesPassed = frames;
                //Log.println(Log.ERROR, "loop", framerateOutput + frames);
                //Log.println(Log.ERROR, "update", "" + ups);
                totalTime = 0;
                frames = 0;
                ups = 0;
            }
            frames++;

            synchronized(pauseLock)
            {
                while(paused)
                {
                    try
                    {
                        pauseLock.wait();
                    }
                    catch(InterruptedException e)
                    {

                    }
                }
            }
        }
    }

    public void setPlaying(boolean value)
    {
        synchronized(playingMutex)
        {
            playing = value;
        }
    }

    public void feedInput(InputBuffer input)
    {
        synchronized(inputQueueMutex)
        {
            try
            {
                inputQueue.put(input);
            }
            catch (InterruptedException e)
            {
                Log.e("thread", e.getMessage(), e);
            }
            //this.input = input;
        }
    }

    public void processInput()
    {
        synchronized(inputQueueMutex)
        {
            ArrayBlockingQueue<InputBuffer> inputQueue = this.inputQueue;
            while (!inputQueue.isEmpty())
            {
                try
                {
                    InputBuffer input = inputQueue.take();
                    if (input.eventType == InputBuffer.EVENT_TYPE_KEY)
                    {
                        //processKeyEvent(input);
                    }
                    else if (input.eventType == InputBuffer.EVENT_TYPE_TOUCH)
                    {
                        processMotionEvent(input);
                    }
                    input.returnToPool();
                }
                catch (InterruptedException e)
                {
                    Log.e("thread", e.getMessage(), e);
                }
            }
        }
        /*if(input != null)
        {
            processMotionEvent(input);
        }*/
    }

    private void processMotionEvent(InputBuffer input)
    {
        Input.getInput(input);
    }

    public void onPause()
    {
        synchronized(pauseLock)
        {
            paused = true;
        }
    }

    public void onResume()
    {
        synchronized(pauseLock)
        {
            unpauseTime = System.nanoTime();
            paused = false;
            pauseLock.notifyAll();
        }
    }

    public static void nextState()
    {
        state = state.getNext();
    }

    public static void setState(GameState newState)
    {
        state = newState;
    }

    public void processFlags()
    {
        boolean wasAdReady = isAdReady;
        boolean wasInstitialAdReady = isInstitialAdReady;

        isAdReady = Flag.isAdReady();
        isInstitialAdReady = Flag.isInstitialAdReady();

        if(isAdReady && !wasAdReady)
        {
            handler.removeMessages(0);
            handler.sendEmptyMessage(0);
        }
        else if(!isAdReady && wasAdReady)
        {
            handler.removeMessages(1);
            handler.sendEmptyMessage(1);
        }

        if(isInstitialAdReady && !wasInstitialAdReady)
        {
            handler.removeMessages(2);
            handler.sendEmptyMessage(2);
        }
    }

    public boolean getAdState()
    {
        return isAdReady;
    }

    public boolean getInstitialAdState()
    {
        return isInstitialAdReady;
    }
}
