package game.android.com.catstacks.engine.physics;

import java.util.Comparator;

import game.android.com.catstacks.engine.pool.GenericObjectPool;

public class ManifoldComparator implements Comparator
{
    public static final GenericObjectPool<ManifoldComparator> pool = new GenericObjectPool<ManifoldComparator>(ManifoldComparator.class);

    @Override
    public int compare(Object o1, Object o2)
    {
        Manifold collision1 = (Manifold) o1;
        Manifold collision2 = (Manifold) o2;
        return Float.compare(collision1.getEnterTime(), collision2.getEnterTime());
    }
}
