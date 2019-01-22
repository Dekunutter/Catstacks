package game.android.com.catstacks.engine.pool;

import java.util.Stack;

public class ObjectPool<Type> implements Pool<Type>
{
    protected final Stack<Type> freeObjects;
    protected PoolObjectFactory<Type> factory;

    public ObjectPool()
    {
        freeObjects = new Stack<Type>();
    }

    public void recycle(final Type data)
    {
        freeObjects.push(data);
    }

    public Type get()
    {
        if(freeObjects.isEmpty())
        {
            return factory.newObject();
        }

        return freeObjects.pop();
    }

    public void setFactory(final PoolObjectFactory<Type> factory)
    {
        this.factory = factory;
    }

    public void reset()
    {
        freeObjects.clear();
    }

    public String debug()
    {
        return "Current Pool Size: " + freeObjects.size();
    }
}
