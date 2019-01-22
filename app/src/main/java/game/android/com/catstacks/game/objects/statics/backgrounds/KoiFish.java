package game.android.com.catstacks.game.objects.statics.backgrounds;

import android.util.Log;

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

public class KoiFish extends BackgroundScenery
{
    private float originalX;

    private final static int DEFAULT_FRAME = 0;
    private final static int BLUB_FRAME = 1;

    private final static String BLUB_ANIM = "blub";

    public KoiFish(Vec position)
    {
        addSprite(R.drawable.koi_fish, 1, 2, -6.5f, 0);

        activeFrames.add(textures.get(0).getFrame(DEFAULT_FRAME));

        initAnimations();
        getAnimation(BLUB_ANIM).activate();

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
    }

    private void initAnimations()
    {
        animations = ArrayPool.animationPool.get();

        ArrayList<float[]> frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(DEFAULT_FRAME));
        frames.add(textures.get(0).getFrame(BLUB_FRAME));
        Animation blubbing = Animation.pool.get();
        blubbing.init(BLUB_ANIM, 0, frames, 20);

        animations.add(blubbing);
    }

    @Override
    public void update()
    {
        super.update();

        if(body.getX() >= Screen.screen.getWidth() + Screen.screen.getPixelDensity(100))
        {
            float newX = originalX;
            body.move(body.getX() - newX, body.getY(), rotation);
            body.setX(newX);
            body.setY(body.getY());
        }
        else
        {
            body.move(body.getX()  + (20 * getMovementDelta()), body.getY(), rotation);
            body.setX(body.getX() + (20 * getMovementDelta()));
        }
    }
}
