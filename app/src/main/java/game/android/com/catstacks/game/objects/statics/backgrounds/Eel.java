package game.android.com.catstacks.game.objects.statics.backgrounds;

import java.util.ArrayList;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.core.Time;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.rendering.Animation;

public class Eel extends BackgroundScenery
{
    private boolean emerging;
    private float emergeTimer;

    private final static int DEFAULT_FRAME = 0;
    private final static int EMERGE_FRAME_1 = 1;
    private final static int EMERGE_FRAME_2 = 2;
    private final static int EMERGE_FRAME_3 = 3;
    private final static int EMERGE_FRAME_4 = 4;

    private final static String EMERGING_ANIM = "emerge";

    public Eel(Vec position)
    {
        addSpriteToPosition(R.drawable.eel, 1, 5, -4, 0, position);

        activeFrames.add(textures.get(0).getFrame(DEFAULT_FRAME));
        defaultFrames.add(DEFAULT_FRAME);

        initAnimations();

        emerging = false;
        emergeTimer = 300;
    }

    private void initAnimations()
    {
        animations = ArrayPool.animationPool.get();

        ArrayList<float[]> frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(EMERGE_FRAME_1));
        frames.add(textures.get(0).getFrame(EMERGE_FRAME_2));
        frames.add(textures.get(0).getFrame(EMERGE_FRAME_3));
        frames.add(textures.get(0).getFrame(EMERGE_FRAME_4));
        frames.add(textures.get(0).getFrame(EMERGE_FRAME_4));
        frames.add(textures.get(0).getFrame(EMERGE_FRAME_3));
        frames.add(textures.get(0).getFrame(EMERGE_FRAME_2));
        frames.add(textures.get(0).getFrame(EMERGE_FRAME_1));
        Animation emerging = Animation.pool.get();
        emerging.init(EMERGING_ANIM, 0, frames, 15);
        emerging.setLooping(false);
        animations.add(emerging);
    }

    @Override
    public void update()
    {
        super.update();

        if(emergeTimer <= 0 && !emerging)
        {
            getAnimation(EMERGING_ANIM).activate();
            emerging = true;
            emergeTimer = 300;
        } else
        {
            emergeTimer -= Time.getDelta();
        }

        if(emerging && !getAnimation(EMERGING_ANIM).isActive())
        {
            emerging = false;
        }
    }
}
