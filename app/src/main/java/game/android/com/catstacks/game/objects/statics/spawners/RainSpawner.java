package game.android.com.catstacks.game.objects.statics.spawners;

import android.util.Log;

import java.util.Random;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.core.RenderedState;
import game.android.com.catstacks.engine.core.Screen;
import game.android.com.catstacks.engine.objects.statics.spawners.Spawner;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.rendering.Texture;
import game.android.com.catstacks.engine.rendering.TextureLoader;
import game.android.com.catstacks.engine.states.World;
import game.android.com.catstacks.game.objects.statics.backgrounds.weather.Raindrop;
import game.android.com.catstacks.game.objects.statics.backgrounds.weather.TechnoDrop;

public class RainSpawner extends Spawner
{
    private Texture rainDrop;

    public RainSpawner(int worldWidth, int worldHeight)
    {
        super(worldWidth, worldHeight);

        rainDrop = TextureLoader.loadTexture(RenderedState.getContext(), R.drawable.raindrop);

        intervals = ArrayPool.floatMaster.get(30);
        for(int i = 0; i < intervals.length; i++)
        {
            intervals[i] = 4f;
        }

        initRain();
    }

    private void initRain()
    {
        Random r = ArrayPool.randomPool.get();
        Vec spawnPosition = Vec.pool.get();

        for(int i = 0; i < 50; i++)
        {
            spawnPosition.x = (float) r.nextInt((int) Screen.screen.getPixelDensity(280) - (int)Screen.screen.getPixelDensity(5) + 1) + (int)Screen.screen.getPixelDensity(5);
            spawnPosition.y = height;
            Raindrop drop = new Raindrop(rainDrop, spawnPosition);
            Raindrop.pool.recycle(drop);
        }

        Vec.pool.recycle(spawnPosition);
        ArrayPool.randomPool.recycle(r);
    }

    @Override
    public void update()
    {
        updateSpawnTimer();

        if(isSpawnReady())
        {
            spawnEffect();
        }

        setSpawn(false);
    }

    private void spawnEffect()
    {
        Random r = ArrayPool.randomPool.get();
        Vec spawnPosition = Vec.pool.get();
        spawnPosition.x = (float) r.nextInt((int)Screen.screen.getPixelDensity(280) - (int)Screen.screen.getPixelDensity(5) + 1) + (int)Screen.screen.getPixelDensity(5);
        spawnPosition.y = height;

        if(Raindrop.pool.size() > 0)
        {
            Raindrop drop = Raindrop.pool.get();
            drop.move(spawnPosition);
            World.world.spawnObject(drop);
        }
        else
        {
            /*Raindrop drop = Raindrop.pool.get();
            drop.init(rainDrop, spawnPosition);
            drop.setSpawned(true);
            World.world.spawnObject(drop);*/
        }

        Vec.pool.recycle(spawnPosition);
        ArrayPool.randomPool.recycle(r);
    }
}
