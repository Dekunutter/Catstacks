package game.android.com.catstacks.engine.gameloop;

import game.android.com.catstacks.engine.input.InputBuffer;

public interface GameLoop
{
    void getInput(InputBuffer input);
    void update();
}
