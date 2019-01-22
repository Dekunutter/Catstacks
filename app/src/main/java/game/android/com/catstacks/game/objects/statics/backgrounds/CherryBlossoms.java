package game.android.com.catstacks.game.objects.statics.backgrounds;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;

public class CherryBlossoms extends BackgroundScenery
{
    public CherryBlossoms(Vec position)
    {
        addSpriteToPosition(R.drawable.cherry_blossoms, -6, 0, position);

        activeFrames.add(null);
    }
}
