package game.android.com.catstacks.game.objects.statics.backgrounds;

import android.util.Log;

import java.util.ArrayList;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.core.Screen;
import game.android.com.catstacks.engine.core.Time;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Polygon;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.rendering.Animation;

public class Ufo extends BackgroundScenery
{
    private float originalX, respawnTimer;
    private boolean respawning;

    private final static int LIGHTING_FRAME_1 = 0;
    private final static int LIGHTING_FRAME_2 = 1;
    private final static int LIGHTING_FRAME_3 = 2;

    private final static String LIGHTING_ANIM = "lighting";

    public Ufo(Vec position)
    {
        addSprite(R.drawable.ufo, 1, 3, -6.5f, 0);

        activeFrames.add(textures.get(0).getFrame(LIGHTING_FRAME_1));

        initAnimations();
        getAnimation(LIGHTING_ANIM).activate();

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

        respawning = false;
        respawnTimer = 0;
    }

    private void initAnimations()
    {
        animations = ArrayPool.animationPool.get();

        ArrayList<float[]> frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(LIGHTING_FRAME_1));
        frames.add(textures.get(0).getFrame(LIGHTING_FRAME_2));
        frames.add(textures.get(0).getFrame(LIGHTING_FRAME_3));
        Animation lighting = Animation.pool.get();
        lighting.init(LIGHTING_ANIM, 0, frames, 5);

        animations.add(lighting);
    }

    @Override
    public void update()
    {
        super.update();

        if(respawnTimer > 0 && respawning)
        {
            respawnTimer -= Time.getDelta();
            return;
        }
        else
        {
            respawnTimer = 50;
            respawning = false;
        }

        if(body.getX() >= Screen.screen.getWidth() + Screen.screen.getPixelDensity(100))
        {
            float newX = originalX;
            body.move(body.getX() - newX, body.getY(), rotation);
            body.setX(newX);
            body.setY(body.getY());

            respawning = true;
        }
        else
        {
            body.move(body.getX() + (15 * getMovementDeltaX()), body.getY(), rotation);
            body.setX(body.getX() + (15 * getMovementDeltaX()));
        }
    }
}
