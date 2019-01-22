package game.android.com.catstacks.engine.rendering;

public class ColourSprite extends Sprite
{
    public ColourSprite(float[] vertices)
    {
        initBasicBuffers(vertices);
        initSolidShader();
    }
}
