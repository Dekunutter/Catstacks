package game.android.com.catstacks.game.objects.ui;

import android.content.Context;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.TextObject;
import game.android.com.catstacks.engine.rendering.TextureLoader;

public class HighScoreboard extends TextObject
{
    public HighScoreboard(Context context, long highScore, float x, float y)
    {
        super(String.valueOf(highScore), x, y, true);

        textures.add(TextureLoader.loadTexture(context, R.drawable.font_5));

        prepareDraw();

        unmovable = true;
    }

    public HighScoreboard(Context context, long currentScore, long highScore, float x, float y)
    {
        super(currentScore + "/" + highScore, x, y, true);

        textures.add(TextureLoader.loadTexture(context, R.drawable.font_5));

        prepareDraw();

        unmovable = true;
    }
}
