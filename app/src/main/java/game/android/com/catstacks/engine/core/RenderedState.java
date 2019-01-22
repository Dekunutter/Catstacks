package game.android.com.catstacks.engine.core;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import game.android.com.catstacks.engine.gameloop.GameLoop;
import game.android.com.catstacks.engine.input.InputBuffer;
import game.android.com.catstacks.engine.objects.RenderedObject;
import game.android.com.catstacks.engine.objects.WorldObject;
import game.android.com.catstacks.engine.rendering.BufferObject;
import game.android.com.catstacks.engine.rendering.Shaders;

public abstract class RenderedState implements GameLoop
{
    protected static Context context;

    protected ArrayList<WorldObject> objects;
    protected ArrayList<WorldObject> newSpawns;
    protected ArrayList<WorldObject> toRemove;
    protected ArrayList<WorldObject> removeSpawns;
    protected List<BufferObject> buffers;
    protected ArrayList<BufferObject> removeBuffer;

    protected Object bufferMutex = new Object();

    protected boolean isAdReady;
    protected int institialAdCounter;

    protected RenderedState(Context context)
    {
        this.context = context;

        objects = new ArrayList<>();
        newSpawns = new ArrayList<>();
        toRemove = new ArrayList<>();
        removeSpawns = new ArrayList<>();
        buffers = Collections.synchronizedList(new ArrayList<BufferObject>());
        removeBuffer = new ArrayList<>();

        isAdReady = false;
        institialAdCounter = 0;
    }

    public static Context getContext()
    {
        return context;
    }

    @Override
    public void getInput(InputBuffer input)
    {
        for(int i = 0; i < objects.size(); i++)
        {
            objects.get(i).getInput(input);
        }
    }

    @Override
    public void update()
    {
        playAnimations();

        for(int i = 0; i < objects.size(); i++)
        {
            if(!objects.get(i).getRemove())
            {
                objects.get(i).update();
            }
        }
    }

    public void playAnimations()
    {
        for(int i = 0; i < objects.size(); i++)
        {
            if(objects.get(i) instanceof RenderedObject)
            {
                if(!objects.get(i).getRemove() && objects.get(i).isAnimated())
                {
                    objects.get(i).animate();
                }
            }
        }
    }

    public void buffer()
    {
        synchronized(bufferMutex)
        {
            removeBuffer.clear();

            for(int i = 0; i < objects.size(); i++)
            {
                if(objects.get(i).getRemove())
                {
                    toRemove.add(objects.get(i));
                    removeBuffer.add(buffers.get(i));
                }
                else
                {
                    if(objects.get(i).isRendered())
                    {
                        if(objects.get(i).getBody() != null)
                        {
                            buffers.get(i).updateBodyParams(objects.get(i).getBody());
                        }

                        if(objects.get(i).hasVerticesChanged())
                        {
                            buffers.get(i).updateVertices(objects.get(i).getRenderVertices());
                            objects.get(i).setVerticesChanged(false);
                        }

                        if(objects.get(i).hasColoursChanged())
                        {
                            buffers.get(i).updateColours(objects.get(i).getRenderColours());
                            objects.get(i).setColoursChanged(false);
                        }

                        if(objects.get(i).hasFrameChanged())
                        {
                            buffers.get(i).updateActiveFrame(objects.get(i).getActiveFrames());
                            objects.get(i).setFrameChanged(false);
                        }

                        if(objects.get(i).hasIndicesChanged())
                        {
                            buffers.get(i).updateIndices(objects.get(i).getIndices());
                            objects.get(i).setIndicesChanged(false);
                        }
                    }
                }
            }

            for(int i = 0; i < toRemove.size(); i++)
            {
                toRemove.get(i).destruct();
                objects.remove(toRemove.get(i));
            }
            toRemove.clear();

            for(int i = 0; i < removeBuffer.size(); i++)
            {
                removeBuffer.get(i).destruct();
                buffers.remove(removeBuffer.get(i));
            }

            for(int i = 0; i < newSpawns.size(); i++)
            {
                if(newSpawns.get(i).isRendered())
                {
                    if(newSpawns.get(i).getBody() != null)
                    {
                        BufferObject buffer = BufferObject.pool.get();
                        buffer.init(newSpawns.get(i).getBody(), newSpawns.get(i).getTextures(), newSpawns.get(i).getRenderVertices(), newSpawns.get(i).getRenderColours(), newSpawns.get(i).getActiveFrames(), newSpawns.get(i).getIndices(), newSpawns.get(i).hasAlpha(), newSpawns.get(i).isStoringBitmap(), newSpawns.get(i).isTiled());
                        buffers.add(buffer);
                    }
                    else
                    {
                        buffers.add(new BufferObject(newSpawns.get(i).getTextures(), newSpawns.get(i).getRenderVertices(), newSpawns.get(i).getRenderColours(), newSpawns.get(i).getActiveFrames(), newSpawns.get(i).getIndices(), newSpawns.get(i).hasAlpha(), newSpawns.get(i).isStoringBitmap(), newSpawns.get(i).isTiled()));
                    }
                }
                else
                {
                    buffers.add(new BufferObject());
                }

                objects.add(newSpawns.get(i));

                removeSpawns.add(newSpawns.get(i));
            }

            /*if(!newSpawns.isEmpty())
            {
                Iterator<WorldObject> i = newSpawns.iterator();
                while(i.hasNext())
                {
                    WorldObject object = i.next();
                    if(object.isRendered())
                    {
                        if(object.getBody() != null)
                        {
                            buffers.add(new BufferObject(object.getBody(), object.getTextures(), object.getRenderVertices(), object.getRenderColours(), object.getActiveFrames(), object.getIndices(), object.hasAlpha(), object.isStoringBitmap(), object.isTiled()));
                        } else
                        {
                            buffers.add(new BufferObject(object.getTextures(), object.getRenderVertices(), object.getRenderColours(), object.getActiveFrames(), object.getIndices(), object.hasAlpha(), object.isStoringBitmap(), object.isTiled()));
                        }
                    } else
                    {
                        buffers.add(new BufferObject());
                    }

                    objects.add(object);

                    i.remove();
                }
            }*/

            for(int i = 0; i < removeSpawns.size(); i++)
            {
                newSpawns.remove(removeSpawns.get(i));
            }
            removeSpawns.clear();
        }
    }

    public void render(float[] mvpMatrix)
    {
        synchronized(bufferMutex)
        {
            for(int i = 0; i < buffers.size(); i++)
            {
                buffers.get(i).render(mvpMatrix);
            }
        }
    }

    public boolean isAdReady()
    {
        return isAdReady;
    }

    public int getInstitialAdCounter()
    {
        return institialAdCounter;
    }

    public boolean isInstitialAdReady()
    {
        if(institialAdCounter != 0 && institialAdCounter % 5 == 0)
        {
            institialAdCounter = 0;
            return true;
        }
        return false;
    }
}
