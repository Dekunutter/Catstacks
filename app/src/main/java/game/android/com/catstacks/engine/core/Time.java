package game.android.com.catstacks.engine.core;

public class Time
{
    private final static float trueDelta = 0.66f;
    private static float currentDelta = 0.66f;
    private static long currentTime, lastTime;

    public static long getTime()
    {
        return System.nanoTime();
    }

    public static float getDelta()
    {
        return currentDelta;
    }

    public static float getTrueDelta()
    {
        return trueDelta;
    }

    public static void update()
    {
        lastTime = currentTime;
        currentTime = getTime();
    }

    public static void init()
    {
        currentTime = getTime();
        lastTime = getTime();
    }

    public static void updateDelta(float multiplier)
    {
        currentDelta = trueDelta * multiplier;
    }
}
