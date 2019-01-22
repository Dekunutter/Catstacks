package game.android.com.catstacks.game.objects.statics.backgrounds;

import java.util.ArrayList;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.rendering.Animation;

public class GoldenCat extends BackgroundScenery
{
    private final static int HAND_FRAME_1 = 0;
    private final static int HAND_FRAME_2 = 1;
    private final static int HAND_FRAME_3 = 2;

    private final static String HAND_ANIM = "hand";

    public GoldenCat(Vec position)
    {
        addSpriteToPosition(R.drawable.golden_cat_statue1, 3, 1, -8f, 0, position);

        activeFrames.add(textures.get(0).getFrame(HAND_FRAME_1));

        initAnimations();
        getAnimation(HAND_ANIM).activate();
    }

    private void initAnimations()
    {
        animations = ArrayPool.animationPool.get();

        ArrayList<float[]> frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(HAND_FRAME_1));
        frames.add(textures.get(0).getFrame(HAND_FRAME_2));
        frames.add(textures.get(0).getFrame(HAND_FRAME_3));
        frames.add(textures.get(0).getFrame(HAND_FRAME_2));
        Animation hand = Animation.pool.get();
        hand.init(HAND_ANIM, 0, frames, 35);

        animations.add(hand);
    }
}
