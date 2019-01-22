package game.android.com.catstacks.engine.pool;

import android.graphics.Bitmap;
import android.os.Message;

import java.util.ArrayList;
import java.util.Random;

import game.android.com.catstacks.engine.objects.WorldObject;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.QuadTree;
import game.android.com.catstacks.engine.physics.Vec;

public class ArrayPool
{
    public static final GenericObjectPool<ArrayList> vertexPool = new GenericObjectPool<ArrayList>(ArrayList.class);
    public static final GenericObjectPool<ArrayList> objectsPool = new GenericObjectPool<ArrayList>(ArrayList.class);
    public static final GenericObjectPool<ArrayList> edgePool = new GenericObjectPool<ArrayList>(ArrayList.class);
    public static final GenericObjectPool<ArrayList> animationPool = new GenericObjectPool<ArrayList>(ArrayList.class);
    public static final GenericObjectPool<ArrayList> texturePool = new GenericObjectPool<ArrayList>(ArrayList.class);
    public static final GenericObjectPool<ArrayList> spritePool = new GenericObjectPool<ArrayList>(ArrayList.class);
    public static final GenericObjectPool<ArrayList> floatArrayPool = new GenericObjectPool<ArrayList>(ArrayList.class);
    public static final GenericObjectPool<ArrayList> intObjectPool = new GenericObjectPool<ArrayList>(ArrayList.class);
    public static final GenericObjectPool<ArrayList> pairPool = new GenericObjectPool<ArrayList>(ArrayList.class);
    public static final GenericObjectPool<ArrayList> manifoldPool = new GenericObjectPool<ArrayList>(ArrayList.class);

    public static final ArrayMasterPool<float[]> floatMaster = new ArrayMasterPool<float[]>(float[].class);
    public static final ArrayMasterPool<short[]> shortMaster = new ArrayMasterPool<short[]>(short[].class);
    public static final ArrayMasterPool<boolean[]> booleanMaster = new ArrayMasterPool<boolean[]>(boolean[].class);
    public static final ArrayMasterPool<BackgroundScenery[]> backgroundSceneryPool = new ArrayMasterPool<BackgroundScenery[]>(BackgroundScenery[].class);
    public static final ArrayMasterPool<Vec[]> vertexMaster = new ArrayMasterPool<Vec[]>(Vec[].class);
    public static final ArrayMasterPool<QuadTree[]> quadMaster = new ArrayMasterPool<QuadTree[]>(QuadTree[].class);

    public static final GenericObjectPool<String> stringPool = new GenericObjectPool<String>(String.class);
    public static final GenericObjectPool<Integer> intPool = new GenericObjectPool<Integer>(Integer.class);
    public static final GenericObjectPool<Bitmap> bitmapPool = new GenericObjectPool<Bitmap>(Bitmap.class);
    public static final GenericObjectPool<Random> randomPool = new GenericObjectPool<Random>(Random.class);
}
