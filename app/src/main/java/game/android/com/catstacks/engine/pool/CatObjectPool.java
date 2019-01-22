package game.android.com.catstacks.engine.pool;

import java.util.ArrayList;
import game.android.com.catstacks.game.objects.dynamic.Cat;

public class CatObjectPool<Type>
{
    protected final ArrayList<Type> freeCats;

    public CatObjectPool()
    {
        this.freeCats = new ArrayList<Type>();
    }

    public Type get(int index)
    {
        synchronized(this)
        {
            return freeCats.remove(index);
        }
    }

    public void recycle(final Type newCat)
    {
        synchronized(this)
        {
            freeCats.add(newCat);
        }
    }

    public int size()
    {
        synchronized(this)
        {
            return freeCats.size();
        }
    }
}
