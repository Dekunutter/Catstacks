package game.android.com.catstacks.game.objects.dynamic;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.core.Screen;
import game.android.com.catstacks.engine.core.Time;
import game.android.com.catstacks.engine.objects.WorldObject;
import game.android.com.catstacks.engine.physics.CollisionBitmasks;
import game.android.com.catstacks.engine.physics.Polygon;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.pool.CatObjectPool;
import game.android.com.catstacks.engine.rendering.Animation;
import game.android.com.catstacks.engine.rendering.Texture;
import game.android.com.catstacks.engine.sound.Sound;
import game.android.com.catstacks.engine.states.World;

public class Cat extends StackableObject
{
    protected boolean landed, deposited, lost;
    private float jumping;
    protected float blinkTimer, meowTimer, flickTimer, wagTimer, despawnTimer, depositTimer;

    private boolean movingLeft, movingRight;

    private Sound meowSound, catchSound1, catchSound2, catchSound3;

    private final static int DESPAWN_TIME = 30;
    private final static int DEPOSIT_TIME = 50;

    private final static int FALLING_FRAME = 22;
    private final static int FALLING_FRAME_BLINK = 23;

    private final static int STACKING_FRAME_1 = 0;
    private final static int STACKING_FRAME_2 = 1;
    private final static int STACKING_FRAME_3 = 2;
    private final static int STACKING_FRAME_4 = 3;
    private final static int STACKING_FRAME_5 = 4;
    private final static int STACKING_FRAME_6 = 5;
    private final static int STACKING_FRAME_7 = 6;
    private final static int STACKING_FRAME_8 = 7;
    private final static int STACKING_FRAME_9 = 8;
    private final static int POUNCE_FRAME_1 = 9;
    private final static int POUNCE_FRAME_2 = 10;
    private final static int POUNCE_FRAME_3 = 11;
    private final static int FLICK_FRAME_1 = 12;
    private final static int FLICK_FRAME_2 = 13;
    private final static int WAG_FRAME_1 = 36;
    private final static int WAG_FRAME_2 = 37;
    private final static int WAG_FRAME_3 = 38;
    private final static int WAG_FRAME_4 = 39;
    private final static int WAG_FRAME_5 = 40;
    private final static int WAG_FRAME_6 = 41;
    private final static int WAG_FRAME_7 = 42;
    private final static int WAG_FRAME_8 = 43;

    private final static int WALKING_FRAME_1 = 27;
    private final static int WALKING_FRAME_2 = 28;
    private final static int WALKING_FRAME_3 = 29;
    private final static int WALKING_FRAME_4 = 30;
    private final static int WALKING_FRAME_5 = 31;
    private final static int WALKING_FRAME_6 = 32;
    private final static int WALKING_FRAME_7 = 33;
    private final static int WALKING_FRAME_8 = 34;
    private final static int WALKING_FRAME_9 = 35;

    private final static int DANGLING_FRAME_1 = 14;
    private final static int DANGLING_FRAME_2 = 15;
    private final static int DANGLING_FRAME_3 = 16;
    private final static int DANGLING_FRAME_4 = 17;
    private final static int DANGLING_FRAME_5 = 18;
    private final static int DANGLING_FRAME_6 = 19;
    private final static int DANGLING_FRAME_7 = 20;
    private final static int DANGLING_FRAME_8 = 21;

    private final static int ASCENDING_FRAME = 24;
    private final static int MIDJUMP_FRAME = 25;
    private final static int DESCENDING_FRAME = 26;

    private final static String BLINKING_ANIM = "blinking";
    private final static String MEOWING_ANIM = "meowing";
    private final static String WALKING_ANIM = "walking";
    private final static String POUNCE_ANIM = "pounce";
    private final static String FLICKING_ANIM = "flicking";
    private final static String WAGGING_ANIM = "wagging";
    private final static String DANGLE_MEOWING_ANIM = "dangle_meow";

    public static final CatObjectPool<Cat> pool = new CatObjectPool<Cat>();

    public Cat()
    {
        textures = new ArrayList<Texture>();
        sprVertices = new ArrayList<float[]>();
        sprColours = new ArrayList<float[]>();
        activeFrames = new ArrayList<float[]>();
        indices = ArrayPool.shortMaster.get(6);
        indices[0] = 0;
        indices[1] = 1;
        indices[2] = 2;
        indices[3] = 0;
        indices[4] = 2;
        indices[5] = 3;
        animations = new ArrayList<Animation>();
    }

    public Cat(Vec position, Texture texture, Sound meowSound, Sound catchSound1, Sound catchSound2, Sound catchSound3)
    {
        addSpriteFromExistingTexture(texture, -0.1f, 1, 31, 16);

        activeFrames.add(textures.get(0).getFrame(FALLING_FRAME));
        defaultFrames.add(22);

        float[] vertices = ArrayPool.floatMaster.get(12);
        vertices[0] = 0;
        vertices[1] = 0;
        vertices[2] = 0;
        vertices[3] = 0;
        vertices[4] = height / 10;
        vertices[5] = 0;
        vertices[6] = width;
        vertices[7] = height / 10;
        vertices[8] = 0;
        vertices[9] = width;
        vertices[10] = 0;
        vertices[11] = 0;
        body = new Polygon(position.x, position.y, 0, vertices);
        body.move(position.x, position.y, 0);
        body.setMass(5);

        ArrayPool.floatMaster.recycle(vertices, vertices.length);

        mask = CollisionBitmasks.CAT_ID;

        landed = false;
        caught = false;
        deposited = false;
        lost = false;

        stackPosition = 0;
        holding = null;
        heldBy = null;

        Random r = ArrayPool.randomPool.get();
        blinkTimer = r.nextInt(250 - 50 + 1) + 50;
        meowTimer = r.nextInt(400 - 150 + 1) + 150;
        flickTimer = r.nextInt(600 - 250 + 1) + 250;
        wagTimer = r.nextInt(100 - 20 + 1) + 20;
        ArrayPool.randomPool.recycle(r);

        despawnTimer = 0;
        depositTimer = 0;

        initAnimations();

        movingLeft = false;
        movingRight = false;

        storeBitmap = true;

        this.meowSound = meowSound;
        this.catchSound1 = catchSound1;
        this.catchSound2 = catchSound2;
        this.catchSound3 = catchSound3;
    }

    public void init(Vec position, Texture texture, Sound meowSound, Sound catchSound1, Sound catchSound2, Sound catchSound3)
    {
        hasAlpha = true;
        tiled = false;

        verticesChanged = false;
        coloursChanged = false;
        textureChanged = false;
        frameChanged = false;
        indicesChanged = false;

        addSpriteFromExistingTexture(texture, -0.1f, 1, 31, 16);

        activeFrames.add(textures.get(0).getFrame(FALLING_FRAME));
        defaultFrames.add(22);

        float[] vertices = ArrayPool.floatMaster.get(12);
        vertices[0] = 0;
        vertices[1] = 0;
        vertices[2] = 0;
        vertices[3] = 0;
        vertices[4] = height / 10;
        vertices[5] = 0;
        vertices[6] = width;
        vertices[7] = height / 10;
        vertices[8] = 0;
        vertices[9] = width;
        vertices[10] = 0;
        vertices[11] = 0;
        body = Polygon.pool.get();
        body.init(position.x, position.y, 0, vertices);
        body.move(position.x, position.y, 0);
        body.setMass(5);

        ArrayPool.floatMaster.recycle(vertices, vertices.length);

        mask = CollisionBitmasks.CAT_ID;

        landed = false;
        caught = false;
        deposited = false;
        lost = false;

        stackPosition = 0;
        holding = null;
        heldBy = null;

        Random r = ArrayPool.randomPool.get();
        blinkTimer = r.nextInt(250 - 50 + 1) + 50;
        meowTimer = r.nextInt(400 - 150 + 1) + 150;
        flickTimer = r.nextInt(600 - 250 + 1) + 250;
        wagTimer = r.nextInt(100 - 20 + 1) + 20;
        ArrayPool.randomPool.recycle(r);

        despawnTimer = 0;
        depositTimer = 0;

        initAnimations();

        movingLeft = false;
        movingRight = false;

        storeBitmap = true;

        this.meowSound = meowSound;
        this.catchSound1 = catchSound1;
        this.catchSound2 = catchSound2;
        this.catchSound3 = catchSound3;
    }

    private void initAnimations()
    {
        animations = ArrayPool.animationPool.get();

        ArrayList<float[]> frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(FALLING_FRAME));
        frames.add(textures.get(0).getFrame(FALLING_FRAME_BLINK));
        Animation blinking = Animation.pool.get();
        blinking.init(BLINKING_ANIM, 0, frames, 2);
        blinking.setLooping(false);
        animations.add(blinking);

        frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(WALKING_FRAME_1));
        frames.add(textures.get(0).getFrame(WALKING_FRAME_2));
        frames.add(textures.get(0).getFrame(WALKING_FRAME_3));
        frames.add(textures.get(0).getFrame(WALKING_FRAME_4));
        frames.add(textures.get(0).getFrame(WALKING_FRAME_5));
        frames.add(textures.get(0).getFrame(WALKING_FRAME_6));
        frames.add(textures.get(0).getFrame(WALKING_FRAME_7));
        frames.add(textures.get(0).getFrame(WALKING_FRAME_8));
        frames.add(textures.get(0).getFrame(WALKING_FRAME_9));
        Animation walking = Animation.pool.get();
        walking.init(WALKING_ANIM, 0, frames, 2);
        animations.add(walking);

        frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(STACKING_FRAME_1));
        frames.add(textures.get(0).getFrame(STACKING_FRAME_2));
        frames.add(textures.get(0).getFrame(STACKING_FRAME_3));
        frames.add(textures.get(0).getFrame(STACKING_FRAME_4));
        frames.add(textures.get(0).getFrame(STACKING_FRAME_5));
        frames.add(textures.get(0).getFrame(STACKING_FRAME_6));
        frames.add(textures.get(0).getFrame(STACKING_FRAME_7));
        frames.add(textures.get(0).getFrame(STACKING_FRAME_8));
        frames.add(textures.get(0).getFrame(STACKING_FRAME_9));
        Animation meowing = Animation.pool.get();
        meowing.init(MEOWING_ANIM, 0, frames, 3);
        meowing.setLooping(false);
        animations.add(meowing);

        frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(POUNCE_FRAME_1));
        frames.add(textures.get(0).getFrame(POUNCE_FRAME_2));
        frames.add(textures.get(0).getFrame(POUNCE_FRAME_3));
        frames.add(textures.get(0).getFrame(POUNCE_FRAME_2));
        Animation pouncing = Animation.pool.get();
        pouncing.init(POUNCE_ANIM, 0, frames, 5);
        animations.add(pouncing);

        frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(FLICK_FRAME_1));
        frames.add(textures.get(0).getFrame(FLICK_FRAME_2));
        Animation earFlick = Animation.pool.get();
        earFlick.init(FLICKING_ANIM, 0, frames, 1);
        earFlick.setLooping(false);
        animations.add(earFlick);

        frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(WAG_FRAME_1));
        frames.add(textures.get(0).getFrame(WAG_FRAME_2));
        frames.add(textures.get(0).getFrame(WAG_FRAME_3));
        frames.add(textures.get(0).getFrame(WAG_FRAME_4));
        frames.add(textures.get(0).getFrame(WAG_FRAME_5));
        frames.add(textures.get(0).getFrame(WAG_FRAME_6));
        frames.add(textures.get(0).getFrame(WAG_FRAME_7));
        frames.add(textures.get(0).getFrame(WAG_FRAME_8));
        Animation wagging = Animation.pool.get();
        wagging.init(WAGGING_ANIM, 0, frames, 2);
        wagging.setLooping(false);
        animations.add(wagging);

        frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(DANGLING_FRAME_1));
        frames.add(textures.get(0).getFrame(DANGLING_FRAME_2));
        frames.add(textures.get(0).getFrame(DANGLING_FRAME_3));
        frames.add(textures.get(0).getFrame(DANGLING_FRAME_4));
        frames.add(textures.get(0).getFrame(DANGLING_FRAME_5));
        frames.add(textures.get(0).getFrame(DANGLING_FRAME_6));
        frames.add(textures.get(0).getFrame(DANGLING_FRAME_7));
        frames.add(textures.get(0).getFrame(DANGLING_FRAME_8));
        Animation dangleMeow = Animation.pool.get();
        dangleMeow.init(DANGLE_MEOWING_ANIM, 0, frames, 1);
        dangleMeow.setLooping(false);
        animations.add(dangleMeow);
    }

    public boolean isLanded()
    {
        return landed;
    }

    public void setLanded(boolean value)
    {
        landed = value;
    }

    @Override
    public void depositItem(WorldObject depo)
    {
        meowSound.stopSound();

        deposited = true;
        jumpToPosition(depo.getPosition());

        super.depositItem(depo);
    }

    public boolean isDeposited()
    {
        return deposited;
    }

    private void jumpToPosition(Vec position)
    {
        Vec offset = Vec.pool.get();
        offset.x = position.x - body.getPosition().x;
        offset.y = position.y - body.getPosition().y;
        Vec movement = Vec.pool.get();
        movement.x = offset.x / 100;
        movement.y = 160 * Time.getDelta();

        jumping = 20;

        Vec newDisplacement = Vec.pool.get();
        newDisplacement.x = movement.x * getMovementDeltaX();
        newDisplacement.y = movement.y;
        body.setDisplacement(newDisplacement);

        loadJumpingSprite();

        Vec.pool.recycle(newDisplacement);
        Vec.pool.recycle(movement);
        Vec.pool.recycle(offset);
    }

    @Override
    protected void leaveStack()
    {
        meowSound.stopSound();

        jumping = 20;
        lost = true;
        super.leaveStack();
        resetBody();
        loadJumpingSprite();
    }

    @Override
    public void setCaught(StackableObject holder)
    {
        alterBody();
        setHolder(holder);
    }

    @Override
    public StackableObject swapHolder(StackableObject newHolder)
    {
        loadDanglingSprite();
        return super.swapHolder(newHolder);
    }

    //rather than doing this (or "reinitializing" the body elsewhere) it may be best to implement a body array
    //this would require bodies to have an active/inactive state that they can switch between depending on
    //status. Should work well though. Would be better than reforming the body entirely I think.
    private void alterBody()
    {
        //float width = 23 * PIXEL_SCALE;
        //float height = 22 * PIXEL_SCALE;

        /*float[] vertices = ArrayPool.floatMaster.get(12);
        vertices[0] = 0;
        vertices[1] = (height - height / 10);
        vertices[2] = 0;
        vertices[3] = 0;
        vertices[4] = height;
        vertices[5] = 0;
        vertices[6] = width;
        vertices[7] = height;
        vertices[8] = 0;
        vertices[9] = width;
        vertices[10] = (height - height / 10);
        vertices[11] = 0;*/
        float[] vertices = ArrayPool.floatMaster.get(24);
        vertices[0] = 0;
        vertices[1] = (height - height / 10);
        vertices[2] = 0;
        vertices[3] = 0;
        vertices[4] = height;
        vertices[5] = 0;
        vertices[6] = width;
        vertices[7] = height;
        vertices[8] = 0;
        vertices[9] = width;
        vertices[10] = (height - height / 10);
        vertices[11] = 0;
        vertices[12] = (width / 2) + 1;
        vertices[13] = (height - height / 10);
        vertices[14] = 0;
        vertices[15] = (width / 2) + 1;
        vertices[16] = 0;
        vertices[17] = 0;
        vertices[18] = (width / 2) - 1;
        vertices[19] = (height - height / 10);;
        vertices[20] = 0;
        vertices[21] = (width / 2) - 1;
        vertices[22] = 0;
        vertices[23] = 0;

        //body = new Polygon(body.getPosition().x, body.getPosition().y, 0, vertices);
        body.init(body.getPosition().x, body.getPosition().y, 0, vertices);
        body.move(body.getPosition().x, body.getPosition().y, 0);
        body.setMass(5);

        ArrayPool.floatMaster.recycle(vertices, vertices.length);
    }

    private void resetBody()
    {
        float[] vertices = ArrayPool.floatMaster.get(12);
        vertices[0] = 0;
        vertices[1] = 0;
        vertices[2] = 0;
        vertices[3] = 0;
        vertices[4] = height / 10;
        vertices[5] = 0;
        vertices[6] = width;
        vertices[7] = height / 10;
        vertices[8] = 0;
        vertices[9] = width;
        vertices[10] = 0;
        vertices[11] = 0;

        //body = new Polygon(body.getPosition().x, body.getPosition().y, 0, vertices);
        body.init(body.getPosition().x, body.getPosition().y, 0, vertices);
        body.move(body.getPosition().x, body.getPosition().y, 0);
        body.setMass(5);

        ArrayPool.floatMaster.recycle(vertices, vertices.length);
    }

    @Override
    public void applyFriction()
    {
        if(!deposited)
        {
            super.applyFriction();
        }
    }

    @Override
    public void applyMovement()
    {
        if(landed)
        {
            Vec movement = Vec.pool.get();
            if(movingRight)
            {
                movement.x = 20;
                movement.y = 0;
            }
            else if(movingLeft)
            {
                movement.x = -20;
                movement.y = 0;
            }

            Vec newDisplacement = Vec.pool.get();
            newDisplacement.x = movement.x * getMovementDeltaX();
            newDisplacement.y = movement.y * getMovementDeltaY();
            body.setDisplacement(newDisplacement);

            Vec.pool.recycle(movement);
            Vec.pool.recycle(newDisplacement);
        }
    }

    @Override
    public void update()                                                        //update the object's logic
    {
        if(!caught && !landed && !deposited && !lost && jumping <= 0)
        {
            if(blinkTimer <= 0)
            {
                Random r = ArrayPool.randomPool.get();
                blinkTimer = r.nextInt(250 - 50 + 1) + 50;

                if(getAnimation(BLINKING_ANIM) != null)
                {
                    getAnimation(BLINKING_ANIM).activate();
                }

                ArrayPool.randomPool.recycle(r);
            }
            else
            {
                blinkTimer -= Time.getDelta();
            }
        }

        if(unmovable)
        {
            return;
        }

        if(landed)
        {
            despawnTimer += Time.getDelta();
            if(despawnTimer >= DESPAWN_TIME)
            {
                remove();
            }
        }

        if(deposited)
        {
            depositTimer += Time.getDelta();
            if(despawnTimer >= DEPOSIT_TIME)
            {
                remove();
            }
        }

        if(jumping > 0)
        {
            jumping -= Time.getDelta();

            Vec newDisplacement = Vec.pool.get();
            newDisplacement.x = body.getDisplacementX();
            newDisplacement.y = body.getDisplacementY() + Screen.screen.getPixelDensity(2f);
            body.setDisplacement(newDisplacement);
            Vec.pool.recycle(newDisplacement);

            if(body.getDisplacementY() > 10)
            {
                changeFrame(0, ASCENDING_FRAME);
                defaultFrames.set(0, ASCENDING_FRAME);
            }
            else if(body.getDisplacementY() < 0)
            {
                changeFrame(0, DESCENDING_FRAME);
                defaultFrames.set(0, DESCENDING_FRAME);
                jumping = 0;
            }
            else
            {
                changeFrame(0, MIDJUMP_FRAME);
                defaultFrames.set(0, MIDJUMP_FRAME);
            }
        }

        if(deposited)
        {
            body.move(body.getPosition().x + body.getDisplacementX(), body.getPosition().y + body.getDisplacementY(), rotation);
            body.setX(body.getPosition().x + body.getDisplacementX());
            body.setY(body.getPosition().y + body.getDisplacementY());
        }
        else if(caught  && jumping <= 0)
        {
            if(dangling)
            {
                if(meowTimer <= 0)
                {
                    Random r = ArrayPool.randomPool.get();
                    meowTimer = r.nextInt(250 - 50 + 1) + 50;

                    if(getAnimation(DANGLE_MEOWING_ANIM) != null && !isCurrentlyAnimated())
                    {
                        getAnimation(DANGLE_MEOWING_ANIM).activate();
                        meowSound.playSound(Time.getDelta());
                    }

                    ArrayPool.randomPool.recycle(r);
                }
                else
                {
                    meowTimer -= Time.getDelta();
                }

                return;
            }

            if(meowTimer <= 0)
            {
                Random r = ArrayPool.randomPool.get();
                meowTimer = r.nextInt(250 - 50 + 1) + 50;

                if(getAnimation(MEOWING_ANIM) != null && !isCurrentlyAnimated())
                {
                    getAnimation(MEOWING_ANIM).activate();
                    meowSound.playSound(Time.getDelta());
                }

                ArrayPool.randomPool.recycle(r);
            }
            else
            {
                meowTimer -= Time.getDelta();
            }

            if(flickTimer <= 0)
            {
                Random r = ArrayPool.randomPool.get();
                flickTimer = r.nextInt(650 - 250 + 1) + 250;

                if(getAnimation(FLICKING_ANIM) != null && !isCurrentlyAnimated())
                {
                    getAnimation(FLICKING_ANIM).activate();
                }

                ArrayPool.randomPool.recycle(r);
            }
            else
            {
                flickTimer -= Time.getDelta();
            }

            if(wagTimer <= 0)
            {
                Random r = ArrayPool.randomPool.get();
                wagTimer = r.nextInt(150 - 20 + 1) + 20;

                if(getAnimation(WAGGING_ANIM) != null && !isCurrentlyAnimated())
                {
                    getAnimation(WAGGING_ANIM).activate();
                }

                ArrayPool.randomPool.recycle(r);
            }
            else
            {
                wagTimer -= Time.getDelta();
            }

            //body.move(body.getPosition().x + body.getDisplacementX(), 90 + (50 * (stackPosition - 1)), rotation);
            //body.setX(body.getPosition().x + body.getDisplacementX());
            //body.setY(90 + (50 * (stackPosition - 1)));
            body.move(heldBy.getPosition().x, heldBy.holdPositionY(), rotation);
            body.setX(heldBy.getPosition().x);
            body.setY(heldBy.holdPositionY());
        }
        else
        {
            body.move(body.getPosition().x + body.getDisplacementX(), body.getPosition().y + body.getDisplacementY(), rotation);
            body.setX(body.getPosition().x + body.getDisplacementX());
            body.setY(body.getPosition().y + body.getDisplacementY());
        }
    }

    public void playCatchSound()
    {
        Random r = ArrayPool.randomPool.get();
        int choice = r.nextInt(3);
        if(choice == 0)
        {
            catchSound1.playSound();
        }
        else if(choice == 1)
        {
            catchSound2.playSound();
        }
        else if(choice == 2)
        {
            catchSound3.playSound();
        }
    }

    @Override
    public float holdPositionY()
    {
        if(dangling)
        {
            return body.getPosition().y - (holding.getHeight() - Screen.screen.getPixelDensity(10));
        }
        else
        {
            return body.getPosition().y + (height - Screen.screen.getPixelDensity(10));
        }
    }

    public void loadLandingSprite()
    {
        defaultFrames.set(0, 27);
        float width = Screen.screen.getPixelDensity(41 * PIXEL_SCALE);
        float height = Screen.screen.getPixelDensity(17 * PIXEL_SCALE);

        if(body.getPosition().x >= World.world.getWorldWidth() / 2)
        {
            changeVertices(0, 0, 0, width, height, -0.1f);
            movingLeft = false;
            movingRight = true;
        }
        else
        {
            changeVerticesFlippedX(0, 0, 0, width, height, -0.1f);
            movingLeft = true;
            movingRight = false;
        }

        changeFrame(0, WALKING_FRAME_1);

        getAnimation(WALKING_ANIM).activate();
    }

    public void loadStackedSprite()
    {
        defaultFrames.set(0, 0);
        width = Screen.screen.getPixelDensity(23 * PIXEL_SCALE);
        height = Screen.screen.getPixelDensity(22 * PIXEL_SCALE);

        //changeVertices(0, new float[] {0, height, 0, 0, 0, 0, width, 0, 0, width, height, 0});

        //changeVertices(0, new float[] {0, height, getStackPosition() / -10, 0, 0, getStackPosition() / -10, width, 0, getStackPosition() / -10, width, height, getStackPosition() / -10});
        changeVertices(0, 0, 0, width, height, (float)((getStackPosition()) / -10f));

        changeFrame(0, STACKING_FRAME_1);

        //height -= Screen.screen.getPixelDensity(10);

        if(getStackPosition() >= getMaxStackSize())
        {
            getAnimation(POUNCE_ANIM).activate();
        }

        getAnimation(BLINKING_ANIM).deactivate();
    }

    public void loadJumpingSprite()
    {
        defaultFrames.set(0, 26);
        float width = Screen.screen.getPixelDensity(37 * PIXEL_SCALE);
        float height = Screen.screen.getPixelDensity(22 * PIXEL_SCALE);

        if(body.getDisplacementX() > 0)
        {
            changeVertices(0, 0, 0, width, height, 0);
        }
        else
        {
            changeVerticesFlippedX(0, 0, 0, width, height, -0.1f);
        }
        changeFrame(0, ASCENDING_FRAME);

        getAnimation(POUNCE_ANIM).deactivate();
        getAnimation(MEOWING_ANIM).deactivate();
        getAnimation(WAGGING_ANIM).deactivate();
    }

    public void loadDanglingSprite()
    {
        defaultFrames.set(0, 14);
        float width = Screen.screen.getPixelDensity(22 * PIXEL_SCALE);
        float height = Screen.screen.getPixelDensity(25 * PIXEL_SCALE);

        changeVertices(0, 0, 0, width, height, (float)((getStackPosition()) / -10f));
        changeFrame(0, DANGLING_FRAME_1);

        getAnimation(POUNCE_ANIM).deactivate();
        getAnimation(MEOWING_ANIM).deactivate();
    }

    @Override
    public void destruct()
    {
        landed = false;
        deposited = false;
        jumping = 0;
        Random r = ArrayPool.randomPool.get();
        blinkTimer = r.nextInt(250 - 50 + 1) + 50;
        meowTimer = r.nextInt(400 - 150 + 1) + 150;
        flickTimer = r.nextInt(600 - 250 + 1) + 250;
        wagTimer = r.nextInt(100 - 20 + 1) + 20;
        ArrayPool.randomPool.recycle(r);
        despawnTimer = 0;
        movingLeft = false;
        movingRight = false;

        removed = false;
        caught = false;
        dangling = false;
        lost = false;
        holding = null;
        heldBy = null;
        stackPosition = 0;

        solid = true;

        hasAlpha = true;
        tiled = false;

        verticesChanged = false;
        coloursChanged = false;
        textureChanged = false;
        frameChanged = false;
        indicesChanged = false;

        float[] vertices = ArrayPool.floatMaster.get(12);
        vertices[0] = 0;
        vertices[1] = 0;
        vertices[2] = 0;
        vertices[3] = 0;
        vertices[4] = height / 10;
        vertices[5] = 0;
        vertices[6] = width;
        vertices[7] = height / 10;
        vertices[8] = 0;
        vertices[9] = width;
        vertices[10] = 0;
        vertices[11] = 0;
        body.init(0, 0, 0, vertices);
        body.move(0, 0, 0);
        ArrayPool.floatMaster.recycle(vertices, vertices.length);

        body.resetPosition();
        body.setDisplacement(0, 0);

        width = Screen.screen.getPixelDensity(31 * PIXEL_SCALE);
        height = Screen.screen.getPixelDensity(16 * PIXEL_SCALE);

        for(int i = 0; i < animations.size(); i++)
        {
            animations.get(i).deactivate();
        }

        changeVertices(0, 0, 0, width, height, -0.1f);

        defaultFrames.set(0, FALLING_FRAME);

        changeFrame(0, FALLING_FRAME);

        Cat.pool.recycle(this);
        /*body.destruct();
        Polygon.pool.recycle(body);

        textures.clear();

        sprVertices.clear();

        sprColours.clear();

        activeFrames.clear();

        animations.clear();

        defaultFrames.clear();

        Cat.pool.recycle(this);*/
    }
}
