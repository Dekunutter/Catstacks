package game.android.com.catstacks.game.objects.ui;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.core.RenderedState;
import game.android.com.catstacks.engine.objects.TextObject;
import game.android.com.catstacks.engine.rendering.TextureLoader;
import game.android.com.catstacks.game.objects.dynamic.Player;

public class Scoreboard extends TextObject
{
    private Player tracking;
    private int score;

    public Scoreboard(int startingValue, float x, float y, Player tracking)
    {
        super(String.valueOf(startingValue), 0.5f, x, y);
        this.tracking = tracking;
        score = startingValue;

        textures.add(TextureLoader.loadTexture(RenderedState.getContext(), R.drawable.font_5));

        prepareDraw();

        unmovable = true;
    }

    public int getScore()
    {
        return score;
    }

    @Override
    public void update()
    {
        if(tracking.getScore() != score)
        {
            score = tracking.getScore();
            setText(Integer.toString(score));
            prepareDraw();
        }
    }
}
