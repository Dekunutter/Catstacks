package game.android.com.catstacks.engine.rendering;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class TextureLoader
{
    public static Texture loadTexture(final Context context, int resource)
    {
        return loadTexture(context, resource, 1, 1);
    }

    public static Texture loadTexture(final Context context, int resource, int x, int y)
    {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resource, options);

        //return new Texture(bitmap, x, y);
        Texture texture = Texture.pool.get();
        texture.init(bitmap, x, y);
        return texture;
    }

    public static Texture loadTexture(final Context context, int resource, float[] coordsX, float[] coordsY)
    {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resource, options);

        if(coordsX == null || coordsX.length == 0)
        {
            coordsX = new float[] {bitmap.getWidth()};
        }
        if(coordsY == null || coordsY.length == 0)
        {
            coordsY = new float[] {bitmap.getHeight()};
        }

        //return new Texture(bitmap, coordsX, coordsY);
        Texture texture = Texture.pool.get();
        texture.init(bitmap, coordsX, coordsY);
        return texture;
    }

    public static Texture loadTexture(final Context context, int resource, float[][] rowsX, float[][] rowsY)
    {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resource, options);

        //return new Texture(bitmap, rowsX, rowsY);
        Texture texture = Texture.pool.get();
        texture.init(bitmap, rowsX, rowsY);
        return texture;
    }
}
