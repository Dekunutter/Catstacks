package game.android.com.catstacks.game.objects.statics.backgrounds;

import java.util.ArrayList;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.rendering.Animation;

public class StreetLamps extends BackgroundScenery
{
    private final static int SHINE_FRAME_1 = 0;
    private final static int SHINE_FRAME_2 = 1;
    private final static int SHINE_FRAME_3 = 2;

    private final static String SHINING_ANIM = "shining";

    public StreetLamps(Vec position)
    {
        addSpriteToPosition(R.drawable.street_lamps, 3, 1, -4f, 0, position);

        activeFrames.add(textures.get(0).getFrame(SHINE_FRAME_1));

        initAnimations();
        getAnimation(SHINING_ANIM).activate();
    }

    private void initAnimations()
    {
        animations = ArrayPool.animationPool.get();

        ArrayList<float[]> frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(SHINE_FRAME_1));
        frames.add(textures.get(0).getFrame(SHINE_FRAME_2));
        frames.add(textures.get(0).getFrame(SHINE_FRAME_3));
        frames.add(textures.get(0).getFrame(SHINE_FRAME_2));
        Animation shining = Animation.pool.get();
        shining.init(SHINING_ANIM, 0, frames, 10);

        animations.add(shining);
    }
}
