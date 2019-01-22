package game.android.com.catstacks.game.objects.statics.backgrounds;

import java.util.ArrayList;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.rendering.Animation;

public class GiantClock extends BackgroundScenery
{
    private final static int DEFAULT_FRAME = 0;
    private final static int PENDULUM_FRAME_1 = 1;
    private final static int PENDULUM_FRAME_2 = 2;

    private final static String PENDULUM_ANIM = "pendulum";

    public GiantClock(Vec position)
    {
        addSpriteToPosition(R.drawable.giant_clock, 3, 1, -7f, 0, position);

        activeFrames.add(textures.get(0).getFrame(DEFAULT_FRAME));

        initAnimations();
        getAnimation(PENDULUM_ANIM).activate();
    }

    private void initAnimations()
    {
        animations = ArrayPool.animationPool.get();

        ArrayList<float[]> frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(DEFAULT_FRAME));
        frames.add(textures.get(0).getFrame(PENDULUM_FRAME_1));
        frames.add(textures.get(0).getFrame(DEFAULT_FRAME));
        frames.add(textures.get(0).getFrame(PENDULUM_FRAME_2));
        Animation swinging = Animation.pool.get();
        swinging.init(PENDULUM_ANIM, 0, frames, 7);

        animations.add(swinging);
    }
}
