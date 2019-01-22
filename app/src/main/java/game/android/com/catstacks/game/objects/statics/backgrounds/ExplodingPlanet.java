package game.android.com.catstacks.game.objects.statics.backgrounds;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;

public class ExplodingPlanet extends BackgroundScenery
{
    public ExplodingPlanet(Vec position)
    {
        addSpriteToPosition(R.drawable.exploding_planet, -9f, 0, position);

        activeFrames.add(null);

        unmovable = true;

        solid = false;
    }
}
