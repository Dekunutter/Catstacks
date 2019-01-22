package game.android.com.catstacks.engine.physics;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import game.android.com.catstacks.engine.pool.ArrayObjectPool;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.pool.GenericObjectPool;

public class Polygon
{
    private ArrayList<Vec> origins;
    private ArrayList<Vec> vertices;
    private ArrayList<Edge> edges;
    private AABB aabb;
    private Vec center;
    private float x, y, rotation, radius, displacementX, displacementY, displacementR, mass;
    private float[] arr;
    private Vec position;

    public static final GenericObjectPool<Polygon> pool = new GenericObjectPool<Polygon>(Polygon.class);

    public Polygon()
    {

    }

    public void init(float x, float y, float rotation, float[] values)
    {
        //vertices = new ArrayList<>();
        vertices = ArrayPool.vertexPool.get();
        for(int i = 0; i < values.length; i += 3)
        {
            Vec vertex = Vec.pool.get();
            vertex.x = values[i];
            vertex.y = values[i + 1];
            //Vec vertex = new Vec(values[i], values[i + 1]);
            vertices.add(vertex);
        }

        this.x = x;
        this.y = y;
        position = Vec.pool.get();
        position.x = x;
        position.y = y;
        //position = new Vec(x, y);

        this.rotation = rotation;

        initializeAABB();

        //origins = new ArrayList<>();
        origins = ArrayPool.vertexPool.get();
        for(int i = 0; i < vertices.size(); i++)
        {
            Vec origin = Vec.pool.get();
            origin.x = vertices.get(i).x;
            origin.y = vertices.get(i).y;
            this.origins.add(origin);
            //this.origins.add(new Vec(vertex.x, vertex.y));
            aabb.setBounds(vertices.get(i));
        }

        //edges = new ArrayList<>();
        edges = ArrayPool.edgePool.get();
        initializeEdges();
        //triangulate();
        //calculateCenterOfMass();

        //arr = new float[2];
        arr = ArrayPool.floatMaster.get(2);
    }

    public Polygon(ArrayList<Vec> vertices)
    {
        this(0, 0, 0, vertices);
    }

    public Polygon(float x, float y, float rotation, ArrayList<Vec> vertices)
    {
        this.x = x;
        this.y = y;
        position = new Vec(x, y);

        this.rotation = rotation;
        this.vertices = vertices;

        initializeAABB();

        origins = new ArrayList<>();
        for(Vec vertex : vertices)
        {
            this.origins.add(new Vec(vertex.x, vertex.y));
            aabb.setBounds(vertex);
        }

        edges = new ArrayList<>();
        initializeEdges();
        //triangulate();
        //calculateCenterOfMass();

        //arr = new float[2];
        arr = ArrayPool.floatMaster.get(2);
    }

    public Polygon(float x, float y, float rotation, float[] values)
    {
        ArrayList<Vec> vertices = new ArrayList<Vec>();
        for(int i = 0; i < values.length; i += 3)
        {
            Vec vertex = new Vec(values[i], values[i + 1]);
            vertices.add(vertex);
        }

        this.x = x;
        this.y = y;
        position = new Vec(x, y);

        this.rotation = rotation;
        this.vertices = vertices;

        initializeAABB();

        origins = new ArrayList<>();
        for(Vec vertex : vertices)
        {
            this.origins.add(new Vec(vertex.x, vertex.y));
            aabb.setBounds(vertex);
        }

        edges = new ArrayList<>();
        initializeEdges();
        //triangulate();
        //calculateCenterOfMass();

        //arr = new float[2];
        arr = ArrayPool.floatMaster.get(2);
    }

    public Vec getPosition()
    {
        //return new Vec(x, y);
        position.x = x;
        position.y = y;
        return position;
    }

    public void resetPosition()
    {
        position.x = 0;
        position.y = 0;
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public void setPosition(Vec pos)
    {
        move(pos.x, pos.y, rotation);

        x = pos.x;
        y = pos.y;
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public void setY(float y)
    {
        this.y = y;
    }

    public float getRotation()
    {
        return rotation;
    }

    public void setRotation(float value)
    {
        rotation = value;
    }

    public Vec getDisplacement()
    {
        //return new Vec(displacementX, displacementY);
        Vec result = Vec.pool.get();
        result.x = displacementX;
        result.y = displacementY;
        return result;
    }

    public float getDisplacementX()
    {
        return displacementX;
    }

    public float getDisplacementY()
    {
        return displacementY;
    }

    public void setDisplacement(Vec value)
    {
        displacementX = value.x;
        displacementY = value.y;
    }

    public void setDisplacement(float x, float y)
    {
        displacementX = x;
        displacementY = y;
    }

    public float getRotationDisplacement()
    {
        return displacementR;
    }

    public void setRotationDisplacement(float value)
    {
        displacementR = value;
    }

    public ArrayList<Vec> getVertices()
    {
        return vertices;
    }

    public int getVertexCount()
    {
        return vertices.size();
    }

    public ArrayList<Vec> getOrigins()
    {
        return origins;
    }

    public ArrayList<Edge> getEdges()
    {
        return edges;
    }

    public int getEdgeCount()
    {
        return edges.size();
    }

    public AABB getAABB()
    {
        return aabb;
    }

    public void move(float x, float y, float rotation)
    {
        rotation = (float) Math.toRadians(rotation);

        for(int i = 0; i < vertices.size(); i++)
        {
            float sinCalc = (float) Math.sin(rotation);
            float cosCalc = (float) Math.cos(rotation);

            float newPositionX = (origins.get(i).x * cosCalc) - (origins.get(i).y * sinCalc);
            float newPositionY = (origins.get(i).x * sinCalc) + (origins.get(i).y * cosCalc);

            vertices.get(i).x = newPositionX + x;
            vertices.get(i).y = newPositionY + y;
        }

        //moveEdges();
    }

    private void initializeEdges()
    {
        for(int i = 0; i < vertices.size(); i++)
        {
            int j = i + 1;
            if(j == vertices.size())
            {
                j = 0;
            }
            Edge edge = Edge.pool.get();
            edge.init(vertices.get(i), vertices.get(j));
            edges.add(edge);
            //edges.add(new Edge(vertices.get(i), vertices.get(j)));
        }
    }

    private void moveEdges()
    {
        for(int i = 0; i < vertices.size(); i++)
        {
            int next = i + 1;
            if(next >= vertices.size())
            {
                next = 0;
            }

            //edges.set(i, new Edge(vertices.get(i), vertices.get(next)));
            edges.get(i).reinit(vertices.get(i), vertices.get(next));
        }
    }

    private void initializeAABB()
    {
        aabb = new AABB(vertices.get(0));
    }

    public Vec getCenter()
    {
        return new Vec((aabb.getX() + aabb.getWidth()) / 2, (aabb.getY() + aabb.getHeight()) / 2);
    }

    public float getCenterX()
    {
        return (aabb.getX() + (aabb.getWidth() / 2));
    }

    public float getCenterY()
    {
        return (aabb.getY() + aabb.getHeight()) / 2;
    }

    public float getMass()
    {
        return mass;
    }

    public void setMass(float value)
    {
        mass = value;
    }

    public float getInverseMass()
    {
        if(getMass() > 0)
        {
            return 1/getMass();
        }
        return getMass();
    }

    private void calculateBoundingCircleRadius()
    {
        float distance = 0;
        for(int i = 0; i < origins.size(); i++)
        {
            float newDistance = Vec.getDistance(getCenterOfMass(), origins.get(i));
            if(newDistance > distance)
            {
                distance = newDistance;
            }
        }
        radius = distance;
    }

    public Vec getCenterOfMass()
    {
        return center;
    }

    public float[] getProjection(Vec axis)
    {
        float min = getOrigins().get(0).dotProd(axis);
        float max = min;
        for(int i = 0; i < getVertexCount(); i++)
        {
            float projection = getOrigins().get(i).dotProd(axis);
            if(projection < min)
            {
                min = projection;
            } else if(projection > max)
            {
                max = projection;
            }
        }
        //float[] arr = {min, max};
        arr[0] = min;
        arr[1] = max;
        return arr;
    }

    public void destruct()
    {
        x = 0;
        y = 0;

        ArrayPool.floatMaster.recycle(arr, arr.length);

        for(int i = 0; i < edges.size(); i++)
        {
            edges.get(i).destruct();
            Edge.pool.recycle(edges.get(i));
        }
        edges.clear();

        for(int i = 0; i < origins.size(); i++)
        {
            Vec.pool.recycle(origins.get(i));
        }
        origins.clear();

        Vec.pool.recycle(position);

        for(int i = 0; i < vertices.size(); i++)
        {
            Vec.pool.recycle(vertices.get(i));
        }
        vertices.clear();
    }
}
