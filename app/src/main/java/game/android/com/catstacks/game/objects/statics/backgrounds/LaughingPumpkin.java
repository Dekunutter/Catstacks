package game.android.com.catstacks.game.objects.statics.backgrounds;

import java.util.ArrayList;
import java.util.Random;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.core.Time;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.rendering.Animation;

public class LaughingPumpkin extends BackgroundScenery
{
    private float laughTimer;

    private final static int DEFAULT_FRAME = 0;
    private final static int LAUGH_FRAME = 1;

    private final static String LAUGHING_ANIM = "laugh";

    public LaughingPumpkin(Vec position)
    {
        addSpriteToPosition(R.drawable.laughing_pumpkin, 2, 1, -6f, 0, position);

        activeFrames.add(textures.get(0).getFrame(DEFAULT_FRAME));

        initAnimations();
        getAnimation(LAUGHING_ANIM).activate();

        Random r = ArrayPool.randomPool.get();
        laughTimer = r.nextInt(350 - 250 + 1) + 250;
        ArrayPool.randomPool.recycle(r);
    }

    @Override
    public void update()
    {
        super.update();

        if(laughTimer <= 0)
        {
            Random r = ArrayPool.randomPool.get();
            laughTimer = r.nextInt(350 - 250 + 1) + 250;
            ArrayPool.randomPool.recycle(r);

            if(getAnimation(LAUGHING_ANIM) != null)
            {
                getAnimation(LAUGHING_ANIM).activate();
            }
        }
        else
        {
            laughTimer -= Time.getDelta();
        }
    }

    private void initAnimations()
    {
        animations = ArrayPool.animationPool.get();

        ArrayList<float[]> frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(DEFAULT_FRAME));
        frames.add(textures.get(0).getFrame(LAUGH_FRAME));
        frames.add(textures.get(0).getFrame(DEFAULT_FRAME));
        frames.add(textures.get(0).getFrame(LAUGH_FRAME));
        frames.add(textures.get(0).getFrame(DEFAULT_FRAME));
        frames.add(textures.get(0).getFrame(LAUGH_FRAME));
        frames.add(textures.get(0).getFrame(DEFAULT_FRAME));
        frames.add(textures.get(0).getFrame(LAUGH_FRAME));
        Animation laughing = Animation.pool.get();
        laughing.init(LAUGHING_ANIM, 0, frames, 7);
        laughing.setLooping(false);

        animations.add(laughing);
    }
}