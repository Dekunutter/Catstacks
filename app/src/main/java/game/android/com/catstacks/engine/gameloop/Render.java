package game.android.com.catstacks.engine.gameloop;

import game.android.com.catstacks.engine.core.GameThread;
import game.android.com.catstacks.engine.states.Menu;
import game.android.com.catstacks.engine.states.World;

public class Render
{
    public static void render(float[] mvpMatrix)
    {
        switch(GameThread.state)
        {
            case MENU:
                Menu.menu.render(mvpMatrix);
                break;
            case GAME:
                World.world.render(mvpMatrix);
                break;
        }
    }
}
