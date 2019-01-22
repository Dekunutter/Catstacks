package game.android.com.catstacks.game.objects.ui;

import android.util.Log;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.core.GameThread;
import game.android.com.catstacks.engine.core.RenderedState;
import game.android.com.catstacks.engine.core.Screen;
import game.android.com.catstacks.engine.input.InputBuffer;
import game.android.com.catstacks.engine.objects.WorldObject;
import game.android.com.catstacks.engine.physics.Manifold;
import game.android.com.catstacks.engine.physics.Polygon;
import game.android.com.catstacks.engine.physics.SAT;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.pool.GenericObjectPool;
import game.android.com.catstacks.engine.states.Menu;
import game.android.com.catstacks.engine.states.World;

public class MenuButton extends WorldObject
{
    protected boolean pushedDown, pressed;

    protected final static int UNPRESSED_FRAME = 0;
    protected final static int PRESSED_FRAME = 1;

    public static final GenericObjectPool<MenuButton> pool = new GenericObjectPool<MenuButton>(MenuButton.class);

    protected MenuButton()
    {
        super();
    }

    public MenuButton(Vec position)
    {
        addSprite(R.drawable.start_button, 1, 2, 0, 1);

        Vec middle = Vec.pool.get();
        middle.x = width / 2;
        middle.y = height / 2;
        position.subtract(middle);

        activeFrames.add(textures.get(0).getFrame(UNPRESSED_FRAME));
        defaultFrames.add(0);

        float vertices[] = ArrayPool.floatMaster.get(12);
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

        ArrayPool.floatMaster.recycle(vertices, vertices.length);

        unmovable = true;

        pushedDown = false;
        pressed = false;

        solid = false;

        Vec.pool.recycle(middle);
    }

    public void init(Vec position)
    {
        addSprite(R.drawable.start_button, 1, 2, 0, 1);

        Vec middle = Vec.pool.get();
        middle.x = width / 2;
        middle.y = height / 2;
        position.subtract(middle);

        activeFrames.add(textures.get(0).getFrame(UNPRESSED_FRAME));
        defaultFrames.add(0);

        float vertices[] = ArrayPool.floatMaster.get(12);
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

        ArrayPool.floatMaster.recycle(vertices, vertices.length);

        unmovable = true;

        pushedDown = false;
        pressed = false;

        solid = false;
        storeBitmap = true;

        Vec.pool.recycle(middle);
    }

    public void getInput(InputBuffer input)
    {
        float vertices[] = ArrayPool.floatMaster.get(12);
        vertices[0] = -1;
        vertices[1] = -1;
        vertices[2] = 0;
        vertices[3] = -1;
        vertices[4] = 1;
        vertices[5] = 0;
        vertices[6] = 1;
        vertices[7] = 1;
        vertices[8] = 0;
        vertices[9] = 1;
        vertices[10] = -1;
        vertices[11] = 0;

        Polygon touchBody = Polygon.pool.get();
        if(input.action == InputBuffer.ACTION_TOUCH_UP)
        {
            touchBody.init(input.previousX, input.previousY, 0, vertices);
            touchBody.move(input.previousX, input.previousY, 0);
        }
        else
        {
            touchBody.init(input.x, input.y, 0, vertices);
            touchBody.move(input.x, input.y, 0);
        }
        Vec offset = Vec.pool.get();
        offset.x = body.getX();
        offset.y = body.getY();
        offset.subtract(touchBody.getPosition());

        if(input.action == InputBuffer.ACTION_TOUCH_DOWN)
        {
            //check if within object. push down if so, push up if not
            Vec vector = Vec.pool.get();
            Manifold touchResult = SAT.checkCollisions(body, touchBody, vector, offset);
            if(touchResult.isOverlapped())
            {
                pushedDown = true;
            }
            else
            {
                pushedDown = false;
            }
            touchResult.destruct();
            Manifold.pool.recycle(touchResult);
            Vec.pool.recycle(vector);
        }
        else if(input.action == InputBuffer.ACTION_TOUCH_UP)
        {
            if(pushedDown)
            {
                //check if within object and push up and process press if so
                Vec vector = Vec.pool.get();
                Manifold touchResult = SAT.checkCollisions(body, touchBody, vector, offset);
                if(touchResult.isOverlapped())
                {
                    pressed = true;
                }
                pushedDown = false;

                touchResult.destruct();
                Manifold.pool.recycle(touchResult);
                Vec.pool.recycle(vector);
            }
        }

        Vec.pool.recycle(offset);
        touchBody.destruct();
        Polygon.pool.recycle(touchBody);
        ArrayPool.floatMaster.recycle(vertices, vertices.length);
    }

    public void update()
    {
        if(pushedDown)
        {
            changeFrame(0, PRESSED_FRAME);
        }
        else
        {
            changeFrame(0, UNPRESSED_FRAME);
        }

        if(hasBeenPressed())
        {
            Menu.menu.queueStateChange();

            pressed = false;
        }
    }

    public void move(float moveX, float moveY)
    {
        body.move(body.getX() + moveX, body.getY() +  moveY, 0);
        body.setX(body.getX() + moveX);
        body.setY(body.getY() + moveY);
    }

    public boolean hasBeenPressed()
    {
        return pressed;
    }

    public void destruct()
    {
        super.destruct();

        if(this.getClass().equals(MenuButton.class))
        {
            MenuButton.pool.recycle(this);
        }
    }
}