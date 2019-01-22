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

public class Snowflake extends BackgroundScenery
{
    private float x, y, depth, originalY, floatTimer;
    private float horizontalFloat;

    public final static GenericObjectPool<Snowflake> pool = new GenericObjectPool<Snowflake>(Snowflake.class);

    public Snowflake()
    {

    }

    public Snowflake(Texture texture, Vec position)
    {
        depth = -6;
        addSpriteFromExistingTexture(texture, depth, 0, position);

        activeFrames.add(null);
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

        solid = false;
        unmovable = true;

        originalY = y;

        Random r = ArrayPool.randomPool.get();
        floatTimer = r.nextInt(25 - 5 + 1) + 5;
        ArrayPool.randomPool.recycle(r);
        horizontalFloat = 0.2f;
    }

    public void init(Texture texture, Vec position)
    {
        depth = -6;
        addSpriteFromExistingTexture(texture, depth, 0, position);

        activeFrames.add(null);
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

        solid = false;
        unmovable = true;

        originalY = y;

        Random r = ArrayPool.randomPool.get();
        floatTimer = r.nextInt(25 - 5 + 1) + 5;
        ArrayPool.randomPool.recycle(r);
        horizontalFloat = 0.2f;
    }

    @Override
    public void update()
    {
        if(!fadedIn)
        {
            changeColourAlpha(0, 1);

            fadedIn = true;
        }

        if(floatTimer <= 0)
        {
            horizontalFloat *= -1;
            Random r = ArrayPool.randomPool.get();
            floatTimer = r.nextInt(25 - 5 + 1) + 5;
            ArrayPool.randomPool.recycle(r);
        }
        else
        {
            floatTimer -= Time.getDelta();
        }

        if(body.getY() <= - Screen.screen.getHeight())
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
            body.move(body.getX() + horizontalFloat, body.getY() - (10 * getMovementDeltaY()), rotation);
            body.setX(body.getX() + horizontalFloat);
            body.setY(body.getY() - (10 * getMovementDeltaY()));
        }

        if(body.getY() <= 0)
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

        changeVertices(0, x, y, width, height, depth);

        Random r = ArrayPool.randomPool.get();
        floatTimer = r.nextInt(25 - 5 + 1) + 5;
        ArrayPool.randomPool.recycle(r);
        horizontalFloat = 0.2f;

        Snowflake.pool.recycle(this);
    }
}
