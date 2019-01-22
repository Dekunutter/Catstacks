package game.android.com.catstacks.game.objects.ui;

import android.util.Log;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.core.RenderedState;
import game.android.com.catstacks.engine.objects.TextObject;
import game.android.com.catstacks.engine.rendering.TextureLoader;
import game.android.com.catstacks.game.objects.dynamic.Player;

public class StackCounter extends TextObject
{
    private Player tracking;
    private int counter;

    public StackCounter(int startingValue, float x, float y, Player tracking)
    {
        super(String.valueOf(startingValue), 0.5f, x, y, true);
        this.tracking = tracking;
        counter = startingValue;

        textures.add(TextureLoader.loadTexture(RenderedState.getContext(), R.drawable.font_5));

        prepareDraw();

        unmovable = true;
    }

    @Override
    public void update()
    {
        if(tracking.getCats() != counter)
        {
            counter = tracking.getCats();
            if(counter == tracking.getMaxStackSize())
            {
                setText("MAX");
            }
            else
            {
                setText(Integer.toString(counter));
            }
            prepareDraw();
        }
    }
}
