package game.android.com.catstacks.engine.gameloop;

import game.android.com.catstacks.engine.core.GameThread;
import game.android.com.catstacks.engine.input.InputBuffer;
import game.android.com.catstacks.engine.states.Menu;
import game.android.com.catstacks.engine.states.World;

public class Input
{
    public static void getInput(InputBuffer input)
    {
        switch(GameThread.state)
        {
            case MENU:
                Menu.menu.getInput(input);
                break;
            case GAME:
                World.world.getInput(input);
                break;
        }
    }
}
