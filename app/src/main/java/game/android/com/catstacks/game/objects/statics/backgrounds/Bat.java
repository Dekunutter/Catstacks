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

public class Bat extends BackgroundScenery
{
    private float movementX, floatTimer, depth;
    private int floatFrame;
    private boolean flipped;

    private final static int FLAP_FRAME_1 = 0;
    private final static int FLAP_FRAME_2 = 1;
    private final static int FLAP_FRAME_3 = 2;
    private final static int FLAP_FRAME_4 = 3;
    private final static int FLAP_FRAME_5 = 4;

    private final static String FLAP_ANIM = "flap";

    public Bat(Vec position)
    {
        depth = -4;
        addSpriteToPosition(R.drawable.bat, 1, 5, depth, 0, position);

        activeFrames.add(textures.get(0).getFrame(FLAP_FRAME_1));

        initAnimations();
        getAnimation(FLAP_ANIM).activate();

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

        movementX = 10;

        floatTimer = 5;
        floatFrame = 0;
        flipped = false;
    }

    private void initAnimations()
    {
        animations = ArrayPool.animationPool.get();

        ArrayList<float[]> frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(FLAP_FRAME_1));
        frames.add(textures.get(0).getFrame(FLAP_FRAME_2));
        frames.add(textures.get(0).getFrame(FLAP_FRAME_3));
        frames.add(textures.get(0).getFrame(FLAP_FRAME_4));
        frames.add(textures.get(0).getFrame(FLAP_FRAME_5));
        frames.add(textures.get(0).getFrame(FLAP_FRAME_4));
        frames.add(textures.get(0).getFrame(FLAP_FRAME_3));
        frames.add(textures.get(0).getFrame(FLAP_FRAME_2));
        Animation flapping = Animation.pool.get();
        flapping.init(FLAP_ANIM, 0, frames, 3);

        animations.add(flapping);
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
                spriteY = -2;
                spriteHeight = height - 2;
            }
            else if(floatFrame == 2)
            {
                spriteY = -5;
                spriteHeight = height - 5;
            }
            else if(floatFrame == 3)
            {
                spriteY = -2;
                spriteHeight = height - 2;
            }
            else if(floatFrame == 4)
            {
                spriteY = 0;
                spriteHeight = height;
            }
            else if(floatFrame == 5)
            {
                spriteY = -2;
                spriteHeight = height - 2;
            }
            else if(floatFrame == 6)
            {
                spriteY = -4;
                spriteHeight = height - 4;
            }
            else if(floatFrame == 7)
            {
                spriteY = -2;
                spriteHeight = height - 2;
            }
            else if(floatFrame == 8)
            {
                spriteY = 0;
                spriteHeight = height;
                floatFrame = 0;
            }

            if(flipped)
            {
                changeVerticesFlippedX(0, 0, spriteY, width, spriteHeight, depth);
            }
            else
            {
                changeVertices(0, 0, spriteY, width, spriteHeight, depth);
            }

            floatTimer = 5;
        }
        else
        {
            floatTimer -= Time.getDelta();
        }

        if(body.getX() >= Screen.screen.getWidth() + Screen.screen.getPixelDensity(80))
        {
            movementX *= -1;
            flipped = true;
            changeVerticesFlippedX(0, 0, 0, width, height, depth);
        }
        else if(body.getX() <= Screen.screen.getPixelDensity(-160))
        {
            movementX *= -1;
            flipped = false;
            changeVertices(0, 0, 0, width, height, depth);
        }

        body.move(body.getX()  + (movementX * getMovementDelta()), body.getY(), rotation);
        body.setX(body.getX() + (movementX * getMovementDelta()));
    }
}
