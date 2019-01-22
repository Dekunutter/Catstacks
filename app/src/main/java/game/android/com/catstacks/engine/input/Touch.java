package game.android.com.catstacks.engine.input;

public class Touch
{
    public boolean active, primary;
    public float x, y;
    public int id;

    public Touch()
    {
        active = false;
        primary = false;
    }

    public void onDown(int id, float x, float y)
    {
        this.id = id;
        this.x = x;
        this.y = y;

        active = true;
    }

    public void onMove(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public void onUp()
    {
        active = false;
        primary = false;
    }
}
