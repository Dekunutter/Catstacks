package game.android.com.catstacks.engine.physics;

import game.android.com.catstacks.engine.objects.WorldObject;
import game.android.com.catstacks.engine.pool.GenericObjectPool;

public class Pair
{
    private WorldObject objectA, objectB;

    public static final GenericObjectPool<Pair> pool = new GenericObjectPool<Pair>(Pair.class);

    public Pair()
    {

    }

    public Pair(WorldObject objectA, WorldObject objectB)
    {
        this.objectA = objectA;
        this.objectB = objectB;
    }

    public WorldObject getObjectA()
    {
        return objectA;
    }

    public void setObjectA(WorldObject object)
    {
        objectA = object;
    }

    public WorldObject getObjectB()
    {
        return objectB;
    }

    public void setObjectB(WorldObject object)
    {
        objectB = object;
    }

    @Override
    public String toString()
    {
        return objectA + " - " + objectB;
    }

    @Override
    public boolean equals(Object object)
    {
        if((object != null) && (object instanceof Pair))
        {
            Pair com = (Pair)object;
            if(((objectA == com.objectA) && (objectB == com.objectB)) || ((objectB == com.objectA) && (objectA == com.objectB)))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        int result = 17;
        result = 31 * result + objectA.hashCode();
        result = 31 * result + objectB.hashCode();
        return result;
    }

    public void destruct()
    {
        objectA = null;
        objectB = null;
    }
}
