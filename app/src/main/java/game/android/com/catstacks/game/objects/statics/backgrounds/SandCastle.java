package game.android.com.catstacks.game.objects.statics.backgrounds;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;

public class SandCastle extends BackgroundScenery
{
    public SandCastle(Vec position)
    {
        addSpriteToPosition(R.drawable.sand_castle, -4f, 0, position);

        activeFrames.add(null);
    }
}
