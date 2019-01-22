package game.android.com.catstacks.game.objects.statics;

import game.android.com.catstacks.engine.objects.WorldObject;
import game.android.com.catstacks.engine.physics.CollisionBitmasks;
import game.android.com.catstacks.engine.physics.Polygon;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;

public class Barrier extends WorldObject
{
    public Barrier(Vec position, Vec dimensions)
    {
        super();

        float vertices[] = ArrayPool.floatMaster.get(12);
        vertices[0] = 0;
        vertices[1] = 0;
        vertices[2] = 0;
        vertices[3] = 0;
        vertices[4] = dimensions.y;
        vertices[5] = 0;
        vertices[6] = dimensions.x;
        vertices[7] = dimensions.y;
        vertices[8] = 0;
        vertices[9] = dimensions.x;
        vertices[10] = 0;
        vertices[11] = 0;
        body = new Polygon(position.x, position.y, 0, vertices);
        body.move(position.x, position.y, 0);

        ArrayPool.floatMaster.recycle(vertices, vertices.length);

        setUnmovable(true);
        body.setMass(0);

        mask = CollisionBitmasks.BARRIER_ID;
    }
}
