package game.android.com.catstacks.game.objects.statics.backgrounds;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;

public class Pagoda extends BackgroundScenery
{
    public Pagoda(Vec position)
    {
        addSpriteToPosition(R.drawable.pagoda, -6.5f, 0, position);

        activeFrames.add(null);
    }
}
