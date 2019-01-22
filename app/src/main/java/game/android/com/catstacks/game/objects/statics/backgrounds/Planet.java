package game.android.com.catstacks.game.objects.statics.backgrounds;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;

public class Planet extends BackgroundScenery
{
    public Planet(Vec position)
    {
        addSpriteToPosition(R.drawable.planet, -9f, 0, position);

        activeFrames.add(null);

        unmovable = true;

        solid = false;
    }
}
