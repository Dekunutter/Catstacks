package game.android.com.catstacks.game.objects.ui;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.physics.CollisionBitmasks;
import game.android.com.catstacks.engine.physics.Polygon;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.sound.SoundManager;

public class QuitButton extends MenuButton
{
    public QuitButton(Vec position)
    {
        addSprite(R.drawable.quit_button, 1, 2, 0, 1);

        activeFrames.add(textures.get(0).getFrame(UNPRESSED_FRAME));
        defaultFrames.add(0);

        float[] vertices = ArrayPool.floatMaster.get(12);
        vertices[0] = 0;
        vertices[1] = 0;
        vertices[2] = 0;
        vertices[3] = 0;
        vertices[4] = height;
        vertices[5] = 0;
        vertices[6] = width;
        vertices[7] = height;
        vertices[8] = 0;
        vertices[9] = width;
        vertices[10] = 0;
        vertices[11] = 0;
        body = new Polygon(position.x, position.y, 0, vertices);
        body.move(position.x, position.y, 0);

        ArrayPool.floatMaster.recycle(vertices, vertices.length);

        unmovable = true;

        pushedDown = false;
        pressed = false;

        mask = CollisionBitmasks.UI_ID;

        solid = false;
    }

    @Override
    public void update()
    {
        if(pushedDown)
        {
            changeFrame(0, PRESSED_FRAME);
        }
        else
        {
            changeFrame(0, UNPRESSED_FRAME);
        }

        if(hasBeenPressed())
        {
            SoundManager.soundManager.clearAllStreams();

            pressed = false;

            System.exit(0);
        }
    }
}
