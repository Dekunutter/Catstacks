package game.android.com.catstacks.engine.gameloop;

import game.android.com.catstacks.engine.core.GameThread;
import game.android.com.catstacks.engine.states.Menu;
import game.android.com.catstacks.engine.states.World;

public class Save
{
    public static void save()
    {
        switch(GameThread.state)
        {
            case MENU:
                Menu.menu.save();
                break;
            case GAME:
                World.world.save();
                break;
        }
    }

    public static void restore()
    {
        switch(GameThread.state)
        {
            case MENU:
                Menu.menu.restore();
                break;
            case GAME:
                World.world.restore();
                break;
        }
    }
}
