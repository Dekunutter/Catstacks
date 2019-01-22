package game.android.com.catstacks.game.objects.statics.backgrounds;

import java.util.ArrayList;
import java.util.Random;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.core.Screen;
import game.android.com.catstacks.engine.core.Time;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Polygon;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.rendering.Animation;

public class Penguin extends BackgroundScenery
{
    private float movementX, turnTimer, restTimer, depth;
    private boolean flipped, resting;

    private final static int DEFAULT_FRAME = 0;
    private final static int WALKING_FRAME_1 = 1;
    private final static int WALKING_FRAME_2 = 2;

    private final static String WALKING_ANIM = "walking";

    public Penguin(Vec position)
    {
        depth = -4f;
        addSpriteToPosition(R.drawable.penguin, 3, 1, depth, 0, position);

        activeFrames.add(textures.get(0).getFrame(DEFAULT_FRAME));

        initAnimations();

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

        movementX = -2.5f;

        Random r = ArrayPool.randomPool.get();
        turnTimer = r.nextInt(150 - 15 + 1) + 15;
        restTimer = r.nextInt(25 - 1);
        flipped = false;
        ArrayPool.randomPool.recycle(r);

        changeVertices(0, 0, 0, width, height, depth);
    }

    private void initAnimations()
    {
        animations = ArrayPool.animationPool.get();

        ArrayList<float[]> frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(WALKING_FRAME_1));
        frames.add(textures.get(0).getFrame(WALKING_FRAME_2));
        Animation walking = Animation.pool.get();
        walking.init(WALKING_ANIM, 0, frames, 3);

        animations.add(walking);
    }

    @Override
    public void update()
    {
        super.update();

        Random r = ArrayPool.randomPool.get();

        if(turnTimer <= 0)
        {
            turnAround(r);
        }
        else
        {
            turnTimer -= Time.getDelta();
        }

        if(resting)
        {
            if(getAnimation(WALKING_ANIM).isActive())
            {
                getAnimation(WALKING_ANIM).deactivate();
            }

            restTimer -= Time.getDelta();

            if(restTimer <= 0)
            {
                resting = false;
                restTimer = r.nextInt(25 - 1);
            }
        }
        else
        {
            getAnimation(WALKING_ANIM).activate();

            if(body.getX() >= Screen.screen.getWidth() + Screen.screen.getPixelDensity(32))
            {
                turnAround(r);
            }
            else if(body.getX() <= Screen.screen.getPixelDensity(-64))
            {
                turnAround(r);
            }

            body.move(body.getX() + (movementX * getMovementDeltaX()), body.getY(), rotation);
            body.setX(body.getX() + (movementX * getMovementDeltaX()));
        }

        ArrayPool.randomPool.recycle(r);
    }

    private void turnAround(Random r)
    {
        movementX *= -1;

        if(!flipped)
        {
            changeVerticesFlippedX(0, 0, 0, width, height, depth);
            flipped = true;
        }
        else
        {
            changeVertices(0, 0, 0, width, height, depth);
            flipped = false;
        }

        resting = true;

        turnTimer = r.nextInt(150 - 15 + 1) + 15;
    }
}
