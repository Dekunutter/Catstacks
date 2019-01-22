package game.android.com.catstacks.engine.physics;

import game.android.com.catstacks.engine.pool.GenericObjectPool;

public class Edge
{
    private Vec dir, point1, point2;

    private Vec normal;
    private float distanceToOrigin;
    private int indexOfClosestPoint;

    private Vec max;

    public static final GenericObjectPool<Edge> pool = new GenericObjectPool<Edge>(Edge.class);

    public Edge()
    {

    }

    public void init(Vec point1, Vec point2)
    {
        //dir = Vec.subtract(point2, point1);

        dir = Vec.pool.get();
        //dir.add(point2);
        dir.x = point2.x;
        dir.y = point2.y;
        dir.subtract(point1);

        //dir = point2.subtract(point1);
        this.point1 = point1;
        this.point2 = point2;
    }

    public Edge(Edge edge)
    {
        //dir = edge.getPoint2().subtract(edge.getPoint1());
        dir = Vec.pool.get();
        dir.x = edge.getPoint2().x;
        dir.y = edge.getPoint2().y;
        dir.subtract(edge.getPoint1());

        point1 = edge.getPoint1();
        point2 = edge.getPoint2();
        max = edge.getMax();
    }

    public Edge(Vec point1, Vec point2)
    {
        //dir = point2.subtract(point1);
        dir = Vec.pool.get();
        dir.x = point2.x;
        dir.y = point2.y;
        dir.subtract(point1);

        this.point1 = point1;
        this.point2 = point2;
    }

    public Edge(Vec point1, Vec point2, Vec max)
    {
        this(point1, point2);
        this.max = max;
    }

    public void reinit(Vec point1, Vec point2)
    {
        //dir = point2.subtract(point1);
        dir.x = point2.x - point1.x;
        dir.y = point2.y - point1.y;

        this.point1 = point1;
        this.point2 = point2;
    }

    public Vec getDirection()
    {
        //return dir;
        dir.x = point2.x - point1.x;
        dir.y = point2.y - point1.y;
        return dir;
    }

    public Vec getPoint1()
    {
        return point1;
    }

    public Vec getPoint2()
    {
        return point2;
    }

    public void setNormal(Vec value)
    {
        normal = new Vec(value.x, value.y);
    }

    public Vec getNormal()
    {
        return normal;
    }

    public void setDistanceToOrigin(float value)
    {
        distanceToOrigin = value;
    }

    public float getDistanceToOrigin()
    {
        return distanceToOrigin;
    }

    public void setIndexOfClosestPoint(int value)
    {
        indexOfClosestPoint = value;
    }

    public int getIndexOfClosestPoint()
    {
        return indexOfClosestPoint;
    }

    public void setMax(Vec value)
    {
        max = value;
    }

    public Vec getMax()
    {
        return max;
    }

    public void destruct()
    {
        Vec.pool.recycle(dir);
    }
}
