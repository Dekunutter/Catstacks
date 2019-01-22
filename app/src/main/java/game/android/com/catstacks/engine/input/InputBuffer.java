package game.android.com.catstacks.engine.input;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import game.android.com.catstacks.engine.core.Screen;

public class InputBuffer
{
    public static final byte EVENT_TYPE_KEY = 1;
    public static final byte EVENT_TYPE_TOUCH = 2;
    public static final int ACTION_KEY_DOWN = 1;
    public static final int ACTION_KEY_UP = 2;
    public static final int ACTION_TOUCH_DOWN = 3;
    public static final int ACTION_TOUCH_MOVE = 4;
    public static final int ACTION_TOUCH_UP = 5;

    public static final int ACTION_KEY_MULTIPLE = 6;

    public ArrayBlockingQueue<InputBuffer> pool;
    public byte eventType;
    public long time;
    public int action;
    public int keyCode;
    public int x, previousX;
    public int y, previousY;
    public static int currentTouches = 0;
    public static int current = 0, previous = 0;
    public static ArrayList<Integer> others = new ArrayList<>();
    public boolean touched;

    public InputBuffer(ArrayBlockingQueue<InputBuffer> pool)
    {
        this.pool = pool;
    }

    public void useEvent(KeyEvent event)
    {
        eventType = EVENT_TYPE_KEY;
        int a = event.getAction();
        switch (a)
        {
            case KeyEvent.ACTION_DOWN:
                action = ACTION_KEY_DOWN;
                break;
            case KeyEvent.ACTION_UP:
                action = ACTION_KEY_UP;
                break;
            case KeyEvent.ACTION_MULTIPLE:
                action = ACTION_KEY_MULTIPLE;
                break;
            default:
                action = 0;
        }
        time = event.getEventTime();
        keyCode = event.getKeyCode();
    }

    public void useEvent(MotionEvent event)
    {
        eventType = EVENT_TYPE_TOUCH;
        int a = event.getActionMasked();
        touched = false;
        switch (a)
        {
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_DOWN:
                action = ACTION_TOUCH_DOWN;
                previous = current;
                others.add(current);
                current = event.getPointerId(event.getActionIndex());
                currentTouches++;
                touched = true;
                break;
            case MotionEvent.ACTION_MOVE:
                action = ACTION_TOUCH_MOVE;
                touched = true;
                break;
            case MotionEvent.ACTION_UP:
                currentTouches = 0;
                current = 0;
                previous = 0;
                others.clear();
                action = ACTION_TOUCH_UP;
                touched = false;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                action = ACTION_TOUCH_UP;
                if(current == event.getPointerId(event.getActionIndex()))
                {
                    current = others.get(others.size() - 1);
                    others.remove(others.size() - 1);
                }
                else
                {
                    for(int i = 0; i < others.size(); i++)
                    {
                        if(others.get(i) == event.getPointerId(event.getActionIndex()))
                        {
                            others.remove(i);
                        }
                    }
                }
                touched = true;
                currentTouches--;
                break;
            default:
                action = 0;
        }

        time = event.getEventTime();
        if(currentTouches > 0)
        {
            if(current >= event.getPointerCount())
            {
                x = (int) event.getX(event.getPointerCount() - 1);
                y = Screen.screen.getHeight() - (int) event.getY(event.getPointerCount() - 1);
            }
            else
            {
                x = (int) event.getX(current);
                y = Screen.screen.getHeight() - (int) event.getY(current);
            }
        }
        else
        {
            x = (int) event.getX(current);
            y = Screen.screen.getHeight() - (int) event.getY(current);
            previousX = x;
            previousY = y;
        }
        //int realY = Screen.screen.getHeight() - (int) event.getY();
        //y = realY + (int)(((Screen.screen.getHeight()) - Screen.screen.newheight) / Screen.getSSU()) / 2;
    }

    public void useEventHistory(MotionEvent event, int historyItem)
    {
        eventType = EVENT_TYPE_TOUCH;
        action = ACTION_TOUCH_MOVE;
        time = event.getHistoricalEventTime(historyItem);
        x = (int) event.getHistoricalX(historyItem);
        //y = (int) event.getHistoricalY(historyItem);
        y = Screen.screen.getHeight() - (int) event.getHistoricalY(historyItem);
    }

    public void returnToPool()
    {
        pool.add(this);
    }
}

