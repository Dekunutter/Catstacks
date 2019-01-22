package game.android.com.catstacks.engine.physics;

import android.util.Log;

import game.android.com.catstacks.engine.physics.Manifold;
import game.android.com.catstacks.engine.physics.Polygon;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;

public class CollisionResolver
{
    public static void solveCollision(Manifold manifold)
    {
        if(!manifold.isSolid())
        {
            //NOTE: instead of just a return here, I could handle the special logic of the player/cat collisions if I had a means of distinguishing it from others
            return;
        }

        if(manifold.isCollided())
        {
            //Log.println(Log.ERROR, "collisions", "collided " + manifold.getEnterTime());
            solveImpulses(manifold);
        }
        else if(manifold.isOverlapped())
        {
            //Log.println(Log.ERROR, "collisions", "overlapped " + manifold.getDiscreteEnterTime());
            rectifyPositions(manifold);
        }
    }

    public static void solveImpulses(Manifold manifold)
    {
        Polygon colliderA = manifold.getColliderA();
        Polygon colliderB = manifold.getColliderB();

        float n = manifold.getDeltaVelocity().dotProd(manifold.getEnterNormal());
        //Vec velN = manifold.getEnterNormal().multiply(n);
        Vec velN = Vec.pool.get();
        velN.x = manifold.getEnterNormal().x;
        velN.y = manifold.getEnterNormal().y;
        velN.multiply(n);
        //Vec velT = manifold.getDeltaVelocity().subtract(velN);
        Vec velT = Vec.pool.get();
        velT.x = manifold.getDeltaVelocity().x;
        velT.y = manifold.getDeltaVelocity().y;
        velT.subtract(velN);

        float friction = 0.0f;
        float elasticity = 0.0f;
        if(velT.length() < 0.00001f)
        {
            //
        }

        //velN = velN.multiply(- 1 + elasticity);
        velN.multiply(-1 + elasticity);
        //velT = velT.multiply(- friction);
        velT.multiply(-friction);

        //Vec velFinal = velT.add(velN);
        Vec velFinal = Vec.pool.get();
        velFinal.x = velT.x;
        velFinal.y = velT.y;
        velFinal.add(velN);

        float[] proportions = getMassProportions(colliderA.getInverseMass(), colliderB.getInverseMass());

        //velFinal = velFinal.add(manifold.getDeltaVelocity().multiply(manifold.getEnterTime()).multiply(manifold.getEnterNormal().abs()));
        Vec absNormal = Vec.pool.get();
        absNormal.x = manifold.getEnterNormal().x;
        absNormal.y = manifold.getEnterNormal().y;
        absNormal.abs();
        Vec deltaTime = Vec.pool.get();
        deltaTime.x = manifold.getDeltaVelocity().x;
        deltaTime.y = manifold.getDeltaVelocity().y;
        deltaTime.multiply(manifold.getEnterTime());
        velFinal.add(deltaTime);
        velFinal.multiply(absNormal);

        //colliderA.setDisplacement(colliderA.getDisplacement().add(velFinal.multiply(proportions[0])));
        Vec finalA = Vec.pool.get();
        finalA.x = velFinal.x;
        finalA.y = velFinal.y;
        finalA.multiply(proportions[0]);
        Vec newDisplacementA = Vec.pool.get();
        newDisplacementA.x = colliderA.getDisplacementX();
        newDisplacementA.y = colliderA.getDisplacementY();
        newDisplacementA.add(finalA);
        colliderA.setDisplacement(newDisplacementA);
        //colliderB.setDisplacement(colliderB.getDisplacement().subtract(velFinal.multiply(proportions[1])));
        Vec finalB = Vec.pool.get();
        finalB.x = velFinal.x;
        finalB.y = velFinal.y;
        finalB.multiply(proportions[1]);
        Vec newDisplacementB = Vec.pool.get();
        newDisplacementB.x = colliderB.getDisplacementX();
        newDisplacementB.y = colliderB.getDisplacementY();
        newDisplacementB.subtract(finalB);
        colliderB.setDisplacement(newDisplacementB);

        Vec.pool.recycle(newDisplacementB);
        Vec.pool.recycle(finalB);
        Vec.pool.recycle(newDisplacementA);
        Vec.pool.recycle(finalA);
        Vec.pool.recycle(deltaTime);
        Vec.pool.recycle(absNormal);
        ArrayPool.floatMaster.recycle(proportions, proportions.length);
        Vec.pool.recycle(velFinal);
        Vec.pool.recycle(velT);
        Vec.pool.recycle(velN);
    }

    public static void rectifyPositions(Manifold manifold)
    {
        Polygon colliderA = manifold.getColliderA();
        Polygon colliderB = manifold.getColliderB();

        float[] proportions = getMassProportions(colliderA.getInverseMass(), colliderB.getInverseMass());
        //colliderA.setPosition(colliderA.getPosition().add(manifold.getMTD().multiply(proportions[0])));
        Vec mtdA = Vec.pool.get();
        mtdA.x = manifold.getMTD().x;
        mtdA.y = manifold.getMTD().y;
        mtdA.multiply(proportions[0]);
        Vec newPositionA = Vec.pool.get();
        newPositionA.x = colliderA.getX();
        newPositionA.y = colliderA.getY();
        newPositionA.add(mtdA);
        colliderA.setPosition(newPositionA);
        //colliderB.setPosition(colliderB.getPosition().subtract(manifold.getMTD().multiply(proportions[1])));
        Vec mtdB = Vec.pool.get();
        mtdB.x = manifold.getMTD().x;
        mtdB.y = manifold.getMTD().y;
        mtdB.multiply(proportions[1]);
        Vec newPositionB = Vec.pool.get();
        newPositionB.x = colliderB.getX();
        newPositionB.y = colliderB.getY();
        newPositionB.subtract(mtdB);
        colliderB.setPosition(newPositionB);

        Manifold newManifold = Manifold.pool.get();
        newManifold.init();
        newManifold.setColliderA(colliderA);
        newManifold.setColliderB(colliderB);
        //newManifold.setEnterNormal(manifold.getMTD().normalize());
        Vec normalizedMTD = Vec.pool.get();
        normalizedMTD.x = manifold.getMTD().x;
        normalizedMTD.y = manifold.getMTD().y;
        normalizedMTD.normalize();
        newManifold.setEnterNormal(normalizedMTD);
        newManifold.setEnterTime(0.0f);
        newManifold.setDeltaVelocity(manifold.getDeltaVelocity());

        solveImpulses(newManifold);

        Vec.pool.recycle(normalizedMTD);
        Vec.pool.recycle(newPositionB);
        Vec.pool.recycle(mtdB);
        Vec.pool.recycle(newPositionA);
        Vec.pool.recycle(mtdA);
        ArrayPool.floatMaster.recycle(proportions, proportions.length);
        newManifold.destruct();
        Manifold.pool.recycle(newManifold);
    }

    private static float[] getMassProportions(float invMassA, float invMassB)
    {
        float totalInvMass = invMassA + invMassB;

        float r0 = invMassA / totalInvMass;
        float r1 = invMassB / totalInvMass;

        float[] results = ArrayPool.floatMaster.get(2);
        results[0] = r0;
        results[1] = r1;
        return results;
    }
}
