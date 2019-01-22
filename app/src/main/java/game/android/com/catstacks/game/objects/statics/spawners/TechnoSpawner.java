package game.android.com.catstacks.game.objects.statics.spawners;

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
import game.android.com.catstacks.game.objects.statics.backgrounds.weather.TechnoDrop;

public class TechnoSpawner extends Spawner
{
    private Texture technoDrop;

    public TechnoSpawner(int worldWidth, int worldHeight)
    {
        super(worldWidth, worldHeight, true);

        technoDrop = TextureLoader.loadTexture(RenderedState.getContext(), R.drawable.techno_drop2, 6, 1);

        intervals = ArrayPool.floatMaster.get(20);
        for(int i = 0; i < intervals.length; i++)
        {
            intervals[i] = 13f;
        }

        initEffect();
    }

    private void initEffect()
    {
        Random r = ArrayPool.randomPool.get();
        Vec spawnPosition = Vec.pool.get();

        for(int i = 0; i < 20; i++)
        {
            spawnPosition.x = (float) r.nextInt((int)Screen.screen.getPixelDensity(280) - (int)Screen.screen.getPixelDensity(5)  + 1) + (int)Screen.screen.getPixelDensity(5);
            spawnPosition.y = height;
            TechnoDrop drop = new TechnoDrop(technoDrop, spawnPosition);
            TechnoDrop.pool.recycle(drop);
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

        if(TechnoDrop.pool.size() > 0)
        {
            TechnoDrop drop = TechnoDrop.pool.get();
            drop.setPosition(spawnPosition);
            World.world.spawnObject(drop);
        }
        else
        {
            /*TechnoDrop drop = TechnoDrop.pool.get();
            drop.init(technoDrop, spawnPosition);
            drop.setSpawned(true);
            World.world.spawnObject(drop);*/
        }

        Vec.pool.recycle(spawnPosition);
        ArrayPool.randomPool.recycle(r);
    }
}
