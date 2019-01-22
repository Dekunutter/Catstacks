package game.android.com.catstacks.game.objects.statics.backgrounds;

import java.util.ArrayList;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.rendering.Animation;

public class Campfire extends BackgroundScenery
{
    private final static int BURN_FRAME_1 = 0;
    private final static int BURN_FRAME_2 = 1;
    private final static int BURN_FRAME_3 = 2;
    private final static int BURN_FRAME_4 = 3;

    private final static String BURN_ANIM = "burning";

    public Campfire(Vec position)
    {
        addSpriteToPosition(R.drawable.campfire, 4, 1, -4f, 0, position);

        activeFrames.add(textures.get(0).getFrame(BURN_FRAME_1));

        initAnimations();
        getAnimation(BURN_ANIM).activate();
    }

    private void initAnimations()
    {
        animations = ArrayPool.animationPool.get();

        ArrayList<float[]> frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(BURN_FRAME_1));
        frames.add(textures.get(0).getFrame(BURN_FRAME_2));
        frames.add(textures.get(0).getFrame(BURN_FRAME_3));
        frames.add(textures.get(0).getFrame(BURN_FRAME_4));
        Animation burning = Animation.pool.get();
        burning.init(BURN_ANIM, 0, frames, 2);
        animations.add(burning);
    }
}
