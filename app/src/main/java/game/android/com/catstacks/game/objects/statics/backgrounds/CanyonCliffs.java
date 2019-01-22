package game.android.com.catstacks.game.objects.statics.backgrounds;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;

public class CanyonCliffs extends BackgroundScenery
{
    public CanyonCliffs(Vec position)
    {
        addSpriteToPosition(R.drawable.canyon_cliffs2, -7f, 0, position);

        activeFrames.add(null);
    }
}
