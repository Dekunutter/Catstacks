package game.android.com.catstacks.engine.pool;

import game.android.com.catstacks.engine.physics.Pair;
import game.android.com.catstacks.engine.pool.PoolObjectFactory;

public class PairFactory implements PoolObjectFactory<Pair>
{
    public Pair newObject()
    {
        return new Pair();
    }
}
