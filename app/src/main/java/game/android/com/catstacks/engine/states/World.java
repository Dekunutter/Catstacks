package game.android.com.catstacks.engine.states;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.core.GameThread;
import game.android.com.catstacks.engine.core.SavedState;
import game.android.com.catstacks.engine.physics.ManifoldComparator;
import game.android.com.catstacks.engine.core.RenderedState;
import game.android.com.catstacks.engine.core.ScoreSession;
import game.android.com.catstacks.engine.core.Screen;
import game.android.com.catstacks.engine.core.Time;
import game.android.com.catstacks.engine.objects.WorldObject;
import game.android.com.catstacks.engine.physics.AABB;
import game.android.com.catstacks.engine.physics.CollisionResolver;
import game.android.com.catstacks.engine.physics.Manifold;
import game.android.com.catstacks.engine.physics.ManifoldDiscreteComparator;
import game.android.com.catstacks.engine.physics.Pair;
import game.android.com.catstacks.engine.physics.QuadTree;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.rendering.BufferObject;
import game.android.com.catstacks.engine.rendering.TextureLoader;
import game.android.com.catstacks.engine.sound.SoundManager;
import game.android.com.catstacks.game.collision.CollisionSensor;
import game.android.com.catstacks.game.logic.BackgroundTheme;
import game.android.com.catstacks.game.objects.dynamic.Cat;
import game.android.com.catstacks.game.objects.dynamic.Player;
import game.android.com.catstacks.game.objects.dynamic.Rocket;
import game.android.com.catstacks.game.objects.statics.Barrier;
import game.android.com.catstacks.game.objects.statics.Floor;
import game.android.com.catstacks.game.objects.statics.backgrounds.AngelFish;
import game.android.com.catstacks.game.objects.statics.backgrounds.weather.TechnoDrop;
import game.android.com.catstacks.game.objects.statics.spawners.BackgroundSpawner;
import game.android.com.catstacks.game.objects.statics.spawners.BalloonCatSpawner;
import game.android.com.catstacks.game.objects.statics.spawners.BubbleSpawner;
import game.android.com.catstacks.game.objects.statics.spawners.CatSpawner;
import game.android.com.catstacks.game.objects.statics.spawners.CherryLeafSpawner;
import game.android.com.catstacks.game.objects.statics.spawners.GhostSpawner;
import game.android.com.catstacks.game.objects.statics.spawners.RainSpawner;
import game.android.com.catstacks.game.objects.statics.spawners.SnowSpawner;
import game.android.com.catstacks.game.objects.statics.spawners.TechnoSpawner;
import game.android.com.catstacks.game.objects.ui.HighScoreboard;
import game.android.com.catstacks.game.objects.ui.LiveCounter;
import game.android.com.catstacks.game.objects.ui.QuitButton;
import game.android.com.catstacks.game.objects.ui.RestartButton;
import game.android.com.catstacks.game.objects.ui.Scoreboard;
import game.android.com.catstacks.game.objects.ui.StackCounter;
import game.android.com.catstacks.game.objects.ui.TextBox;

public class World extends RenderedState
{
    public static World world;
    public final WorldSave save = new WorldSave();

    private int width, height;

    private static Player player;

    private Vec gravity;
    private QuadTree quad;

    private boolean isGameOver = false;

    private static float gameSpeed;
    private static int previousScore;

    private BackgroundTheme.Theme activeTheme;

    public World()
    {
        super(RenderedState.getContext());

        width = Screen.screen.getWidth();
        height = Screen.screen.getHeight();

        gravity = new Vec(0, -9.8f);

        isGameOver = false;

        gameSpeed = 0.4f;
        Time.updateDelta(gameSpeed);
        previousScore = 0;
    }

    public World(int adCounter)
    {
        this();
        institialAdCounter = adCounter;
    }

    @Override
    public void update()
    {
        if(! isGameOver && isGameOver())
        {
            initGameOverMenu();
            saveScore();
            isGameOver = true;

            isAdReady = true;
            institialAdCounter++;
        }

        toRemove.clear();

        for(int i = 0; i < objects.size(); i++)
        {
            objects.get(i).applyMovement();
            objects.get(i).applyGravity(gravity);
            objects.get(i).applyFriction();
        }

        initQuadTree();
        solveCollisions();

        super.update();

        //NOTE: This should update on score increases, not every frame in the loop
        if(player.hasScored())
        {
            updateGameSpeed();
            player.setScored(false);
        }
    }

    public void buffer()
    {
        synchronized(bufferMutex)
        {
            removeBuffer.clear();

            for(int i = 0; i < objects.size(); i++)
            {
                if(objects.get(i).getRemove())
                {
                    toRemove.add(objects.get(i));
                    removeBuffer.add(buffers.get(i));
                }
                else
                {
                    if(objects.get(i).isRendered())
                    {
                        if(objects.get(i).getBody() != null)
                        {
                            buffers.get(i).updateBodyParams(objects.get(i).getBody());
                        }

                        if(objects.get(i).hasVerticesChanged())
                        {
                            buffers.get(i).updateVertices(objects.get(i).getRenderVertices());
                            objects.get(i).setVerticesChanged(false);
                        }

                        if(objects.get(i).hasColoursChanged())
                        {
                            buffers.get(i).updateColours(objects.get(i).getRenderColours());
                            objects.get(i).setColoursChanged(false);
                        }

                        if(objects.get(i).hasFrameChanged())
                        {
                            buffers.get(i).updateActiveFrame(objects.get(i).getActiveFrames());
                            objects.get(i).setFrameChanged(false);
                        }

                        if(objects.get(i).hasIndicesChanged())
                        {
                            buffers.get(i).updateIndices(objects.get(i).getIndices());
                            objects.get(i).setIndicesChanged(false);
                        }

                        if(objects.get(i).isTiled())
                        {
                            buffers.get(i).setTiled(true);
                        }

                        if(objects.get(i).isStoringBitmap())
                        {
                            buffers.get(i).setStoreBitmap(true);
                        }
                    }
                }
            }

            for(int i = 0; i < toRemove.size(); i++)
            {
                toRemove.get(i).destruct();
                objects.remove(toRemove.get(i));
            }
            toRemove.clear();

            for(int i = 0; i < removeBuffer.size(); i++)
            {
                //removeBuffer.get(i).destruct();
                buffers.remove(removeBuffer.get(i));
            }

            for(int i = 0; i < newSpawns.size(); i++)
            {
                if(newSpawns.get(i).isRendered())
                {
                    if(newSpawns.get(i).getBody() != null)
                    {
                        BufferObject buffer = BufferObject.pool.get();
                        buffer.init(newSpawns.get(i).getBody(), newSpawns.get(i).getTextures(), newSpawns.get(i).getRenderVertices(), newSpawns.get(i).getRenderColours(), newSpawns.get(i).getActiveFrames(), newSpawns.get(i).getIndices(), newSpawns.get(i).hasAlpha(), newSpawns.get(i).isStoringBitmap(), newSpawns.get(i).isTiled());
                        buffers.add(buffer);
                    }
                    else
                    {
                        buffers.add(new BufferObject(newSpawns.get(i).getTextures(), newSpawns.get(i).getRenderVertices(), newSpawns.get(i).getRenderColours(), newSpawns.get(i).getActiveFrames(), newSpawns.get(i).getIndices(), newSpawns.get(i).hasAlpha(), newSpawns.get(i).isStoringBitmap(), newSpawns.get(i).isTiled()));
                    }
                }
                else
                {
                    buffers.add(new BufferObject());
                }

                objects.add(newSpawns.get(i));

                removeSpawns.add(newSpawns.get(i));
            }

            for(int i = 0; i < removeSpawns.size(); i++)
            {
                newSpawns.remove(removeSpawns.get(i));
            }
            removeSpawns.clear();
        }
    }

    public void chooseTheme()
    {
        activeTheme = BackgroundTheme.randomTheme();
        //activeTheme = BackgroundTheme.Theme.DESERT;
    }

    public void initLevel()
    {
        chooseTheme();

        int width = Screen.screen.getWidth();
        int height = Screen.screen.getHeight();

        AABB bounds = AABB.pool.get();
        Vec lower = Vec.pool.get();
        bounds.init(lower);
        bounds.setBounds(0, 0);
        bounds.setBounds(width, height);
        quad = QuadTree.pool.get();
        quad.init(0, bounds);

        synchronized(newSpawns)
        {
            player = new Player(new Vec(width / 2, Screen.screen.getPixelDensity(20)), activeTheme);
            newSpawns.add(player);

            newSpawns.add(new TextBox("Score", 0.5f, 0, height - Screen.screen.getPixelDensity(16), false));
            newSpawns.add(new Scoreboard(0, 0, height - Screen.screen.getPixelDensity(32), player));
            newSpawns.add(new TextBox("Stack", 0.5f, width / 2, height - Screen.screen.getPixelDensity(16)));
            newSpawns.add(new StackCounter(0, width / 2, height - Screen.screen.getPixelDensity(32), player));
            newSpawns.add(new TextBox("Lives", 0.5f, width - Screen.screen.getPixelDensity(60), height - Screen.screen.getPixelDensity(16), false));
            newSpawns.add(new LiveCounter(3, width - (Screen.screen.getPixelDensity(80) / 2), height - Screen.screen.getPixelDensity(32), player));

            Vec barrierDimensions = new Vec(1, height);
            Vec barrierPos = new Vec(-1, 0);
            Barrier leftBarrier = new Barrier(barrierPos, barrierDimensions);
            barrierPos = new Vec(width, 0);
            Barrier rightBarrier = new Barrier(barrierPos, barrierDimensions);
            newSpawns.add(leftBarrier);
            newSpawns.add(rightBarrier);

            Vec floorDimensions = new Vec(width, Screen.screen.getPixelDensity(20));
            Vec floorPos = new Vec(0, 0);
            Floor floor = new Floor(floorPos, floorDimensions, activeTheme);
            newSpawns.add(floor);

            Vec rocketPos = new Vec(0, Screen.screen.getPixelDensity(20));
            newSpawns.add(new Rocket(rocketPos));
            rocketPos = new Vec(width - Screen.screen.getPixelDensity(44), Screen.screen.getPixelDensity(20));
            newSpawns.add(new Rocket(rocketPos));

            newSpawns.add(new CatSpawner(width, height, player));
            newSpawns.add(new BalloonCatSpawner(width, height, player));

            BackgroundSpawner backgroundSpawner = new BackgroundSpawner(width, height, player, activeTheme);
            newSpawns.add(backgroundSpawner);

            SoundManager.soundManager.stopMusic();
            SoundManager.soundManager.initMediaPlayer(context, backgroundSpawner.getMusicId());
            SoundManager.soundManager.playMusicLooped();
        }
    }

    private void initQuadTree()
    {
        //quad.clear();
        quad = QuadTree.pool.get();
        AABB bounds = (AABB) AABB.pool.get();
        Vec newBound = Vec.pool.get();
        bounds.init(newBound);
        bounds.setBounds(0, 0);
        bounds.setBounds(width, height);
        quad.init(0, bounds);

        for(int i = 0; i < objects.size(); i++)
        {
            if(objects.get(i).isCollidable())
            {
                quad.insert(objects.get(i));
            }
        }
    }

    private void solveCollisions()
    {
        ArrayList<Pair> pairs = quad.retrievePairs(objects);
        /*ArrayList<Pair> pairs = ArrayPool.pairPool.get();
        for(int i = 0; i < objects.size(); i++)
        {
            if(!objects.get(i).isCollidable())
            {
                continue;
            }

            for(int j = 0; j < objects.size(); j++)
            {
                if(objects.get(i).equals(objects.get(j)))
                {
                    continue;
                }

                if(! objects.get(j).isCollidable())
                {
                    continue;
                }

                Pair newPair = Pair.pool.get();
                newPair.setObjectA(objects.get(i));
                newPair.setObjectB(objects.get(j));

                if(!pairs.contains(newPair))
                {
                    pairs.add(newPair);
                }
                else
                {
                    Pair.pool.recycle(newPair);
                }
            }
        }*/

        int j = 0;
        while(true)
        {
            ArrayList<Manifold> manifolds = ArrayPool.manifoldPool.get();

            for(int i = 0; i < pairs.size(); i++)
            {
                Manifold results = CollisionSensor.categorizeCollision(pairs.get(i));

                if(results != null)
                {
                    if(results.isCollided() || results.isOverlapped())
                    {
                        manifolds.add(results);
                    }
                    else
                    {
                        results.destruct();
                        Manifold.pool.recycle(results);
                    }
                }
            }

            if(manifolds.isEmpty())
            {
                manifolds.clear();
                ArrayPool.manifoldPool.recycle(manifolds);
                break;
            }


            //NOTE: Sorting provides frame-perfect collisions but might be a performance cost. Keep that in mind
            //ManifoldComparator compare = ManifoldComparator.pool.get();
            //Collections.sort(manifolds, compare);
            //ManifoldComparator.pool.recycle(compare);
            /*for(int i = 0; i < manifolds.size(); i++)
            {
                int min = i;
                for(int k = i; k < manifolds.size(); k++)
                {
                    if(manifolds.get(min).getEnterTime() > manifolds.get(k).getEnterTime())
                    {
                        min = k;
                    }
                }

                Manifold tmp = manifolds.get(i);
                manifolds.set(i, manifolds.get(min));
                manifolds.set(min, tmp);
            }

            //ManifoldDiscreteComparator compareDiscrete = ManifoldDiscreteComparator.pool.get();
            //Collections.sort(manifolds, compareDiscrete);
            //ManifoldDiscreteComparator.pool.recycle(compareDiscrete);
            for(int i = 0; i < manifolds.size(); i++)
            {
                int min = i;
                for(int k = i; k < manifolds.size(); k++)
                {
                    if(manifolds.get(min).getDiscreteEnterTime() > manifolds.get(k).getDiscreteEnterTime())
                    {
                        min = k;
                    }
                }

                Manifold tmp = manifolds.get(i);
                manifolds.set(i, manifolds.get(min));
                manifolds.set(min, tmp);
            }*/

            for(int i = 0; i < manifolds.size(); i++)
            {
                //NOTE: might be better (in case the bottom commented out if statement is actually important) to pass the manifold list into the function instead of recalling on each manifold
                CollisionResolver.solveCollision(manifolds.get(i));
            }

            for(int i = 0; i < manifolds.size(); i++)
            {
                manifolds.get(i).destruct();
                Manifold.pool.recycle(manifolds.get(i));
            }
            manifolds.clear();
            ArrayPool.manifoldPool.recycle(manifolds);

            if(j == 0)
            {
                break;
            }
            j++;
        }

        for(int i = 0; i < pairs.size(); i++)
        {
            pairs.get(i).destruct();
            Pair.pool.recycle(pairs.get(i));
        }
        pairs.clear();
        ArrayPool.pairPool.recycle(pairs);

        quad.destruct();
    }

    public void spawnObject(WorldObject object)
    {
        newSpawns.add(object);
    }

    public int getWorldWidth()
    {
        return width;
    }

    public int getWorldHeight()
    {
        return height;
    }

    public void subtractLife()
    {
        player.subtractLife();
    }

    public void addLife()
    {
        player.addLife();
    }

    public boolean isGameOver()
    {
        return player.getLives() <= 0;
    }

    private void initGameOverMenu()
    {
        newSpawns.add(new RestartButton(new Vec(Screen.screen.getPixelDensity(100), Screen.screen.getPixelDensity(150))));
        newSpawns.add(new QuitButton(new Vec(Screen.screen.getPixelDensity(200), Screen.screen.getPixelDensity(150))));

        long highScore = ScoreSession.session.readScore();
        if(player.getScore() > highScore)
        {
            newSpawns.add(new TextBox("NEW HIGH SCORE: ", World.world.getWorldWidth() / 2, Screen.screen.getPixelDensity(300)));
            newSpawns.add(new HighScoreboard(context, player.getScore(), World.world.getWorldWidth() / 2, Screen.screen.getPixelDensity(250)));
        }
        else
        {
            newSpawns.add(new TextBox("FINAL SCORE: ", World.world.getWorldWidth() / 2, Screen.screen.getPixelDensity(300)));
            newSpawns.add(new HighScoreboard(context, player.getScore(), highScore, World.world.getWorldWidth() / 2, Screen.screen.getPixelDensity(250)));
        }
    }

    private void saveScore()
    {
        ScoreSession.session.saveScore(player.getScore());
    }

    public static float getGameSpeed()
    {
        return gameSpeed;
    }

    public static void updateGameSpeed()
    {
        if(player.getScore() >= 100000)
        {
            gameSpeed = 1.45f;
        }
        else if(player.getScore() >= 90000)
        {
            gameSpeed = 1.425f;
        }
        else if(player.getScore() >= 80000)
        {
            gameSpeed = 1.4f;
        }
        else if(player.getScore() >= 70000)
        {
            gameSpeed = 1.375f;
        }
        else if(player.getScore() >= 60000)
        {
            gameSpeed = 1.35f;
        }
        else if(player.getScore() >= 50000)
        {
            gameSpeed = 1.325f;
        }
        else if(player.getScore() >= 40000)
        {
            gameSpeed = 1.3f;
        }
        else if(player.getScore() >= 30000)
        {
            gameSpeed = 1.275f;
        }
        else if(player.getScore() >= 27500)
        {
            gameSpeed = 1.255f;
        }
        else if(player.getScore() >= 25000)
        {
            gameSpeed = 1.225f;
        }
        else if(player.getScore() >= 22500)
        {
            gameSpeed = 1.2f;
        }
        else if(player.getScore() >= 20000)
        {
            gameSpeed = 1.175f;
        }
        else if(player.getScore() >= 17500)
        {
            gameSpeed = 1.155f;
        }
        else if(player.getScore() >= 15000)
        {
            gameSpeed = 1.125f;
        }
        else if(player.getScore() >= 12500)
        {
            gameSpeed = 1.1f;
        }
        else if(player.getScore() >= 10000)
        {
            gameSpeed = 1.075f;
        }
        else if(player.getScore() >= 9000)
        {
            gameSpeed = 1.05f;
        }
        else if(player.getScore() >= 8000)
        {
            gameSpeed = 1.025f;
        }
        else if(player.getScore() >= 7000)
        {
            gameSpeed = 1f;
        }
        else if(player.getScore() >= 6000)
        {
            gameSpeed = 0.975f;
        }
        else if(player.getScore() >= 5000)
        {
            gameSpeed = 0.95f;
        }
        else if(player.getScore() >= 4500)
        {
            gameSpeed = 0.925f;
        }
        else if(player.getScore() >= 4000)
        {
            gameSpeed = 0.9f;
        }
        else if(player.getScore() >= 3500)
        {
            gameSpeed = 0.875f;
        }
        else if(player.getScore() >= 3000)
        {
            gameSpeed = 0.85f;
        }
        else if(player.getScore() >= 2500)
        {
            gameSpeed = 0.825f;
        }
        else if(player.getScore() >= 2000)
        {
            gameSpeed = 0.8f;
        }
        else if(player.getScore() >= 1500)
        {
            gameSpeed = 0.75f;
        }
        else if(player.getScore() >= 1000)
        {
            gameSpeed = 0.7f;
        }
        else if(player.getScore() >= 750)
        {
            gameSpeed = 0.65f;
        }
        else if(player.getScore() >= 500)
        {
            gameSpeed = 0.6f;
        }
        else if(player.getScore() >= 250)
        {
            gameSpeed = 0.55f;
        }
        else if(player.getScore() >= 150)
        {
            gameSpeed = 0.5f;
        }
        else if(player.getScore() >= 100)
        {
            gameSpeed = 0.45f;
        }
        Time.updateDelta(gameSpeed);
    }

    public void save()
    {
        synchronized(save)
        {
            save.context = context;

            save.objects = objects;
            save.newSpawns = newSpawns;
            save.toRemove = toRemove;
            save.removeSpawns = removeSpawns;
            save.buffers = buffers;
            save.removeBuffer = removeBuffer;

            save.isAdReady = isAdReady;
            save.institialAdCounter = institialAdCounter;

            save.width = width;
            save.height = height;
            save.player = player;
            save.gravity = gravity;
            save.quad = quad;

            save.session = ScoreSession.session;
            save.isGameOver = isGameOver;

            save.gameSpeed = gameSpeed;
            save.previousScore = previousScore;

            save.isValid = true;
        }
    }

    public void restore()
    {
        synchronized(save)
        {
            WorldSave save = this.save;
            if(!save.isValid)
            {
                reset();
                save();
                return;
            }

            context = save.context;

            objects = save.objects;
            newSpawns = save.newSpawns;
            toRemove = save.toRemove;
            removeSpawns = save.removeSpawns;
            buffers = save.buffers;
            removeBuffer = save.removeBuffer;

            isAdReady = save.isAdReady;
            institialAdCounter = save.institialAdCounter;

            width = save.width;
            height = save.height;
            player = save.player;
            gravity = save.gravity;
            quad = save.quad;

            ScoreSession.session = save.session;
            isGameOver = save.isGameOver;

            gameSpeed = save.gameSpeed;
            previousScore = save.previousScore;
        }
    }

    public void reset()
    {
        width = Screen.screen.getWidth();
        height = Screen.screen.getHeight();

        gravity = new Vec(0, -9.8f);

        ScoreSession.session = new ScoreSession(context);
        isGameOver = false;

        gameSpeed = 0.25f;
        Time.updateDelta(gameSpeed);
        previousScore = 0;
    }

    public static class WorldSave extends SavedState
    {
        public int width, height;

        public static Player player;

        public Vec gravity;
        public QuadTree quad;

        public ScoreSession session;
        public boolean isGameOver;

        public static float gameSpeed;
        public static int previousScore;
    }

    public void destruct()
    {
        for(int i = 0; i < objects.size(); i++)
        {
            objects.get(i).remove();
        }
    }
}
