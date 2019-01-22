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

public class TechnoDrop extends BackgroundScenery
{
    private float x, y, depth, originalY;
    private int colour;

    public final static GenericObjectPool<TechnoDrop> pool = new GenericObjectPool<TechnoDrop>(TechnoDrop.class);

    public TechnoDrop()
    {

    }

    public TechnoDrop(Texture texture, Vec position)
    {
        depth = -6;
        addSpriteFromExistingTexture(texture, depth, 0, position);

        Random r = ArrayPool.randomPool.get();
        colour = r.nextInt(5 + 1);
        activeFrames.add(textures.get(0).getFrame(colour));
        defaultFrames.add(colour);
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

        Random r = ArrayPool.randomPool.get();
        colour = r.nextInt(5 + 1);
        activeFrames.add(textures.get(0).getFrame(colour));
        defaultFrames.add(colour);
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


        //maybe move this to class scope and just alter and set in the arraylist as needed

        //changeVertices(0, x, y, width, height, depth);

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
            body.move(body.getX(), body.getY() - (40 * getMovementDeltaY()), rotation);
            body.setY(body.getY() - (40 * getMovementDeltaY()));

        }

        if(y <= Screen.screen.getPixelDensity(10))
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

        activeFrames.set(0, textures.get(0).getFrame(colour));
        defaultFrames.set(0, colour);

        TechnoDrop.pool.recycle(this);
    }
}
