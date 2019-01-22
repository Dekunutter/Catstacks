package game.android.com.catstacks.game.objects.statics.backgrounds;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;

public class BasketballHoop extends BackgroundScenery
{
    public BasketballHoop(Vec position)
    {
        addSpriteToPosition(R.drawable.basketball_hoop, -4f, 0, position);

        activeFrames.add(null);
    }
}
