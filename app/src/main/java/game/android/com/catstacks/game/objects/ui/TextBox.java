package game.android.com.catstacks.game.objects.ui;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.core.RenderedState;
import game.android.com.catstacks.engine.objects.TextObject;
import game.android.com.catstacks.engine.pool.GenericObjectPool;
import game.android.com.catstacks.engine.rendering.TextureLoader;

public class TextBox extends TextObject
{
    public TextBox(String text, float x, float y)
    {
        super(text, x, y, true);

        textures.add(TextureLoader.loadTexture(RenderedState.getContext(), R.drawable.font_5));

        prepareDraw();

        unmovable = true;
    }

    public TextBox(String text, float size, float x, float y)
    {
        super(text, size, x, y, true);

        textures.add(TextureLoader.loadTexture(RenderedState.getContext(), R.drawable.font_5));

        prepareDraw();

        unmovable = true;
    }

    public TextBox(String text, float size, float x, float y, boolean centered)
    {
        super(text, size, x, y, centered);

        textures.add(TextureLoader.loadTexture(RenderedState.getContext(), R.drawable.font_5));

        prepareDraw();

        unmovable = true;
    }

    public TextBox(String text, float size, float x, float y, float setColour, boolean centered)
    {
        super(text, size, x, y, setColour, centered);

        textures.add(TextureLoader.loadTexture(RenderedState.getContext(), R.drawable.font_5));

        prepareDraw();

        unmovable = true;
    }

    public void move(float moveX, float moveY)
    {
        x += moveX;
        y += moveY;
        prepareDraw();
    }
}
