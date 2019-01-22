package game.android.com.catstacks.game.objects.statics.backgrounds;

import java.util.ArrayList;
import java.util.Random;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.core.Time;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.rendering.Animation;

public class SpookyForest extends BackgroundScenery
{
    private float blinkTimer;

    private final static int BLINK_FRAME_1 = 0;
    private final static int BLINK_FRAME_2 = 1;

    private final static String BLINK_ANIM = "blink";

    public SpookyForest(Vec position)
    {
        addSpriteToPosition(R.drawable.spooky_forest, 1, 2, -6f, 0, position);

        activeFrames.add(textures.get(0).getFrame(BLINK_FRAME_1));

        initAnimations();
        getAnimation(BLINK_ANIM).activate();

        Random r = ArrayPool.randomPool.get();
        blinkTimer = r.nextInt(150 - 50 + 1) + 50;
        ArrayPool.randomPool.recycle(r);
    }

    @Override
    public void update()
    {
        super.update();

        if(blinkTimer <= 0)
        {
            Random r = ArrayPool.randomPool.get();
            blinkTimer = r.nextInt(150 - 50 + 1) + 50;
            ArrayPool.randomPool.recycle(r);

            if(getAnimation(BLINK_ANIM) != null)
            {
                getAnimation(BLINK_ANIM).activate();
            }
        }
        else
        {
            blinkTimer -= Time.getDelta();
        }
    }

    private void initAnimations()
    {
        animations = ArrayPool.animationPool.get();

        ArrayList<float[]> frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(BLINK_FRAME_1));
        frames.add(textures.get(0).getFrame(BLINK_FRAME_2));
        Animation blinking = Animation.pool.get();
        blinking.init(BLINK_ANIM, 0, frames, 20);
        blinking.setLooping(false);

        animations.add(blinking);
    }
}