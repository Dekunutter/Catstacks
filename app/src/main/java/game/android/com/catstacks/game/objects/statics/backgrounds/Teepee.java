package game.android.com.catstacks.game.objects.statics.backgrounds;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;

public class Teepee extends BackgroundScenery
{
    public Teepee(Vec position)
    {
        addSpriteToPosition(R.drawable.teepee, -4f, 0, position);

        activeFrames.add(null);
    }
}
