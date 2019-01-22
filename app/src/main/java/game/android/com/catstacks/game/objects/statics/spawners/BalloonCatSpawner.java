package game.android.com.catstacks.game.objects.statics.spawners;

import android.util.Log;

import java.util.Random;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.core.RenderedState;
import game.android.com.catstacks.engine.core.Screen;
import game.android.com.catstacks.engine.objects.statics.spawners.ScoreSpawner;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.rendering.Texture;
import game.android.com.catstacks.engine.rendering.TextureLoader;
import game.android.com.catstacks.engine.sound.Sound;
import game.android.com.catstacks.engine.states.World;
import game.android.com.catstacks.game.objects.dynamic.BalloonCat;
import game.android.com.catstacks.game.objects.dynamic.Player;

public class BalloonCatSpawner extends ScoreSpawner
{
    private Texture texture, textureBlack, textureTabby, texturePointed, textureMixed, balloonTexture;

    private Sound meowSound, catchSound1, catchSound2;

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
    private float[][] rowsX = {row1X, row2X, row3X, row4X, row5X, row6X, row7X, row8X, row9X};
    private float[][] rowsY = {row1Y, row2Y, row3Y, row4Y, row5Y, row6Y, row7Y, row8Y, row9Y};

    private int position = 0;

    private static final int FIRST_SPAWN = 5000;
    private static final int SCORE_INTERVAL = 1000;

    private static final float SPAWN_HEIGHT = Screen.screen.getPixelDensity(380);

    public BalloonCatSpawner(int worldWidth, int worldHeight, Player tracking)
    {
        super(worldWidth, worldHeight, tracking, FIRST_SPAWN);

        initSounds();
        
        texture = TextureLoader.loadTexture(RenderedState.getContext(), R.drawable.cat, rowsX, rowsY);
        textureBlack = TextureLoader.loadTexture(RenderedState.getContext(), R.drawable.cat, rowsX, rowsY);
        textureTabby = TextureLoader.loadTexture(RenderedState.getContext(), R.drawable.cat, rowsX, rowsY);
        texturePointed = TextureLoader.loadTexture(RenderedState.getContext(), R.drawable.cat, rowsX, rowsY);
        textureMixed = TextureLoader.loadTexture(RenderedState.getContext(), R.drawable.cat, rowsX, rowsY);

        balloonTexture = TextureLoader.loadTexture(RenderedState.getContext(), R.drawable.balloons, 7, 2);

        initCats();
    }

    private void initCats()
    {
        Random r = ArrayPool.randomPool.get();

        for(int i = 0; i < 5; i++)
        {
            int choice = r.nextInt(5);
            int balloonColour = new Random().nextInt(14);

            Vec position = Vec.pool.get();
            position.x = Screen.screen.getPixelDensity(-40);
            position.y = SPAWN_HEIGHT;

            BalloonCat newCat = new BalloonCat();
            if(choice == 0)
            {
                newCat.init(position, texture, balloonTexture, balloonColour, meowSound, catchSound1, catchSound2);
            }
            else if(choice == 1)
            {
                newCat.init(position, textureBlack, balloonTexture, balloonColour, meowSound, catchSound1, catchSound2);
            }
            else if(choice == 2)
            {
                newCat.init(position, textureTabby, balloonTexture, balloonColour, meowSound, catchSound1, catchSound2);
            }
            else if(choice == 3)
            {
                newCat.init(position, texturePointed, balloonTexture, balloonColour, meowSound, catchSound1, catchSound2);
            }
            else if(choice == 4)
            {
                newCat.init(position, textureMixed, balloonTexture, balloonColour, meowSound, catchSound1, catchSound2);
            }
            Vec.pool.recycle(position);

            BalloonCat.pool.recycle(newCat);
        }
        ArrayPool.randomPool.recycle(r);
    }

    private void initSounds()
    {
        meowSound = Sound.pool.get();
        meowSound.init(R.raw.meow_kitten6);

        catchSound1 = Sound.pool.get();
        catchSound1.init(R.raw.catch_balloon1, 0.5f);

        catchSound2 = Sound.pool.get();
        catchSound2.init(R.raw.catch_balloon2, 0.5f);
    }

    @Override
    public void update()
    {
        updateSpawnScoreTracker(SCORE_INTERVAL);

        if(isSpawnReady())
        {
            spawnCat();
        }

        setSpawn(false);
    }

    private void spawnCat()
    {
        if(position < BalloonCat.pool.size())
        {
            Vec spawnPosition = Vec.pool.get();
            spawnPosition.x = Screen.screen.getPixelDensity(-40);
            spawnPosition.y = SPAWN_HEIGHT;

            BalloonCat newCat = BalloonCat.pool.get(position);
            newCat.getBody().setPosition(spawnPosition);
            World.world.spawnObject(newCat);

            Vec.pool.recycle(spawnPosition);
            position++;
        }
        else
        {
            position = 0;
            spawnCat();
        }
    }
}
