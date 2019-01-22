package game.android.com.catstacks.game.objects.statics.backgrounds;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.core.Screen;
import game.android.com.catstacks.engine.core.Time;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Polygon;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;

public class Iceburg extends BackgroundScenery
{
    private float originalX, movementX, floatTimer, depth;
    private int floatFrame;

    public Iceburg(Vec position)
    {
        depth = -6.5f;
        addSpriteToPosition(R.drawable.iceburg, depth, 0, position);

        activeFrames.add(null);

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
        body = Polygon.pool.get();
        body.init(position.x, position.y, 0, vertices);
        body.move(position.x, position.y, 0);
        body.setMass(0.01f);
        ArrayPool.floatMaster.recycle(vertices, vertices.length);

        solid = false;
        unmovable = true;

        movementX = -0.5f;

        originalX = position.x;
        floatTimer = 25;
        floatFrame = 0;
    }

    @Override
    public void update()
    {
        super.update();

        if(floatTimer <= 0)
        {
            floatFrame++;

            float spriteY = 0;
            float spriteHeight = height;

            if(floatFrame == 1)
            {
                changeVertices(0, 0, -2, width, height - 2, depth);
            }
            else if(floatFrame == 2)
            {
                changeVertices(0, 0, 0, width, height, depth);
                floatFrame = 0;
            }

            floatTimer = 25;
        }
        else
        {
            floatTimer -= Time.getDelta();
        }


        if(body.getX() <= -Screen.screen.getPixelDensity(150))
        {
            float newX = originalX;
            body.move(body.getX() - newX, body.getY(), rotation);
            body.setX(newX);
            body.setY(body.getY());
        }
        else
        {
            body.move(body.getX() + (movementX * getMovementDeltaX()), body.getY(), rotation);
            body.setX(body.getX() + (movementX * getMovementDeltaX()));
        }
    }
}
