package game.android.com.catstacks.game.objects.statics.backgrounds;

import java.util.ArrayList;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.rendering.Animation;

public class Seaweed extends BackgroundScenery
{
    private final static int SWAY_FRAME_1 = 0;
    private final static int SWAY_FRAME_2 = 1;

    private final static String SWAYING_ANIM = "sway";

    public Seaweed(Vec position)
    {
        addSpriteToPosition(R.drawable.seaweed, 2, 1, -6, 0, position);

        activeFrames.add(textures.get(0).getFrame(SWAY_FRAME_1));

        initAnimations();
        getAnimation(SWAYING_ANIM).activate();
    }

    public Seaweed(Vec position, boolean isShort)
    {
        if(isShort)
        {
            addSpriteToPosition(R.drawable.seaweed_short, 2, 1, -6, 0, position);
        }
        else
        {
            addSpriteToPosition(R.drawable.seaweed, 2, 1, -6, 0, position);
        }

        activeFrames.add(textures.get(0).getFrame(SWAY_FRAME_1));

        initAnimations();
        getAnimation(SWAYING_ANIM).activate();
    }

    private void initAnimations()
    {
        animations = ArrayPool.animationPool.get();

        ArrayList<float[]> frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(SWAY_FRAME_1));
        frames.add(textures.get(0).getFrame(SWAY_FRAME_2));
        Animation swaying = Animation.pool.get();
        swaying.init(SWAYING_ANIM, 0, frames, 20);

        animations.add(swaying);
    }
}
