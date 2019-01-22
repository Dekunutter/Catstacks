package game.android.com.catstacks.engine.gameloop;

import game.android.com.catstacks.engine.core.GameThread;
import game.android.com.catstacks.engine.states.Menu;
import game.android.com.catstacks.engine.states.World;

public class Buffer
{
    public static void buffer()
    {
        switch(GameThread.state)
        {
            case MENU:
                Menu.menu.buffer();
                break;
            case GAME:
                World.world.buffer();
                break;
        }
    }
}
