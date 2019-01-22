package game.android.com.catstacks.engine.physics;

import java.util.ArrayList;

import game.android.com.catstacks.engine.pool.GenericObjectPool;
import game.android.com.catstacks.engine.pool.ObjectPool;
import game.android.com.catstacks.engine.pool.Pool;
import game.android.com.catstacks.engine.pool.ReusableStack;
import game.android.com.catstacks.engine.pool.VecObjectPool;

public class Vec
{
    public float x, y, z;

    public static final VecObjectPool pool = new VecObjectPool();

    public Vec()
    {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Vec(Vec value)
    {
        this.x = value.x;
        this.y = value.y;
        this.z = 0;
    }

    public Vec(float x, float y)
    {
        this.x = x;
        this.y = y;
        this.z = 0;
    }

    public void invert()
    {
        x *= -1;
        y *= -1;
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public float getZ()
    {
        return z;
    }

    public void perp()
    {
        float tempX = x;
        x = y;
        y = tempX * -1;
    }

    public Vec right()
    {
        return new Vec(y * -1, x);
    }

    public void left()
    {
        perp();
    }

    public float dotProd(Vec axis)
    {
        return (x * axis.x) + (y * axis.y);
    }

    public float crossProd(Vec axis)
    {
        return (x * axis.y) - (y * axis.x);
    }

    public float length()
    {
        return (float)(Math.sqrt((x * x) + (y * y)));
    }

    public float squaredMagnitude()
    {
        return (float)((x * x) + (y * y));
    }

    public void normalize()
    {
        float magnitude = length();
        if(magnitude != 0.0f)
        {
            x /= magnitude;
            y /= magnitude;
        }
    }

    public void abs()
    {
        x = Math.abs(x);
        y = Math.abs(y);
    }

    public void add(Vec other)
    {
        x += other.x;
        y += other.y;
    }

    public Vec add(float value)
    {
        return new Vec(x + value, y + value);
    }

    public void subtract(Vec other)
    {
        x -= other.x;
        y -= other.y;
    }

    public Vec subtract(int value)
    {
        return new Vec(x - value, y - value);
    }

    public void multiply(Vec other)
    {
        //return new Vec(other.x * x, other.y * y);
        x *= other.x;
        y *= other.y;
    }

    public void multiply(float value)
    {
        //return new Vec(value * x, value * y);
        x *= value;
        y *= value;
    }

    public Vec divide(float value)
    {
        return new Vec(x/value, y/value);
    }

    public boolean isZero()
    {
        if((x == 0) && (y == 0))
        {
            return true;
        }
        return false;
    }

    public static float getDistance(Vec vertexA, Vec vertexB)
    {
        return (float)(Math.sqrt(Math.pow((vertexB.x - vertexA.x), 2) + Math.pow((vertexB.y - vertexA.y), 2)));
    }

    @Override
    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object object)
    {
        if((object != null) && (object instanceof Vec))
        {
            Vec com = (Vec)object;
            if((x == com.x) && (y == com.y))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 79 * hash + Float.floatToIntBits(this.x);
        hash = 79 * hash + Float.floatToIntBits(this.y);
        return hash;
    }







    public Vec abs2()
    {
        return new Vec(Math.abs(x), Math.abs(y));
    }

    public Vec add2(Vec other)
    {
        return new Vec(x + other.x, y + other.y);
    }

    public Vec add2(float value)
    {
        return new Vec(x + value, y + value);
    }

    public Vec subtract2(Vec other)
    {
        return new Vec(x - other.x, y - other.y);
    }

    public Vec subtract2(int value)
    {
        return new Vec(x - value, y - value);
    }

    public Vec multiply2(Vec other)
    {
        return new Vec(other.x * x, other.y * y);
    }

    public Vec multiply2(float value)
    {
        return new Vec(value * x, value * y);
    }

    public Vec normalize2()
    {
        float magnitude = length();
        if(magnitude == 0.0f)
        {
            return this;
        }
        return new Vec(x/magnitude, y/magnitude);
    }

    public Vec perp2()
    {
        return new Vec(y, x * -1);
    }

    public Vec invert2()
    {
        return new Vec(x * -1, y * -1);
    }
}
