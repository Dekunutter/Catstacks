package game.android.com.catstacks.game.objects.statics;

import android.util.Log;

import java.util.Arrays;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.objects.WorldObject;
import game.android.com.catstacks.engine.physics.CollisionBitmasks;
import game.android.com.catstacks.engine.physics.Polygon;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.game.logic.BackgroundTheme;

public class Floor extends WorldObject
{
    public Floor(Vec position, Vec dimensions, BackgroundTheme.Theme activeTheme)
    {
        super();

        int tilesX = 0;
        if(activeTheme == BackgroundTheme.Theme.ASIAN)
        {
            tilesX = addTiledSprite(R.drawable.dirt_ground, 0, 1, dimensions);
        }
        else if(activeTheme == BackgroundTheme.Theme.SPOOKY)
        {
            tilesX = addTiledSprite(R.drawable.dirt_ground, 0, 1, dimensions);
        }
        else if(activeTheme == BackgroundTheme.Theme.CHRISTMAS)
        {
            tilesX = addTiledSprite(R.drawable.snow_ground, 0, 1, dimensions);
        }
        else if(activeTheme == BackgroundTheme.Theme.BEACH)
        {
            tilesX = addTiledSprite(R.drawable.sand_ground, 0, 1, dimensions);
        }
        else if(activeTheme == BackgroundTheme.Theme.DESERT)
        {
            tilesX = addTiledSprite(R.drawable.sand_ground, 0, 1, dimensions);
        }
        else if(activeTheme == BackgroundTheme.Theme.CANYON)
        {
            tilesX = addTiledSprite(R.drawable.sand_ground, 0, 1, dimensions);
        }
        else if(activeTheme == BackgroundTheme.Theme.FOREST)
        {
            tilesX = addTiledSprite(R.drawable.dirt_ground, 0, 1, dimensions);
        }
        else if(activeTheme == BackgroundTheme.Theme.CITY)
        {
            tilesX = addTiledSprite(R.drawable.brick_ground, 0, 1, dimensions);
        }
        else if(activeTheme == BackgroundTheme.Theme.CITYPARK)
        {
            tilesX = addTiledSprite(R.drawable.dirt_ground, 0, 1, dimensions);
        }
        else if(activeTheme == BackgroundTheme.Theme.UNDERWATER)
        {
            tilesX = addTiledSprite(R.drawable.sand_ground, 0, 1, dimensions);
        }
        else
        {
            tilesX = addTiledSprite(R.drawable.dirt_ground, 0, 1, dimensions);
        }

        activeFrames.add(new float[]{0, 0, 0, 1, tilesX, 1, tilesX, 0});
        defaultFrames.add(0);

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

        body = Polygon.pool.get();
        body.init(position.x, position.y, 0, vertices);
        body.move(position.x, position.y, 0);

        ArrayPool.floatMaster.recycle(vertices, vertices.length);

        setUnmovable(true);
        body.setMass(0);

        mask = CollisionBitmasks.GROUND_ID;

        hasAlpha = false;
        tiled = true;
    }
}
