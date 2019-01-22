package game.android.com.catstacks.game.objects.statics.backgrounds.weather;

import java.util.Random;

import game.android.com.catstacks.engine.core.Screen;
import game.android.com.catstacks.engine.core.Time;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Polygon;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.pool.GenericObjectPool;
import game.android.com.catstacks.engine.rendering.Texture;

public class Raindrop extends BackgroundScenery
{
    private float x, y, depth, originalY;

    public final static GenericObjectPool<Raindrop> pool = new GenericObjectPool<Raindrop>(Raindrop.class);

    public Raindrop()
    {

    }

    public Raindrop(Texture texture, Vec position)
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
    }

    @Override
    public void update()
    {
        if(!fadedIn)
        {
            changeColourAlpha(0, 1);

            fadedIn = true;
        }


        /*float[] newPosition = ArrayPool.floatMaster.get(12);
        newPosition[0] = x;
        newPosition[1] = y + height;
        newPosition[2] = depth;
        newPosition[3] = x;
        newPosition[4] = y;
        newPosition[5] = depth;
        newPosition[6] = x + width;
        newPosition[7] = y;
        newPosition[8] = depth;
        newPosition[9] = x + width;
        newPosition[10] = y + height;
        newPosition[11] = depth;
        changeVertices(0, newPosition);*/

        if(body.getY() <= -Screen.screen.getHeight())
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
            body.move(body.getX(), body.getY() - (20 * getMovementDelta()), rotation);
            body.setY(body.getY() - (20 * getMovementDelta()));
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

        Raindrop.pool.recycle(this);
    }
}
