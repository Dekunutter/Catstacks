package game.android.com.catstacks.engine.objects.statics.spawners;

import android.util.Log;

import game.android.com.catstacks.engine.core.Time;
import game.android.com.catstacks.engine.objects.WorldObject;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;

public abstract class Spawner extends WorldObject
{
    protected int width, height;

    protected int spawned;
    protected float[] intervals;
    protected float accumulatedTime;
    protected Vec[] positions;
    protected boolean spawn, looping;

    protected Spawner(int worldWidth, int worldHeight)
    {
        super();

        width = worldWidth;
        height = worldHeight;

        unmovable = true;

        spawned = 0;
        accumulatedTime = 0;
        spawn = false;
        looping = false;
    }

    protected Spawner(int worldWidth, int worldHeight, boolean looping)
    {
        this(worldWidth, worldHeight);

        this.looping = looping;
    }

    public void setLooping(boolean value)
    {
        looping = value;
    }

    protected boolean isSpawnReady()
    {
        return spawn;
    }

    protected void setSpawn(boolean value)
    {
        spawn = value;
    }

    protected Vec getCurrentSpawnPosition()
    {
        return positions[spawned - 1];
    }

    protected void updateSpawnTimer()
    {
        int counter = spawned;

        if(!looping)
        {
            if(spawned >= intervals.length)
            {
                return;
            }
        }
        else
        {
            if(spawned >= intervals.length)
            {
                counter = spawned % intervals.length;
            }
        }

        accumulatedTime += Time.getDelta();
        if(accumulatedTime > intervals[counter])
        {
            spawned++;
            accumulatedTime = 0;
            spawn = true;
        }
    }

    @Override
    public abstract void update();

    public void destruct()
    {
        super.destruct();

        ArrayPool.floatMaster.recycle(intervals, intervals.length);
    }
}
