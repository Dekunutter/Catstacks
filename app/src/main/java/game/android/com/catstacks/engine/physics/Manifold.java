package game.android.com.catstacks.engine.physics;

import android.util.Log;

import game.android.com.catstacks.engine.objects.WorldObject;
import game.android.com.catstacks.engine.pool.GenericObjectPool;

public class Manifold
{
    private Polygon colliderA, colliderB;
    private boolean collided, overlapped, solid;
    private Vec enterNormal, discreteEnterNormal, deltaVel, mtd;
    private float enterTime, leaveTime, discreteEnterTime, mtdLengthSquared;

    public static final GenericObjectPool<Manifold> pool = new GenericObjectPool<Manifold>(Manifold.class);
    public static final GenericObjectPool<Vec> vecPool = new GenericObjectPool<Vec>(Vec.class);

    public Manifold()
    {

    }

    public Manifold(Polygon colliderA, Polygon colliderB)
    {
        this.colliderA = colliderA;
        this.colliderB = colliderB;

        collided = false;
        overlapped = false;
        solid = true;

        enterNormal = Vec.pool.get();
        discreteEnterNormal = Vec.pool.get();

        enterTime = -1;
        leaveTime = Float.MAX_VALUE;
        discreteEnterTime = 1;

        mtdLengthSquared = -1;

        deltaVel = Vec.pool.get();
        mtd = Vec.pool.get();
    }

    public void init()
    {
        collided = false;
        overlapped = false;
        solid = true;

        //enterNormal = new Vec();
        //discreteEnterNormal = new Vec();
        enterNormal = vecPool.get();
        discreteEnterNormal = vecPool.get();

        enterTime = -1;
        leaveTime = Float.MAX_VALUE;
        discreteEnterTime = 1;

        mtdLengthSquared = -1;

        //deltaVel = new Vec();
        //mtd = new Vec();
        deltaVel = vecPool.get();
        mtd = vecPool.get();
    }

    public void destruct()
    {
        enterNormal.x = 0;
        enterNormal.y = 0;
        discreteEnterNormal.x = 0;
        discreteEnterNormal.y = 0;
        deltaVel.x = 0;
        deltaVel.y = 0;
        mtd.x = 0;
        mtd.y = 0;

        collided = false;
        overlapped = false;

        vecPool.recycle(enterNormal);
        vecPool.recycle(discreteEnterNormal);
        vecPool.recycle(deltaVel);
        vecPool.recycle(mtd);
    }

    public Polygon getColliderA()
    {
        return colliderA;
    }

    public void setColliderA(Polygon object)
    {
        colliderA = object;
    }

    public Polygon getColliderB()
    {
        return colliderB;
    }

    public void setColliderB(Polygon object)
    {
        colliderB = object;
    }

    public boolean isCollided()
    {
        return collided;
    }

    public void setCollided(boolean value)
    {
        collided = value;
    }

    public boolean isOverlapped()
    {
        return overlapped;
    }

    public void setOverlapped(boolean value)
    {
        overlapped = value;
    }

    public boolean isSolid()
    {
        return solid;
    }

    public void setSolid(boolean value)
    {
        solid = value;
    }

    public Vec getEnterNormal()
    {
        return enterNormal;
    }

    public void setEnterNormal(Vec value)
    {
        //enterNormal = new Vec(value);
        enterNormal.x = value.x;
        enterNormal.y = value.y;
    }

    public Vec getDiscreteEnterNormal()
    {
        return discreteEnterNormal;
    }

    public void setDiscreteEnterNormal(Vec value)
    {
        //discreteEnterNormal = new Vec(value);
        discreteEnterNormal.x = value.x;
        discreteEnterNormal.y = value.y;
    }

    public float getEnterTime()
    {
        return enterTime;
    }

    public void setEnterTime(float value)
    {
        enterTime = value;
    }

    public float getLeaveTime()
    {
        return leaveTime;
    }

    public void setLeaveTime(float value)
    {
        leaveTime = value;
    }

    public float getDiscreteEnterTime()
    {
        return discreteEnterTime;
    }

    public void setDiscreteEnterTime(float value)
    {
        discreteEnterTime = value;
    }

    public Vec getDeltaVelocity()
    {
        return deltaVel;
    }

    public void setDeltaVelocity(Vec value)
    {
        //deltaVel = new Vec(value);
        deltaVel.x = value.x;
        deltaVel.y = value.y;
    }

    public Vec getMTD()
    {
        return mtd;
    }

    public void setMTD(Vec value)
    {
       // mtd = new Vec(value);
        mtd.x = value.x;
        mtd.y = value.y;
    }
}
