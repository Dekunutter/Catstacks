package game.android.com.catstacks.game.objects.dynamic;

import java.util.Random;

import game.android.com.catstacks.engine.core.Time;
import game.android.com.catstacks.engine.objects.WorldObject;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;

public class StackableObject extends WorldObject
{
    protected int stackPosition;
    protected StackableObject holding, heldBy;
    protected boolean caught, dangling;

    public StackableObject()
    {
        super();
    }

    public boolean isCaught()
    {
        return caught;
    }

    protected void setCaught(boolean value)
    {
        caught = value;
    }

    public float holdPositionY()
    {
        if(dangling)
        {
            return body.getPosition().y - holding.getHeight();
        }
        else
        {
            return body.getPosition().y + height;
        }
    }

    public void moveHeldItem()
    {
        if(isHolding())
        {
            holding.body.move(body.getPosition().x, holdPositionY(), rotation);
            holding.body.setX(body.getPosition().x);
            holding.body.setY(holdPositionY());

            holding.moveHeldItem();
        }
    }

    public boolean isDangling()
    {
        return dangling;
    }

    public int getStackPosition()
    {
        return stackPosition;
    }

    protected void setStackPosition(int value)
    {
        stackPosition = value;
    }

    public void setCaught(StackableObject holder)
    {
        setHolder(holder);
    }

    public void setHolder(StackableObject holder)
    {
        heldBy = holder;
        setCaught(true);

        setStackPosition(heldBy.getStackPosition() + 1);
        heldBy.holdItem(this);
        heldBy.incrementStack();
    }

    public StackableObject swapHolder(StackableObject newHolder)
    {
        dangling = true;
        disableCollision();

        setStackPosition(newHolder.getStackPosition() + 1);
        StackableObject newHolding = heldBy.swapHolder(this);

        holding = newHolding;
        heldBy = newHolder;
        setCaught(true);

        heldBy.holdItem(this);
        return this;
    }

    public void incrementStack()
    {
        heldBy.incrementStack();
    }

    public boolean isHolding()
    {
        if(holding == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public void holdItem(StackableObject item)
    {
        holding = item;
    }

    public void depositItem(WorldObject depo)
    {
        heldBy = null;

        if(isHolding())
        {
            holding.depositItem(depo);
            holding = null;
        }
    }

    public int getMaxStackSize()
    {
        return heldBy.getMaxStackSize();
    }

    public void despawnHeldItem()
    {
        if(isHolding())
        {
            holding.despawnHeldItem();
        }
        remove();
    }

    public void dropTopItem()
    {
        if(isHolding())
        {
            if(!holding.isHolding())
            {
                holding.leaveStack();
                holding.setCaught(false);
                holding = null;
            }
            else
            {
                holding.dropTopItem();
            }
        }
    }

    protected void leaveStack()
    {
        Random r = ArrayPool.randomPool.get();
        //Vec movement = new Vec((float) r.nextInt(40 - (-40) + 1) - 40, 50);
        Vec movement = (Vec)Vec.pool.get();
        movement.x = (float) r.nextInt(40 - (-40) + 1) - 40;
        movement.y = 50;
        //body.setDisplacement(new Vec(body.getDisplacement().x + (movement.x * Time.getDelta()), body.getDisplacement().y + (movement.y * Time.getDelta())));
        body.setDisplacement(body.getDisplacementX() + (movement.x * getMovementDeltaX()), body.getDisplacementY() + (movement.y * getMovementDeltaY()));

        ArrayPool.randomPool.recycle(r);
        Vec.pool.recycle(movement);
    }
}
