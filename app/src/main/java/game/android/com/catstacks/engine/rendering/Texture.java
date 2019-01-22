package game.android.com.catstacks.engine.rendering;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.pool.GenericObjectPool;

public class Texture
{
    private Bitmap bitmap;
    private int tilesX, tilesY;
    ArrayList<float[]> frames;

    public static final GenericObjectPool<Texture> pool = new GenericObjectPool<Texture>(Texture.class);

    public Texture()
    {

    }

    public Texture(Bitmap bitmap, int tilesX, int tilesY)
    {
        this.bitmap = bitmap;
        this.tilesX = tilesX;
        this.tilesY = tilesY;

        if(tilesX > 1 || tilesY > 1)
        {
            //converts tiled texture portions into non-tiled texture coords
            /*float[] coordsX = new float[tilesX];
            float valueX = (float) (bitmap.getWidth() / tilesX);
            for(int i = 0; i < tilesX; i++)
            {
                coordsX[i] = valueX * (i + 1);
            }

            float[] coordsY = new float[tilesY];
            float valueY = (float) (bitmap.getHeight() / tilesY);
            for(int i = 0; i < tilesY; i++)
            {
                coordsY[i] = valueY * (i + 1);
            }

            initFrames(coordsX, coordsY);*/

            initFrames();
        }
    }

    public void init(Bitmap bitmap, int tilesX, int tilesY)
    {
        this.bitmap = bitmap;
        this.tilesX = tilesX;
        this.tilesY = tilesY;

        if(tilesX > 1 || tilesY > 1)
        {
            initFrames();
        }
    }

    public Texture(Bitmap bitmap, float[] coordsX, float[] coordsY)
    {
        this.bitmap = bitmap;

        if(coordsX.length > 1 || coordsY.length > 1)
        {
            initFrames(coordsX, coordsY);
        }
    }

    public void init(Bitmap bitmap, float[] coordsX, float[] coordsY)
    {
        this.bitmap = bitmap;

        if(coordsX.length > 1 || coordsY.length > 1)
        {
            initFrames(coordsX, coordsY);
        }
    }

    public Texture(Bitmap bitmap, float[][] rowsX, float[][] rowsY)
    {
        this.bitmap = bitmap;

        initFrames(rowsX, rowsY);
    }

    public void init(Bitmap bitmap, float[][] rowsX, float[][] rowsY)
    {
        this.bitmap = bitmap;

        initFrames(rowsX, rowsY);
    }

    public int getWidth()
    {
        return bitmap.getWidth() / tilesX;
    }

    public int getHeight()
    {
        return bitmap.getHeight() / tilesY;
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }

    private void initFrames()
    {
        float incrementX = 1.0f / tilesX;
        float incrementY = 1.0f / tilesY;

        frames = new ArrayList<float[]>();

        for(int i = 0; i < tilesY; i++)
        {
            for(int j = 0; j < tilesX; j++)
            {
                float[] frame = {(incrementX * j), (incrementY * i), (incrementX * j), (incrementY * (i + 1)), (incrementX * (j + 1)), (incrementY * (i + 1)), (incrementX * (j + 1)), (incrementY * i)};
                frames.add(frame);
            }
        }
    }

    private void initFrames(float[] coordsX, float[] coordsY)
    {
        frames = new ArrayList<>();
        float starterCoordsX = 0, starterCoordsY = 0;

        for(int i = 0; i < coordsY.length; i++)
        {
            float valueY = 1 / (bitmap.getHeight() / coordsY[i]);
            for(int j = 0; j < coordsX.length; j++)
            {
                float valueX = 1 / (bitmap.getWidth() / coordsX[j]);
                float[] frame = {starterCoordsX, starterCoordsY, starterCoordsX, valueY, valueX, valueY, valueX, starterCoordsY};
                frames.add(frame);
                starterCoordsX = valueX;
            }
            starterCoordsY = valueY;
            starterCoordsX = 0;
        }
    }

    private void initFrames(float[][] rowsX, float[][] rowsY)
    {
        frames = new ArrayList<>();
        float starterCoordsX = 0;

        for(int i = 0; i < rowsY.length; i++)
        {
            float upperY = 1 / (bitmap.getHeight() / rowsY[i][0]);
            float lowerY = 1 / (bitmap.getHeight() / rowsY[i][1]);
            for(int j = 0; j < rowsX[i].length; j++)
            {
                float valueX = 1 / (bitmap.getWidth() / rowsX[i][j]);
                float[] frame = {starterCoordsX, upperY, starterCoordsX, lowerY, valueX, lowerY, valueX, upperY};
                frames.add(frame);
                starterCoordsX = valueX;
            }
            starterCoordsX = 0;
        }
    }

    public float[] getFrame(int position)
    {
        return frames.get(position);
    }

    public int getFrameCount()
    {
        return frames.size();
    }

    public boolean isTiled()
    {
        if(frames != null && !frames.isEmpty())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void destruct()
    {
        //ArrayPool.bitmapPool.recycle(bitmap);

        if(frames != null)
        {
            for(int i = 0; i < frames.size(); i++)
            {
                ArrayPool.floatMaster.recycle(frames.get(i), frames.get(i).length);
            }
            frames.clear();
            ArrayPool.floatArrayPool.recycle(frames);
        }
    }
}
