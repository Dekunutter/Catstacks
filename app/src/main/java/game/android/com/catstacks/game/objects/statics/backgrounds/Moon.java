package game.android.com.catstacks.game.objects.statics.backgrounds;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;

public class Moon extends BackgroundScenery
{
    public Moon(Vec position)
    {
        addSpriteToPosition(R.drawable.crescent_moon, -9f, 0, position);

        activeFrames.add(null);
    }
}
