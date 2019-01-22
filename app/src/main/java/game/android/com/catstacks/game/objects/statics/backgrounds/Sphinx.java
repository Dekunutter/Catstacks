package game.android.com.catstacks.game.objects.statics.backgrounds;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;

public class Sphinx extends BackgroundScenery
{
    public Sphinx(Vec position)
    {
        addSpriteToPosition(R.drawable.sphinx, -6, 0, position);

        activeFrames.add(null);
    }
}
