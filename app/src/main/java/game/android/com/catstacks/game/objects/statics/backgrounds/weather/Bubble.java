package game.android.com.catstacks.game.objects.statics.backgrounds.weather;

import android.util.Log;

import java.util.Random;

import game.android.com.catstacks.engine.core.Screen;
import game.android.com.catstacks.engine.core.Time;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Polygon;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.pool.GenericObjectPool;
import game.android.com.catstacks.engine.rendering.Texture;

public class Bubble extends BackgroundScenery
{
    private float x, y, depth, originalY, horizontalFloat, popTimer, floatTimer, removeTimer;
    private boolean pop;

    private final static int DEFAULT_FRAME = 0;
    private final static int POP_FRAME = 1;

    public final static GenericObjectPool<Bubble> pool = new GenericObjectPool<Bubble>(Bubble.class);

    public Bubble()
    {

    }

    public Bubble(Texture texture, Vec position)
    {
        depth = -7;
        addSpriteFromExistingTexture(texture, depth, 0, position);

        activeFrames.add(textures.get(0).getFrame(DEFAULT_FRAME));
        defaultFrames.add(DEFAULT_FRAME);
        storeBitmap = true;

        x = position.x;
        y = position.y;

        float[] vertices = ArrayPool.floatMaster.get(12);
        vertices[0] = 0;
        vertices[1] = 0;
        vertices[2] = 0;
        vertices[3] = 0;
        vertices[4] = 1;
        vertices[5] = 0;
        vertices[6] = 1;
        vertices[7] = 1;
        vertices[8] = 0;
        vertices[9] = 1;
        vertices[10] = 0;
        vertices[11] = 0;
        body = Polygon.pool.get();
        body.init(x, y, 0, vertices);
        body.move(x, y, 0);
        body.setMass(0.01f);
        ArrayPool.floatMaster.recycle(vertices, vertices.length);

        Random r = ArrayPool.randomPool.get();
        popTimer = r.nextInt(150 - 100 + 1) + 100;
        floatTimer = r.nextInt(40 - 25 + 1) + 25;
        removeTimer = 5;
        ArrayPool.randomPool.recycle(r);
        pop = false;
        horizontalFloat = 0.2f;

        solid = false;
        unmovable = true;

        originalY = y;
    }

    public void init(Texture texture, Vec position)
    {
        depth = -7;
        addSpriteFromExistingTexture(texture, depth, 0, position);

        activeFrames.add(textures.get(0).getFrame(DEFAULT_FRAME));
        defaultFrames.add(DEFAULT_FRAME);
        storeBitmap = true;

        x = position.x;
        y = position.y;

        float[] vertices = ArrayPool.floatMaster.get(12);
        vertices[0] = 0;
        vertices[1] = 0;
        vertices[2] = 0;
        vertices[3] = 0;
        vertices[4] = 1;
        vertices[5] = 0;
        vertices[6] = 1;
        vertices[7] = 1;
        vertices[8] = 0;
        vertices[9] = 1;
        vertices[10] = 0;
        vertices[11] = 0;
        body = Polygon.pool.get();
        body.init(x, y, 0, vertices);
        body.move(x, y, 0);
        body.setMass(0.01f);
        ArrayPool.floatMaster.recycle(vertices, vertices.length);

        Random r = ArrayPool.randomPool.get();
        popTimer = r.nextInt(150 - 100 + 1) + 100;
        floatTimer = r.nextInt(40 - 25 + 1) + 25;
        removeTimer = 5;
        ArrayPool.randomPool.recycle(r);
        pop = false;
        horizontalFloat = 0.2f;

        solid = false;
        unmovable = true;

        originalY = y;
    }

    @Override
    public void update()
    {
        if(!fadedIn)
        {
            changeColourAlpha(0, 1);

            fadedIn = true;
        }

        if(pop)
        {
            if(removeTimer <= 0)
            {
                remove();
            }
            else
            {
                removeTimer -= Time.getDelta();
            }
        }

        if(popTimer <= 0 && !pop)
        {
            pop = true;
            changeFrame(0, POP_FRAME);
            defaultFrames.set(0, POP_FRAME);
        }
        else
        {
            popTimer -= Time.getDelta();
        }

        if(floatTimer <= 0)
        {
            horizontalFloat *= -1;
            Random r = ArrayPool.randomPool.get();
            floatTimer = r.nextInt(40 - 25 + 1) + 25;
            ArrayPool.randomPool.recycle(r);
        }
        else
        {
            floatTimer -= Time.getDelta();
        }

        if(body.getY() >= Screen.screen.getHeight())
        {
            Random r = ArrayPool.randomPool.get();
            float newX = r.nextInt((int)Screen.screen.getPixelDensity(280) - (int)Screen.screen.getPixelDensity(5) + 1) + Screen.screen.getPixelDensity(5);
            float newY = originalY - body.getY();
            body.move(body.getX(), body.getY() + newY, rotation);
            body.setX(newX);
            body.setY(body.getY() + newY);
            ArrayPool.randomPool.recycle(r);
        }
        else
        {
            body.move(body.getX() + horizontalFloat, body.getY() + (3.5f * getMovementDelta()), rotation);
            body.setX(body.getX() + horizontalFloat);
            body.setY(body.getY() + (3.5f * getMovementDelta()));
        }

        if(body.getY() >= Screen.screen.getHeight())
        {
            remove();
        }
    }

    public void move(Vec position)
    {
        body.setX(position.x);
        body.setY(position.y);
    }

    public void setPosition(Vec position)
    {
        x = position.x;
        y = position.y;
    }

    @Override
    public void remove()
    {
        super.remove();

        Random r = ArrayPool.randomPool.get();
        float newX = r.nextInt((int)Screen.screen.getPixelDensity(280) - (int)Screen.screen.getPixelDensity(5) + 1) + Screen.screen.getPixelDensity(5);
        float newY = originalY - body.getY();
        body.move(body.getX(), body.getY() + newY, rotation);
        body.setX(newX);
        body.setY(body.getY() + newY);
        ArrayPool.randomPool.recycle(r);
    }

    public void destruct()
    {
        hasAlpha = true;
        tiled = false;

        verticesChanged = false;
        coloursChanged = false;
        textureChanged = false;
        frameChanged = false;
        indicesChanged = false;

        x = 0;
        y = 0;

        fadedIn = false;
        removed = false;
        pop = false;

        activeFrames.set(0, textures.get(0).getFrame(DEFAULT_FRAME));
        defaultFrames.set(0, DEFAULT_FRAME);

        Random r = ArrayPool.randomPool.get();
        popTimer = r.nextInt(150 - 100 + 1) + 100;
        floatTimer = r.nextInt(40 - 25 + 1) + 25;
        removeTimer = 5;
        ArrayPool.randomPool.recycle(r);
        horizontalFloat = 0.2f;

        Bubble.pool.recycle(this);
    }
}
