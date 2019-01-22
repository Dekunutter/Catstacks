package game.android.com.catstacks.game.objects.statics.backgrounds;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;

public class Pyramid extends BackgroundScenery
{
    public Pyramid(Vec position)
    {
        addSpriteToPosition(R.drawable.pyramid, -8f, 0, position);

        activeFrames.add(null);

        unmovable = true;
        solid = false;
    }
}
