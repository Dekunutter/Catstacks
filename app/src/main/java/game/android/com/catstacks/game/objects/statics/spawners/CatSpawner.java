package game.android.com.catstacks.game.objects.statics.spawners;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.core.RenderedState;
import game.android.com.catstacks.engine.core.Screen;
import game.android.com.catstacks.engine.core.Time;
import game.android.com.catstacks.engine.objects.statics.spawners.Spawner;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.rendering.Texture;
import game.android.com.catstacks.engine.rendering.TextureLoader;
import game.android.com.catstacks.engine.sound.Sound;
import game.android.com.catstacks.engine.states.World;
import game.android.com.catstacks.game.objects.dynamic.Cat;
import game.android.com.catstacks.game.objects.dynamic.Player;

public class CatSpawner extends Spawner
{
    private Texture cat, catBlack, catMixed, catPointed, catTabby;

    private Sound meowSound, catchSound1, catchSound2, catchSound3;

    private float[] row1X = {23, 46, 69, 92, 115, 138, 161, 184};
    private float[] row1Y = {0, 22};
    private float[] row2X = {23};
    private float[] row2Y = {22, 44};
    private float[] row3X = {23, 46, 69};
    private float[] row3Y = {44, 66};
    private float[] row4X = {23, 46};
    private float[] row4Y = {66, 88};
    private float[] row5X = {22, 44, 66, 88, 110, 132, 154, 176};
    private float[] row5Y = {88, 113};
    private float[] row6X = {31, 62};
    private float[] row6Y = {138, 154};
    private float[] row7X = {37, 74, 107};
    private float[] row7Y = {154, 176};
    private float[] row8X = {41, 82, 123, 164, 205};
    private float[] row8Y = {176, 193};
    private float[] row9X = {41, 82, 123, 164};
    private float[] row9Y = {193, 210};
    private float[] row10X = {23, 46, 69, 92, 115, 138, 161, 184};
    private float[] row10Y = {210, 232};
    private float[][] rowsX = {row1X, row2X, row3X, row4X, row5X, row6X, row7X, row8X, row9X, row10X};
    private float[][] rowsY = {row1Y, row2Y, row3Y, row4Y, row5Y, row6Y, row7Y, row8Y, row9Y, row10Y};

    private int position = 0;
    private int totalSpawned = 0;
    private float interval;
    private Player tracking;

    public CatSpawner(int worldWidth, int worldHeight, Player tracking)
    {
        super(worldWidth, worldHeight);
        this.tracking = tracking;

        initCatSounds();

        cat = TextureLoader.loadTexture(RenderedState.getContext(), R.drawable.cat, rowsX, rowsY);
        catBlack = TextureLoader.loadTexture(RenderedState.getContext(), R.drawable.cat_black, rowsX, rowsY);
        catTabby = TextureLoader.loadTexture(RenderedState.getContext(), R.drawable.cat_tabby, rowsX, rowsY);
        catPointed = TextureLoader.loadTexture(RenderedState.getContext(), R.drawable.cat_pointed, rowsX, rowsY);
        catMixed = TextureLoader.loadTexture(RenderedState.getContext(), R.drawable.cat_mixed, rowsX, rowsY);

        interval = 30;

        intervals = ArrayPool.floatMaster.get(45);
        intervals[0] = 15.5f;
        intervals[1] = 25f;
        intervals[2] = 25f;
        intervals[3] = 25f;
        intervals[4] = 25f;
        intervals[5] = 25f;
        intervals[6] = 25f;
        intervals[7] = 25f;
        intervals[8] = 25f;
        intervals[9] = 25f;
        intervals[10] = 25f;
        intervals[11] = 25f;
        intervals[12] = 25f;
        intervals[13] = 25f;
        intervals[14] = 25f;
        intervals[15] = 15f;
        intervals[16] = 15f;
        intervals[17] = 15f;
        intervals[18] = 15f;
        intervals[19] = 15f;
        intervals[20] = 15f;
        intervals[21] = 15f;
        intervals[22] = 15f;
        intervals[23] = 15f;
        intervals[24] = 15f;
        intervals[25] = 15f;
        intervals[26] = 15f;
        intervals[27] = 10f;
        intervals[28] = 10f;
        intervals[29] = 10f;
        intervals[30] = 10f;
        intervals[31] = 10f;
        intervals[32] = 10f;
        intervals[33] = 10f;
        intervals[34] = 10f;
        intervals[35] = 10f;
        intervals[36] = 10f;
        intervals[37] = 10f;
        intervals[38] = 10f;
        intervals[39] = 10f;
        intervals[40] = 10f;
        intervals[41] = 10f;
        intervals[42] = 10f;
        intervals[43] = 10f;
        intervals[44] = 10f;

        positions = ArrayPool.vertexMaster.get(45);
        positions[0] = new Vec(Screen.screen.getPixelDensity(50), worldHeight);
        positions[1] = new Vec(Screen.screen.getPixelDensity(100), worldHeight);
        positions[2] = new Vec(Screen.screen.getPixelDensity(150), worldHeight);
        positions[3] = new Vec(Screen.screen.getPixelDensity(200), worldHeight);
        positions[4] = new Vec(Screen.screen.getPixelDensity(250), worldHeight);
        positions[5] = new Vec(Screen.screen.getPixelDensity(50), worldHeight);
        positions[6] = new Vec(Screen.screen.getPixelDensity(100), worldHeight);
        positions[7] = new Vec(Screen.screen.getPixelDensity(150), worldHeight);
        positions[8] = new Vec(Screen.screen.getPixelDensity(200), worldHeight);
        positions[9] = new Vec(Screen.screen.getPixelDensity(250), worldHeight);
        positions[10] = new Vec(Screen.screen.getPixelDensity(50), worldHeight);
        positions[11] = new Vec(Screen.screen.getPixelDensity(100), worldHeight);
        positions[12] = new Vec(Screen.screen.getPixelDensity(150), worldHeight);
        positions[13] = new Vec(Screen.screen.getPixelDensity(200), worldHeight);
        positions[14] = new Vec(Screen.screen.getPixelDensity(250), worldHeight);
        positions[15] = new Vec(Screen.screen.getPixelDensity(50), worldHeight);
        positions[16] = new Vec(Screen.screen.getPixelDensity(100), worldHeight);
        positions[17] = new Vec(Screen.screen.getPixelDensity(150), worldHeight);
        positions[18] = new Vec(Screen.screen.getPixelDensity(200), worldHeight);
        positions[19] = new Vec(Screen.screen.getPixelDensity(250), worldHeight);
        positions[20] = new Vec(Screen.screen.getPixelDensity(50), worldHeight);
        positions[21] = new Vec(Screen.screen.getPixelDensity(100), worldHeight);
        positions[22] = new Vec(Screen.screen.getPixelDensity(150), worldHeight);
        positions[23] = new Vec(Screen.screen.getPixelDensity(200), worldHeight);
        positions[24] = new Vec(Screen.screen.getPixelDensity(250), worldHeight);
        positions[25] = new Vec(Screen.screen.getPixelDensity(50), worldHeight);
        positions[26] = new Vec(Screen.screen.getPixelDensity(100), worldHeight);
        positions[27] = new Vec(Screen.screen.getPixelDensity(150), worldHeight);
        positions[28] = new Vec(Screen.screen.getPixelDensity(200), worldHeight);
        positions[29] = new Vec(Screen.screen.getPixelDensity(250), worldHeight);
        positions[30] = new Vec(Screen.screen.getPixelDensity(50), worldHeight);
        positions[31] = new Vec(Screen.screen.getPixelDensity(100), worldHeight);
        positions[32] = new Vec(Screen.screen.getPixelDensity(150), worldHeight);
        positions[33] = new Vec(Screen.screen.getPixelDensity(200), worldHeight);
        positions[34] = new Vec(Screen.screen.getPixelDensity(250), worldHeight);
        positions[35] = new Vec(Screen.screen.getPixelDensity(50), worldHeight);
        positions[36] = new Vec(Screen.screen.getPixelDensity(100), worldHeight);
        positions[37] = new Vec(Screen.screen.getPixelDensity(150), worldHeight);
        positions[38] = new Vec(Screen.screen.getPixelDensity(200), worldHeight);
        positions[39] = new Vec(Screen.screen.getPixelDensity(250), worldHeight);
        positions[40] = new Vec(Screen.screen.getPixelDensity(50), worldHeight);
        positions[41] = new Vec(Screen.screen.getPixelDensity(100), worldHeight);
        positions[42] = new Vec(Screen.screen.getPixelDensity(150), worldHeight);
        positions[43] = new Vec(Screen.screen.getPixelDensity(200), worldHeight);
        positions[44] = new Vec(Screen.screen.getPixelDensity(250), worldHeight);

        initCats();
    }

    private void initCats()
    {
        Random r = ArrayPool.randomPool.get();

        for(int i = 0; i < 45; i++)
        {
            int choice = r.nextInt(5);

            Cat newCat = new Cat();
            if(choice == 0)
            {
                newCat.init(positions[i], cat, meowSound, catchSound1, catchSound2, catchSound3);
            }
            else if(choice == 1)
            {
                newCat.init(positions[i], catBlack, meowSound, catchSound1, catchSound2, catchSound3);
            }
            else if(choice == 2)
            {
                newCat.init(positions[i], catMixed, meowSound, catchSound1, catchSound2, catchSound3);
            }
            else if(choice == 3)
            {
                newCat.init(positions[i], catPointed, meowSound, catchSound1, catchSound2, catchSound3);
            }
            else if(choice == 4)
            {
                newCat.init(positions[i], catTabby, meowSound, catchSound1, catchSound2, catchSound3);
            }

            Cat.pool.recycle(newCat);
        }
        ArrayPool.randomPool.recycle(r);
    }

    private void initCatSounds()
    {
        meowSound = Sound.pool.get();
        meowSound.init(R.raw.meow_kitten6);

        catchSound1 = Sound.pool.get();
        catchSound1.init(R.raw.catch1, 0.5f);

        catchSound2 = Sound.pool.get();
        catchSound2.init(R.raw.catch2, 0.5f);

        catchSound3 = Sound.pool.get();
        catchSound3.init(R.raw.catch3, 0.5f);
    }

    @Override
    public void update()
    {
        updateSpawnTimer();

        if(isSpawnReady())
        {
            spawnCat();
        }


        setSpawn(false);
    }

    private void spawnCat()
    {
        //if(position < Cat.pool.size())
        if(Cat.pool.size() > 0)
        {
            Random r = ArrayPool.randomPool.get();

            Vec spawnPosition = Vec.pool.get();
            spawnPosition.x = (float) r.nextInt((int)Screen.screen.getPixelDensity(250) - (int)Screen.screen.getPixelDensity(50) + 1) + (int)Screen.screen.getPixelDensity(50);
            spawnPosition.y = World.world.getWorldHeight();

            //Cat newCat = Cat.pool.get(0);
            Cat newCat = Cat.pool.get(Cat.pool.size() - 1);
            newCat.getBody().setPosition(spawnPosition);
            World.world.spawnObject(newCat);

            Vec.pool.recycle(spawnPosition);
            ArrayPool.randomPool.recycle(r);
            position++;
        }
        else
        {
            //position = 0;
            //spawnCat();
        }
    }

    @Override
    protected void updateSpawnTimer()
    {
        if(spawned >= 45)
        {
            totalSpawned += spawned;
            spawned = 0;
        }

        accumulatedTime += Time.getDelta();
        if(accumulatedTime > interval)
        {
            spawned++;
            accumulatedTime = 0;
            spawn = true;
            updateInterval();
        }
    }

    private void updateInterval()
    {
        if(tracking.getScore() >= 60000)
        {
            interval = 8;
        }
        else if(tracking.getScore() >= 50000)
        {
            interval = 9f;
        }
        else if(tracking.getScore() >= 40000)
        {
            interval = 10f;
        }
        else if(tracking.getScore() >= 30000)
        {
            interval = 11f;
        }
        else if(tracking.getScore() >= 20000)
        {
            interval = 12f;
        }
        else if(tracking.getScore() >= 17500)
        {
            interval = 13f;
        }
        else if(tracking.getScore() >= 15000)
        {
            interval = 14f;
        }
        else if(tracking.getScore() >= 12500)
        {
            interval = 16f;
        }
        else if(tracking.getScore() >= 10000)
        {
            interval = 18f;
        }
        else if(tracking.getScore() >= 7500)
        {
            interval = 19f;
        }
        else if(tracking.getScore() >= 5000)
        {
            interval = 20f;
        }
        else if(tracking.getScore() >= 2500)
        {
            interval = 24f;
        }
        else if(tracking.getScore() >= 1000)
        {
            interval = 26f;
        }
        else if(tracking.getScore() >= 250)
        {
            interval = 28f;
        }
    }
}
