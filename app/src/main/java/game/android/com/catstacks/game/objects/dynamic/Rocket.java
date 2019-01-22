package game.android.com.catstacks.game.objects.dynamic;

import java.util.ArrayList;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.core.RenderedState;
import game.android.com.catstacks.engine.core.Screen;
import game.android.com.catstacks.engine.core.Time;
import game.android.com.catstacks.engine.objects.WorldObject;
import game.android.com.catstacks.engine.physics.CollisionBitmasks;
import game.android.com.catstacks.engine.physics.Polygon;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.rendering.Animation;
import game.android.com.catstacks.engine.rendering.Texture;
import game.android.com.catstacks.engine.rendering.TextureLoader;
import game.android.com.catstacks.engine.sound.Sound;
import game.android.com.catstacks.engine.states.World;

public class Rocket extends WorldObject
{
    private float currentLaunch, launchTimer, liftOffTimer, launchFrame;
    private boolean launched;

    private Sound startupSound, liftoffSound;

    private static final int EMPTY_FRAME = 0;
    private static final int EXPAND_FRAME_1 = 1;
    private static final int EXPAND_FRAME_2 = 2;

    private static final String EXPANDING_ANIM = "expanding";
    private static final String LIFTOFF_ANIM = "liftoff";

    public Rocket(Vec position)
    {
        super();

        float[] texCoordsX = ArrayPool.floatMaster.get(3);
        texCoordsX[0] = 22;
        texCoordsX[1] = 46;
        texCoordsX[2] = 73;
        float[] texCoordsY = ArrayPool.floatMaster.get(1);
        texCoordsY[0] = 44;
        addSprite(R.drawable.rocket3, texCoordsX, texCoordsY, 0, 1);

        ArrayPool.floatMaster.recycle(texCoordsY, texCoordsY.length);
        ArrayPool.floatMaster.recycle(texCoordsX, texCoordsX.length);

        activeFrames.add(textures.get(0).getFrame(EMPTY_FRAME));
        defaultFrames.add(0);

        //addSprite(R.drawable.flame, 1, 1, 0, 0);
        Texture flame = TextureLoader.loadTexture(RenderedState.getContext(), R.drawable.flame, 2, 1);
        float[] flameVertices = ArrayPool.floatMaster.get(12);
        flameVertices[0] = 0;
        flameVertices[1] = 0;
        flameVertices[2] = 0;
        flameVertices[3] = 0;
        flameVertices[4] = Screen.screen.getPixelDensity(-flame.getHeight() * PIXEL_SCALE);
        flameVertices[5] = 0;
        flameVertices[6] = width;
        flameVertices[7] = Screen.screen.getPixelDensity(-flame.getHeight() * PIXEL_SCALE);
        flameVertices[8] = 0;
        flameVertices[9] = width;
        flameVertices[10] = 0;
        flameVertices[11] = 0;
        sprVertices.add(flameVertices);
        float[] colours = ArrayPool.floatMaster.get(16);
        colours[0] = 1;
        colours[1] = 1;
        colours[2] = 1;
        colours[3] = 0;
        colours[4] = 1;
        colours[5] = 1;
        colours[6] = 1;
        colours[7] = 0;
        colours[8] = 1;
        colours[9] = 1;
        colours[10] = 1;
        colours[11] = 0;
        colours[12] = 1;
        colours[13] = 1;
        colours[14] = 1;
        colours[15] = 0;
        sprColours.add(colours);
        textures.add(flame);

        defaultFrames.add(0);
        activeFrames.add(textures.get(1).getFrame(0));

        //float[] vertices = {0, 0, 0, 0, height, 0, width, height, 0, width, 0, 0};
        float[] vertices = ArrayPool.floatMaster.get(9);
        vertices[0] = 0;
        vertices[1] = 0;
        vertices[2] = 0;
        vertices[3] = width / 2;
        vertices[4] = height;
        vertices[5] = 0;
        vertices[6] = width;
        vertices[7] = 0;
        vertices[8] = 0;
        body = new Polygon(position.x, position.y, 0, vertices);
        body.move(position.x, position.y, 0);
        body.setMass(0);

        ArrayPool.floatMaster.recycle(vertices, vertices.length);

        mask = CollisionBitmasks.ROCKET_ID;

        unmovable = true;

        launchTimer = 4;
        currentLaunch = 4;
        launchFrame = 0;
        liftOffTimer = 100;
        launched = false;

        initAnimations();

        initSounds();
    }

    public void expand()
    {
        getAnimation(EXPANDING_ANIM).activate();
    }

    public void initAnimations()
    {
        animations = ArrayPool.animationPool.get();

        ArrayList<float[]> frames =  ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(EMPTY_FRAME));
        frames.add(textures.get(0).getFrame(EXPAND_FRAME_1));
        frames.add(textures.get(0).getFrame(EXPAND_FRAME_2));
        frames.add(textures.get(0).getFrame(EXPAND_FRAME_1));
        ArrayList<float[]> animationVertices = ArrayPool.floatArrayPool.get();
        float[] animVertices1 = ArrayPool.floatMaster.get(12);
        animVertices1[0] = 0;
        animVertices1[1] = height;
        animVertices1[2] = 0;
        animVertices1[3] = 0;
        animVertices1[4] = 0;
        animVertices1[5] = 0;
        animVertices1[6] = width;
        animVertices1[7] = 0;
        animVertices1[8] = 0;
        animVertices1[9] = width;
        animVertices1[10] = height;
        animVertices1[11] = 0;
        animationVertices.add(animVertices1);
        float[] animVertices2 = ArrayPool.floatMaster.get(12);
        animVertices2[0] = 0;
        animVertices2[1] = height;
        animVertices2[2] = 0;
        animVertices2[3] = 0;
        animVertices2[4] = 0;
        animVertices2[5] = 0;
        animVertices2[6] = Screen.screen.getPixelDensity(24 * 2);
        animVertices2[7] = 0;
        animVertices2[8] = 0;
        animVertices2[9] = Screen.screen.getPixelDensity(24 * 2);
        animVertices2[10] = height;
        animVertices2[11] = 0;
        animationVertices.add(animVertices2);
        float[] animVertices3 = ArrayPool.floatMaster.get(12);
        animVertices3[0] = 0;
        animVertices3[1] = height;
        animVertices3[2] = 0;
        animVertices3[3] = 0;
        animVertices3[4] = 0;
        animVertices3[5] = 0;
        animVertices3[6] = Screen.screen.getPixelDensity(27 * 2);
        animVertices3[7] = 0;
        animVertices3[8] = 0;
        animVertices3[9] = Screen.screen.getPixelDensity(27 * 2);
        animVertices3[10] = height;
        animVertices3[11] = 0;
        animationVertices.add(animVertices3);
        float[] animVertices4 = ArrayPool.floatMaster.get(12);
        animVertices4[0] = 0;
        animVertices4[1] = height;
        animVertices4[2] = 0;
        animVertices4[3] = 0;
        animVertices4[4] = 0;
        animVertices4[5] = 0;
        animVertices4[6] = Screen.screen.getPixelDensity(24 * 2);
        animVertices4[7] = 0;
        animVertices4[8] = 0;
        animVertices4[9] = Screen.screen.getPixelDensity(24 * 2);
        animVertices4[10] = height;
        animVertices4[11] = 0;
        animationVertices.add(animVertices4);
        Animation expanding = Animation.pool.get();
        expanding.init(EXPANDING_ANIM, 0, frames, 3, animationVertices);
        expanding.setLooping(false);

        animations.add(expanding);

        ArrayList<float[]> flameFrames = ArrayPool.floatArrayPool.get();
        flameFrames.add(textures.get(1).getFrame(0));
        flameFrames.add(textures.get(1).getFrame(1));
        Animation liftoffFlame = Animation.pool.get();
        liftoffFlame.init(LIFTOFF_ANIM, 1, flameFrames, 5);
        liftoffFlame.setLooping(false);

        animations.add(liftoffFlame);

        initSounds();
    }

    private void initSounds()
    {
        startupSound = Sound.pool.get();
        startupSound.init(R.raw.rocket_start, 0.25f);

        liftoffSound = Sound.pool.get();
        liftoffSound.init(R.raw.liftoff, 0.25f);
    }

    @Override
    public void update()
    {
        if(World.world.isGameOver())
        {
            liftOffTimer -= Time.getDelta();
            if(liftOffTimer <= 0)
            {
                if(!launched)
                {
                    liftoffSound.playSound();
                }

                launched = true;
                changeVertices(0, 0, 0, width, height, 0);
                changeColourAlpha(1, 1);

                getAnimation(LIFTOFF_ANIM).activate();
            }

            if(!launched)
            {
                if(launchTimer <= 0)
                {
                    launchFrame++;
                    startupSound.playSound();

                    if(launchFrame == 1)
                    {
                        changeVertices(0, - 2, 0, width - 2, height, 0);
                    }
                    else if(launchFrame == 2)
                    {
                        changeVertices(0, 0, 0, width, height, 0);
                        launchFrame = 0;
                    }

                    launchTimer = currentLaunch;
                    currentLaunch -= Time.getDelta() / 4;
                }
                else
                {
                    launchTimer -= Time.getDelta();
                }
            }
            else
            {
                body.move(body.getX(), body.getY() + (18 * getMovementDelta()), rotation);
                body.setY(body.getY() + (18 * getMovementDelta()));
            }
        }
    }
}