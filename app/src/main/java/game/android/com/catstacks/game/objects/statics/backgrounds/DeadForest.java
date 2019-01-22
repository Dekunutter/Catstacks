package game.android.com.catstacks.game.objects.statics.backgrounds;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;

public class DeadForest extends BackgroundScenery
{
    public DeadForest(Vec position)
    {
        addSpriteToPosition(R.drawable.dead_forest, -6.1f, 0, position);

        activeFrames.add(null);
    }
}
