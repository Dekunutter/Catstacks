package game.android.com.catstacks.game.objects.statics.backgrounds;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;

public class HauntedCastle extends BackgroundScenery
{
    public HauntedCastle(Vec position)
    {
        addSpriteToPosition(R.drawable.haunted_castle, -6.5f, 0, position);

        activeFrames.add(null);
    }
}
