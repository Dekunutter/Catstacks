package game.android.com.catstacks.game.objects.statics.backgrounds;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.core.RenderedState;
import game.android.com.catstacks.engine.core.Screen;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.rendering.Texture;
import game.android.com.catstacks.engine.rendering.TextureLoader;

public class StormClouds extends BackgroundScenery
{
    public StormClouds(Vec oldPosition)
    {
        addSpriteToPosition(R.drawable.storm_clouds, -8f, 0, oldPosition);

        activeFrames.add(null);
    }

    @Override
    public void addSpriteToPosition(int resourceId, float depth, float alpha, Vec oldPosition)
    {
        Texture texture = TextureLoader.loadTexture(RenderedState.getContext(), resourceId);
        width = Screen.screen.getPixelDensity(texture.getWidth() * PIXEL_SCALE);
        height = Screen.screen.getPixelDensity(texture.getHeight() * PIXEL_SCALE);

        Vec position = Vec.pool.get();
        position.x = oldPosition.x;
        position.y = oldPosition.y - height;
        float[] newVertices = ArrayPool.floatMaster.get(12);
        newVertices[0] = position.x;
        newVertices[1] = position.y + height;
        newVertices[2] = depth;
        newVertices[3] = position.x;
        newVertices[4] = position.y;
        newVertices[5] = depth;
        newVertices[6] = position.x + width;
        newVertices[7] = position.y;
        newVertices[8] = depth;
        newVertices[9] = position.x + width;
        newVertices[10] = position.y + height;
        newVertices[11] = depth;

        float[] newColours = ArrayPool.floatMaster.get(16);
        newColours[0] = 1;
        newColours[1] = 1;
        newColours[2] = 1;
        newColours[3] = alpha;
        newColours[4] = 1;
        newColours[5] = 1;
        newColours[6] = 1;
        newColours[7] = alpha;
        newColours[8] = 1;
        newColours[9] = 1;
        newColours[10] = 1;
        newColours[11] = alpha;
        newColours[12] = 1;
        newColours[13] = 1;
        newColours[14] = 1;
        newColours[15] = alpha;

        sprVertices.add(newVertices);
        sprColours.add(newColours);
        textures.add(texture);

        Vec.pool.recycle(position);
    }
}
