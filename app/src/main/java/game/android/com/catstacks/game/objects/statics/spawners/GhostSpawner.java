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
import game.android.com.catstacks.game.objects.statics.backgrounds.weather.Ghost;

public class GhostSpawner extends Spawner
{
    private Texture ghost;

    public GhostSpawner(int worldWidth, int worldHeight)
    {
        super(worldWidth, worldHeight, true);

        ghost = TextureLoader.loadTexture(RenderedState.getContext(), R.drawable.ghosts, 3, 1);

        intervals = ArrayPool.floatMaster.get(3);
        for(int i = 0; i < intervals.length; i++)
        {
            intervals[i] = 25f;
        }

        initEffect();
    }

    private void initEffect()
    {
        Random r = ArrayPool.randomPool.get();
        Vec spawnPosition = Vec.pool.get();

        for(int i = 0; i < 20; i++)
        {
            spawnPosition.x = (float) r.nextInt((int) Screen.screen.getPixelDensity(280) - (int)Screen.screen.getPixelDensity(5)  + 1) + (int)Screen.screen.getPixelDensity(5);
            spawnPosition.y = 0;
            Ghost ghostObject = new Ghost(ghost, spawnPosition);
            Ghost.pool.recycle(ghostObject);
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
        spawnPosition.y = 0;

        if(Ghost.pool.size() > 0)
        {
            Ghost ghostObject = Ghost.pool.get();
            ghostObject.setPosition(spawnPosition);
            World.world.spawnObject(ghostObject);
        }
        else
        {
            /*Ghost ghostObject = Ghost.pool.get();
            ghostObject.init(ghost, spawnPosition);
            ghostObject.setSpawned(true);
            World.world.spawnObject(ghostObject);*/
        }

        Vec.pool.recycle(spawnPosition);
        ArrayPool.randomPool.recycle(r);
    }
}
