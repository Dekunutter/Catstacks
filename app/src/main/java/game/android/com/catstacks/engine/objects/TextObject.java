package game.android.com.catstacks.engine.objects;

import android.util.Log;

import java.util.Arrays;

import game.android.com.catstacks.engine.core.Screen;
import game.android.com.catstacks.engine.pool.ArrayPool;

public class TextObject extends WorldObject
{
    protected String text;
    protected float x, y, size, setColour;
    protected boolean isCentered;
    protected float[] vertices;
    protected float[] colours;
    protected float[] uvs;

    private int indexVertices, indexColours, indexUVs, indexIndices;

    //private static int[] l_size = {36,29,30,34,25,25,34,33,11,20,31,24,48,35,39,29,42,31,27,31,34,35,46,35,31,27,30,26,28,26,31,28,28,28,29,29,14,24,30,18,26,14,14,14,25,28,31,0,0,38,39,12,36,34,0,0,0,38,0,0,0,0,0,0};
    private static int[] l_size = {48,48,48,48,48,40,48,40,16,48,48,48,60,60,60,36,56,48,48,48,40,48,64,48,48,48,48,32,48,48,48,48,48,48,48,48,16,48,32,32,32,16,16,16,48,48,56,64,0,64,64,32,64,64,0,0,64,0,0,0,0,0,0};

    private static final float RI_TEXT_UV_BOX_WIDTH = 0.125f;
    //private static final float RI_TEXT_WIDTH = Screen.screen.getPixelDensity(32);
    private static final float RI_TEXT_WIDTH = Screen.screen.getPixelDensity(28);
    private static final float RI_TEXT_SPACESIZE = Screen.screen.getPixelDensity(20);

    public TextObject(float x, float y, boolean isCentered)
    {
        super();

        text = "";
        this.x = x;
        this.y = y;
        size = 1;
        setColour = 1;

        solid = false;

        this.isCentered = isCentered;
    }

    public TextObject(String text, float x, float y)
    {
        super();

        this.text = text;
        this.x = x;
        this.y = y;
        this.size = 1;
        setColour = 1;

        solid = false;
        isCentered = false;
    }

    public TextObject(String text, float size, float x, float y)
    {
        super();

        this.text = text;
        this.x = x;
        this.y = y;
        this.size = size;
        setColour = 1;

        solid = false;
        isCentered = false;
    }

    public TextObject(String text, float size, float x, float y, float setColour)
    {
        super();

        this.text = text;
        this.x = x;
        this.y = y;
        this.size = size;
        this.setColour = setColour;

        solid = false;
        isCentered = false;
    }

    public TextObject(String text, float x, float y, boolean isCentered)
    {
        this(text, x, y);

        this.isCentered = isCentered;
    }

    public TextObject(String text, float size, float x, float y, boolean isCentered)
    {
        this(text, size, x, y);

        this.isCentered = isCentered;
    }

    public TextObject(String text, float size, float x, float y, float setColour, boolean isCentered)
    {
        this(text, size, x, y, setColour);

        this.isCentered = isCentered;
    }

    public String getText()
    {
        return text;
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public float getSize()
    {
        return size;
    }

    protected void setText(String value)
    {
        text = value;
    }

    protected void prepareDraw()
    {
        int charCount = text.length();
        if(charCount <= 0)
        {
            return;
        }

        indexVertices = 0;
        indexColours = 0;
        indexUVs = 0;
        indexIndices = 0;

        /*if(vertices != null)
        {
            ArrayPool.floatMaster.recycle(vertices, vertices.length);
        }
        if(colours != null)
        {
            ArrayPool.floatMaster.recycle(colours, colours.length);
        }
        if(uvs != null)
        {
            ArrayPool.floatMaster.recycle(uvs, uvs.length);
        }
        if(indices != null)
        {
            ArrayPool.shortMaster.recycle(indices, indices.length);
        }*/

        vertices = ArrayPool.floatMaster.get(charCount * 12);
        colours = ArrayPool.floatMaster.get(charCount * 16);
        uvs = ArrayPool.floatMaster.get(charCount * 8);
        indices = ArrayPool.shortMaster.get(charCount * 6);

        float x = this.x;
        float y = this.y;

        //could redo all this code to be more optimal, but not now. Later maybe. Doesn't need a second standalone loop for this
        if(isCentered)
        {
            float newX = x;
            for(int i = 0; i < charCount; i++)
            {
                char c = text.charAt(i);
                int c_val = (int) c;

                int index = convertCharToIndex(c_val);

                if(index == - 1)
                {
                    newX += (RI_TEXT_SPACESIZE * size);
                    continue;
                }

                newX += ((Screen.screen.getPixelDensity(l_size[index]) / 2 * (size)));
            }
            x -= ((newX - x) / 2);
        }

        for(int i = 0; i < charCount; i++)
        {
            char c = text.charAt(i);
            int c_val = (int) c;

            int index = convertCharToIndex(c_val);

            if(index == -1)
            {
                x += (RI_TEXT_SPACESIZE * size);
                continue;
            }

            //int row = index / 8;
            int row = index / 8;
            int column = index % 8;

            float v = row * RI_TEXT_UV_BOX_WIDTH;
            float v2 = v + RI_TEXT_UV_BOX_WIDTH;
            float u = column * RI_TEXT_UV_BOX_WIDTH;
            float u2 = u + RI_TEXT_UV_BOX_WIDTH;

            float[] vertices = ArrayPool.floatMaster.get(12);
            float[] colours = ArrayPool.floatMaster.get(16);
            float[] uv = ArrayPool.floatMaster.get(8);

            vertices[0] = x;
            vertices[1] = y + (RI_TEXT_WIDTH * size);
            vertices[2] = 0.99f;
            vertices[3] = x;
            vertices[4] = y;
            vertices[5] = 0.99f;
            vertices[6] = x + (RI_TEXT_WIDTH * size);
            vertices[7] = y;
            vertices[8] = 0.99f;
            vertices[9] = x + (RI_TEXT_WIDTH * size);
            vertices[10] = y + (RI_TEXT_WIDTH * size);
            vertices[11] = 0.99f;

            colours[0] = setColour;
            colours[1] = setColour;
            colours[2] = setColour;
            colours[3] = 1;
            colours[4] = setColour;
            colours[5] = setColour;
            colours[6] = setColour;
            colours[7] = 1;
            colours[8] = setColour;
            colours[9] = setColour;
            colours[10] = setColour;
            colours[11] = 1;
            colours[12] = setColour;
            colours[13] = setColour;
            colours[14] = setColour;
            colours[15] = 1;

            /*uv[0] = u + 0.001f;
            uv[1] = v + 0.001f;
            uv[2] = u + 0.001f;
            uv[3] = v2 - 0.001f;
            uv[4] = u2 - 0.001f;
            uv[5] = v2 - 0.001f;
            uv[6] = u2 - 0.001f;
            uv[7] = v + 0.001f;*/
            uv[0] = u;
            uv[1] = v;
            uv[2] = u;
            uv[3] = v2;
            uv[4] = u2;
            uv[5] = v2;
            uv[6] = u2;
            uv[7] = v;

            short[] indices = ArrayPool.shortMaster.get(6);
            indices[0] = 0;
            indices[1] = 1;
            indices[2] = 2;
            indices[3] = 0;
            indices[4] = 2;
            indices[5] = 3;

            addCharRenderInfo(vertices, colours, uv, indices);

            x += ((Screen.screen.getPixelDensity(l_size[index]) / 2 * (size)));

            ArrayPool.floatMaster.recycle(vertices, vertices.length);
            ArrayPool.floatMaster.recycle(colours, colours.length);
            ArrayPool.floatMaster.recycle(uv, uv.length);
            ArrayPool.shortMaster.recycle(indices, indices.length);
        }

        if(sprVertices.isEmpty())
        {
            sprVertices.add(vertices);
        }
        else
        {
            //sprVertices.set(0, vertices);
            changeVertices(0, vertices);
        }

        if(sprColours.isEmpty())
        {
            sprColours.add(colours);
        }
        else
        {
            changeColours(0, colours);
        }

        if(activeFrames.isEmpty())
        {
            activeFrames.add(uvs);
        }
        else
        {
            //activeFrames.set(0, uvs);
            changeFrame(0, uvs);
        }
    }

    private int convertCharToIndex(int c_val)
    {
        int index = -1;

        if(c_val > 64 && c_val < 91)
        {
            index = c_val - 65;
        }
        else if(c_val > 96 && c_val < 123)
        {
            index = c_val - 97;
        }
        else if(c_val > 47 && c_val < 58)
        {
            index = c_val - 48 + 26;
        }
        else if(c_val == 43)
        {
            index = 38;
        }
        else if(c_val == 45)
        {
            index = 39;
        }
        else if(c_val == 33)
        {
            index = 36;
        }
        else if(c_val == 63)
        {
            index = 37;
        }
        else if(c_val == 61)
        {
            index = 40;
        }
        else if(c_val == 58)
        {
            index = 41;
        }
        else if(c_val == 46)
        {
            index = 42;
        }
        else if(c_val == 44)
        {
            index = 43;
        }
        else if(c_val == 42)
        {
            index = 44;
        }
        else if(c_val == 36)
        {
            index = 45;
        }
        else if(c_val == 47)
        {
            index = 47;
        }

        return index;
    }

    private void addCharRenderInfo(float[] vertices, float[] colours, float[] uv, short[] indices)
    {
        short base = (short) (indexVertices / 3);

        for(int i = 0; i < vertices.length; i++)
        {
            this.vertices[indexVertices] = vertices[i];
            indexVertices++;
        }

        for(int i = 0; i < colours.length; i++)
        {
            this.colours[indexColours] = colours[i];
            indexColours++;
        }

        for(int i = 0; i < uv.length; i++)
        {
            this.uvs[indexUVs] = uv[i];
            indexUVs++;
        }

        for(int i = 0; i < indices.length; i++)
        {
            this.indices[indexIndices] = (short) (base + indices[i]);
            indexIndices++;
            indicesChanged = true;
        }
    }
}
