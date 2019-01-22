package game.android.com.catstacks.engine.core;

public enum GameState
{
    BOOT, MENU, GAME;

    public GameState getNext()
    {
        return values() [(ordinal() + 1) % values().length];
    }
}
