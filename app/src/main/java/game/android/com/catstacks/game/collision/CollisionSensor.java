package game.android.com.catstacks.game.collision;

import android.util.Log;

import game.android.com.catstacks.engine.objects.WorldObject;
import game.android.com.catstacks.engine.physics.AABB;
import game.android.com.catstacks.engine.physics.CollisionBitmasks;
import game.android.com.catstacks.engine.physics.Manifold;
import game.android.com.catstacks.engine.physics.Pair;
import game.android.com.catstacks.engine.physics.Polygon;
import game.android.com.catstacks.engine.physics.SAT;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.Pool;
import game.android.com.catstacks.engine.states.World;
import game.android.com.catstacks.game.objects.dynamic.BalloonCat;
import game.android.com.catstacks.game.objects.dynamic.Cat;
import game.android.com.catstacks.game.objects.dynamic.Player;
import game.android.com.catstacks.game.objects.dynamic.Rocket;
import game.android.com.catstacks.game.objects.statics.Floor;

public class CollisionSensor
{
    public static Manifold aabbCheck(WorldObject objectA, WorldObject objectB)
    {
        Polygon bodyA = objectA.getBody();
        Polygon bodyB = objectB.getBody();

        Manifold results = Manifold.pool.get();
        results.setColliderA(bodyA);
        results.setColliderB(bodyB);

        AABB a = bodyA.getAABB();
        AABB b = bodyB.getAABB();

        if(a.getX() > b.getWidth() || a.getY() > b.getHeight() || a.getWidth() < b.getX() || a.getHeight() < b.getY())
        {
            return null;
        }
        else
        {
            results.setOverlapped(true);
            return results;
        }
    }

    public static Manifold categorizeCollision(Pair pair)
    {
        Vec displacementVel = Vec.pool.get();
        displacementVel.x = pair.getObjectA().getDisplacementX();
        displacementVel.y = pair.getObjectA().getDisplacementY();
        Vec displacementB = Vec.pool.get();
        displacementB.x = pair.getObjectB().getDisplacementX();
        displacementB.y = pair.getObjectB().getDisplacementY();
        displacementVel.subtract(displacementB);

        Vec offset = Vec.pool.get();
        offset.x = pair.getObjectA().getPosition().x;
        offset.y = pair.getObjectA().getPosition().y;
        offset.subtract(pair.getObjectB().getPosition());

        Manifold results = SAT.checkCollisions(pair.getObjectA().getBody(), pair.getObjectB().getBody(), displacementVel, offset);

        Vec.pool.recycle(offset);
        Vec.pool.recycle(displacementB);
        Vec.pool.recycle(displacementVel);

        if(results.isCollided() || results.isOverlapped())
        {
            validateCollision(results, pair);
            return results;
        }
        else
        {
            results.destruct();
            Manifold.pool.recycle(results);
        }

        return null;
    }

    public static boolean isCollidable(Pair pair)
    {
        if(checkCollisionBits(CollisionBitmasks.CAT_ID, CollisionBitmasks.CAT_ID, pair.getObjectA().getMask(), pair.getObjectB().getMask()))
        {
            Cat cat = (Cat) pair.getObjectA();
            Cat cat2 = (Cat) pair.getObjectB();

            if(cat.isDeposited() || cat2.isDeposited())
            {
                return false;
            }

            if(cat.isCaught() && cat2.isCaught())
            {
                return false;
            }

            if(!(cat instanceof BalloonCat) && !(cat2 instanceof BalloonCat))
            {
                if(cat.isDangling() || cat2.isDangling())
                {
                    return false;
                }
            }

            if(!cat.isCaught() && !cat2.isCaught())
            {
                return false;
            }
            else if(cat.isCaught() || cat2.isCaught())
            {
                if(World.world.isGameOver())
                {
                    return false;
                }

                if(cat.isHolding() || cat2.isHolding())
                {
                    return false;
                }

                if(cat.getStackPosition() >= 10 && !(cat2 instanceof BalloonCat))
                {
                    return false;
                }
                else if(cat2.getStackPosition() >= 10 && !(cat instanceof BalloonCat))
                {
                    return false;
                }
            }
        }
        else if(checkCollisionBits(CollisionBitmasks.PLAYER_ID, CollisionBitmasks.CAT_ID, pair.getObjectA().getMask(), pair.getObjectB().getMask()))
        {
            Cat cat;
            Player player;
            if(pair.getObjectA() instanceof Cat)
            {
                cat = (Cat) pair.getObjectA();
                player = (Player) pair.getObjectB();
            }
            else
            {
                player = (Player) pair.getObjectA();
                cat = (Cat) pair.getObjectB();
            }

            if(World.world.isGameOver())
            {
                return false;
            }

            if(cat.isLanded())
            {
                return false;
            }
            else if(cat.isCaught())
            {
                return false;
            }
            else
            {
                if(player.isHolding())
                {
                    return false;
                }
            }
        }
        else if(checkCollisionBits(CollisionBitmasks.CAT_ID, CollisionBitmasks.ROCKET_ID, pair.getObjectA().getMask(), pair.getObjectB().getMask()))
        {
            Cat cat;
            if(pair.getObjectA() instanceof Cat)
            {
                cat = (Cat) pair.getObjectA();
            }
            else
            {
                cat = (Cat) pair.getObjectB();
            }

            if(!cat.isDeposited())
            {
                return false;
            }
        }
        else if(checkCollisionBits(CollisionBitmasks.CAT_ID, CollisionBitmasks.BARRIER_ID, pair.getObjectA().getMask(), pair.getObjectB().getMask()))
        {
            return false;
        }
        else if(checkCollisionBit(CollisionBitmasks.UI_ID, pair.getObjectA().getMask(), pair.getObjectB().getMask()))
        {
            return false;
        }
        return true;
    }

    private static void validateCollision(Manifold manifold, Pair pair)
    {
        if(checkCollisionBits(CollisionBitmasks.GROUND_ID, CollisionBitmasks.CAT_ID, pair.getObjectA().getMask(), pair.getObjectB().getMask()))
        {
            Cat cat;
            if(pair.getObjectA() instanceof Cat)
            {
                cat = (Cat) pair.getObjectA();
            }
            else
            {
                cat = (Cat) pair.getObjectB();
            }

            if(!cat.isLanded() && !cat.isCaught())
            {
                if(!World.world.isGameOver())
                {
                    World.world.subtractLife();
                }
                cat.setLanded(true);
                cat.loadLandingSprite();
            }
        }
        else if(checkCollisionBits(CollisionBitmasks.PLAYER_ID, CollisionBitmasks.CAT_ID, pair.getObjectA().getMask(), pair.getObjectB().getMask()))
        {
            Cat cat;
            Player player;
            if(pair.getObjectA() instanceof Cat)
            {
                cat = (Cat) pair.getObjectA();
                player = (Player) pair.getObjectB();
            }
            else
            {
                player = (Player) pair.getObjectA();
                cat = (Cat) pair.getObjectB();
            }

            if(cat.isLanded())
            {
                manifold.setSolid(false);
            }
            else
            {
                if(!World.world.isGameOver())
                {
                    if(! cat.isCaught() && ! player.isHolding())
                    {
                        cat.setCaught(player);
                        cat.loadStackedSprite();
                        cat.playCatchSound();
                    }
                    else if(! cat.isCaught() && player.isHolding())
                    {
                        manifold.setSolid(false);
                    }
                }
                else
                {
                    manifold.setSolid(false);
                }
            }

            if(cat.isCaught())
            {
                manifold.setSolid(false);
            }
        }
        else if(checkCollisionBits(CollisionBitmasks.CAT_ID, CollisionBitmasks.CAT_ID, pair.getObjectA().getMask(), pair.getObjectB().getMask()))
        {
            Cat cat = (Cat) pair.getObjectA();
            Cat cat2 = (Cat) pair.getObjectB();

            if(!cat.isDeposited() && !cat2.isDeposited())
            {
                if(cat.isCaught() || cat2.isCaught())
                {
                    if(! World.world.isGameOver())
                    {
                        if(cat.isCaught() && ! cat2.isCaught())
                        {
                            if(cat2 instanceof BalloonCat)
                            {
                                if(cat instanceof BalloonCat)
                                {
                                    manifold.setSolid(false);
                                }
                                else
                                {
                                    if(cat.getStackPosition() == cat.getMaxStackSize() && ! cat.isHolding())
                                    {
                                        if(!cat.isDangling())
                                        {
                                            cat2.setCaught(cat);
                                            cat.swapHolder(cat2);
                                            World.world.addLife();
                                            cat2.disableCollision();
                                            cat2.playCatchSound();
                                        }
                                    }
                                }
                            }
                            else
                            {
                                if(cat.getStackPosition() < cat.getMaxStackSize())
                                {
                                    if(! cat.isHolding())
                                    {
                                        cat2.setCaught(cat);
                                        cat2.loadStackedSprite();
                                        cat2.playCatchSound();
                                    }
                                }
                                else
                                {
                                    manifold.setSolid(false);
                                }
                            }
                        }
                        else if(cat2.isCaught() && !cat.isCaught())
                        {
                            if(cat instanceof BalloonCat)
                            {
                                if(cat2 instanceof BalloonCat)
                                {
                                    manifold.setSolid(false);
                                }
                                else
                                {
                                    if(cat2.getStackPosition() == cat2.getMaxStackSize() && ! cat2.isHolding())
                                    {
                                        if(!cat2.isDangling())
                                        {
                                            cat.setCaught(cat2);
                                            cat2.swapHolder(cat);
                                            World.world.addLife();
                                            cat.disableCollision();
                                            cat.playCatchSound();
                                        }
                                    }
                                }
                            }
                            else
                            {
                                if(cat2.getStackPosition() < cat2.getMaxStackSize())
                                {
                                    if(! cat2.isHolding())
                                    {
                                        cat.setCaught(cat2);
                                        cat.loadStackedSprite();
                                        cat.playCatchSound();
                                    }
                                } else
                                {
                                    manifold.setSolid(false);
                                }
                            }
                        }
                    }
                }
            }
            manifold.setSolid(false);
        }
        else if(checkCollisionBits(CollisionBitmasks.PLAYER_ID, CollisionBitmasks.ROCKET_ID, pair.getObjectA().getMask(), pair.getObjectB().getMask()))
        {
            Player player;
            Rocket rocket;
            if(pair.getObjectA() instanceof Player)
            {
                player = (Player) pair.getObjectA();
                rocket = (Rocket) pair.getObjectB();
            }
            else
            {
                rocket = (Rocket) pair.getObjectA();
                player = (Player) pair.getObjectB();
            }

            if(!World.world.isGameOver())
            {
                player.depositStack(rocket);
            }
        }
        else if(checkCollisionBits(CollisionBitmasks.CAT_ID, CollisionBitmasks.ROCKET_ID, pair.getObjectA().getMask(), pair.getObjectB().getMask()))
        {
            Cat cat;
            Rocket rocket;
            if(pair.getObjectA() instanceof Cat)
            {
                cat = (Cat) pair.getObjectA();
                rocket = (Rocket) pair.getObjectB();
            }
            else
            {
                cat = (Cat) pair.getObjectB();
                rocket = (Rocket) pair.getObjectA();
            }

            rocket.expand();
            if(!cat.isDeposited())
            {
                manifold.setSolid(false);
            }
            else
            {
                cat.remove();
            }
        }
    }

    private static boolean checkCollisionBits(int expectedA, int expectedB, int bitA, int bitB)
    {
        if(bitA == expectedA)
        {
            if(bitB == expectedB)
            {
                return true;
            }
        }
        else if(bitB == expectedA)
        {
            if(bitA == expectedB)
            {
                return true;
            }
        }

        return false;
    }

    private static boolean checkCollisionBit(int expected, int bitA, int bitB)
    {
        if(bitA == expected || bitB == expected)
        {
            return true;
        }
        return false;
    }
}
