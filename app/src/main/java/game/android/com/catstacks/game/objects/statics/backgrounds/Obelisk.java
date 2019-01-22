package game.android.com.catstacks.game.objects.statics.backgrounds;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;

public class Obelisk extends BackgroundScenery
{
    public Obelisk(Vec position)
    {
        addSpriteToPosition(R.drawable.obelisk, -6.5f, 0, position);

        activeFrames.add(null);
    }
}
