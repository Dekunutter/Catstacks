package game.android.com.catstacks.engine.objects;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import game.android.com.catstacks.engine.core.Screen;
import game.android.com.catstacks.engine.gameloop.GameLoop;
import game.android.com.catstacks.engine.input.InputBuffer;
import game.android.com.catstacks.engine.core.Time;
import game.android.com.catstacks.engine.physics.CollisionBitmasks;
import game.android.com.catstacks.engine.physics.Polygon;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.GenericObjectPool;
import game.android.com.catstacks.engine.states.World;

public abstract class WorldObject extends RenderedObject
{
    protected float rotation;
    protected Polygon body;
    protected boolean unmovable, solid = true, removed;
    protected int mask = CollisionBitmasks.DEFAULT_ID;

    public static final GenericObjectPool<WorldObject> pool = new GenericObjectPool<WorldObject>(WorldObject.class);

    public WorldObject()
    {
        super();
    }

    @Override
    public void getInput(InputBuffer input)
    {

    }

    public void applyMovement()
    {

    }

    @Override
    public void update()                                                        //update the object's logic
    {
        if(!unmovable)
        {
            body.move(body.getPosition().x + body.getDisplacementX(), body.getPosition().y + body.getDisplacementY(), rotation);
            body.setX(body.getPosition().x + body.getDisplacementX());
            body.setY(body.getPosition().y + body.getDisplacementY());
        }
    }

    public void setUnmovable(boolean value)
    {
        unmovable = value;
    }

    public boolean isUnmovable()
    {
        return unmovable;
    }

    public Vec getDisplacement()
    {
        //return new Vec(body.getDisplacement());
        return body.getDisplacement();
    }

    public float getDisplacementX()
    {
        return body.getDisplacementX();
    }

    public float getDisplacementY()
    {
        return body.getDisplacementY();
    }

    public Polygon getBody()
    {
        return body;
    }

    public Vec getPosition()
    {
        return body.getPosition();
    }

    public void applyGravity(Vec gravity)
    {
        if(!unmovable)
        {
            float currentX = body.getDisplacementX();
            float currentY = body.getDisplacementY();
            //body.setDisplacement(new Vec(currentX + (gravity.x * Time.getDelta()), Math.max(gravity.y * Time.getDelta(), currentY + (gravity.y * Time.getDelta()))));
            body.setDisplacement(currentX + (gravity.x * getMovementDeltaX()), Math.max(gravity.y * getMovementDeltaY(), currentY + (gravity.y * getMovementDeltaY())));
        }
    }

    public void applyFriction()
    {
        if(!unmovable)
        {
            if(body.getDisplacementX() > 0)
            {
                //body.setDisplacement(new Vec(body.getDisplacementX() - (1.5f * Time.getDelta()), body.getDisplacementY()));
                body.setDisplacement(body.getDisplacementX() - (1.5f * getMovementDeltaX()), body.getDisplacementY());
                if(body.getDisplacementX() <= 0)
                {
                    //body.setDisplacement(new Vec(0, body.getDisplacementY()));
                    body.setDisplacement(0, body.getDisplacementY());
                }
            } else if(body.getDisplacementX() < 0)
            {
                //body.setDisplacement(new Vec(body.getDisplacementX() + (1.5f * Time.getDelta()), body.getDisplacementY()));
                body.setDisplacement(body.getDisplacementX() + (1.5f * getMovementDeltaX()), body.getDisplacementY());
                if(body.getDisplacementX() >= 0)
                {
                    //body.setDisplacement(new Vec(0, body.getDisplacementY()));
                    body.setDisplacement(0, body.getDisplacementY());
                }
            }
        }
    }

    public boolean getRemove()
    {
        return removed;
    }

    public void remove()
    {
        removed = true;
    }

    public int getMask()
    {
        return mask;
    }

    public float getWidth()
    {
        return width;
    }

    public float getHeight()
    {
        return height;
    }

    public boolean isCollidable()
    {
        if(body == null || removed)
        {
            return false;
        }
        return solid;
    }

    public void disableCollision()
    {
        solid = false;
    }

    public void destruct()
    {
        super.destruct();
        if(body != null)
        {
            body.destruct();
            Polygon.pool.recycle(body);
        }

        removed = false;
    }

    public float getMovementDelta()
    {
        return Time.getDelta();
    }

    public float getMovementDeltaX()
    {
        return (Time.getDelta() * ((float)(Screen.screen.getWidth() / 360f)));
        //return Time.getDelta();
    }

    public float getMovementDeltaY()
    {
        return (Time.getDelta() * ((float)(Screen.screen.getHeight() / 592f)));
        //return Time.getDelta();
    }
}
