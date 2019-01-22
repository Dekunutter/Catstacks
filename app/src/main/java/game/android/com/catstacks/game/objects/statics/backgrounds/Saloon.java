package game.android.com.catstacks.game.objects.statics.backgrounds;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;

public class Saloon extends BackgroundScenery
{
    public Saloon(Vec position)
    {
        addSpriteToPosition(R.drawable.saloon, -4, 0, position);

        activeFrames.add(null);
    }
}
