package game.android.com.catstacks.engine.physics;

import game.android.com.catstacks.engine.pool.GenericObjectPool;

public class AABB
{
    private Vec lowerBound;
    private float width, height;

    public static final GenericObjectPool<AABB> pool = new GenericObjectPool<AABB>(AABB.class);

    public AABB()
    {

    }

    public AABB(Vec lowerBound)
    {
        this.lowerBound = lowerBound;
        width = 0;
        height = 0;
    }

    public void init(Vec lowerBound)
    {
        this.lowerBound = lowerBound;
        width = 0;
        height = 0;
    }

    public Vec getLowerBound()
    {
        return lowerBound;
    }

    public float getX()
    {
        return lowerBound.x;
    }

    public float getY()
    {
        return lowerBound.y;
    }

    public float getWidth()
    {
        return width;
    }

    public float getHeight()
    {
        return height;
    }

    public void setBounds(Vec newBound)
    {
        if(newBound.x < lowerBound.x && newBound.y < lowerBound.y)
        {
            lowerBound = newBound;
        }
        else
        {
            if(newBound.x > lowerBound.x)
            {
                width = newBound.x - lowerBound.x;
            }
            if(newBound.y > lowerBound.y)
            {
                height = newBound.y - lowerBound.y;
            }
        }
    }

    public void setBounds(float x, float y)
    {
        if(x < lowerBound.x)
        {
            lowerBound.x = x;
        }
        else if(x > width)
        {
            width = x;
        }

        if(y < lowerBound.y)
        {
            lowerBound.y = y;
        }
        else if(y > height)
        {
            height = y;
        }
    }

    public void destruct()
    {
        lowerBound = null;
        width = 0;
        height = 0;
    }

    //since Polygon objects are passing in a REFERENCE to the vertex as the AABB lower bound,
    //but Quadtree needs to create its OWN vertex that is REFERENCED as the lower bound,
    //we want to make sure to recycle the corresponding vertex in the Quadtree AABB,
    //but not do that in the Polygon AABB (instead setting the reference to null)
    public void destructWithVec()
    {
        Vec.pool.recycle(lowerBound);
        width = 0;
        height = 0;
    }

    @Override
    public String toString()
    {
        //return "((" + x + ", " + y + ")(" + width + ", " + height + "))";
        return "(" + lowerBound.toString() + "(" + width + ", " + height + "))";
    }
}
