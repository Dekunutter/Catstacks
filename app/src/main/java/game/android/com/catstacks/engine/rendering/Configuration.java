package game.android.com.catstacks.engine.rendering;

import android.content.Context;
import android.util.Log;

public class Configuration
{
    private static final float[] DEFAULT_COLOUR = {1, 1, 0, 1};
    public float screenHeight = 768;
    public float screenWidth = 1280;
    public int shp = 480;
    public float ssu = 1;
    public float ssx = 1;
    public float ssy = 1;
    public int swp = 320;

    public float getSSU()
    {
        return ssu;
    }

    public float getSSX()
    {
        return ssx;
    }

    public float getSSY()
    {
        return ssy;
    }

    public float getScreenHeight()
    {
        return screenHeight;
    }

    public float getScreenWidth()
    {
        return screenWidth;
    }

    public void recalculateScreenPixels(Context context)
    {
        swp = context.getResources().getDisplayMetrics().widthPixels;
        shp = context.getResources().getDisplayMetrics().heightPixels;
    }

    public void recalculateScreenSize()
    {
        ssx = (swp / 320);
        ssy = (shp / 480);
    }

    public void setScreenHeight(float newHeight)
    {
        screenHeight = newHeight;
    }

    public void setScreenUnits(float newSSU)
    {
        ssu = newSSU;
    }

    public void setScreenWidth(float width)
    {
        screenWidth = width;
    }
}
