package game.android.com.catstacks.engine.gameloop;

import game.android.com.catstacks.engine.core.GameThread;
import game.android.com.catstacks.engine.states.Menu;
import game.android.com.catstacks.engine.states.World;

public class Flag
{
    public static boolean isAdReady()
    {
        boolean result = false;

        switch(GameThread.state)
        {
            case MENU:
                result = Menu.menu.isAdReady();
                break;
            case GAME:
                result = World.world.isAdReady();
                break;
        }

        return result;
    }

    public static boolean isInstitialAdReady()
    {
        boolean result = false;

        switch(GameThread.state)
        {
            case MENU:
                result = Menu.menu.isInstitialAdReady();
                break;
            case GAME:
                result = World.world.isInstitialAdReady();
                break;
        }

        return result;
    }
}
