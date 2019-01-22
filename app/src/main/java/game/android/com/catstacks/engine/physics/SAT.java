package game.android.com.catstacks.engine.physics;

import android.util.Log;

import game.android.com.catstacks.engine.pool.GenericObjectPool;
import game.android.com.catstacks.engine.pool.Pool;

public class SAT
{
    public static Manifold checkCollisions(Polygon bodyA, Polygon bodyB, Vec deltaVel, Vec offset)
    {
        //Manifold result = new Manifold(bodyA, bodyB);
        Manifold result = Manifold.pool.get();
        result.setColliderA(bodyA);
        result.setColliderB(bodyB);
        result.init();

        for(int i = 0; i < bodyA.getEdgeCount(); i++)
        {
            Vec axis = Vec.pool.get();
            axis.x = bodyA.getEdges().get(i).getDirection().x;
            axis.y = bodyA.getEdges().get(i).getDirection().y;
            axis.perp();
            //Vec axis = bodyA.getEdges().get(i).getDirection();
            //axis = axis.perp();

            float[] projectionA = bodyA.getProjection(axis);
            float[] projectionB = bodyB.getProjection(axis);

            if(isSeparated(projectionA, projectionB, axis, deltaVel, offset, result))
            {
                Vec.pool.recycle(axis);
                return result;
            }
            Vec.pool.recycle(axis);
        }

        for(int i = 0; i < bodyB.getEdgeCount(); i++)
        {
            Vec axis = Vec.pool.get();
            axis.x = bodyB.getEdges().get(i).getDirection().x;
            axis.y = bodyB.getEdges().get(i).getDirection().y;
            axis.perp();
            //Vec axis = bodyB.getEdges().get(i).getDirection();
            //axis = axis.perp();

            float[] projectionA = bodyA.getProjection(axis);
            float[] projectionB = bodyB.getProjection(axis);

            if(isSeparated(projectionA, projectionB, axis, deltaVel, offset, result))
            {
                Vec.pool.recycle(axis);
                return result;
            }
            Vec.pool.recycle(axis);
        }

        if(result.getEnterNormal().dotProd(offset) > 0)
        {
            Vec invertedNormal = Vec.pool.get();
            invertedNormal.x = result.getEnterNormal().x;
            invertedNormal.y = result.getEnterNormal().y;
            invertedNormal.invert();
            result.setEnterNormal(invertedNormal);
            Vec.pool.recycle(invertedNormal);
            //result.setEnterNormal(result.getEnterNormal().invert());
        }

        if(result.getDiscreteEnterNormal().dotProd(offset) > 0)
        {
            Vec invertedNormal = Vec.pool.get();
            invertedNormal.x = result.getDiscreteEnterNormal().x;
            invertedNormal.y = result.getDiscreteEnterNormal().y;
            invertedNormal.invert();
            result.setDiscreteEnterNormal(invertedNormal);
            Vec.pool.recycle(invertedNormal);
            //result.setDiscreteEnterNormal(result.getDiscreteEnterNormal().invert());
        }

        Vec normalizedNormal = Vec.pool.get();
        normalizedNormal.x = result.getEnterNormal().x;
        normalizedNormal.y = result.getEnterNormal().y;
        normalizedNormal.normalize();
        result.setEnterNormal(normalizedNormal);
        Vec.pool.recycle(normalizedNormal);
        //result.setEnterNormal(result.getEnterNormal().normalize());
        Vec normalizedDNormal = Vec.pool.get();
        normalizedDNormal.x = result.getDiscreteEnterNormal().x;
        normalizedDNormal.y = result.getDiscreteEnterNormal().y;
        normalizedDNormal.normalize();
        result.setDiscreteEnterNormal(normalizedDNormal);
        Vec.pool.recycle(normalizedDNormal);
        //result.setDiscreteEnterNormal(result.getDiscreteEnterNormal().normalize());

        if(result.getEnterTime() < 0 || result.getEnterTime() > 1)
        {
            result.setOverlapped(true);
        }
        else
        {
            result.setCollided(true);
        }

        result.setDeltaVelocity(deltaVel);
        Vec mtd = Vec.pool.get();
        mtd.x = result.getDiscreteEnterNormal().x;
        mtd.y = result.getDiscreteEnterNormal().y;
        mtd.multiply(result.getDiscreteEnterTime());
        result.setMTD(mtd);
        Vec.pool.recycle(mtd);
        //result.setMTD(result.getDiscreteEnterNormal().multiply(result.getDiscreteEnterTime()));

        return result;
    }

    private static boolean isSeparated(float[] projectionA, float[] projectionB, Vec normal, Vec deltaVel, Vec offset, Manifold currentManifold)
    {
        float h = offset.dotProd(normal);
        float v = deltaVel.dotProd(normal);

        projectionA[0] += h;
        projectionA[1] += h;

        float d0 = (projectionA[0] - projectionB[1]);
        float d1 = (projectionB[0] - projectionA[1]);

        if(d0 >= 0.0f || d1 > 0.0f)
        {
            if(Math.abs(v) < 0.00001f)
            {
                return true;
            }

            float enterTime = -d0 / v;
            float leaveTime = d1 / v;

            if(enterTime > leaveTime)
            {
                float temp = enterTime;
                enterTime = leaveTime;
                leaveTime = temp;
            }

            if(enterTime < 0.0f || enterTime >= 1)
            {
                return true;
            }

            if(enterTime > currentManifold.getEnterTime())
            {
                currentManifold.setEnterTime(enterTime);
                currentManifold.setEnterNormal(normal);
            }

            if(leaveTime < currentManifold.getLeaveTime())
            {
                currentManifold.setLeaveTime(leaveTime);
            }

            return false;
        }
        else
        {
            float taxis = d0 > d1 ? d0 : d1;
            float n = normal.length();
            taxis /= n;

            if(taxis > currentManifold.getDiscreteEnterTime() || currentManifold.getDiscreteEnterTime() == 1.0f)
            {
                currentManifold.setDiscreteEnterNormal(normal);
                currentManifold.setDiscreteEnterTime(taxis);
            }
            return false;
        }
    }
}
