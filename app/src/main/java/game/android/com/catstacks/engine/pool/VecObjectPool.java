package game.android.com.catstacks.engine.pool;

import game.android.com.catstacks.engine.physics.Vec;

public class VecObjectPool extends GenericObjectPool<Vec>
{
    public VecObjectPool()
    {
        super(Vec.class);
    }

    @Override
    public Vec get()
    {
        return super.get();
    }

    @Override
    public void recycle(Vec data)
    {
        data.x = 0;
        data.y = 0;
        freeObjects.push(data);
    }
}
