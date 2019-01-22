package game.android.com.catstacks.engine.rendering;

import android.opengl.GLES20;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import game.android.com.catstacks.engine.physics.Polygon;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.pool.GenericObjectPool;

public class BufferObject
{
    private Vec position;
    private Vec centerOfMass;
    private float rotation;
    private ArrayList<Sprite> sprites;
    ArrayList<Texture> textures;
    public ArrayList<float[]> vertices;
    private ArrayList<float[]> colours;
    private ArrayList<float[]> activeFrames;
    private short[] indices;
    private boolean hasAlpha, isTiled, storeBitmap, placeholder, initialized;
    private boolean isFrameUpdated, isVertexUpdated, isColourUpdated, isIndexUpdated;

    public static final GenericObjectPool<BufferObject> pool = new GenericObjectPool<BufferObject>(BufferObject.class);

    public BufferObject()
    {
        placeholder = true;
    }

    public BufferObject(Polygon body, ArrayList<Texture> textures, ArrayList<float[]> vertices, ArrayList<float[]> colours, ArrayList<float[]> activeFrames, short[] indices, boolean hasAlpha, boolean isStoringBitmap, boolean isTiled)
    {
        position = new Vec(body.getPosition());
        if(body.getCenterOfMass() != null)
        {
            centerOfMass = new Vec(body.getCenterOfMass());
        }
        rotation = body.getRotation();
        this.textures = new ArrayList<>(textures);
        this.vertices = new ArrayList<>(vertices);
        this.colours = new ArrayList<>(colours);
        this.activeFrames = new ArrayList<>(activeFrames);
        this.indices = indices;
        this.hasAlpha = hasAlpha;
        this.storeBitmap = isStoringBitmap;
        this.isTiled = isTiled;

        placeholder = false;
        initialized = false;

        isFrameUpdated = false;
        isVertexUpdated = false;
        isColourUpdated = false;
        isIndexUpdated = false;
    }

    public BufferObject(ArrayList<Texture> textures, ArrayList<float[]> vertices,  ArrayList<float[]> colours, ArrayList<float[]> activeFrames, short[] indices, boolean hasAlpha, boolean isStoringBitmap, boolean isTiled)
    {
        this.position = new Vec(0, 0);
        centerOfMass = new Vec(0, 0);
        rotation = 0;
        this.textures = new ArrayList<>(textures);
        this.vertices = new ArrayList<>(vertices);
        this.colours = new ArrayList<>(colours);
        this.activeFrames = new ArrayList<>(activeFrames);
        this.indices = indices;
        this.hasAlpha = hasAlpha;
        this.storeBitmap = isStoringBitmap;
        this.isTiled = isTiled;

        placeholder = false;
        initialized = false;

        isFrameUpdated = false;
        isVertexUpdated = false;
        isColourUpdated = false;
        isIndexUpdated = false;
    }

    public void init(Polygon body, ArrayList<Texture> textures, ArrayList<float[]> vertices, ArrayList<float[]> colours, ArrayList<float[]> activeFrames, short[] indices, boolean hasAlpha, boolean isStoringBitmap, boolean isTiled)
    {
        position = Vec.pool.get();
        position.x = body.getPosition().x;
        position.y = body.getPosition().y;
        if(body.getCenterOfMass() != null)
        {
            centerOfMass = Vec.pool.get();
            centerOfMass.x = body.getCenterOfMass().x;
            centerOfMass.y = body.getCenterOfMass().y;
        }
        rotation = body.getRotation();
        this.textures = ArrayPool.texturePool.get();
        this.textures.addAll(textures);
        this.vertices = ArrayPool.floatArrayPool.get();
        this.vertices.addAll(vertices);
        this.colours = ArrayPool.floatArrayPool.get();
        this.colours.addAll(colours);
        this.activeFrames = ArrayPool.floatArrayPool.get();
        this.activeFrames.addAll(activeFrames);
        this.indices = ArrayPool.shortMaster.get(indices.length);
        this.indices = indices;
        this.hasAlpha = hasAlpha;
        this.storeBitmap = isStoringBitmap;
        this.isTiled = isTiled;

        placeholder = false;
        initialized = false;

        isFrameUpdated = false;
        isVertexUpdated = false;
        isColourUpdated = false;
        isIndexUpdated = false;
    }

    public void render(float[] vpMatrix)
    {
        if(!initialized && !placeholder)
        {
            init();
            initialized = true;
        }

        if(!placeholder)
        {
            for(int i = 0; i < sprites.size(); i++)
            {
                if(sprites.get(i) instanceof TexturedSprite)
                {

                    if(isVertexUpdated)
                    {
                        sprites.get(i).setVertices(vertices.get(i));
                    }
                    if(isFrameUpdated)
                    {
                        ((TexturedSprite) sprites.get(i)).setTextureCoords(activeFrames.get(i));
                    }

                    if(!vertices.isEmpty())
                    {
                        if(vertices.get(i) != null)
                        {
                            if(indices != null)
                            {
                                if(isVertexUpdated && isIndexUpdated)
                                {
                                    sprites.get(i).setVertices(vertices.get(i), indices);
                                }
                                if(isColourUpdated)
                                {
                                    sprites.get(i).setColours(colours.get(i));
                                }
                            }
                        }
                    }
                }
                sprites.get(i).render(vpMatrix, position, rotation, centerOfMass);
            }
        }

        isFrameUpdated = false;
        isIndexUpdated = false;
        isVertexUpdated = false;
        isColourUpdated = false;
    }

    public void updateBodyParams(Polygon body)
    {
        position.x = body.getX();
        position.y = body.getY();
        if(body.getCenterOfMass() != null)
        {
            centerOfMass = new Vec(body.getCenterOfMass());
        }
        rotation = body.getRotation();
    }

    public void updateVertices(ArrayList<float[]> vertices)
    {
        this.vertices = vertices;
        isVertexUpdated = true;
    }

    public void updateColours(ArrayList<float[]> colours)
    {
        this.colours = colours;
        isColourUpdated = true;
    }

    public void updateTextures(ArrayList<Texture> textures)
    {
        this.textures = textures;
        initialized = false;
    }

    public void updateActiveFrame(ArrayList<float[]> activeFrames)
    {
        //this.activeFrames = new ArrayList<>(activeFrames);
        //Collections.copy(this.activeFrames, activeFrames);
        this.activeFrames = activeFrames;
        isFrameUpdated = true;
    }

    public void updateIndices(short[] indices)
    {
        this.indices = indices;
        isIndexUpdated = true;
    }

    public void setTiled(boolean value)
    {
        this.isTiled = value;
    }

    public void setStoreBitmap(boolean value)
    {
        this.storeBitmap = value;
    }

    private void init()
    {
        sprites = new ArrayList<>();
        for(int i = 0; i < vertices.size(); i++)
        {
            if(i < textures.size())
            {
                if(activeFrames != null && !activeFrames.isEmpty())
                {
                    sprites.add(new TexturedSprite(vertices.get(i), colours.get(i), textures.get(i), activeFrames.get(i), indices, hasAlpha, isTiled, storeBitmap));
                } else
                {
                    sprites.add(new TexturedSprite(vertices.get(i), colours.get(i), textures.get(i), hasAlpha, isTiled, storeBitmap));
                }
            }
            else
            {
                sprites.add(new ColourSprite(vertices.get(i)));
            }
        }
        isFrameUpdated = false;
        isVertexUpdated = false;
        isColourUpdated = false;
        isIndexUpdated = false;
    }

    public void destruct()
    {
        if(!placeholder)
        {
            Vec.pool.recycle(position);
            if(centerOfMass != null)
            {
                Vec.pool.recycle(centerOfMass);
            }

            if(sprites != null)
            {
                for(int i = 0; i < sprites.size(); i++)
                {
                    sprites.get(i).destruct();
                    Sprite.pool.recycle(sprites.get(i));
                }
                sprites.clear();
                ArrayPool.spritePool.recycle(sprites);
            }
            textures.clear();
            ArrayPool.texturePool.recycle(textures);

            vertices.clear();
            ArrayPool.floatArrayPool.recycle(vertices);

            colours.clear();
            ArrayPool.floatArrayPool.recycle(colours);

            activeFrames.clear();
            ArrayPool.floatArrayPool.recycle(activeFrames);

            ArrayPool.shortMaster.recycle(indices, indices.length);
        }
    }
    /*public void destruct()
    {
        if(!placeholder)
        {
            Vec.pool.recycle(position);
            if(centerOfMass != null)
            {
                Vec.pool.recycle(centerOfMass);
            }

            if(sprites != null)
            {
                for(int i = 0; i < sprites.size(); i++)
                {
                    sprites.get(i).destruct();
                    Sprite.pool.recycle(sprites.get(i));
                }
                sprites.clear();
                ArrayPool.spritePool.recycle(sprites);
            }
            for(int i = 0; i < textures.size(); i++)
            {
                textures.get(i).destruct();
                Texture.pool.recycle(textures.get(i));
            }
            textures.clear();
            ArrayPool.texturePool.recycle(textures);

            for(int i = 0; i < vertices.size(); i++)
            {
                ArrayPool.floatMaster.recycle(vertices.get(i), vertices.get(i).length);
            }
            vertices.clear();
            ArrayPool.floatArrayPool.recycle(vertices);

            for(int i = 0; i < colours.size(); i++)
            {
                ArrayPool.floatMaster.recycle(colours.get(i), colours.get(i).length);
            }
            colours.clear();
            ArrayPool.floatArrayPool.recycle(colours);

            for(int i = 0; i < activeFrames.size(); i++)
            {
                ArrayPool.floatMaster.recycle(activeFrames.get(i), activeFrames.get(i).length);
            }
            activeFrames.clear();
            ArrayPool.floatArrayPool.recycle(activeFrames);

            ArrayPool.shortMaster.recycle(indices, indices.length);
        }
    }*/
}
