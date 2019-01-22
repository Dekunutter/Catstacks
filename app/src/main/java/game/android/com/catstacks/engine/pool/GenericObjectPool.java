package game.android.com.catstacks.engine.pool;

import android.util.Log;

import java.util.Stack;

import game.android.com.catstacks.game.objects.statics.backgrounds.Sky;

public class GenericObjectPool<Type> implements Pool<Type>
{
    protected final Stack<Type> freeObjects;
    protected PoolObjectFactory<Type> factory;
    protected final Class<Type> classOfType;

    public GenericObjectPool(Class<Type> classOfType)
    {
        this.classOfType = classOfType;
        freeObjects = new Stack<Type>();
    }

    public void recycle(final Type data)
    {
        synchronized(this)
        {
            freeObjects.push(data);
        }
    }

    public Type get()
    {
        synchronized(this)
        {
            if(freeObjects.isEmpty())
            {
                return newType();
                //return factory.newObject();
            }

            return freeObjects.pop();
        }
    }

    public void setFactory(final PoolObjectFactory<Type> factory)
    {
        this.factory = factory;
    }

    public void reset()
    {
        synchronized(this)
        {
            freeObjects.clear();
        }
    }

    private final Type newType()
    {
        try
        {
            return classOfType.newInstance();
        }
        catch(Exception e)
        {
            Log.println(Log.ERROR, "object pool", "Exception thrown when creating new object instance: " + e.getMessage());
        }
        return null;
    }

    public int size()
    {
        synchronized(this)
        {
            return freeObjects.size();
        }
    }

    public synchronized String debug()
    {
        return "Current Pool Size: " + freeObjects.size();
    }
}