package game.android.com.catstacks.game.objects.ui;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.core.RenderedState;
import game.android.com.catstacks.engine.objects.TextObject;
import game.android.com.catstacks.engine.rendering.TextureLoader;
import game.android.com.catstacks.game.objects.dynamic.Player;

public class LiveCounter extends TextObject
{
    private int lives;
    private Player tracking;

    public LiveCounter(int startingValue, float x, float y, Player tracking)
    {
        super(String.valueOf(startingValue), 0.5f, x, y);
        lives = startingValue;
        this.tracking = tracking;

        textures.add(TextureLoader.loadTexture(RenderedState.getContext(), R.drawable.font_5));

        prepareDraw();

        unmovable = true;
    }

    @Override
    public void update()
    {
        if(tracking.getLives() != lives)
        {
            lives = tracking.getLives();
            setText(String.valueOf(lives));
            prepareDraw();
        }
    }
}
