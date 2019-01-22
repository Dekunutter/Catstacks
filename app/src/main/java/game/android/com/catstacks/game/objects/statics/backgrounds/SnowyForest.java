package game.android.com.catstacks.game.objects.statics.backgrounds;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;

public class SnowyForest extends BackgroundScenery
{
    public SnowyForest(Vec position)
    {
        addSpriteToPosition(R.drawable.snowy_forest, -6f, 0, position);

        activeFrames.add(null);
    }
}
