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
import game.android.com.catstacks.game.objects.statics.backgrounds.weather.Bubble;
import game.android.com.catstacks.game.objects.statics.backgrounds.weather.Ghost;

public class BubbleSpawner extends Spawner
{
    private Texture bubble, smallBubble;

    public BubbleSpawner(int worldWidth, int worldHeight)
    {
        super(worldWidth, worldHeight, true);

        bubble = TextureLoader.loadTexture(RenderedState.getContext(), R.drawable.bubble, 2, 1);
        smallBubble = TextureLoader.loadTexture(RenderedState.getContext(), R.drawable.bubble_small, 2, 1);

        intervals = ArrayPool.floatMaster.get(10);
        for(int i = 0; i < intervals.length; i++)
        {
            intervals[i] = 30f;
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

            int choice = r.nextInt(2);
            if(choice == 0)
            {
                Bubble bubbleObject = new Bubble(bubble, spawnPosition);
                Bubble.pool.recycle(bubbleObject);
            }
            else if(choice == 1)
            {
                Bubble bubbleObject = new Bubble(smallBubble, spawnPosition);
                Bubble.pool.recycle(bubbleObject);
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
        spawnPosition.y = 0;

        if(Bubble.pool.size() > 0)
        {
            Bubble bubbleObject = Bubble.pool.get();
            bubbleObject.setPosition(spawnPosition);
            World.world.spawnObject(bubbleObject);
        }
        else
        {
            /*Bubble bubbleObject = Bubble.pool.get();
            int choice = r.nextInt(2);
            if(choice == 1)
            {
                bubbleObject.init(bubble, spawnPosition);
            }
            else if(choice == 2)
            {
                bubbleObject.init(smallBubble, spawnPosition);
            }
            bubbleObject.setSpawned(true);
            World.world.spawnObject(bubbleObject);*/
        }

        Vec.pool.recycle(spawnPosition);
        ArrayPool.randomPool.recycle(r);
    }
}
