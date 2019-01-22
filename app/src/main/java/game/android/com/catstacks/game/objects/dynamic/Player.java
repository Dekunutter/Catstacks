package game.android.com.catstacks.game.objects.dynamic;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.core.RenderedState;
import game.android.com.catstacks.engine.core.Screen;
import game.android.com.catstacks.engine.core.Time;
import game.android.com.catstacks.engine.input.InputBuffer;
import game.android.com.catstacks.engine.input.Touch;
import game.android.com.catstacks.engine.objects.WorldObject;
import game.android.com.catstacks.engine.physics.CollisionBitmasks;
import game.android.com.catstacks.engine.physics.Polygon;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.rendering.Animation;
import game.android.com.catstacks.engine.rendering.Texture;
import game.android.com.catstacks.engine.rendering.TextureLoader;
import game.android.com.catstacks.engine.sound.Sound;
import game.android.com.catstacks.engine.states.World;
import game.android.com.catstacks.game.logic.BackgroundTheme;

public class Player extends StackableObject
{
    private boolean movingLeft, movingRight, scored;
    private int cats;
    private int score;
    private float dropFrames;
    private int lives, faceInc, bodyInc;

    private Sound walkingSound1, walkingSound2, walkingSound3, walkingSound4, walkingSound5;
    private float walkingTimer;

    private final static int MAX_STACK_SIZE = 10;

    private final static int FACE_TEXTURE = 0;
    private final static int BODY_TEXTURE = 1;

    private final static int HAPPY_FRAME = 0;
    private final static int MISTAKE_FRAME = 1;
    private final static int SAD_FRAME = 2;
    private final static int UNHAPPY_FRAME = 3;
    private final static int INDIFFERENT_FRAME = 4;
    private final static int STANDING_FRAME = 0;
    private final static int WALKING_FRAME_1 = 1;
    private final static int WALKING_FRAME_2 = 2;

    private final static int BEACH_FACE_INC = 6;
    private final static int BEACH_BODY_INC = 4;
    private final static int SPOOKY_FACE_INC = 12;
    private final static int SPOOKY_BODY_INC = 8;
    private final static int CHRISTMAS_FACE_INC = 18;
    private final static int CHRISTMAS_BODY_INC = 12;
    private final static int UNDERWATER_FACE_INC = 24;
    private final static int UNDERWATER_BODY_INC = 16;

    private final static String WALKING_ANIM = "walking";

    public Player(Vec position, BackgroundTheme.Theme activeTheme)
    {
        movingLeft = false;
        movingRight = false;

        if(activeTheme == BackgroundTheme.Theme.SPOOKY)
        {
            faceInc = SPOOKY_FACE_INC;
            bodyInc = SPOOKY_BODY_INC;
        }
        else if(activeTheme == BackgroundTheme.Theme.BEACH)
        {
            faceInc = BEACH_FACE_INC;
            bodyInc = BEACH_BODY_INC;
        }
        else if(activeTheme == BackgroundTheme.Theme.CHRISTMAS)
        {
            faceInc = CHRISTMAS_FACE_INC;
            bodyInc = CHRISTMAS_BODY_INC;
        }
        else if(activeTheme == BackgroundTheme.Theme.UNDERWATER)
        {
            faceInc = UNDERWATER_FACE_INC;
            bodyInc = UNDERWATER_BODY_INC;
        }
        else
        {
            faceInc = 0;
            bodyInc = 0;
        }
        addSprites();

        //NOTE: Don't need collision on head.
        //May need to think of a means to have two different collision bodies (one for general collisions and one for the cats)
        //Collision with cats could potentially by done with some handy math (fetching the faceHeight and subtracting it to see if still colliding for example)
        //float totalVertices[] = {0, 0, 0, 0, height, 0, width, height, 0, width, 0, 0};
        float totalVertices[] = ArrayPool.floatMaster.get(12);
        totalVertices[0] = 0;
        totalVertices[1] = 0;
        totalVertices[2] = 0;
        totalVertices[3] = 0;
        totalVertices[4] = height;
        totalVertices[5] = 0;
        totalVertices[6] = width;
        totalVertices[7] = height;
        totalVertices[8] = 0;
        totalVertices[9] = width;
        totalVertices[10] = 0;
        totalVertices[11] = 0;
        super.body = new Polygon(position.x, position.y, 0, totalVertices);
        super.body.move(position.x, position.y, 0);
        super.body.setMass(10);

        ArrayPool.floatMaster.recycle(totalVertices, totalVertices.length);

        initAnimations();

        mask = CollisionBitmasks.PLAYER_ID;

        cats = 0;
        lives = 3;

        holding = null;
        dropFrames = 0;

        walkingSound1 = Sound.pool.get();
        walkingSound1.init(R.raw.step, 0.5f);
        walkingSound2 = Sound.pool.get();
        walkingSound2.init(R.raw.step2, 0.5f);
        walkingSound3 = Sound.pool.get();
        walkingSound3.init(R.raw.step3, 0.5f);
        walkingSound4 = Sound.pool.get();
        walkingSound4.init(R.raw.step4, 0.5f);
        walkingSound5 = Sound.pool.get();
        walkingSound5.init(R.raw.step5, 0.5f);

        scored = false;
    }

    private void addSprites()
    {
        Texture faceTexture = TextureLoader.loadTexture(RenderedState.getContext(), R.drawable.emotes, 3, 10);
        int faceWidth = faceTexture.getWidth() * 2;
        int faceHeight = faceTexture.getHeight() * 2;

        Texture bodyTexture = TextureLoader.loadTexture(RenderedState.getContext(), R.drawable.body_tile, 2, 10);
        int bodyWidth = bodyTexture.getWidth() * 2;
        int bodyHeight = bodyTexture.getHeight() * 2;

        width = Screen.screen.getPixelDensity(faceWidth);
        float totalHeight = Screen.screen.getPixelDensity(faceHeight + bodyHeight);
        float spriteHeight = totalHeight - Screen.screen.getPixelDensity(faceHeight);
        float armPosition = Screen.screen.getPixelDensity(8);
        height = spriteHeight - armPosition;

        float[] vertices = ArrayPool.floatMaster.get(12);
        vertices[0] = 0;
        vertices[1] = totalHeight;
        vertices[2] = -0.8f;
        vertices[3] = 0;
        vertices[4] = spriteHeight;
        vertices[5] = -0.8f;
        vertices[6] = width;
        vertices[7] = spriteHeight;
        vertices[8] = -0.8f;
        vertices[9] = width;
        vertices[10] = totalHeight;
        vertices[11] = -0.8f;
        sprVertices.add(vertices);
        float[] colours = ArrayPool.floatMaster.get(16);
        colours[0] = 1;
        colours[1] = 1;
        colours[2] = 1;
        colours[3] = 1;
        colours[4] = 1;
        colours[5] = 1;
        colours[6] = 1;
        colours[7] = 1;
        colours[8] = 1;
        colours[9] = 1;
        colours[10] = 1;
        colours[11] = 1;
        colours[12] = 1;
        colours[13] = 1;
        colours[14] = 1;
        colours[15] = 1;
        sprColours.add(colours);
        textures.add(faceTexture);

        vertices = ArrayPool.floatMaster.get(12);
        vertices[0] = 0;
        vertices[1] = spriteHeight;
        vertices[2] = -0.8f;
        vertices[3] = 0;
        vertices[4] = 0;
        vertices[5] = -0.8f;
        vertices[6] = width;
        vertices[7] = 0;
        vertices[8] = -0.8f;
        vertices[9] = width;
        vertices[10] = spriteHeight;
        vertices[11] = -0.8f;
        sprVertices.add(vertices);
        colours = ArrayPool.floatMaster.get(16);
        colours[0] = 1;
        colours[1] = 1;
        colours[2] = 1;
        colours[3] = 1;
        colours[4] = 1;
        colours[5] = 1;
        colours[6] = 1;
        colours[7] = 1;
        colours[8] = 1;
        colours[9] = 1;
        colours[10] = 1;
        colours[11] = 1;
        colours[12] = 1;
        colours[13] = 1;
        colours[14] = 1;
        colours[15] = 1;
        sprColours.add(colours);
        textures.add(bodyTexture);

        activeFrames.add(faceTexture.getFrame(HAPPY_FRAME + bodyInc));
        defaultFrames.add(HAPPY_FRAME + bodyInc);
        activeFrames.add(bodyTexture.getFrame(STANDING_FRAME + bodyInc));
        defaultFrames.add(STANDING_FRAME + bodyInc);
    }

    private void initAnimations()
    {
        animations = ArrayPool.animationPool.get();

        ArrayList<float[]> frames = ArrayPool.floatArrayPool.get();
        frames.add(textures.get(BODY_TEXTURE).getFrame(WALKING_FRAME_1 + bodyInc));
        frames.add(textures.get(BODY_TEXTURE).getFrame(WALKING_FRAME_2 + bodyInc));
        Animation walking = Animation.pool.get();
        walking.init(WALKING_ANIM, BODY_TEXTURE, frames, 2);

        animations.add(walking);
    }

    public void getInput(InputBuffer input)
    {
        if(World.world.isGameOver())
        {
            return;
        }

        boolean controlling = true;

        if(input.action == InputBuffer.ACTION_TOUCH_DOWN)
        {
            controlling = true;
        }
        else if(input.action == InputBuffer.ACTION_TOUCH_UP)
        {
            if(!input.touched)
            {
                controlling = false;
            }
        }
        else if(input.action == InputBuffer.ACTION_TOUCH_MOVE)
        {
            return;
        }

        if(controlling)
        {
            if(input.x < (super.body.getCenterX() - width/2))
            {
                movingLeft = true;
                movingRight = false;
                getAnimation(WALKING_ANIM).activate();

                //changeFrame(BODY_TEXTURE, WALKING_FRAME_1 + bodyInc);
            }
            else if(input.x > (super.body.getCenterX() + width/2))
            {
                movingLeft = false;
                movingRight = true;
                getAnimation(WALKING_ANIM).activate();

                //changeFrame(BODY_TEXTURE, WALKING_FRAME_2 + bodyInc);
            }
            else
            {
                movingLeft = false;
                movingRight = false;
                getAnimation(WALKING_ANIM).deactivate();

                changeFrame(BODY_TEXTURE, STANDING_FRAME + bodyInc);
            }
        }
        else
        {
            movingLeft = false;
            movingRight = false;
            getAnimation(WALKING_ANIM).deactivate();

            changeFrame(BODY_TEXTURE, STANDING_FRAME + bodyInc);
        }
    }

    public void applyMovement()
    {
        Vec movement = Vec.pool.get();
        movement.x = 0;
        movement.y = 0;

        if(movingLeft)
        {
            movement.x = -10;
            movement.y = 0;
        }
        else if(movingRight)
        {
            movement.x = 10;
            movement.y = 0;
        }

        Vec newDisplacement = Vec.pool.get();
        newDisplacement.x = movement.x * getMovementDeltaX();
        newDisplacement.y = movement.y * getMovementDeltaY();
        super.body.setDisplacement(newDisplacement);

        Vec.pool.recycle(movement);
        Vec.pool.recycle(newDisplacement);
    }

    public void update()
    {
        if(World.world.isGameOver())
        {
            movingLeft = false;
            movingRight = false;
            getAnimation(WALKING_ANIM).deactivate();

            changeFrame(FACE_TEXTURE, SAD_FRAME + faceInc);
            defaultFrames.set(0, SAD_FRAME + faceInc);
            changeFrame(BODY_TEXTURE, STANDING_FRAME + bodyInc);

            //walkingSound.stopSound();

            if(isHolding() && dropFrames <= 0)
            {
                dropTopItem();
                dropFrames = 40;
            }
            dropFrames -= Time.getDelta();
        }
        else
        {

            if(getLives() >= 3)
            {
                changeFrame(FACE_TEXTURE, HAPPY_FRAME + faceInc);
                defaultFrames.set(0, HAPPY_FRAME + faceInc);
            }
            else if(getLives() == 2)
            {
                changeFrame(FACE_TEXTURE, UNHAPPY_FRAME + faceInc);
                defaultFrames.set(0, UNHAPPY_FRAME + faceInc);
            }
            else if(getLives() == 1)
            {
                changeFrame(FACE_TEXTURE, INDIFFERENT_FRAME + faceInc);
                defaultFrames.set(0, INDIFFERENT_FRAME + faceInc);
            }

            //rotation = rotation++;

            //Log.println(Log.ERROR, "cats", "" + getCats());

            if(movingLeft || movingRight)
            {
                //walkingSound.playSoundLooped();
                if(walkingTimer <= 0)
                {
                    Random r = ArrayPool.randomPool.get();
                    int choice = r.nextInt(5);
                    if(choice == 0)
                    {
                        walkingSound1.playSound();
                    }
                    else if(choice == 1)
                    {
                        walkingSound2.playSound();
                    }
                    else if(choice == 2)
                    {
                        walkingSound3.playSound();
                    }
                    else if(choice == 3)
                    {
                        walkingSound4.playSound();
                    }
                    else if(choice == 4)
                    {
                        walkingSound5.playSound();
                    }
                    ArrayPool.randomPool.recycle(r);
                    walkingTimer = Time.getDelta() * 10;
                }
                else
                {
                    walkingTimer -= Time.getDelta();
                }
            } else
            {
                //walkingSound.stopSound();
            }

            super.body.move(super.body.getPosition().x + super.body.getDisplacementX(), super.body.getPosition().y + super.body.getDisplacementY(), rotation);
            super.body.setX(super.body.getPosition().x + super.body.getDisplacementX());
            super.body.setY(super.body.getPosition().y + super.body.getDisplacementY());
        }
    }

    public void applyFriction()
    {
        if(!isMoving())
        {
            super.applyFriction();
        }
    }

    private boolean isMoving()
    {
        return (movingLeft || movingRight);
    }

    @Override
    public void incrementStack()
    {
        cats++;
    }

    public int getCats()
    {
        return cats;
    }

    public void depositStack(WorldObject depo)
    {
        addScore();
        cats = 0;
        depositItem(depo);
    }

    public void addScore()
    {
        for(int i = 1; i <= cats; i++)
        {
            score += (i * 10);
        }
        scored = true;
    }

    public boolean hasScored()
    {
        return scored;
    }

    public void setScored(boolean value)
    {
        scored = value;
    }

    public void setCats(int value)
    {
        cats = value;
    }

    public int getScore()
    {
        return score;
    }

    @Override
    public int getMaxStackSize()
    {
        return MAX_STACK_SIZE;
    }

    @Override
    public StackableObject swapHolder(StackableObject newHolder)
    {
        addScore();
        holding = null;
        setCats(0);

        return null;
    }

    public int getLives()
    {
        return lives;
    }

    public void subtractLife()
    {
        lives--;
    }

    public void addLife()
    {
        lives++;
    }
}
