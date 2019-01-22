package game.android.com.catstacks.game.objects.statics.backgrounds;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;

public class RedSun extends BackgroundScenery
{
    public RedSun(Vec position)
    {
        addSpriteToPosition(R.drawable.red_sun, -9.5f, 0, position);

        activeFrames.add(null);
    }
}
