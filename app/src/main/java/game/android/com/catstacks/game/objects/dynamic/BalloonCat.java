package game.android.com.catstacks.game.objects.dynamic;

import java.util.ArrayList;
import java.util.Random;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.core.Screen;
import game.android.com.catstacks.engine.core.Time;
import game.android.com.catstacks.engine.physics.CollisionBitmasks;
import game.android.com.catstacks.engine.physics.Polygon;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.pool.CatObjectPool;
import game.android.com.catstacks.engine.rendering.Animation;
import game.android.com.catstacks.engine.rendering.Texture;
import game.android.com.catstacks.engine.sound.Sound;

public class BalloonCat extends Cat
{
    private float flyingDespawnTimer;
    private float meowTimer, floatTimer;
    private int floatFrame;

    private float balloonWidth, balloonHeight;

    private Sound meowSound, catchSound1, catchSound2;

    private final static int DESPAWN_TIME = (int) Screen.screen.getPixelDensity(42);
    private final static int FLYING_DESPAWN_TIME = 800;

    private final float balloonOffsetX = Screen.screen.getPixelDensity(-6 * PIXEL_SCALE);
    private final float balloonOffsetY = Screen.screen.getPixelDensity(12 * PIXEL_SCALE);

    private final static int CONTENT_FRAME = 14;
    private final static int PUCKER_FRAME = 15;
    private final static int OPENING_FRAME = 16;
    private final static int OPEN_FRAME = 17;
    private final static int SAD_FRAME = 18;
    private final static int SHOCKED_FRAME = 19;
    private final static int CLOSING_FRAME = 20;
    private final static int PUCKER_FRAME_2 = 21;

    private final static int RED_BALLOON = 0;
    private final static int YELLOW_BALLOON = 1;
    private final static int GREEN_BALLOON = 2;
    private final static int VIOLET_BALLOON = 3;
    private final static int ORANGE_BALLOON = 4;
    private final static int PINK_BALLOON = 5;
    private final static int PURPLE_BALLOON = 6;
    private final static int BROWN_BALLOON = 7;
    private final static int MAROON_BALLOON = 8;
    private final static int WHITE_BALLOON = 9;
    private final static int LIGHT_BLUE_BALLOON = 10;
    private final static int DARK_GREEN_BALLOON = 11;
    private final static int BLUE_BALLOON = 12;
    private final static int MUSTARD_BALLON = 13;

    private final static String MEOWING_ANIM = "meowing";

    //public final static GenericObjectPool<BalloonCat> pool = new GenericObjectPool<BalloonCat>(BalloonCat.class);
    public final static CatObjectPool<BalloonCat> pool = new CatObjectPool<BalloonCat>();

    public BalloonCat()
    {

    }

    public BalloonCat(Vec position, Texture texture, Texture balloon, int balloonColour, Sound meowSound, Sound catchSound1, Sound catchSound2)
    {
        addSprites(texture, balloon, balloonColour);

        //float vertices[] = {0, 0, 0, 0, height/10, 0, width, height/10, 0, width, 0, 0};
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
        body = new Polygon(position.x, position.y, 0, vertices);
        body.move(position.x, position.y, 0);
        body.setMass(5);

        ArrayPool.floatMaster.recycle(vertices, vertices.length);

        mask = CollisionBitmasks.CAT_ID;

        caught = false;
        dangling = true;

        heldBy = null;

        Random r = ArrayPool.randomPool.get();
        meowTimer = r.nextInt(500 - 100 + 1) + 100;
        floatTimer = 25;
        floatFrame = 0;

        ArrayPool.randomPool.recycle(r);

        initAnimations();

        storeBitmap = true;

        this.meowSound = meowSound;
        this.catchSound1 = catchSound1;
        this.catchSound2 = catchSound2;
    }

    public void init(Vec position, Texture texture, Texture balloon, int balloonColour, Sound meowSound, Sound catchSound1, Sound catchSound2)
    {
        addSprites(texture, balloon, balloonColour);

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
        body.move(position.x, position.y, 0);
        body.setMass(5);

        ArrayPool.floatMaster.recycle(vertices, vertices.length);

        mask = CollisionBitmasks.CAT_ID;

        caught = false;
        dangling = true;

        heldBy = null;

        Random r = ArrayPool.randomPool.get();
        meowTimer = r.nextInt(500 - 100 + 1) + 100;
        floatTimer = 25;
        floatFrame = 0;

        ArrayPool.randomPool.recycle(r);

        initAnimations();

        storeBitmap = true;

        this.meowSound = meowSound;
        this.catchSound1 = catchSound1;
        this.catchSound2 = catchSound2;
    }

    private void addSprites(Texture texture, Texture balloon, int balloonColour)
    {
        addSpriteFromExistingTexture(texture, 0, 1, 22, 25);

        activeFrames.add(textures.get(0).getFrame(CONTENT_FRAME));
        defaultFrames.add(CONTENT_FRAME);

        textures.add(balloon);
        balloonWidth = Screen.screen.getPixelDensity(balloon.getWidth() * PIXEL_SCALE);
        balloonHeight = Screen.screen.getPixelDensity(balloon.getHeight() * PIXEL_SCALE);

        float[] newVertices = ArrayPool.floatMaster.get(12);
        newVertices[0] = balloonOffsetX;
        newVertices[1] = balloonOffsetY + balloonHeight;
        newVertices[2] = 0;
        newVertices[3] = balloonOffsetX;
        newVertices[4] = balloonOffsetY;
        newVertices[5] = 0;
        newVertices[6] = balloonOffsetX + balloonWidth;
        newVertices[7] = balloonOffsetY;
        newVertices[8] = 0;
        newVertices[9] = balloonOffsetX + balloonWidth;
        newVertices[10] = balloonOffsetY + balloonHeight;
        newVertices[11] = 0;
        sprVertices.add(newVertices);

        float[] newColours = ArrayPool.floatMaster.get(16);
        for(int i = 0; i < newColours.length; i++)
        {
            newColours[i] = 1;
        }
        sprColours.add(newColours);

        activeFrames.add(textures.get(1).getFrame(balloonColour));
        defaultFrames.add(balloonColour);
    }

    private void initAnimations()
    {
        animations = ArrayPool.animationPool.get();

        ArrayList<float[]> frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(0).getFrame(PUCKER_FRAME));
        frames.add(textures.get(0).getFrame(OPENING_FRAME));
        frames.add(textures.get(0).getFrame(OPEN_FRAME));
        frames.add(textures.get(0).getFrame(SAD_FRAME));
        frames.add(textures.get(0).getFrame(SHOCKED_FRAME));
        frames.add(textures.get(0).getFrame(CLOSING_FRAME));
        frames.add(textures.get(0).getFrame(PUCKER_FRAME_2));
        Animation meowing = Animation.pool.get();
        meowing.init(MEOWING_ANIM, 0, frames, 3);
        meowing.setLooping(false);

        animations.add(meowing);
    }

    @Override
    public void applyMovement()
    {
        Vec movement = Vec.pool.get();
        if(!caught)
        {
            movement.x = 2.5f * getMovementDeltaX();
            movement.y = 0 * getMovementDeltaY();
            super.body.setDisplacement(movement);
        }
        else
        {
            movement.x = 2.5f * getMovementDeltaX();
            movement.y = 20 * getMovementDeltaY();
            super.body.setDisplacement(movement);
        }
        Vec.pool.recycle(movement);
    }

    @Override
    public void update()
    {
        if(meowTimer <= 0)
        {
            Random r = ArrayPool.randomPool.get();
            meowTimer = r.nextInt(500 - 100 + 1) + 100;
            ArrayPool.randomPool.recycle(r);

            getAnimation(MEOWING_ANIM).activate();
            meowSound.playSound(Time.getDelta());
        }
        else
        {
            meowTimer -= Time.getDelta();
        }

        if(!caught)
        {
            if(floatTimer <= 0)
            {
                floatFrame++;

                if(floatFrame == 1)
                {
                    changeVertices(0, 0, -2, width, height - 2, 0);
                    changeVertices(1, balloonOffsetX, balloonOffsetY - 2, balloonWidth, balloonHeight - 2, 0);
                }
                else if(floatFrame == 2)
                {
                    changeVertices(0, 0, -4, width, height - 4, 0);
                    changeVertices(1, balloonOffsetX, balloonOffsetY - 4, balloonWidth, balloonHeight - 4, 0);
                }
                else if(floatFrame == 3)
                {
                    changeVertices(0, 0, -2, width, height - 2, 0);
                    changeVertices(1, balloonOffsetX, balloonOffsetY - 2, balloonWidth, balloonHeight - 2, 0);
                }
                else if(floatFrame == 4)
                {
                    changeVertices(0, 0, 0, width, height, 0);
                    changeVertices(1, balloonOffsetX, balloonOffsetY, balloonWidth, balloonHeight, 0);
                }
                else if(floatFrame == 5)
                {
                    changeVertices(0, 0, 2, width, height + 2, 0);
                    changeVertices(1, balloonOffsetX, balloonOffsetY + 2, balloonWidth, balloonHeight + 2, 0);
                }
                else if(floatFrame == 6)
                {
                    changeVertices(0, 0, 4, width, height + 4, 0);
                    changeVertices(1, balloonOffsetX, balloonOffsetY + 4, balloonWidth, balloonHeight + 4, 0);
                }
                else if(floatFrame == 7)
                {
                    changeVertices(0, 0, 2, width, height + 2, 0);
                    changeVertices(1, balloonOffsetX, balloonOffsetY + 2, balloonWidth, balloonHeight + 2, 0);
                }
                else if(floatFrame == 8)
                {
                    changeVertices(0, 0, 0, width, height, 0);
                    changeVertices(1, balloonOffsetX, balloonOffsetY, balloonWidth, balloonHeight, 0);
                    floatFrame = 0;
                }

                floatTimer = 25;
            }
            else
            {
                floatTimer -= Time.getDelta();
            }
        }

        if(unmovable)
        {
            return;
        }

        if(caught)
        {
            despawnTimer += Time.getDelta();
            if(despawnTimer >= DESPAWN_TIME)
            {
                holding.despawnHeldItem();
                remove();
            }

            body.move(body.getPosition().x + body.getDisplacementX(), body.getPosition().y + body.getDisplacementY(), rotation);
            body.setX(body.getPosition().x + body.getDisplacementX());
            body.setY(body.getPosition().y + body.getDisplacementY());

            moveHeldItem();
        }
        else
        {
            flyingDespawnTimer += Time.getDelta();
            if(flyingDespawnTimer >= FLYING_DESPAWN_TIME)
            {
                remove();
            }

            body.move(body.getPosition().x + body.getDisplacementX(), body.getPosition().y, rotation);
            body.setX(body.getPosition().x + body.getDisplacementX());
            body.setY(body.getPosition().y);
        }
    }

    @Override
    public void playCatchSound()
    {
        Random r = ArrayPool.randomPool.get();
        int choice = r.nextInt(2);
        if(choice == 0)
        {
            catchSound1.playSound();
        }
        else if(choice == 1)
        {
            catchSound2.playSound();
        }
    }

    @Override
    public int getMaxStackSize()
    {
        return 7;
    }

    @Override
    public void setHolder(StackableObject holder)
    {
        heldBy = holder;
        setCaught(true);

        setStackPosition(0);
        heldBy.holdItem(this);
    }

    @Override
    public void setCaught(StackableObject holder)
    {
        changeVertices(0, 0, 0, width, height, (float)(10 / -10f));
        changeVertices(1, balloonOffsetX, balloonOffsetY, balloonWidth, balloonHeight, (float)(10 / -10f));

        setHolder(holder);
    }

    public void destruct()
    {
        floatTimer = 25;
        floatFrame = 0;
        Random r = ArrayPool.randomPool.get();
        blinkTimer = r.nextInt(250 - 50 + 1) + 50;
        meowTimer = r.nextInt(500 - 100 + 1) + 100;
        flickTimer = r.nextInt(600 - 250 + 1) + 250;
        despawnTimer = 0;
        flyingDespawnTimer = 0;
        ArrayPool.randomPool.recycle(r);

        landed = false;
        deposited = false;
        despawnTimer = 0;
        removed = false;
        caught = false;
        dangling = true;
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

        body.resetPosition();
        body.setDisplacement(0, 0);

        changeVertices(0, 0, 0, width, height, 0);
        defaultFrames.add(CONTENT_FRAME);
        changeFrame(0, CONTENT_FRAME);

        changeVertices(1, balloonOffsetX, balloonOffsetY, balloonWidth, balloonHeight, 0);
        defaultFrames.add(RED_BALLOON);
        changeFrame(1, RED_BALLOON);

        //Vec.pool.recycle(balloonOffset);

        BalloonCat.pool.recycle(this);
    }
}
