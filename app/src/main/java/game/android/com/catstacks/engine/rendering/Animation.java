package game.android.com.catstacks.engine.rendering;

import java.util.ArrayList;

import game.android.com.catstacks.engine.core.Time;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.pool.GenericObjectPool;

public class Animation
{
    private boolean active, updated, looping;

    private String id;
    private int parent;

    private ArrayList<float[]> frames;
    private int activeFrame;
    protected ArrayList<float[]> animationVertices;

    private float[] intervals;
    private float accumulatedTime;

    public static final GenericObjectPool<Animation> pool = new GenericObjectPool<Animation>(Animation.class);

    public Animation()
    {

    }

    public Animation(String id, int parent, ArrayList<float[]> frames, float interval)
    {
        active = false;
        updated = true;
        looping = true;

        this.id = id;
        this.parent = parent;

        this.frames = frames;
        activeFrame = 0;

        intervals = new float[frames.size()];
        for(int i = 0; i < frames.size(); i++)
        {
            intervals[i] = interval;
        }

        accumulatedTime = 0;
    }

    public Animation(String id, int parent, ArrayList<float[]> frames, float interval, ArrayList<float[]> animationVertices)
    {
        this(id, parent, frames, interval);

        this.animationVertices = animationVertices;
    }

    public Animation(String id, int parent, ArrayList<float[]> frames, float[] interval)
    {
        active = false;
        looping = true;

        this.id = id;
        this.parent = parent;

        this.frames = frames;
        activeFrame = 0;

        intervals = new float[frames.size()];
        for(int i = 0; i < frames.size(); i++)
        {
            intervals[i] = interval[i];
        }

        accumulatedTime = 0;
    }

    public void init(String id, int parent, ArrayList<float[]> frames, float interval)
    {
        active = false;
        updated = true;
        looping = true;

        this.id = id;
        this.parent = parent;

        this.frames = frames;
        activeFrame = 0;

        //intervals = new float[frames.size()];
        intervals = ArrayPool.floatMaster.get(frames.size());
        for(int i = 0; i < frames.size(); i++)
        {
            intervals[i] = interval;
        }

        accumulatedTime = 0;
    }

    public void init(String id, int parent, ArrayList<float[]> frames, float[] interval)
    {
        active = false;
        looping = true;

        this.id = id;
        this.parent = parent;

        this.frames = frames;
        activeFrame = 0;

        intervals = new float[frames.size()];
        for(int i = 0; i < frames.size(); i++)
        {
            intervals[i] = interval[i];
        }

        accumulatedTime = 0;
    }

    public void init(String id, int parent, ArrayList<float[]> frames, float interval, ArrayList<float[]> animationVertices)
    {
        init(id, parent, frames, interval);

        this.animationVertices = animationVertices;
    }

    public void update()
    {
        if(!isActive())
        {
            return;
        }

        accumulatedTime += Time.getDelta();
        if(accumulatedTime > intervals[activeFrame])
        {
            if(activeFrame >= (frames.size() - 1))
            {
                activeFrame = 0;

                if(!isLooping())
                {
                    active = false;
                }
            }
            else
            {
                activeFrame++;
            }

            accumulatedTime = 0;
            updated = true;
        }
    }

    public void activate()
    {
        active = true;
    }

    public void deactivate()
    {
        active = false;
    }

    public boolean isActive()
    {
        return active;
    }

    public String getId()
    {
        return id;
    }

    public int getActiveFrame()
    {
        return activeFrame;
    }

    public float[] getActiveFrameCoords()
    {
        return frames.get(activeFrame);
    }

    public int getParent()
    {
        return parent;
    }

    public void setLooping(boolean value)
    {
        looping = value;
    }

    public boolean isLooping()
    {
        return looping;
    }

    public boolean hasAnimationVertices()
    {
        if(animationVertices != null && !animationVertices.isEmpty())
        {
            return true;
        }
        return false;
    }

    public float[] getActiveAnimationVertex()
    {
        return animationVertices.get(activeFrame);
    }

    public float[] getOriginalAnimationVertex()
    {
        return animationVertices.get(0);
    }

    public boolean isFrameUpdated()
    {
        return updated;
    }

    public void setFrameUpdated(boolean value)
    {
        updated = value;
    }

    public void destruct()
    {
        for(int i = 0; i < frames.size(); i++)
        {
            ArrayPool.floatMaster.recycle(frames.get(i), frames.get(i).length);
        }
        frames.clear();
        ArrayPool.floatArrayPool.recycle(frames);

        if(animationVertices != null && !animationVertices.isEmpty())
        {
            for(int i = 0; i < animationVertices.size(); i++)
            {
                ArrayPool.floatMaster.recycle(animationVertices.get(i), animationVertices.get(i).length);
            }
            animationVertices.clear();
            ArrayPool.floatArrayPool.recycle(animationVertices);
        }

        ArrayPool.floatMaster.recycle(intervals, intervals.length);

        ArrayPool.stringPool.recycle(id);
    }
}
