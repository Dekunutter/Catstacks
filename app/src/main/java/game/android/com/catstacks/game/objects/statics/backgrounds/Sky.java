package game.android.com.catstacks.game.objects.statics.backgrounds;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.core.Screen;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.pool.GenericObjectPool;
import game.android.com.catstacks.game.logic.BackgroundTheme;

public class Sky extends BackgroundScenery
{
    public static final GenericObjectPool<Sky> pool = new GenericObjectPool<Sky>(Sky.class);

    public Sky(BackgroundTheme.Theme activeTheme)
    {
        if(activeTheme == BackgroundTheme.Theme.ASIAN)
        {
            addStretchedSprite(R.drawable.background5, -10f, 1, Screen.screen.getWidth(), Screen.screen.getHeight());
        }
        else if(activeTheme == BackgroundTheme.Theme.SPOOKY)
        {
            addStretchedSprite(R.drawable.background_red, -10f, 1, Screen.screen.getWidth(), Screen.screen.getHeight());
        }
        else if(activeTheme == BackgroundTheme.Theme.CHRISTMAS)
        {
            addStretchedSprite(R.drawable.background4, -10f, 1, Screen.screen.getWidth(), Screen.screen.getHeight());
        }
        else if(activeTheme == BackgroundTheme.Theme.BEACH)
        {
            addStretchedSprite(R.drawable.background5, -10f, 1, Screen.screen.getWidth(), Screen.screen.getHeight());
        }
        else if(activeTheme == BackgroundTheme.Theme.DESERT)
        {
            addStretchedSprite(R.drawable.background5, -10f, 1, Screen.screen.getWidth(), Screen.screen.getHeight());
        }
        else if(activeTheme == BackgroundTheme.Theme.CANYON)
        {
            addStretchedSprite(R.drawable.background7, -10f, 1, Screen.screen.getWidth(), Screen.screen.getHeight());
            //addStretchedSprite(R.drawable.background7, -10f, 1, Screen.screen.getWidth(), Screen.screen.getHeight());
        }
        else if(activeTheme == BackgroundTheme.Theme.FOREST)
        {
            addStretchedSprite(R.drawable.background4, -10f, 1, Screen.screen.getWidth(), Screen.screen.getHeight());
        }
        else if(activeTheme == BackgroundTheme.Theme.CITY)
        {
            addStretchedSprite(R.drawable.background4, -10f, 1, Screen.screen.getWidth(), Screen.screen.getHeight());
        }
        else if(activeTheme == BackgroundTheme.Theme.CITYPARK)
        {
            addStretchedSprite(R.drawable.background4, -10f, 1, Screen.screen.getWidth(), Screen.screen.getHeight());
        }
        else if(activeTheme == BackgroundTheme.Theme.UNDERWATER)
        {
            addStretchedSprite(R.drawable.background_water, -10f, 1, Screen.screen.getWidth(), Screen.screen.getHeight());
        }
        else
        {
            addStretchedSprite(R.drawable.background4, -10f, 1, Screen.screen.getWidth(), Screen.screen.getHeight());
        }

        activeFrames.add(null);

        hasAlpha = false;
    }
}
