package game.android.com.catstacks.engine.gameloop;

import game.android.com.catstacks.engine.core.GameThread;
import game.android.com.catstacks.engine.states.Menu;
import game.android.com.catstacks.engine.states.World;

public class Update
{
    public static void update()
    {
        switch(GameThread.state)
        {
            case MENU:
                Menu.menu.update();
                break;
            case GAME:
                World.world.update();
                break;
        }
    }
}
