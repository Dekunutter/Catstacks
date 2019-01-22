package game.android.com.catstacks.game.objects.statics.backgrounds;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;

public class Volcanoes extends BackgroundScenery
{
    public Volcanoes(Vec position)
    {
        addSpriteToPosition(R.drawable.volcanoes, -7, 0, position);

        activeFrames.add(null);
    }
}
