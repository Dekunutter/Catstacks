package game.android.com.catstacks.game.objects.statics.backgrounds;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.core.Time;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.rendering.Animation;

public class ShootingStar extends BackgroundScenery
{
    private float shootTimer;
    private boolean invisible;

    private final static int SHOOTING_FRAME_1 = 0;
    private final static int SHOOTING_FRAME_2 = 1;
    private final static int SHOOTING_FRAME_3 = 2;
    private final static int SHOOTING_FRAME_4 = 3;
    private final static int SHOOTING_FRAME_5 = 4;
    private final static int SHOOTING_FRAME_6 = 5;
    private final static int SHOOTING_FRAME_7 = 6;
    private final static int SHOOTING_FRAME_8 = 7;
    private final static int SHOOTING_FRAME_9 = 8;

    private final static String SHOOTING_ANIM = "shooting";

    public ShootingStar(Vec position)
    {
        addSpriteToPosition(R.drawable.shooting_star3, 1, 9, -9f, 0, position);

        activeFrames.add(textures.get(0).getFrame(SHOOTING_FRAME_1));

        shootTimer = 200;

        initAnimations();
        getAnimation(SHOOTING_ANIM).activate();

        unmovable = true;

        solid = false;

        invisible = false;
    }

    private void initAnimations()
    {
        animations = ArrayPool.animationPool.get();

        ArrayList<float[]> frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(SHOOTING_FRAME_1));
        frames.add(textures.get(0).getFrame(SHOOTING_FRAME_2));
        frames.add(textures.get(0).getFrame(SHOOTING_FRAME_3));
        frames.add(textures.get(0).getFrame(SHOOTING_FRAME_4));
        frames.add(textures.get(0).getFrame(SHOOTING_FRAME_5));
        frames.add(textures.get(0).getFrame(SHOOTING_FRAME_6));
        frames.add(textures.get(0).getFrame(SHOOTING_FRAME_7));
        frames.add(textures.get(0).getFrame(SHOOTING_FRAME_8));
        frames.add(textures.get(0).getFrame(SHOOTING_FRAME_9));
        float[] intervals = ArrayPool.floatMaster.get(frames.size());
        intervals[0] = 0.75f;
        intervals[1] = 0.75f;
        intervals[2] = 0.75f;
        intervals[3] = 0.75f;
        intervals[4] = 0.75f;
        intervals[5] = 0.75f;
        intervals[6] = 0.75f;
        intervals[7] = 1.5f;
        intervals[8] = 1.5f;
        Animation shooting = Animation.pool.get();
        shooting.init(SHOOTING_ANIM, 0, frames, intervals);
        shooting.setLooping(false);

        animations.add(shooting);
    }

    @Override
    public void update()
    {
        super.update();

        if(shootTimer <= 0)
        {
            shootTimer = 200;

            if(getAnimation(SHOOTING_ANIM) != null && invisible)
            {
                changeColourAlpha(0, alpha);

                getAnimation(SHOOTING_ANIM).activate();
                invisible = false;
            }
        }
        else
        {
            shootTimer -= Time.getDelta();
        }

        if(!getAnimation(SHOOTING_ANIM).isActive() && !invisible)
        {
            changeColourAlpha(0, 0);

            if(fadedIn)
            {
                invisible = true;
            }
        }
    }

    public void destruct()
    {
        super.destruct();
    }
}
