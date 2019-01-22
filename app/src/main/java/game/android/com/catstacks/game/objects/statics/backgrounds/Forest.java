package game.android.com.catstacks.game.objects.statics.backgrounds;

import java.util.ArrayList;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.rendering.Animation;

public class Forest extends BackgroundScenery
{
    public Forest(Vec position)
    {
        addSpriteToPosition(R.drawable.forest, -6, 0, position);

        activeFrames.add(null);

        //addSpriteToPosition(R.drawable.forest2, 1, 2, -6, 0, position);

        //activeFrames.add(textures.get(0).getFrame(0));

        //initAnimations();
        //getAnimation("leafage").activate();
    }

    private void initAnimations()
    {
        animations = ArrayPool.animationPool.get();

        ArrayList<float[]> frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(0));
        frames.add(textures.get(0).getFrame(1));
        Animation walking = Animation.pool.get();
        walking.init("leafage", 0, frames, 20);

        animations.add(walking);
    }
}
