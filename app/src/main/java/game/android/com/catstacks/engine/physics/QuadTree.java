package game.android.com.catstacks.engine.physics;

import android.util.Log;

import java.util.ArrayList;

import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.pool.GenericObjectPool;
import game.android.com.catstacks.engine.objects.WorldObject;
import game.android.com.catstacks.game.collision.CollisionSensor;
import game.android.com.catstacks.game.objects.dynamic.BalloonCat;

public class QuadTree
{
    private final int MAX_OBJECTS = 10;
    private final int MAX_LEVELS = 5;

    private int level;
    private ArrayList<WorldObject> objects;
    private AABB bounds;
    private QuadTree[] nodes;

    public static final GenericObjectPool<QuadTree> pool = new GenericObjectPool<QuadTree>(QuadTree.class);

    public QuadTree()
    {

    }

    public QuadTree(int level, AABB bounds)
    {
        this.level = level;
        objects = new ArrayList<>();
        this.bounds = bounds;
        nodes = new QuadTree[4];
    }

    public void init(int level, AABB bounds)
    {
        this.level = level;
        objects = ArrayPool.objectsPool.get();
        this.bounds = bounds;
        //nodes = new QuadTree[4];
        nodes = ArrayPool.quadMaster.get(4);
        nodes[0] = null;
        nodes[1] = null;
        nodes[2] = null;
        nodes[3] = null;
    }

    public void clear()
    {
        objects.clear();
        ArrayPool.objectsPool.recycle(objects);

        for(int i = 0; i < nodes.length; i++)
        {
            if(nodes[i] != null)
            {
                QuadTree.pool.recycle(nodes[i]);
                nodes[i].clear();
                nodes[i] = null;
            }
        }
    }

    public void insert(WorldObject object)
    {
        if(nodes[0] != null)
        {
            int index = getIndex(object.getBody().getAABB());
            if(index != -1)
            {
                nodes[index].insert(object);
                return;
            }
        }

        objects.add(object);

        if((objects.size() > MAX_OBJECTS) && (level < MAX_LEVELS))
        {
            if(nodes[0] == null)
            {
                split();
            }

            int i = 0;
            while(i < objects.size())
            {
                int index = getIndex(objects.get(i).getBody().getAABB());
                if(index != -1)
                {
                    nodes[index].insert(objects.remove(i));
                }
                else
                {
                    i++;
                }
            }
        }
    }

    public int getIndex(AABB aabb)
    {
        int index = -1;
        float verticalMid = bounds.getX() + (bounds.getWidth() / 2);
        float horizontalMid = bounds.getY() + (bounds.getHeight() / 2);

        boolean top = ((aabb.getY() > horizontalMid) && (aabb.getHeight() < bounds.getHeight()));
        boolean bottom = ((aabb.getHeight() < horizontalMid) && (aabb.getY() > bounds.getY()));
        boolean left = ((aabb.getWidth() < verticalMid) && (aabb.getX() > bounds.getX()));
        boolean right = ((aabb.getX() > verticalMid) && (aabb.getWidth() < bounds.getWidth()));

        if(left)
        {
            if(top)
            {
                index = 1;
            }
            else if(bottom)
            {
                index = 0;
            }
        }
        else if(right)
        {
            if(top)
            {
                index = 2;
            }
            else if(bottom)
            {
                index = 3;
            }
        }

        return index;
    }

    private void split()
    {
        AABB bounds0 = AABB.pool.get();
        Vec lower0 = Vec.pool.get();
        lower0.x = bounds.getLowerBound().x;
        lower0.y = bounds.getLowerBound().y;
        bounds0.init(lower0);
        bounds0.setBounds(bounds.getLowerBound());
        bounds0.setBounds((bounds.getX() + bounds.getWidth()) / 2, (bounds.getY() + bounds.getHeight()) / 2);
        AABB bounds1 = AABB.pool.get();
        Vec lower1 = Vec.pool.get();
        bounds1.init(lower1);
        bounds1.setBounds(bounds.getX(), (bounds.getY() + bounds.getHeight()) / 2);
        bounds1.setBounds((bounds.getX() + bounds.getWidth()) / 2, bounds.getHeight());
        AABB bounds2 = AABB.pool.get();
        Vec lower2 = Vec.pool.get();
        bounds2.init(lower2);
        bounds2.setBounds((bounds.getX() + bounds.getWidth()) / 2, (bounds.getY() + bounds.getHeight()) / 2);
        bounds2.setBounds(bounds.getWidth(), bounds.getHeight());
        AABB bounds3 = AABB.pool.get();
        Vec lower3 = Vec.pool.get();
        bounds3.init(lower3);
        bounds3.setBounds((bounds.getX() + bounds.getWidth()) / 2, bounds.getY());
        bounds3.setBounds(bounds.getWidth(), (bounds.getY() + bounds.getHeight()) / 2);

        nodes[0] = QuadTree.pool.get();
        nodes[0].init(level + 1, bounds0);
        nodes[1] = QuadTree.pool.get();
        nodes[1].init(level + 1, bounds1);
        nodes[2] = QuadTree.pool.get();
        nodes[2].init(level + 1, bounds2);
        nodes[3] = QuadTree.pool.get();
        nodes[3].init(level + 1, bounds3);

        //nodes[0] = new QuadTree(level + 1, new AABB(bounds.getX(), bounds.getY(), (bounds.getX() + bounds.getWidth()) / 2, (bounds.getY() + bounds.getHeight()) / 2), pairsPool);
        //nodes[1] = new QuadTree(level + 1, new AABB(bounds.getX(), (bounds.getY() + bounds.getHeight()) / 2, (bounds.getX() + bounds.getWidth()) / 2, bounds.getHeight()), pairsPool);
        //nodes[2] = new QuadTree(level + 1, new AABB((bounds.getX() + bounds.getWidth()) / 2, (bounds.getY() + bounds.getHeight()) / 2, bounds.getWidth(), bounds.getHeight()), pairsPool);
        //nodes[3] = new QuadTree(level + 1, new AABB((bounds.getX() + bounds.getWidth()) / 2, bounds.getY(), bounds.getWidth(), (bounds.getY() + bounds.getHeight()) / 2), pairsPool);
    }

    public ArrayList retrieve(AABB aabb)
    {
        int index = getIndex(aabb);
        ArrayList<WorldObject> returnObjects = ArrayPool.objectsPool.get();

        if((index != -1) && (nodes[0] != null))
        {
            ArrayList<WorldObject> nodeObjects = nodes[index].retrieve(aabb);
            for(int i = 0; i < nodeObjects.size(); i++)
            {
                returnObjects.add(nodeObjects.get(i));
            }
            nodeObjects.clear();
            ArrayPool.objectsPool.recycle(nodeObjects);
        }
        else if((index == -1) && (nodes[0] != null))
        {
            for(int i = 0; i < nodes.length; i++)
            {
                ArrayList<WorldObject> nodeObjects = nodes[i].retrieve(aabb);
                for(int j = 0; j < nodeObjects.size(); j++)
                {
                    returnObjects.add(nodeObjects.get(j));
                }
                nodeObjects.clear();
                ArrayPool.objectsPool.recycle(nodeObjects);
                //returnObjects.addAll(nodes[i].retrieve(aabb));
            }
        }

        for(int i = 0; i < objects.size(); i++)
        {
            returnObjects.add(objects.get(i));
        }
        return returnObjects;
    }

    public ArrayList<Pair> retrievePairs(ArrayList<WorldObject> objects)
    {
        ArrayList<Pair> returnPairs = ArrayPool.pairPool.get();

        for(int i = 0; i < objects.size(); i++)
        {
            if(!objects.get(i).isCollidable())
            {
                continue;
            }

            AABB aabb = objects.get(i).getBody().getAABB();
            int index = getIndex(aabb);
            ArrayList<WorldObject> returnObjects = null;
            //ArrayList<WorldObject> returnObjects = new ArrayList<>();
            if((index != -1) && (nodes[0] != null))
            {
                returnObjects = nodes[index].retrieve(aabb);
            }
            /*else if((index == -1) && (nodes[0] != null))
            {
                for(int i = 0; i < nodes.length; i++)
                {
                    returnObjects.addAll(nodes[i].retrieve(aabb));
                }
            }*/

            if(returnObjects == null)
            {
                returnObjects = ArrayPool.objectsPool.get();
            }
            for(int j = 0; j < this.objects.size(); j++)
            {
                returnObjects.add(this.objects.get(j));
            }

            for(int j = 0; j < returnObjects.size(); j++)
            {
                if(objects.get(i) != returnObjects.get(j))
                {
                    if(!objects.get(i).isUnmovable() || !returnObjects.get(j).isUnmovable())
                    {
                        //Pair newPair = new Pair(object, worldObject);
                        Pair newPair = Pair.pool.get();
                        newPair.setObjectA(objects.get(i));
                        newPair.setObjectB(returnObjects.get(j));

                        if(!returnPairs.contains(newPair) && CollisionSensor.isCollidable(newPair))
                        {
                            returnPairs.add(newPair);
                        }
                        else
                        {
                            newPair.destruct();
                            Pair.pool.recycle(newPair);
                        }
                        //pairsPool.recycle(newPair);
                        //newPair = null;
                        //currentPairs.add(newPair);
                    }
                }
            }

            returnObjects.clear();
            ArrayPool.objectsPool.recycle(returnObjects);
        }

        return returnPairs;
    }

    public void destruct()
    {
        objects.clear();
        ArrayPool.objectsPool.recycle(objects);

        for(int i = 0; i < nodes.length; i++)
        {
            if(nodes[i] != null)
            {
                nodes[i].destruct();
            }
        }
        ArrayPool.quadMaster.recycle(nodes, nodes.length);

        bounds.destructWithVec();
        AABB.pool.recycle(bounds);

        QuadTree.pool.recycle(this);
    }
}
