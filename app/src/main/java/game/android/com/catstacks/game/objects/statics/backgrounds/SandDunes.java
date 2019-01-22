package game.android.com.catstacks.game.objects.statics.backgrounds;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;

public class SandDunes extends BackgroundScenery
{
    public SandDunes(Vec position)
    {
        addSpriteToPosition(R.drawable.sand_dunes, -6, 0, position);

        activeFrames.add(null);
    }
}
