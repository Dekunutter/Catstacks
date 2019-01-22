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

public class Ghost extends BackgroundScenery
{
    private float x, y, depth, originalY, fadeOutTimer, fadeOutAlpha;
    private int frame;
    private boolean fadeOut;

    public final static GenericObjectPool<Ghost> pool = new GenericObjectPool<Ghost>(Ghost.class);

    public Ghost()
    {

    }

    public Ghost(Texture texture, Vec position)
    {
        depth = -7;
        addSpriteFromExistingTexture(texture, depth, 0, position);

        Random r = ArrayPool.randomPool.get();
        frame = r.nextInt(3);
        activeFrames.add(textures.get(0).getFrame(frame));
        defaultFrames.add(frame);
        storeBitmap = true;

        x = position.y;
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

        fadeOutTimer = r.nextInt(100 - 10 + 1) + 10;
        ArrayPool.randomPool.recycle(r);
        fadeOut = false;
        fadeOutAlpha = 1;

        solid = false;
        unmovable = true;

        originalY = y;
    }

    public void init(Texture texture, Vec position)
    {
        depth = -7;
        addSpriteFromExistingTexture(texture, depth, 0, position);

        Random r = ArrayPool.randomPool.get();
        frame = r.nextInt(3 + 1);
        activeFrames.add(textures.get(0).getFrame(frame));
        defaultFrames.add(frame);
        storeBitmap = true;

        x = position.y;
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

        fadeOutTimer = r.nextInt(100 - 50 + 1) + 50;
        ArrayPool.randomPool.recycle(r);
        fadeOut = false;
        fadeOutAlpha = 1;

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


        if(fadeOutTimer <= 0 && !fadeOut)
        {
            fadeOut = true;
        }
        else
        {
            fadeOutTimer -= Time.getDelta();
        }

        if(fadedIn && fadeOut)
        {
            fadeOutAlpha -= Time.getDelta() * 0.075f;
            if(fadeOutAlpha <= 0)
            {
                fadeOutAlpha = 0;
                changeColourAlpha(0, fadeOutAlpha);
                remove();
                return;
            }
            else
            {
                changeColourAlpha(0, fadeOutAlpha);
            }
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
            body.move(body.getX(), body.getY() + (5 * getMovementDelta()), rotation);
            body.setY(body.getY() + (5 * getMovementDelta()));
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
        fadeOut = false;

        activeFrames.set(0, textures.get(0).getFrame(frame));
        defaultFrames.set(0, frame);

        Random r = ArrayPool.randomPool.get();
        fadeOutTimer = r.nextInt(100 - 10 + 1) + 10;
        ArrayPool.randomPool.recycle(r);
        fadeOutAlpha = 1;

        Ghost.pool.recycle(this);
    }
}
