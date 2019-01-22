package game.android.com.catstacks.game.objects.statics.backgrounds;

import java.util.ArrayList;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.rendering.Animation;

public class CatGenie extends BackgroundScenery
{
    private final static int FLOAT_FRAME_1 = 0;
    private final static int FLOAT_FRAME_2 = 1;
    private final static int FLOAT_FRAME_3 = 2;

    private final static String FLOAT_ANIM = "float";

    public CatGenie(Vec position)
    {
        addSpriteToPosition(R.drawable.cat_genie, 3, 1, -4f, 0, position);

        activeFrames.add(textures.get(0).getFrame(FLOAT_FRAME_1));

        initAnimations();
        getAnimation(FLOAT_ANIM).activate();
    }

    private void initAnimations()
    {
        animations = ArrayPool.animationPool.get();

        ArrayList<float[]> frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(FLOAT_FRAME_1));
        frames.add(textures.get(0).getFrame(FLOAT_FRAME_2));
        frames.add(textures.get(0).getFrame(FLOAT_FRAME_3));
        frames.add(textures.get(0).getFrame(FLOAT_FRAME_2));
        Animation floating = Animation.pool.get();
        floating.init(FLOAT_ANIM, 0, frames, 10);

        animations.add(floating);
    }
}
