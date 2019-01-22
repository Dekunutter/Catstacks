package game.android.com.catstacks.engine.physics;

import java.util.Comparator;

import game.android.com.catstacks.engine.pool.GenericObjectPool;

public class ManifoldDiscreteComparator implements Comparator
{
    public static final GenericObjectPool<ManifoldDiscreteComparator> pool = new GenericObjectPool<ManifoldDiscreteComparator>(ManifoldDiscreteComparator.class);

    @Override
    public int compare(Object o1, Object o2)
    {
        Manifold collision1 = (Manifold) o1;
        Manifold collision2 = (Manifold) o2;
        return Float.compare(collision1.getDiscreteEnterTime(), collision2.getDiscreteEnterTime());
    }
}
