package game.android.com.catstacks.game.objects.statics.backgrounds;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;

public class Bush extends BackgroundScenery
{
    public Bush(Vec position)
    {
        addSpriteToPosition(R.drawable.hills, -8f, 0, position);

        activeFrames.add(null);
    }
}
