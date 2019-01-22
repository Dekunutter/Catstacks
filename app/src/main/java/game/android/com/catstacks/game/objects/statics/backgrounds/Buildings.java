package game.android.com.catstacks.game.objects.statics.backgrounds;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.GenericObjectPool;

public class Buildings extends BackgroundScenery
{
    public static final GenericObjectPool<Buildings> pool = new GenericObjectPool<Buildings>(Buildings.class);

    public Buildings()
    {

    }

    public Buildings(Vec position)
    {
        addSpriteToPosition(R.drawable.buildings, -6f, 0, position);

        activeFrames.add(null);
    }

    public void init(Vec position)
    {
        addSpriteToPosition(R.drawable.buildings, -6f, 0, position);

        activeFrames.add(null);
    }
}
