package game.android.com.catstacks.game.objects.statics.backgrounds;

import java.util.ArrayList;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.core.Screen;
import game.android.com.catstacks.engine.core.Time;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Polygon;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.rendering.Animation;

public class Trash extends BackgroundScenery
{
    private float originalX, initTimer, floatTimer, depth;
    private boolean init;
    private int floatFrame;

    private final static int FLOAT_FRAME_1 = 0;
    private final static int FLOAT_FRAME_2 = 1;
    private final static int FLOAT_FRAME_3 = 2;
    private final static int FLOAT_FRAME_4 = 3;
    private final static int FLOAT_FRAME_5 = 4;
    private final static int FLOAT_FRAME_6 = 5;
    private final static int FLOAT_FRAME_7 = 6;
    private final static int FLOAT_FRAME_8 = 7;
    private final static int FLOAT_FRAME_9 = 8;
    private final static int FLOAT_FRAME_10 = 9;

    private final static String FLOAT_ANIM = "floating";

    public Trash(Vec position)
    {
        this(position, 0);
    }

    public Trash(Vec position, float initTimer)
    {
        depth = -4f;
        addSpriteToPosition(R.drawable.trash, 5, 2, depth, 0, position);

        activeFrames.add(textures.get(0).getFrame(FLOAT_FRAME_1));

        initAnimations();
        //getAnimation(FLOAT_ANIM).activate();

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

        originalX = position.x;
        floatTimer = 5;
        floatFrame = 0;
        this.initTimer = initTimer;
        init = false;
    }

    private void initAnimations()
    {
        animations = ArrayPool.animationPool.get();

        ArrayList<float[]> frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(FLOAT_FRAME_1));
        frames.add(textures.get(0).getFrame(FLOAT_FRAME_2));
        frames.add(textures.get(0).getFrame(FLOAT_FRAME_3));
        frames.add(textures.get(0).getFrame(FLOAT_FRAME_4));
        frames.add(textures.get(0).getFrame(FLOAT_FRAME_5));
        frames.add(textures.get(0).getFrame(FLOAT_FRAME_6));
        frames.add(textures.get(0).getFrame(FLOAT_FRAME_7));
        frames.add(textures.get(0).getFrame(FLOAT_FRAME_8));
        frames.add(textures.get(0).getFrame(FLOAT_FRAME_9));
        frames.add(textures.get(0).getFrame(FLOAT_FRAME_10));
        Animation floating = Animation.pool.get();
        floating.init(FLOAT_ANIM, 0, frames, 4);
        animations.add(floating);
    }

    @Override
    public void update()
    {
        if(initTimer > 0)
        {
            initTimer -= Time.getDelta();
            return;
        }
        else
        {
            getAnimation(FLOAT_ANIM).activate();
        }

        super.update();

        if(floatTimer <= 0)
        {
            floatFrame++;

            if(floatFrame == 1)
            {
                changeVertices(0, 0, -2, width, height - 2, depth);
            }
            else if(floatFrame == 2)
            {
                changeVertices(0, 0, -4, width, height - 4, depth);
            }
            else if(floatFrame == 3)
            {
                changeVertices(0, 0, -2, width, height - 2, depth);
            }
            else if(floatFrame == 4)
            {
                changeVertices(0, 0, 0, width, height, depth);
                floatFrame = 0;
            }
            else if(floatFrame == 5)
            {
                changeVertices(0, 0, 2, width, height + 2, depth);
            }
            else if(floatFrame == 6)
            {
                changeVertices(0, 0, 4, width, height + 4, depth);
            }
            else if(floatFrame == 7)
            {
                changeVertices(0, 0, 2, width, height + 2, depth);
            }
            else if(floatFrame == 8)
            {
                changeVertices(0, 0, 0, width, height, depth);
                floatFrame = 0;
            }

            floatTimer = 5;
        }
        else
        {
            floatTimer -= Time.getDelta();
        }

        if(body.getX() >= Screen.screen.getWidth() + Screen.screen.getPixelDensity(100))
        {
            float newX = originalX;
            body.move(body.getX() - newX, body.getY(), rotation);
            body.setX(newX);
            body.setY(body.getY());
        }
        else
        {
            body.move(body.getX()  + (5 * getMovementDeltaX()), body.getY(), rotation);
            body.setX(body.getX() + (5 * getMovementDeltaX()));
        }
    }
}
