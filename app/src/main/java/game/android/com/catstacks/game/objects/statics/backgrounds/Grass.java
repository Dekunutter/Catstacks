package game.android.com.catstacks.game.objects.statics.backgrounds;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;

public class Grass extends BackgroundScenery
{
    public Grass(Vec position)
    {
        addSpriteToPosition(R.drawable.grass, 0, 0, position);

        activeFrames.add(null);
    }
}
