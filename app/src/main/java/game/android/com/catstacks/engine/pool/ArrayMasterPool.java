package game.android.com.catstacks.engine.pool;

import java.util.ArrayList;

public class ArrayMasterPool<Object>
{
    private final ArrayList<ArrayObjectPool> pools;
    private final Class<Object> classOfType;

    public ArrayMasterPool(Class<Object> classOfType)
    {
        this.classOfType = classOfType;
        pools = new ArrayList<ArrayObjectPool>();
    }

    public void recycle(final Object data, int size)
    {
        synchronized(this)
        {
            for(int i = 0; i < pools.size(); i++)
            {
                if(pools.get(i).getArraySize() == size)
                {
                    pools.get(i).recycle(data);
                    break;
                }
            }
        }
    }

    public Object get(int size)
    {
        synchronized(this)
        {
            for(int i = 0; i < pools.size(); i++)
            {
                if(pools.get(i).getArraySize() == size)
                {
                    return (Object) pools.get(i).get(size);
                }
            }

            pools.add(new ArrayObjectPool(classOfType, size));
            return (Object) pools.get(pools.size() - 1).newType(size);
        }
    }

    public void reset()
    {
        synchronized(this)
        {
            for(int i = 0; i < pools.size(); i++)
            {
                pools.get(i).reset();
            }
            pools.clear();
        }
    }

    public synchronized String debug(int size)
    {
        for(int i = 0; i < pools.size(); i++)
        {
            if(pools.get(i).getArraySize() == size)
            {
                return pools.get(i).debug();
            }
        }
        return "Empty";
    }
}
