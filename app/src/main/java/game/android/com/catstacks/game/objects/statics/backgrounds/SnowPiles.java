package game.android.com.catstacks.game.objects.statics.backgrounds;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;

public class SnowPiles extends BackgroundScenery
{
    public SnowPiles(Vec position)
    {
        addSpriteToPosition(R.drawable.snow_piles, 0, 0, position);

        activeFrames.add(null);
    }
}
