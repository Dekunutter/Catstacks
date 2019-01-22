package game.android.com.catstacks.game.objects.statics.backgrounds;

import java.util.ArrayList;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.rendering.Animation;

public class PaperLanturn extends BackgroundScenery
{
    private final static int LIGHT_FRAME_1 = 0;
    private final static int LIGHT_FRAME_2 = 1;
    private final static int LIGHT_FRAME_3 = 2;

    private final static String LIGHTING_ANIM = "lighting";

    public PaperLanturn(Vec position)
    {
        addSpriteToPosition(R.drawable.paper_lanturn, 3, 1, -5f, 0, position);

        activeFrames.add(textures.get(0).getFrame(LIGHT_FRAME_1));

        initAnimations();
        getAnimation(LIGHTING_ANIM).activate();
    }

    private void initAnimations()
    {
        animations = ArrayPool.animationPool.get();

        ArrayList<float[]> frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(LIGHT_FRAME_1));
        frames.add(textures.get(0).getFrame(LIGHT_FRAME_2));
        frames.add(textures.get(0).getFrame(LIGHT_FRAME_3));
        frames.add(textures.get(0).getFrame(LIGHT_FRAME_2));
        Animation lighting = Animation.pool.get();
        lighting.init(LIGHTING_ANIM, 0, frames, 10);

        animations.add(lighting);
    }
}
