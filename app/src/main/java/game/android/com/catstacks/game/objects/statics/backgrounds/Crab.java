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

public class Crab extends BackgroundScenery
{
    private float movementX;
    private final static int DEFAULT_FRAME = 0;
    private final static int SCUTTLE_FRAME = 1;

    private final static String SCUTTLE_ANIM = "scuttle";

    public Crab(Vec position)
    {
        addSpriteToPosition(R.drawable.crab, 2, 1, -4, 0, position);

        activeFrames.add(textures.get(0).getFrame(DEFAULT_FRAME));

        initAnimations();
        getAnimation(SCUTTLE_ANIM).activate();

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
    }

    private void initAnimations()
    {
        animations = ArrayPool.animationPool.get();

        ArrayList<float[]> frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(DEFAULT_FRAME));
        frames.add(textures.get(0).getFrame(SCUTTLE_FRAME));
        Animation scuttling = Animation.pool.get();
        scuttling.init(SCUTTLE_ANIM, 0, frames, 4);

        animations.add(scuttling);
    }

    @Override
    public void update()
    {
        super.update();

        if(body.getX() >= Screen.screen.getWidth() + Screen.screen.getPixelDensity(20))
        {
            movementX *= -1;
        }
        else if(body.getX() <= Screen.screen.getPixelDensity(-80))
        {
            movementX *= -1;
        }

        body.move(body.getX()  + (movementX * getMovementDelta()), body.getY(), rotation);
        body.setX(body.getX() + (movementX * getMovementDelta()));

    }
}
