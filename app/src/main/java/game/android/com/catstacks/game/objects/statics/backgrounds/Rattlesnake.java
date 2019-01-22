package game.android.com.catstacks.game.objects.statics.backgrounds;

import java.util.ArrayList;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.core.Time;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.rendering.Animation;

public class Rattlesnake extends BackgroundScenery
{
    private boolean rattling, sniffing;
    private float rattleTimer, sniffTimer;

    private final static int RATTLE_FRAME_1 = 0;
    private final static int RATTLE_FRAME_2 = 1;
    private final static int RATTLE_FRAME_3 = 2;

    private final static int SNIFF_FRAME_1 = 3;
    private final static int SNIFF_FRAME_2 = 4;

    private final static String RATTLING_ANIM = "rattling";
    private final static String SNIFFING_ANIM = "sniffing";

    public Rattlesnake(Vec position)
    {
        addSpriteToPosition(R.drawable.rattlesnake2, 1, 5, -4, 0, position);

        activeFrames.add(textures.get(0).getFrame(RATTLE_FRAME_1));
        defaultFrames.add(RATTLE_FRAME_1);

        initAnimations();

        rattling = false;
        sniffing = false;

        rattleTimer = 50;
        sniffTimer = 100;
    }

    private void initAnimations()
    {
        animations = ArrayPool.animationPool.get();

        ArrayList<float[]> frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(RATTLE_FRAME_1));
        frames.add(textures.get(0).getFrame(RATTLE_FRAME_2));
        frames.add(textures.get(0).getFrame(RATTLE_FRAME_3));
        frames.add(textures.get(0).getFrame(RATTLE_FRAME_2));
        frames.add(textures.get(0).getFrame(RATTLE_FRAME_1));
        frames.add(textures.get(0).getFrame(RATTLE_FRAME_2));
        frames.add(textures.get(0).getFrame(RATTLE_FRAME_3));
        frames.add(textures.get(0).getFrame(RATTLE_FRAME_2));
        frames.add(textures.get(0).getFrame(RATTLE_FRAME_1));
        frames.add(textures.get(0).getFrame(RATTLE_FRAME_2));
        frames.add(textures.get(0).getFrame(RATTLE_FRAME_3));
        frames.add(textures.get(0).getFrame(RATTLE_FRAME_2));
        Animation rattling = Animation.pool.get();
        rattling.init(RATTLING_ANIM, 0, frames, 2);
        rattling.setLooping(false);
        animations.add(rattling);

        frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(SNIFF_FRAME_1));
        frames.add(textures.get(0).getFrame(SNIFF_FRAME_2));
        Animation sniffing = Animation.pool.get();
        sniffing.init(SNIFFING_ANIM, 0, frames, 3);
        sniffing.setLooping(false);
        animations.add(sniffing);
    }

    @Override
    public void update()
    {
        super.update();

        if(rattleTimer <= 0 && !rattling && !sniffing)
        {
            getAnimation(RATTLING_ANIM).activate();
            rattling = true;
            rattleTimer = 50;
        } else
        {
            rattleTimer -= Time.getDelta();
        }

        if(sniffTimer <= 0 && !rattling && !sniffing)
        {
            getAnimation(SNIFFING_ANIM).activate();
            sniffing = true;
            sniffTimer = 100;
        } else
        {
            sniffTimer -= Time.getDelta();
        }

        if(rattling && !getAnimation(RATTLING_ANIM).isActive())
        {
            rattling = false;
        }

        if(sniffing && !getAnimation(SNIFFING_ANIM).isActive())
        {
            sniffing = false;
        }
    }
}
