package game.android.com.catstacks.engine.pool;

import android.util.Log;

import java.lang.reflect.Array;

public class ArrayObjectPool<Object> extends GenericObjectPool<Object>
{
    private int size;

    public ArrayObjectPool(Class<Object> classOfType, int size)
    {
        super(classOfType);
        this.size = size;
    }

    public Object get(int size)
    {
        synchronized(this)
        {
            if(freeObjects.isEmpty())
            {
                return newType(size);
            }

            return freeObjects.pop();
        }
    }

    public final Object newType(int size)
    {
        synchronized(this)
        {
            try
            {
                return (Object) Array.newInstance(classOfType.getComponentType(), size);
            } catch(Exception e)
            {
                Log.println(Log.ERROR, "object pool", "Exception thrown when creating new object instance: " + e.getMessage());
            }
            return null;
        }
    }

    public synchronized int getArraySize()
    {
        return size;
    }
}
