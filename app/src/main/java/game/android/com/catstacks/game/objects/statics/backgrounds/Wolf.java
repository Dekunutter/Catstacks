package game.android.com.catstacks.game.objects.statics.backgrounds;

import java.util.ArrayList;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.core.Time;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.rendering.Animation;

public class Wolf extends BackgroundScenery
{
    private boolean howling;
    private float howlTimer;

    private final static int HOWL_FRAME_1 = 0;
    private final static int HOWL_FRAME_2 = 1;
    private final static int HOWL_FRAME_3 = 2;

    private final static String HOWLING_ANIM = "howl";

    public Wolf(Vec position)
    {
        addSpriteToPosition(R.drawable.wolf, 1, 3, -4, 0, position);

        activeFrames.add(textures.get(0).getFrame(HOWL_FRAME_1));
        defaultFrames.add(HOWL_FRAME_1);

        initAnimations();

        howling = false;
        howlTimer = 500;
    }

    private void initAnimations()
    {
        animations = ArrayPool.animationPool.get();

        ArrayList<float[]> frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(HOWL_FRAME_1));
        frames.add(textures.get(0).getFrame(HOWL_FRAME_2));
        frames.add(textures.get(0).getFrame(HOWL_FRAME_3));
        frames.add(textures.get(0).getFrame(HOWL_FRAME_3));
        frames.add(textures.get(0).getFrame(HOWL_FRAME_3));
        frames.add(textures.get(0).getFrame(HOWL_FRAME_3));
        frames.add(textures.get(0).getFrame(HOWL_FRAME_3));
        frames.add(textures.get(0).getFrame(HOWL_FRAME_3));
        frames.add(textures.get(0).getFrame(HOWL_FRAME_3));
        frames.add(textures.get(0).getFrame(HOWL_FRAME_2));
        Animation howling = Animation.pool.get();
        howling.init(HOWLING_ANIM, 0, frames, 5);
        howling.setLooping(false);
        animations.add(howling);
    }

    @Override
    public void update()
    {
        super.update();

        if(howlTimer <= 0 && !howling)
        {
            getAnimation(HOWLING_ANIM).activate();
            howling = true;
            howlTimer = 500;
        } else
        {
            howlTimer -= Time.getDelta();
        }

        if(howling && !getAnimation(HOWLING_ANIM).isActive())
        {
            howling = false;
        }
    }
}
