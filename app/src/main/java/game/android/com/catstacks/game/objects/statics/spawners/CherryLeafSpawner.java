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
import game.android.com.catstacks.game.objects.statics.backgrounds.weather.CherryLeaf;

public class CherryLeafSpawner extends Spawner
{
    private Texture cherryLeaf;

    public CherryLeafSpawner(int worldWidth, int worldHeight)
    {
        super(worldWidth, worldHeight, true);

        cherryLeaf = TextureLoader.loadTexture(RenderedState.getContext(), R.drawable.cherry_leaf);

        intervals = ArrayPool.floatMaster.get(30);
        for(int i = 0; i < intervals.length; i++)
        {
            intervals[i] = 4f;
        }

        initEffect();
    }

    private void initEffect()
    {
        Random r = ArrayPool.randomPool.get();
        Vec spawnPosition = Vec.pool.get();

        for(int i = 0; i < 30; i++)
        {
            spawnPosition.x = (float) r.nextInt((int) Screen.screen.getPixelDensity(280) - (int) Screen.screen.getPixelDensity(5) + 1) + (int) Screen.screen.getPixelDensity(5);
            spawnPosition.y = height;
            CherryLeaf leaf = new CherryLeaf(cherryLeaf, spawnPosition);
            CherryLeaf.pool.recycle(leaf);
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
        spawnPosition.x = (float) r.nextInt((int) Screen.screen.getPixelDensity(280) - (int) Screen.screen.getPixelDensity(5) + 1) + (int) Screen.screen.getPixelDensity(5);
        spawnPosition.y = height;

        if(CherryLeaf.pool.size() > 0)
        {
            CherryLeaf leaf = CherryLeaf.pool.get();
            leaf.move(spawnPosition);
            leaf.setSpawned(true);
            World.world.spawnObject(leaf);
        }
        else
        {
            /*CherryLeaf leaf = CherryLeaf.pool.get();
            leaf.init(cherryLeaf, spawnPosition);
            leaf.setSpawned(true);
            World.world.spawnObject(leaf);*/
        }

        Vec.pool.recycle(spawnPosition);
        ArrayPool.randomPool.recycle(r);
    }
}
