package game.android.com.catstacks.game.objects.statics.backgrounds;

import java.util.ArrayList;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.core.Screen;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.pool.ArrayObjectPool;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.pool.GenericObjectPool;
import game.android.com.catstacks.engine.rendering.Animation;

public class MenuBackground extends BackgroundScenery
{
    private static final int FRAME_1 = 0;
    private static final int FRAME_2 = 1;
    private static final int FRAME_3 = 2;
    private static final int FRAME_4 = 3;
    private static final int FRAME_5 = 4;
    private static final int FRAME_6 = 5;
    private static final int FRAME_7 = 6;

    private static final String RAINBOW_ANIM = "rainbow";

    public static final GenericObjectPool<MenuBackground> pool = new GenericObjectPool<MenuBackground>(MenuBackground.class);

    public MenuBackground()
    {
        addStretchedSprite(R.drawable.background_menu, 7, 1, -10f, 1, Screen.screen.getWidth(), Screen.screen.getHeight());

        activeFrames.add(textures.get(0).getFrame(FRAME_1));

        initAnimations();
        getAnimation(RAINBOW_ANIM).activate();

        hasAlpha = false;
    }

    private void initAnimations()
    {
        animations =  ArrayPool.animationPool.get();

        ArrayList<float[]> frames =  ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(FRAME_1));
        frames.add(textures.get(0).getFrame(FRAME_2));
        frames.add(textures.get(0).getFrame(FRAME_3));
        frames.add(textures.get(0).getFrame(FRAME_4));
        frames.add(textures.get(0).getFrame(FRAME_5));
        frames.add(textures.get(0).getFrame(FRAME_6));
        frames.add(textures.get(0).getFrame(FRAME_7));
        Animation rainbow = Animation.pool.get();
        rainbow.init(RAINBOW_ANIM, 0, frames, 20);

        animations.add(rainbow);
    }

    public void destruct()
    {
        super.destruct();

        MenuBackground.pool.recycle(this);
    }
}
