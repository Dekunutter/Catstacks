package game.android.com.catstacks.game.objects.statics.backgrounds;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;

public class SeaArch extends BackgroundScenery
{
    public SeaArch(Vec position)
    {
        addSpriteToPosition(R.drawable.sea_arch2, -7f, 0, position);

        activeFrames.add(null);
    }
}
