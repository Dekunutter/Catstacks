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
import game.android.com.catstacks.game.objects.statics.backgrounds.weather.Snowflake;

public class SnowSpawner extends Spawner
{
    private Texture snowflake, largeSnowflake;

    public SnowSpawner(int worldWidth, int worldHeight)
    {
        super(worldWidth, worldHeight, true);

        snowflake = TextureLoader.loadTexture(RenderedState.getContext(), R.drawable.snowflake);
        largeSnowflake = TextureLoader.loadTexture(RenderedState.getContext(), R.drawable.snowflake_large);

        intervals = ArrayPool.floatMaster.get(30);
        for(int i = 0; i < intervals.length; i++)
        {
            intervals[i] = 4f;
        }

        initSnow();
    }

    private void initSnow()
    {
        Random r = ArrayPool.randomPool.get();
        Vec spawnPosition = Vec.pool.get();

        int largeInc = 0;
        for(int i = 0; i < 30; i++)
        {
            if(largeInc == 2)
            {
                spawnPosition.x = (float) r.nextInt((int) Screen.screen.getPixelDensity(280) - (int) Screen.screen.getPixelDensity(5) + 1) + (int) Screen.screen.getPixelDensity(5);
                spawnPosition.y = height;
                Snowflake flake = new Snowflake(largeSnowflake, spawnPosition);
                Snowflake.pool.recycle(flake);
                largeInc = 0;
            }
            else
            {
                spawnPosition.x = (float) r.nextInt((int) Screen.screen.getPixelDensity(280) - (int) Screen.screen.getPixelDensity(5) + 1) + (int) Screen.screen.getPixelDensity(5);
                spawnPosition.y = height;
                Snowflake flake = new Snowflake(snowflake, spawnPosition);
                Snowflake.pool.recycle(flake);
                largeInc++;
            }
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

        if(Snowflake.pool.size() > 0)
        {
            Snowflake flake = Snowflake.pool.get();
            //flake.init(snowflake, spawnPosition);
            flake.move(spawnPosition);
            flake.setSpawned(true);
            World.world.spawnObject(flake);
        }
        else
        {
            /*Snowflake flake = Snowflake.pool.get();
            flake.init(snowflake, spawnPosition);
            flake.setSpawned(true);
            World.world.spawnObject(flake);*/
        }

        Vec.pool.recycle(spawnPosition);
        ArrayPool.randomPool.recycle(r);
    }
}
