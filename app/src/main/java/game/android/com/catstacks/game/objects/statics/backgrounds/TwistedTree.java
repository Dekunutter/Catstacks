package game.android.com.catstacks.game.objects.statics.backgrounds;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;

public class TwistedTree extends BackgroundScenery
{
    public TwistedTree(Vec position)
    {
        addSpriteToPosition(R.drawable.twisted_tree, -5f, 0, position);

        activeFrames.add(null);
    }
}
