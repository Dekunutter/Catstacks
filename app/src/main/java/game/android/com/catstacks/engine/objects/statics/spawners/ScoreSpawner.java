package game.android.com.catstacks.engine.objects.statics.spawners;

import game.android.com.catstacks.game.objects.dynamic.Player;

public abstract class ScoreSpawner extends Spawner
{
    protected Player tracking;
    protected boolean usingInitialScore;
    protected int firstSpawnScore, lastSpawn;

    protected ScoreSpawner(int worldWidth, int worldHeight, Player tracking)
    {
        super(worldWidth, worldHeight);

        this.tracking = tracking;
        usingInitialScore = false;
        firstSpawnScore = 0;
        lastSpawn = 0;
        spawned = 0;
    }

    protected ScoreSpawner(int worldWidth, int worldHeight, Player tracking, int firstSpawnScore)
    {
        super(worldWidth, worldHeight);

        this.tracking = tracking;
        usingInitialScore = true;
        this.firstSpawnScore = firstSpawnScore;
        lastSpawn = 0;
        spawned = 0;
    }

    protected void updateSpawnScoreTracker(float interval)
    {
        if(usingInitialScore && (tracking.getScore() < firstSpawnScore))
        {
            return;
        }

        if(tracking.getScore() > 0)
        {
            if(tracking.getScore() >= lastSpawn + interval)
            {
                if(spawned == 0)
                {
                    lastSpawn += firstSpawnScore;
                }
                else
                {
                    lastSpawn += interval;
                }
                spawn = true;
                spawned++;
            }
        }
    }

    @Override
    public abstract void update();
}
