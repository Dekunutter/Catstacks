package game.android.com.catstacks.game.objects.ui;

import android.content.Context;

import java.util.ArrayList;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.physics.CollisionBitmasks;
import game.android.com.catstacks.engine.physics.Polygon;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.pool.GenericObjectPool;
import game.android.com.catstacks.engine.rendering.Animation;
import game.android.com.catstacks.engine.sound.Sound;

public class SingingCat extends MenuButton
{
    private Context context;

    private Sound meowSound;

    private float[] row1X = {23, 46, 69, 92, 115, 138, 161, 184};
    private float[] row1Y = {0, 22};
    private float[] row2X = {23};
    private float[] row2Y = {22, 44};
    private float[] row3X = {23, 46, 69};
    private float[] row3Y = {44, 66};
    private float[] row4X = {23, 46};
    private float[] row4Y = {66, 88};
    private float[] row5X = {22, 44, 66, 88, 110, 132, 154, 176};
    private float[] row5Y = {88, 113};
    private float[] row6X = {31, 62};
    private float[] row6Y = {138, 154};
    private float[] row7X = {37, 74, 107};
    private float[] row7Y = {154, 176};
    private float[] row8X = {41, 82, 123, 164, 205};
    private float[] row8Y = {176, 193};
    private float[] row9X = {41, 82, 123, 164};
    private float[] row9Y = {193, 210};
    private float[][] rowsX = {row1X, row2X, row3X, row4X, row5X, row6X, row7X, row8X, row9X};
    private float[][] rowsY = {row1Y, row2Y, row3Y, row4Y, row5Y, row6Y, row7Y, row8Y, row9Y};

    private final static int STACKING_FRAME_1 = 0;
    private final static int STACKING_FRAME_2 = 1;
    private final static int STACKING_FRAME_3 = 2;
    private final static int STACKING_FRAME_4 = 3;
    private final static int STACKING_FRAME_5 = 4;
    private final static int STACKING_FRAME_6 = 5;
    private final static int STACKING_FRAME_7 = 6;
    private final static int STACKING_FRAME_8 = 7;
    private final static int STACKING_FRAME_9 = 8;

    private final static String MEOWING_ANIM = "meowing";

    public static final GenericObjectPool<SingingCat> pool = new GenericObjectPool<SingingCat>(SingingCat.class);

    public SingingCat()
    {

    }

    public SingingCat(Vec position, int soundId)
    {
        float[] row1X = {23, 46, 69, 92, 115, 138, 161, 184};
        float[] row1Y = {0, 22};
        float[] row2X = {23};
        float[] row2Y = {22, 44};
        float[] row3X = {23, 46, 69};
        float[] row3Y = {44, 66};
        float[] row4X = {23, 46};
        float[] row4Y = {66, 88};
        float[] row5X = {22, 44, 66, 88, 110, 132, 154, 176};
        float[] row5Y = {88, 113};
        float[] row6X = {31, 62};
        float[] row6Y = {138, 154};
        float[] row7X = {37, 74, 107};
        float[] row7Y = {154, 176};
        float[] row8X = {41, 82, 123, 164, 205};
        float[] row8Y = {176, 193};
        float[] row9X = {41, 82, 123, 164};
        float[] row9Y = {193, 210};
        float[][] rowsX = {row1X, row2X, row3X, row4X, row5X, row6X, row7X, row8X, row9X};
        float[][] rowsY = {row1Y, row2Y, row3Y, row4Y, row5Y, row6Y, row7Y, row8Y, row9Y};

        addSprite(R.drawable.cat, rowsX, rowsY, 0, 1, 23, 22);

        activeFrames.add(textures.get(0).getFrame(UNPRESSED_FRAME));
        defaultFrames.add(UNPRESSED_FRAME);

        float vertices[] = ArrayPool.floatMaster.get(12);
        vertices[0] = 0;
        vertices[1] = 0;
        vertices[2] = 0;
        vertices[3] = 0;
        vertices[4] =height;
        vertices[5] = 0;
        vertices[6] = width;
        vertices[7] = height;
        vertices[8] = 0;
        vertices[9] = width;
        vertices[10] = 0;
        vertices[11] = 0;

        body = new Polygon(position.x, position.y, 0, vertices);
        body.move(position.x, position.y, 0);

        ArrayPool.floatMaster.recycle(vertices, vertices.length);

        unmovable = true;

        pushedDown = false;
        pressed = false;

        mask = CollisionBitmasks.UI_ID;

        solid = false;

        initAnimations();

        initSounds(soundId);
    }

    public void init(Vec position, int soundId)
    {
        addSprite(R.drawable.cat, rowsX, rowsY, 0, 1, 23, 22);

        activeFrames.add(textures.get(0).getFrame(UNPRESSED_FRAME));
        defaultFrames.add(UNPRESSED_FRAME);

        //float vertices[] = {0, 0, 0, 0, height, 0, width, height, 0, width, 0, 0};
        float[] vertices = ArrayPool.floatMaster.get(12);
        vertices[0] = 0;
        vertices[1] = 0;
        vertices[2] = 0;
        vertices[3] = 0;
        vertices[4] = height;
        vertices[5] = 0;
        vertices[6] = width;
        vertices[7] = height;
        vertices[8] = 0;
        vertices[9] = width;
        vertices[10] = 0;
        vertices[11] = 0;
        body = Polygon.pool.get();
        body.init(position.x, position.y, 0, vertices);
        //body = new Polygon(position.x, position.y, 0, vertices);
        body.move(position.x, position.y, 0);

        unmovable = true;

        pushedDown = false;
        pressed = false;

        mask = CollisionBitmasks.UI_ID;

        solid = false;

        initAnimations();

        initSounds(soundId);

        ArrayPool.floatMaster.recycle(vertices, vertices.length);
    }

    private void initAnimations()
    {
        animations = ArrayPool.animationPool.get();
        //animations = new ArrayList<>();

        //ArrayList<float[]> frames =  new ArrayList<>();
        ArrayList<float[]> frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(STACKING_FRAME_1));
        frames.add(textures.get(0).getFrame(STACKING_FRAME_2));
        frames.add(textures.get(0).getFrame(STACKING_FRAME_3));
        frames.add(textures.get(0).getFrame(STACKING_FRAME_4));
        frames.add(textures.get(0).getFrame(STACKING_FRAME_5));
        frames.add(textures.get(0).getFrame(STACKING_FRAME_6));
        frames.add(textures.get(0).getFrame(STACKING_FRAME_7));
        frames.add(textures.get(0).getFrame(STACKING_FRAME_8));
        frames.add(textures.get(0).getFrame(STACKING_FRAME_9));
        //Animation meowing = new Animation(MEOWING_ANIM, 0, frames, 3);
        Animation meowing = Animation.pool.get();
        meowing.init(MEOWING_ANIM, 0, frames, 3);
        meowing.setLooping(false);

        animations.add(meowing);

        //frames.clear();
        //floatArrayPool.recycle(frames);
    }

    private void initSounds(int soundId)
    {
        meowSound = Sound.pool.get();
        meowSound.init(soundId);
    }

    @Override
    public void update()
    {
        if(!getAnimation(MEOWING_ANIM).isActive())
        {
            if(pushedDown)
            {
                changeFrame(0, PRESSED_FRAME);
            }
            else
            {
                changeFrame(0, UNPRESSED_FRAME);
            }
        }

        if(hasBeenPressed())
        {
            if(getAnimation(MEOWING_ANIM) != null && !getAnimation(MEOWING_ANIM).isActive())
            {
                getAnimation(MEOWING_ANIM).activate();
                meowSound.playSound();

                pressed = false;
            }
        }
    }

    public void destruct()
    {
        super.destruct();
        SingingCat.pool.recycle(this);
    }
}
