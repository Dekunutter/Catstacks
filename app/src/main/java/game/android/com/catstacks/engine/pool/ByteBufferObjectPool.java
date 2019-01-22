package game.android.com.catstacks.engine.pool;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ByteBufferObjectPool
{
    protected final ArrayList<ByteBuffer> freeBuffers;

    public ByteBufferObjectPool()
    {
        this.freeBuffers = new ArrayList<ByteBuffer>();
    }

    public void recycle(final ByteBuffer data)
    {
        synchronized(this)
        {
            freeBuffers.add(data);
        }
    }

    public ByteBuffer get(int size)
    {
        synchronized(this)
        {
            for(int i = 0; i < freeBuffers.size(); i++)
            {
                if(freeBuffers.get(i).capacity() == size)
                {
                    return freeBuffers.remove(i);
                }
            }

            return ByteBuffer.allocateDirect(size);
        }
    }

    public void reset()
    {
        synchronized(this)
        {
            freeBuffers.clear();
        }
    }
}
