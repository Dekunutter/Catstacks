package game.android.com.catstacks.engine.objects;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import game.android.com.catstacks.engine.core.RenderedState;
import game.android.com.catstacks.engine.core.Screen;
import game.android.com.catstacks.engine.gameloop.GameLoop;
import game.android.com.catstacks.engine.input.InputBuffer;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.rendering.Animation;
import game.android.com.catstacks.engine.rendering.Texture;
import game.android.com.catstacks.engine.rendering.TextureLoader;
import game.android.com.catstacks.game.objects.dynamic.Player;

public abstract class RenderedObject implements GameLoop
{
    protected float width, height;

    protected ArrayList<Texture> textures;
    protected ArrayList<float[]> sprVertices;
    protected ArrayList<float[]> sprColours;
    protected ArrayList<float[]> activeFrames;
    protected short[] indices;
    protected ArrayList<Animation> animations;

    protected boolean hasAlpha, tiled, storeBitmap;
    protected boolean verticesChanged, coloursChanged, textureChanged, frameChanged, indicesChanged;

    protected static final int PIXEL_SCALE = 2;

    protected ArrayList<Integer> defaultFrames = new ArrayList<Integer>();

    public RenderedObject()
    {
        textures = ArrayPool.texturePool.get();
        sprVertices = ArrayPool.floatArrayPool.get();
        sprColours = ArrayPool.floatArrayPool.get();
        activeFrames = ArrayPool.floatArrayPool.get();
        indices = ArrayPool.shortMaster.get(6);
        indices[0] = 0;
        indices[1] = 1;
        indices[2] = 2;
        indices[3] = 0;
        indices[4] = 2;
        indices[5] = 3;
        animations = ArrayPool.animationPool.get();

        hasAlpha = true;
        tiled = false;

        verticesChanged = false;
        coloursChanged = false;
        textureChanged = false;
        frameChanged = false;
        indicesChanged = false;
    }

    protected void addSprite(int resourceId, float depth, float alpha)
    {
        addSprite(resourceId, 1, 1, depth, alpha);
    }

    protected void addSprite(int resourceId, int tilesX, int tilesY, float depth, float alpha)
    {
        Texture texture = TextureLoader.loadTexture(RenderedState.getContext(), resourceId, tilesX, tilesY);
        width = Screen.screen.getPixelDensity(texture.getWidth() * PIXEL_SCALE);
        height = Screen.screen.getPixelDensity(texture.getHeight() * PIXEL_SCALE);
        sprVertices.add(new float[] {0, height, depth, 0, 0, depth, width, 0, depth, width, height, depth});
        sprColours.add(new float[] {1, 1, 1, alpha, 1, 1, 1, alpha, 1, 1, 1, alpha, 1, 1, 1, alpha});
        textures.add(texture);
    }

    protected void addSprite(int resourceId, float[] framesX, float[] framesY, float depth, float alpha)
    {
        Texture texture = TextureLoader.loadTexture(RenderedState.getContext(), resourceId, framesX, framesY);
        width = Screen.screen.getPixelDensity(framesX[0] * PIXEL_SCALE);
        height = Screen.screen.getPixelDensity(framesY[0] * PIXEL_SCALE);
        sprVertices.add(new float[] {0, height, depth, 0, 0, depth, width, 0, depth, width, height, depth});
        sprColours.add(new float[] {1, 1, 1, alpha, 1, 1, 1, alpha, 1, 1, 1, alpha, 1, 1, 1, alpha});
        textures.add(texture);
    }

    protected void addSprite(int resourceId, float[][] rowsX, float[][] rowsY, float depth, float alpha, float customWidth, float customHeight)
    {
        Texture texture = TextureLoader.loadTexture(RenderedState.getContext(), resourceId, rowsX, rowsY);
        width = Screen.screen.getPixelDensity(customWidth * PIXEL_SCALE);
        height = Screen.screen.getPixelDensity(customHeight * PIXEL_SCALE);
        sprVertices.add(new float[] {0, height, depth, 0, 0, depth, width, 0, depth, width, height, depth});
        sprColours.add(new float[] {1, 1, 1, alpha, 1, 1, 1, alpha, 1, 1, 1, alpha, 1, 1, 1, alpha});
        textures.add(texture);
    }

    protected void addSpriteToPosition(int resourceId, float depth, float alpha, Vec position)
    {
        addSpriteToPosition(resourceId, 1, 1, depth, alpha, position);
    }

    protected void addSpriteToPosition(int resourceId, int tilesX, int tilesY, float depth, float alpha, Vec position)
    {
        Texture texture = TextureLoader.loadTexture(RenderedState.getContext(), resourceId, tilesX, tilesY);
        width = Screen.screen.getPixelDensity(texture.getWidth() * PIXEL_SCALE);
        height = Screen.screen.getPixelDensity(texture.getHeight() * PIXEL_SCALE);
        sprVertices.add(new float[] {position.x, position.y + height, depth, position.x, position.y, depth, position.x + width, position.y, depth, position.x + width, position.y + height, depth});
        sprColours.add(new float[] {1, 1, 1, alpha, 1, 1, 1, alpha, 1, 1, 1, alpha, 1, 1, 1, alpha});
        textures.add(texture);
    }

    protected void addSpriteFromExistingTexture(Texture texture, float depth, float alpha)
    {
        addSpriteFromExistingTexture(texture, depth, alpha, texture.getWidth(), texture.getHeight());
    }

    protected void addSpriteFromExistingTexture(Texture texture, float depth, float alpha, Vec position)
    {
        addSpriteFromExistingTexture(texture, depth, alpha, texture.getWidth(), texture.getHeight(), position);
    }

    protected void addSpriteFromExistingTexture(Texture texture, float depth, float alpha, float customWidth, float customHeight)
    {
        //addSpriteFromExistingTexture(texture, depth, alpha, customWidth, customHeight, new Vec(0, 0));
        Vec defaultPos = (Vec) Vec.pool.get();
        addSpriteFromExistingTexture(texture, depth, alpha, customWidth, customHeight, defaultPos);
        Vec.pool.recycle(defaultPos);
    }

    protected void addSpriteFromExistingTexture(Texture texture, float depth, float alpha, float customWidth, float customHeight, Vec position)
    {
        textures.add(texture);
        width = Screen.screen.getPixelDensity(customWidth * PIXEL_SCALE);
        height = Screen.screen.getPixelDensity(customHeight * PIXEL_SCALE);
        float[] vertices = ArrayPool.floatMaster.get(12);
        vertices[0] = position.x;
        vertices[1] = position.y + height;
        vertices[2] = depth;
        vertices[3] = position.x;
        vertices[4] = position.y;
        vertices[5] = depth;
        vertices[6] = position.x + width;
        vertices[7] = position.y;
        vertices[8] = depth;
        vertices[9] = position.x + width;
        vertices[10] = position.y + height;
        vertices[11] = depth;
        sprVertices.add(vertices);
        float[] colours = ArrayPool.floatMaster.get(16);
        colours[0] = 1;
        colours[1] = 1;
        colours[2] = 1;
        colours[3] = alpha;
        colours[4] = 1;
        colours[5] = 1;
        colours[6] = 1;
        colours[7] = alpha;
        colours[8] = 1;
        colours[9] = 1;
        colours[10] = 1;
        colours[11] = alpha;
        colours[12] = 1;
        colours[13] = 1;
        colours[14] = 1;
        colours[15] = alpha;
        sprColours.add(colours);
    }

    protected void addStretchedSprite(int resourceId, float depth, float alpha, int width, int height)
    {
        addStretchedSprite(resourceId, 1, 1, depth, alpha, width, height);
    }

    protected void addStretchedSprite(int resourceId, int tilesX, int tilesY, float depth, float alpha, int width, int height)
    {
        Texture texture = TextureLoader.loadTexture(RenderedState.getContext(), resourceId, tilesX, tilesY);
        this.width = width;
        this.height = height;
        sprVertices.add(new float[] {0, height, depth, 0, 0, depth, width, 0, depth, width, height, depth});
        sprColours.add(new float[] {1, 1, 1, alpha, 1, 1, 1, alpha, 1, 1, 1, alpha, 1, 1, 1, alpha});
        textures.add(texture);
    }

    protected int addTiledSprite(int resourceId, float depth, float alpha, Vec dimensions)
    {
        Texture texture = TextureLoader.loadTexture(RenderedState.getContext(), resourceId);
        width = Screen.screen.getPixelDensity(texture.getWidth() * PIXEL_SCALE);
        height = Screen.screen.getPixelDensity(texture.getHeight() * PIXEL_SCALE);
        sprVertices.add(new float[] {0, dimensions.y, depth, 0, 0, depth, dimensions.x, 0, depth, dimensions.x, dimensions.y, depth});
        sprColours.add(new float[] {1, 1, 1, alpha, 1, 1, 1, alpha, 1, 1, 1, alpha, 1, 1, 1, alpha});
        textures.add(texture);

        return (int) (dimensions.x / width);
    }

    protected Animation getAnimation(String id)
    {
        for(int i = 0; i < animations.size(); i++)
        {
            if(animations.get(i).getId().equals(id))
            {
                return animations.get(i);
            }
        }
        return null;
    }

    public ArrayList<Texture> getTextures()
    {
        return textures;
    }

    public ArrayList<float[]> getRenderVertices()
    {
        return sprVertices;
    }

    public ArrayList<float[]> getRenderColours()
    {
        return sprColours;
    }

    public boolean isRendered()
    {
        if(sprVertices.isEmpty())
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public ArrayList<Animation> getAnimations()
    {
        return animations;
    }

    public ArrayList<float[]> getActiveFrames()
    {
        return activeFrames;
    }

    public short[] getIndices()
    {
        return indices;
    }

    public boolean isAnimated()
    {
        if(animations != null && !animations.isEmpty())
        {
            return true;
        }
        return false;
    }

    public void animate()
    {
        //boolean[] isAnimated = new boolean[textures.size()];
        boolean[] isAnimated = ArrayPool.booleanMaster.get(textures.size());
        for(int i = 0; i < isAnimated.length; i++)
        {
            isAnimated[i] = false;
        }

        for(int i = 0; i < animations.size(); i++)
        {
            animations.get(i).update();
            if(animations.get(i).isActive())
            {
                //animations.get(i).setFrameUpdated(false);
                isAnimated[animations.get(i).getParent()] = true;
                changeFrame(animations.get(i).getParent(), animations.get(i).getActiveFrameCoords());
                if(animations.get(i).hasAnimationVertices())
                {
                    changeVertices(animations.get(i).getParent(), animations.get(i).getActiveAnimationVertex());
                }
            }
            /*else
            {
                if(animations.get(i).hasAnimationVertices())
                {
                    changeVertices(animations.get(i).getParent(), animations.get(i).getOriginalAnimationVertex());
                }
            }*/
        }
        for(int i = 0; i < isAnimated.length; i++)
        {
            if(!isAnimated[i])
            {
                changeFrame(i, defaultFrames.get(i));
            }
        }
        ArrayPool.booleanMaster.recycle(isAnimated, isAnimated.length);
    }

    public boolean isCurrentlyAnimated()
    {
        for(int i = 0; i < animations.size(); i++)
        {
            if(animations.get(i).isActive())
            {
                return true;
            }
        }
        return false;
    }

    public boolean hasVerticesChanged()
    {
        return verticesChanged;
    }

    public void setVerticesChanged(boolean value)
    {
        verticesChanged = value;
    }

    public void changeVertices(int position, float[] vertices)
    {
        sprVertices.set(position, vertices);
        verticesChanged = true;
    }

    public void changeVertices(int texPosition, float positionX, float positionY, float width, float height, float depth)
    {
        float vertices[] = sprVertices.get(texPosition);
        vertices[0] = positionX;
        vertices[1] = positionY + height;
        vertices[2] = depth;
        vertices[3] = positionX;
        vertices[4] = positionY;
        vertices[5] = depth;
        vertices[6] = positionX + width;
        vertices[7] = positionY;
        vertices[8] = depth;
        vertices[9] = positionX + width;
        vertices[10] = positionY + height;
        vertices[11] = depth;
        verticesChanged = true;
    }

    public void changeVerticesFlippedX(int texPosition, float positionX, float positionY, float width, float height, float depth)
    {
        float vertices[] = sprVertices.get(texPosition);
        vertices[0] = positionX + width;
        vertices[1] = positionY + height;
        vertices[2] = depth;
        vertices[3] = positionX + width;
        vertices[4] = positionY;
        vertices[5] = depth;
        vertices[6] = positionX;
        vertices[7] = positionY;
        vertices[8] = depth;
        vertices[9] = positionX;
        vertices[10] = positionY + height;
        vertices[11] = depth;
        verticesChanged = true;
    }

    public void changeVerticesDepth(int texPosition, float depth)
    {
        float vertices[] = sprVertices.get(texPosition);
        vertices[2] = depth;
        vertices[5] = depth;
        vertices[8] = depth;
        vertices[11] = depth;
        verticesChanged = true;
    }

    public void changeVerticesX(int texPosition, float x1, float x2, float x3, float x4)
    {
        float vertices[] = sprVertices.get(texPosition);
        vertices[0] = x1;
        vertices[3] = x2;
        vertices[6] = x3;
        vertices[9] = x4;
        verticesChanged = true;
    }

    public boolean hasColoursChanged()
    {
        return coloursChanged;
    }

    public void setColoursChanged(boolean value)
    {
        coloursChanged = value;
    }

    public void changeColours(int position, float[] colours)
    {
        sprColours.set(position, colours);
        coloursChanged = true;
    }

    public void changeColours(int position, float flatColour)
    {
        float[] colours = sprColours.get(position);
        for(int i = 0; i < colours.length; i++)
        {
            colours[i] = flatColour;
        }
        coloursChanged = true;
    }

    public void changeColourAlpha(int position, float alpha)
    {
        float[] colours = sprColours.get(position);
        colours[3] = alpha;
        colours[7] = alpha;
        colours[11] = alpha;
        colours[15] = alpha;
        coloursChanged = true;
    }

    public boolean hasFrameChanged()
    {
        return frameChanged;
    }

    public void setFrameChanged(boolean value)
    {
        frameChanged = value;
    }

    public void changeFrame(int position, int frame)
    {
        if(!Arrays.equals(activeFrames.get(position), textures.get(position).getFrame(frame)))
        {
            activeFrames.set(position, textures.get(position).getFrame(frame));
            frameChanged = true;
        }
    }

    public void changeFrame(int position, float[] frameCoords)
    {
        if(!Arrays.equals(activeFrames.get(position), frameCoords))
        {
            activeFrames.set(position, frameCoords);
            frameChanged = true;
        }
    }

    public boolean hasIndicesChanged()
    {
        return indicesChanged;
    }

    public void setIndicesChanged(boolean value)
    {
        indicesChanged = value;
    }

    public void changeIndices(int position, short[] indices)
    {
        this.indices = indices;
    }

    public boolean hasAlpha()
    {
        return hasAlpha;
    }

    public boolean isTiled()
    {
        return tiled;
    }

    public boolean isStoringBitmap()
    {
        return storeBitmap;
    }

    @Override
    public abstract void getInput(InputBuffer input);

    @Override
    public abstract void update();

    public void destruct()
    {
        hasAlpha = true;
        tiled = false;

        verticesChanged = false;
        coloursChanged = false;
        textureChanged = false;
        frameChanged = false;
        indicesChanged = false;

        for(int i = 0; i < textures.size(); i++)
        {
            textures.get(i).destruct();
            Texture.pool.recycle(textures.get(i));
        }
        textures.clear();
        ArrayPool.texturePool.recycle(textures);

        for(int i = 0; i < sprVertices.size(); i++)
        {
            ArrayPool.floatMaster.recycle(sprVertices.get(i), sprVertices.get(i).length);
        }
        sprVertices.clear();
        ArrayPool.floatArrayPool.recycle(sprVertices);

        for(int i = 0; i < sprColours.size(); i++)
        {
            ArrayPool.floatMaster.recycle(sprColours.get(i), sprColours.get(i).length);
        }
        sprColours.clear();
        ArrayPool.floatArrayPool.recycle(sprColours);

        for(int i = 0; i < activeFrames.size(); i++)
        {
            if(activeFrames.get(i) != null)
            {
                ArrayPool.floatMaster.recycle(activeFrames.get(i), activeFrames.get(i).length);
            }
        }
        activeFrames.clear();
        ArrayPool.floatArrayPool.recycle(activeFrames);

        ArrayPool.shortMaster.recycle(indices, indices.length);

        for(int i = 0; i < animations.size(); i++)
        {
            animations.get(i).destruct();
            Animation.pool.recycle(animations.get(i));
        }
        animations.clear();
        ArrayPool.animationPool.recycle(animations);

        for(int i = 0; i < defaultFrames.size(); i++)
        {
            ArrayPool.intPool.recycle(defaultFrames.get(i));
        }
        defaultFrames.clear();
        ArrayPool.intObjectPool.recycle(defaultFrames);
    }
}
