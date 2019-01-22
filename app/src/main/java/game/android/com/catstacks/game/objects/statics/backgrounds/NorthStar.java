package game.android.com.catstacks.game.objects.statics.backgrounds;

import java.util.ArrayList;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.rendering.Animation;

public class NorthStar extends BackgroundScenery
{
    private final static int DEFAULT_FRAME = 0;
    private final static int SHINE_FRAME_1 = 1;
    private final static int SHINE_FRAME_2 = 2;
    private final static int SHINE_FRAME_3 = 3;
    private final static int SHINE_FRAME_4 = 4;

    private final static String SHINING_ANIM = "shining";

    public NorthStar(Vec position)
    {
        addSpriteToPosition(R.drawable.north_star2, 3, 2, -9f, 0, position);

        activeFrames.add(textures.get(0).getFrame(DEFAULT_FRAME));

        initAnimations();
        getAnimation(SHINING_ANIM).activate();
    }

    private void initAnimations()
    {
        animations = ArrayPool.animationPool.get();

        ArrayList<float[]> frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(DEFAULT_FRAME));
        frames.add(textures.get(0).getFrame(SHINE_FRAME_1));
        frames.add(textures.get(0).getFrame(SHINE_FRAME_2));
        frames.add(textures.get(0).getFrame(SHINE_FRAME_3));
        frames.add(textures.get(0).getFrame(SHINE_FRAME_4));
        Animation walking = Animation.pool.get();
        walking.init(SHINING_ANIM, 0, frames, 3);

        animations.add(walking);
    }
}
