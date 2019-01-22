package game.android.com.catstacks.game.objects.statics.backgrounds;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;

public class Umbrella extends BackgroundScenery
{
    public Umbrella(Vec position)
    {
        addSpriteToPosition(R.drawable.umbrella, -4f, 0, position);

        activeFrames.add(null);
    }
}
