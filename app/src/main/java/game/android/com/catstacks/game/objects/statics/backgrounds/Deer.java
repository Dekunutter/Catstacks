package game.android.com.catstacks.game.objects.statics.backgrounds;

import java.util.ArrayList;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.core.Time;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.rendering.Animation;

public class Deer extends BackgroundScenery
{
    private boolean watching, readyToSettle, settling, readyToChew;
    private float watchTimer, settleTimer;

    private final static int CHEW_FRAME_1 = 0;
    private final static int CHEW_FRAME_2 = 1;
    private final static int WATCH_FRAME_1 = 2;
    private final static int WATCH_FRAME_2 = 3;
    private final static int WATCH_FRAME_3 = 4;
    private final static int WATCH_FRAME_4 = 5;
    private final static int WATCH_FRAME_5 = 6;

    private final static String CHEWING_ANIM = "chew";
    private final static String WATCH_ANIM = "watch";
    private final static String SETTLE_ANIM = "settle";

    public Deer(Vec position)
    {
        addSpriteToPosition(R.drawable.deer, 1, 7, -4, 0, position);

        activeFrames.add(textures.get(0).getFrame(CHEW_FRAME_1));
        defaultFrames.add(CHEW_FRAME_1);

        initAnimations();
        getAnimation(CHEWING_ANIM).activate();

        watching = false;
        watchTimer = 500;
        readyToSettle = false;
        settling = false;
        settleTimer = 40;
        readyToChew = true;
    }

    private void initAnimations()
    {
        animations = ArrayPool.animationPool.get();

        ArrayList<float[]> frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(CHEW_FRAME_1));
        frames.add(textures.get(0).getFrame(CHEW_FRAME_2));
        Animation chewing = Animation.pool.get();
        chewing.init(CHEWING_ANIM, 0, frames, 5);
        animations.add(chewing);

        frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(WATCH_FRAME_1));
        frames.add(textures.get(0).getFrame(WATCH_FRAME_2));
        frames.add(textures.get(0).getFrame(WATCH_FRAME_3));
        frames.add(textures.get(0).getFrame(WATCH_FRAME_4));
        frames.add(textures.get(0).getFrame(WATCH_FRAME_5));
        Animation watching = Animation.pool.get();
        watching.init(WATCH_ANIM, 0, frames, 4);
        watching.setLooping(false);
        animations.add(watching);

        frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(WATCH_FRAME_5));
        frames.add(textures.get(0).getFrame(WATCH_FRAME_4));
        frames.add(textures.get(0).getFrame(WATCH_FRAME_3));
        frames.add(textures.get(0).getFrame(WATCH_FRAME_2));
        frames.add(textures.get(0).getFrame(WATCH_FRAME_1));
        Animation settle = Animation.pool.get();
        settle.init(SETTLE_ANIM, 0, frames, 4);
        settle.setLooping(false);
        animations.add(settle);
    }

    @Override
    public void update()
    {
        super.update();

        if(watchTimer <= 0 && !watching)
        {
            getAnimation(CHEWING_ANIM).deactivate();
            getAnimation(WATCH_ANIM).activate();
            watching = true;
            readyToChew = false;
            watchTimer = 500;
        }
        else
        {
            watchTimer -= Time.getDelta();
        }

        if(watching && !getAnimation(WATCH_ANIM).isActive())
        {
            watching = false;
            readyToSettle = true;

            activeFrames.set(0, textures.get(0).getFrame(WATCH_FRAME_5));
            defaultFrames.set(0, WATCH_FRAME_5);
        }

        if(readyToSettle)
        {
            if(settleTimer <= 0)
            {
                getAnimation(SETTLE_ANIM).activate();
                settling = true;
                readyToSettle = false;
                settleTimer = 40;
            }
            else
            {
                settleTimer -= Time.getDelta();
            }
        }

        if(settling && !getAnimation(SETTLE_ANIM).isActive())
        {
            settling = false;
            readyToChew = true;
        }

        if(readyToChew && !getAnimation(CHEWING_ANIM).isActive())
        {
            activeFrames.set(0, textures.get(0).getFrame(CHEW_FRAME_1));
            defaultFrames.set(0, CHEW_FRAME_1);

            getAnimation(CHEWING_ANIM).activate();
        }
    }
}
