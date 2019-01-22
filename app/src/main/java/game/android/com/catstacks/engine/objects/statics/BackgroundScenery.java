package game.android.com.catstacks.engine.objects.statics;

import game.android.com.catstacks.engine.core.Time;
import game.android.com.catstacks.engine.objects.WorldObject;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.pool.GenericObjectPool;

public class BackgroundScenery extends WorldObject
{
    protected float alpha = 0;
    protected float fadeTimer = 100;
    protected boolean spawned = false, fadedIn = false;

    protected BackgroundScenery()
    {
        super();

        unmovable = true;

        solid = false;

        defaultFrames.add(0);
    }

    @Override
    public void update()
    {
        if(spawned)
        {
            if(fadeTimer <= 0)
            {
                if(!fadedIn)
                {
                    changeColourAlpha(0, 1);
                    fadedIn = true;
                }
            }
            else
            {
                fadeTimer -= Time.getDelta();
                float inc = 1 / 100f;
                alpha += inc;
                if(alpha >= 1)
                {
                    alpha = 1;
                }

                changeColourAlpha(0, alpha);
            }
        }
    }

    public void setSpawned(boolean value)
    {
        spawned = value;
    }

    public void destruct()
    {
        super.destruct();

        alpha = 0;
        fadeTimer = 100;

        changeColourAlpha(0, 0);
        spawned = false;
        fadedIn = false;
    }
}
