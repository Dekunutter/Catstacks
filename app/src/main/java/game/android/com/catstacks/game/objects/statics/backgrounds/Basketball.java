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

public class Basketball extends BackgroundScenery
{
    private float originalX, movementX, movementY;
    private boolean bouncing;

    private final static int BOUNCE_FRAME_1 = 0;
    private final static int BOUNCE_FRAME_2 = 1;
    private final static int BOUNCE_FRAME_3 = 2;
    private final static int BOUNCE_FRAME_4 = 3;
    private final static int BOUNCE_FRAME_5 = 4;
    private final static int BOUNCE_FRAME_6 = 5;
    private final static int BOUNCE_FRAME_7 = 6;
    private final static int BOUNCE_FRAME_8 = 7;

    private final static String BOUNCING_ANIM = "bounce";

    public Basketball(Vec position)
    {
        addSpriteToPosition(R.drawable.basketball, 8, 1, -4f, 0, position);

        activeFrames.add(textures.get(0).getFrame(BOUNCE_FRAME_1));
        defaultFrames.add(BOUNCE_FRAME_1);

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

        initAnimations();
        getAnimation(BOUNCING_ANIM).activate();

        originalX = position.x;
        bouncing = false;
    }

    private void initAnimations()
    {
        animations = ArrayPool.animationPool.get();

        ArrayList<float[]> frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(BOUNCE_FRAME_1));
        frames.add(textures.get(0).getFrame(BOUNCE_FRAME_2));
        frames.add(textures.get(0).getFrame(BOUNCE_FRAME_3));
        frames.add(textures.get(0).getFrame(BOUNCE_FRAME_4));
        frames.add(textures.get(0).getFrame(BOUNCE_FRAME_5));
        frames.add(textures.get(0).getFrame(BOUNCE_FRAME_6));
        frames.add(textures.get(0).getFrame(BOUNCE_FRAME_7));
        frames.add(textures.get(0).getFrame(BOUNCE_FRAME_8));
        Animation lighting = Animation.pool.get();
        lighting.init(BOUNCING_ANIM, 0, frames, 3);

        animations.add(lighting);
    }

    @Override
    public void update()
    {
        super.update();

        float newX, newY;

        if(body.getX() >= Screen.screen.getWidth() + Screen.screen.getPixelDensity(100))
        {
            newX = originalX;
            movementX = body.getX() - newX;
        }
        else
        {
            //movementX = 5 * Time.getDelta();
            //newX = body.getX() + movementX;
            if(bouncing)
            {
                movementX = Math.min(movementX + (getMovementDeltaX() * 0.75f), 2.5f * getMovementDeltaX());
                newX = body.getX() + movementX;
            }
            else
            {
                movementX = 2.5f * getMovementDeltaX();
                newX = body.getX() + movementX;
            }
        }

        if(body.getY() < Screen.screen.getPixelDensity(0))
        {
            bouncing = true;
            newY = Screen.screen.getPixelDensity(0);
            movementY = body.getY() - newY;

            movementX = 0;
            newX = body.getX();
        }
        else if(body.getY() > Screen.screen.getPixelDensity(50))
        {
            bouncing = false;
            newY = Screen.screen.getPixelDensity(50);
            movementY = body.getY() - newY;
        }
        else
        {
            if(bouncing)
            {
                movementY = Math.min(movementY + (getMovementDeltaY() * 0.5f), 5 * getMovementDeltaY());
                newY = body.getY() + movementY;
            }
            else
            {
                movementY = Math.max(movementY + (getMovementDeltaY() * -0.5f), -5 * getMovementDeltaY());
                newY = body.getY() + movementY;
            }
        }

        body.move(movementX, movementY, rotation);
        body.setX(newX);
        body.setY(newY);
    }
}