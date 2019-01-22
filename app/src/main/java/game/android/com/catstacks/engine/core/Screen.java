package game.android.com.catstacks.engine.core;

import android.content.Context;
import android.util.Log;

public class Screen
{
    public static Screen screen;

    public static float defaultWidth = 360;
    public static float defaultHeight = 592;

    protected int width, height;
    protected static float ssu;

    public Screen(Context context, int width, int height)
    {

        this.width = width;
        this.height = height;

        //ssu = context.getResources().getDisplayMetrics().density;

        ssu = (float)(width / 360f);

        //Log.println(Log.ERROR, "densities", ssu + " " + context.getResources().getDisplayMetrics().densityDpi);
    }

    public static float[] getPixelsDensity(float[] array)
    {
        for(int i = 0; i < array.length; i++)
        {
            getPixelDensity(array[i]);
        }
        return array;
    }

    public static float getPixelDensity(float value)
    {
        return value *= ssu;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public static float getSSU()
    {
        return ssu;
    }
}
