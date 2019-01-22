package game.android.com.catstacks.game.objects.statics.spawners;

import android.util.Log;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.core.Screen;
import game.android.com.catstacks.engine.objects.WorldObject;
import game.android.com.catstacks.engine.objects.statics.BackgroundScenery;
import game.android.com.catstacks.engine.objects.statics.spawners.ScoreSpawner;
import game.android.com.catstacks.engine.objects.statics.spawners.Spawner;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.pool.ArrayPool;
import game.android.com.catstacks.engine.sound.Sound;
import game.android.com.catstacks.engine.states.World;
import game.android.com.catstacks.game.logic.BackgroundTheme;
import game.android.com.catstacks.game.objects.dynamic.Player;
import game.android.com.catstacks.game.objects.statics.backgrounds.AngelFish;
import game.android.com.catstacks.game.objects.statics.backgrounds.Arch;
import game.android.com.catstacks.game.objects.statics.backgrounds.Basketball;
import game.android.com.catstacks.game.objects.statics.backgrounds.BasketballHoop;
import game.android.com.catstacks.game.objects.statics.backgrounds.Bat;
import game.android.com.catstacks.game.objects.statics.backgrounds.BeachBall;
import game.android.com.catstacks.game.objects.statics.backgrounds.Biplane;
import game.android.com.catstacks.game.objects.statics.backgrounds.Brontosaurus;
import game.android.com.catstacks.game.objects.statics.backgrounds.Buildings;
import game.android.com.catstacks.game.objects.statics.backgrounds.Cactuses;
import game.android.com.catstacks.game.objects.statics.backgrounds.Camel;
import game.android.com.catstacks.game.objects.statics.backgrounds.Campfire;
import game.android.com.catstacks.game.objects.statics.backgrounds.CandyCane;
import game.android.com.catstacks.game.objects.statics.backgrounds.CanyonCliffs;
import game.android.com.catstacks.game.objects.statics.backgrounds.CatGenie;
import game.android.com.catstacks.game.objects.statics.backgrounds.CherryBlossoms;
import game.android.com.catstacks.game.objects.statics.backgrounds.Clouds;
import game.android.com.catstacks.game.objects.statics.backgrounds.ConiferForest;
import game.android.com.catstacks.game.objects.statics.backgrounds.CoralReef;
import game.android.com.catstacks.game.objects.statics.backgrounds.CowSkull;
import game.android.com.catstacks.game.objects.statics.backgrounds.Crab;
import game.android.com.catstacks.game.objects.statics.backgrounds.DeadForest;
import game.android.com.catstacks.game.objects.statics.backgrounds.Deer;
import game.android.com.catstacks.game.objects.statics.backgrounds.Eel;
import game.android.com.catstacks.game.objects.statics.backgrounds.ExplodingPlanet;
import game.android.com.catstacks.game.objects.statics.backgrounds.FestiveForest;
import game.android.com.catstacks.game.objects.statics.backgrounds.Fish;
import game.android.com.catstacks.game.objects.statics.backgrounds.Flower;
import game.android.com.catstacks.game.objects.statics.backgrounds.FlowerBush;
import game.android.com.catstacks.game.objects.statics.backgrounds.Forest;
import game.android.com.catstacks.game.objects.statics.backgrounds.FullMoon;
import game.android.com.catstacks.game.objects.statics.backgrounds.GiantClock;
import game.android.com.catstacks.game.objects.statics.backgrounds.GoldenCat;
import game.android.com.catstacks.game.objects.statics.backgrounds.Grass;
import game.android.com.catstacks.game.objects.statics.backgrounds.Gravestone;
import game.android.com.catstacks.game.objects.statics.backgrounds.GravestoneCross;
import game.android.com.catstacks.game.objects.statics.backgrounds.HauntedCastle;
import game.android.com.catstacks.game.objects.statics.backgrounds.Iceburg;
import game.android.com.catstacks.game.objects.statics.backgrounds.KoiFish;
import game.android.com.catstacks.game.objects.statics.backgrounds.LaughingPumpkin;
import game.android.com.catstacks.game.objects.statics.backgrounds.LogCabin;
import game.android.com.catstacks.game.objects.statics.backgrounds.Moon;
import game.android.com.catstacks.game.objects.statics.backgrounds.MountFuji;
import game.android.com.catstacks.game.objects.statics.backgrounds.Mountains;
import game.android.com.catstacks.game.objects.statics.backgrounds.NorthStar;
import game.android.com.catstacks.game.objects.statics.backgrounds.Obelisk;
import game.android.com.catstacks.game.objects.statics.backgrounds.Pagoda;
import game.android.com.catstacks.game.objects.statics.backgrounds.PalmTrees;
import game.android.com.catstacks.game.objects.statics.backgrounds.PaperLanturn;
import game.android.com.catstacks.game.objects.statics.backgrounds.Penguin;
import game.android.com.catstacks.game.objects.statics.backgrounds.Planet;
import game.android.com.catstacks.game.objects.statics.backgrounds.Pyramid;
import game.android.com.catstacks.game.objects.statics.backgrounds.Rattlesnake;
import game.android.com.catstacks.game.objects.statics.backgrounds.RedSun;
import game.android.com.catstacks.game.objects.statics.backgrounds.Saloon;
import game.android.com.catstacks.game.objects.statics.backgrounds.SandCastle;
import game.android.com.catstacks.game.objects.statics.backgrounds.SandDunes;
import game.android.com.catstacks.game.objects.statics.backgrounds.SandPiles;
import game.android.com.catstacks.game.objects.statics.backgrounds.SandstonePillar;
import game.android.com.catstacks.game.objects.statics.backgrounds.SantaSleigh;
import game.android.com.catstacks.game.objects.statics.backgrounds.SeaArch;
import game.android.com.catstacks.game.objects.statics.backgrounds.SeaStack;
import game.android.com.catstacks.game.objects.statics.backgrounds.Seahorse;
import game.android.com.catstacks.game.objects.statics.backgrounds.Seaweed;
import game.android.com.catstacks.game.objects.statics.backgrounds.ShootingStar;
import game.android.com.catstacks.game.objects.statics.backgrounds.Skull;
import game.android.com.catstacks.game.objects.statics.backgrounds.Sky;
import game.android.com.catstacks.game.objects.statics.backgrounds.SmilingSun;
import game.android.com.catstacks.game.objects.statics.backgrounds.SnowPiles;
import game.android.com.catstacks.game.objects.statics.backgrounds.SnowyForest;
import game.android.com.catstacks.game.objects.statics.backgrounds.Sphinx;
import game.android.com.catstacks.game.objects.statics.backgrounds.SpookyForest;
import game.android.com.catstacks.game.objects.statics.backgrounds.Stars;
import game.android.com.catstacks.game.objects.statics.backgrounds.StoneLanturn;
import game.android.com.catstacks.game.objects.statics.backgrounds.StormClouds;
import game.android.com.catstacks.game.objects.statics.backgrounds.StreetLamps;
import game.android.com.catstacks.game.objects.statics.backgrounds.SunkenShip;
import game.android.com.catstacks.game.objects.statics.backgrounds.Teepee;
import game.android.com.catstacks.game.objects.statics.backgrounds.Trash;
import game.android.com.catstacks.game.objects.statics.backgrounds.Tumbleweed;
import game.android.com.catstacks.game.objects.statics.backgrounds.TwistedTree;
import game.android.com.catstacks.game.objects.statics.backgrounds.Ufo;
import game.android.com.catstacks.game.objects.statics.backgrounds.Umbrella;
import game.android.com.catstacks.game.objects.statics.backgrounds.Volcanoes;
import game.android.com.catstacks.game.objects.statics.backgrounds.Wagon;
import game.android.com.catstacks.game.objects.statics.backgrounds.Wolf;

public class BackgroundSpawner extends ScoreSpawner
{
    private BackgroundScenery[] backgrounds;
    private Spawner weather;
    private int musicId;

    private static int initialSpawns, weatherSpawn;
    private static boolean weathered;

    public BackgroundSpawner(int worldWidth, int worldHeight, Player tracking, BackgroundTheme.Theme activeTheme)
    {
        super(worldWidth, worldHeight, tracking);

        weathered = false;

        if(activeTheme == BackgroundTheme.Theme.ASIAN)
        {
            initAsianTheme(activeTheme);
        }
        else if(activeTheme == BackgroundTheme.Theme.SPOOKY)
        {
            initSpookyTheme(activeTheme);
        }
        else if(activeTheme == BackgroundTheme.Theme.CHRISTMAS)
        {
            initChristmasTheme(activeTheme);
        }
        else if(activeTheme == BackgroundTheme.Theme.BEACH)
        {
            initBeachTheme(activeTheme);
        }
        else if(activeTheme == BackgroundTheme.Theme.DESERT)
        {
            initDesertTheme(activeTheme);
        }
        else if(activeTheme == BackgroundTheme.Theme.CANYON)
        {
            initCanyonTheme(activeTheme);
        }
        else if(activeTheme == BackgroundTheme.Theme.FOREST)
        {
            initForestTheme(activeTheme);
        }
        else if(activeTheme == BackgroundTheme.Theme.CITY)
        {
            initCityTheme(activeTheme);
        }
        else if(activeTheme == BackgroundTheme.Theme.CITYPARK)
        {
            initCityParkTheme(activeTheme);
        }
        else if(activeTheme == BackgroundTheme.Theme.UNDERWATER)
        {
            initUnderwaterTheme(activeTheme);
        }
        else
        {
            initForestTheme(activeTheme);
        }

        for(int i = 0; i < initialSpawns; i++)
        {
            BackgroundScenery scenery = backgrounds[spawned];
            scenery.setSpawned(true);
            World.world.spawnObject(scenery);
            spawned++;
        }
    }

    @Override
    public void update()
    {
        if(spawned < intervals.length)
        {
            updateSpawnScoreTracker(intervals[spawned]);

            if(isSpawnReady())
            {
                BackgroundScenery scenery = backgrounds[spawned - 1];
                scenery.setSpawned(true);
                World.world.spawnObject(scenery);
            }
        }

        setSpawn(false);

        if(!weathered && (tracking.getScore() >= weatherSpawn))
        {
            weathered = true;
            World.world.spawnObject(weather);
        }
    }

    public void initAsianTheme(BackgroundTheme.Theme activeTheme)
    {
        Vec position = Vec.pool.get();
        backgrounds = ArrayPool.backgroundSceneryPool.get(17);
        initialSpawns = 1;
        intervals = ArrayPool.floatMaster.get(17);

        intervals[0] = 0;
        intervals[1] = 500;
        intervals[2] = 500;
        intervals[3] = 500;
        intervals[4] = 750;
        intervals[5] = 750;
        intervals[6] = 0;
        intervals[7] = 0;
        intervals[8] = 0;
        intervals[9] = 0;
        intervals[10] = 0;
        intervals[11] = 1000;
        intervals[12] = 0;
        intervals[13] = 1000;
        intervals[14] = 1500;
        intervals[15] = 1500;
        intervals[16] = 2000;

        backgrounds[0] = new Sky(activeTheme);
        position.x = 0;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[1] = new CherryBlossoms(position);
        position.x = 0;
        position.y = World.world.getWorldHeight();
        backgrounds[2] = new Clouds(position);
        position.x = Screen.screen.getPixelDensity(125);
        position.y = (World.world.getWorldHeight() / 2);
        backgrounds[3] = new RedSun(position);
        position.x = Screen.screen.getPixelDensity(70);
        position.y = Screen.screen.getPixelDensity(50);
        backgrounds[4] = new PaperLanturn(position);
        position.x = Screen.screen.getPixelDensity(240);
        position.y = Screen.screen.getPixelDensity(40);
        backgrounds[5] = new PaperLanturn(position);
        position.x = Screen.screen.getPixelDensity(120);
        position.y = Screen.screen.getPixelDensity(70);
        backgrounds[6] = new PaperLanturn(position);
        position.x = Screen.screen.getPixelDensity(20);
        position.y = Screen.screen.getPixelDensity(55);
        backgrounds[7] = new PaperLanturn(position);
        position.x = Screen.screen.getPixelDensity(295);
        position.y = Screen.screen.getPixelDensity(65);
        backgrounds[8] = new PaperLanturn(position);
        position.x = Screen.screen.getPixelDensity(190);
        position.y = Screen.screen.getPixelDensity(45);
        backgrounds[9] = new PaperLanturn(position);
        position.x = Screen.screen.getPixelDensity(100);
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[10] = new StoneLanturn(position);
        position.x = Screen.screen.getPixelDensity(300);
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[11] = new StoneLanturn(position);
        position.x = Screen.screen.getPixelDensity(50);
        position.y = Screen.screen.getPixelDensity(10);
        backgrounds[12] = new Pagoda(position);
        position.x = (Screen.screen.getWidth() / 2);
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[13] = new Arch(position);
        position.x = 0;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[14] = new MountFuji(position);
        position.x = Screen.screen.getPixelDensity(120);
        position.y = Screen.screen.getPixelDensity(80);
        backgrounds[15] = new GoldenCat(position);
        position.x = Screen.screen.getPixelDensity(-200);
        position.y = (World.world.getWorldHeight() / 4) * 3;
        backgrounds[16] = new KoiFish(position);

        Vec.pool.recycle(position);

        weatherSpawn = 12000;
        weather = new CherryLeafSpawner(width, height);

        musicId = R.raw.chill_tappin_riff;
    }

    public int getMusicId()
    {
        return musicId;
    }

    public void initSpookyTheme(BackgroundTheme.Theme activeTheme)
    {
        Vec position = Vec.pool.get();
        backgrounds = ArrayPool.backgroundSceneryPool.get(13);
        initialSpawns = 1;
        intervals = ArrayPool.floatMaster.get(13);

        intervals[0] = 0;
        intervals[1] = 500;
        intervals[2] = 500;
        intervals[3] = 500;
        intervals[4] = 1500;
        intervals[5] = 0;
        intervals[6] = 1000;
        intervals[7] = 1000;
        intervals[8] = 1000;
        intervals[9] = 1000;
        intervals[10] = 1500;
        intervals[11] = 1500;
        intervals[12] = 2000;

        backgrounds[0] = new Sky(activeTheme);
        position.x = 0;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[1] = new SpookyForest(position);
        position.x = 0;
        position.y = World.world.getWorldHeight();
        backgrounds[2] = new StormClouds(position);
        position.x = 0;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[3] = new DeadForest(position);
        position.x = Screen.screen.getPixelDensity(-100);
        position.y = (World.world.getWorldHeight() / 4) * 2.75f;
        backgrounds[4] = new Bat(position);
        position.x = Screen.screen.getPixelDensity(0);
        position.y = Screen.screen.getPixelDensity(100);
        backgrounds[5] = new Bat(position);
        position.x = Screen.screen.getPixelDensity(0);
        position.y = Screen.screen.getPixelDensity(10);
        backgrounds[6] = new HauntedCastle(position);
        position.x = Screen.screen.getPixelDensity(275);
        position.y = Screen.screen.getPixelDensity(400);
        backgrounds[7] = new FullMoon(position);
        position.x = (Screen.screen.getWidth() / 4) * 2.5f;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[8] = new TwistedTree(position);
        position.x = Screen.screen.getPixelDensity(100);
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[9] = new LaughingPumpkin(position);
        position.x = (Screen.screen.getWidth() / 4) * 3;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[10] = new Gravestone(position);
        position.x = Screen.screen.getPixelDensity(35);
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[11] = new Skull(position);
        position.x = (Screen.screen.getWidth() / 4) * 2.65f;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[12] = new GravestoneCross(position);

        Vec.pool.recycle(position);

        weatherSpawn = 2250;
        weather = new GhostSpawner(width, height);

        musicId = R.raw.sneaky_poo;
    }

    public void initChristmasTheme(BackgroundTheme.Theme activeTheme)
    {
        Vec position = Vec.pool.get();
        backgrounds = ArrayPool.backgroundSceneryPool.get(12);
        initialSpawns = 1;
        intervals = ArrayPool.floatMaster.get(12);

        intervals[0] = 0;
        intervals[1] = 500;
        intervals[2] = 500;
        intervals[3] = 1000;
        intervals[4] = 750;
        intervals[5] = 750;
        intervals[6] = 1000;
        intervals[7] = 1000;
        intervals[8] = 1000;
        intervals[9] = 1500;
        intervals[10] = 2000;
        intervals[11] = 2000;

        backgrounds[0] = new Sky(activeTheme);
        position.x = 0;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[1] = new FestiveForest(position);
        position.x = 0;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[2] = new SnowPiles(position);
        position.x = 0;
        position.y = World.world.getWorldHeight();
        backgrounds[3] = new Stars(position);
        position.x = Screen.screen.getPixelDensity(0);
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[4] = new LogCabin(position);
        position.x = Screen.screen.getPixelDensity(75);
        position.y = Screen.screen.getPixelDensity(300);
        backgrounds[5] = new Moon(position);
        position.x = Screen.screen.getWidth();
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[6] = new Iceburg(position);
        position.x = Screen.screen.getPixelDensity(160);
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[7] = new Penguin(position);
        position.x = Screen.screen.getPixelDensity(200);
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[8] = new CandyCane(position);
        position.x = (World.world.getWorldWidth() / 4) * 3;
        position.y = (World.world.getWorldHeight() / 4) * 3.5f;
        backgrounds[9] = new NorthStar(position);
        position.x = Screen.screen.getPixelDensity(25);
        position.y = Screen.screen.getPixelDensity(400);
        backgrounds[10] = new ShootingStar(position);
        position.x = Screen.screen.getPixelDensity(-200);
        position.y = (World.world.getWorldHeight() / 4) * 3;
        backgrounds[11] = new SantaSleigh(position);

        Vec.pool.recycle(position);

        weatherSpawn = 1500;
        weather = new SnowSpawner(width, height);

        musicId = R.raw.four_am_chilli;
    }

    public void initBeachTheme(BackgroundTheme.Theme activeTheme)
    {
        Vec position = Vec.pool.get();
        backgrounds = ArrayPool.backgroundSceneryPool.get(11);
        initialSpawns = 1;
        intervals = ArrayPool.floatMaster.get(11);

        intervals[0] = 0;
        intervals[1] = 500;
        intervals[2] = 500;
        intervals[3] = 750;
        intervals[4] = 750;
        intervals[5] = 1000;
        intervals[6] = 1000;
        intervals[7] = 1500;
        intervals[8] = 2000;
        intervals[9] = 2000;
        intervals[10] = 2000;

        backgrounds[0] = new Sky(activeTheme);
        position.x = 0;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[1] = new PalmTrees(position);
        position.x = Screen.screen.getPixelDensity(75);
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[2] = new SeaStack(position);
        position.x = Screen.screen.getPixelDensity(275);
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[3] = new SeaArch(position);
        position.x = 0;
        position.y = World.world.getWorldHeight();
        backgrounds[4] = new Clouds(position);
        position.x = Screen.screen.getPixelDensity(0);
        position.y = Screen.screen.getPixelDensity(10);
        backgrounds[5] = new Crab(position);
        position.x = Screen.screen.getPixelDensity(125);
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[6] = new SandCastle(position);
        position.x = Screen.screen.getPixelDensity(225);
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[7] = new Umbrella(position);
        position.x = Screen.screen.getPixelDensity(50);
        position.y = Screen.screen.getPixelDensity(450);
        backgrounds[8] = new SmilingSun(position);
        position.x = Screen.screen.getPixelDensity(-200);
        position.y = (World.world.getWorldHeight() / 4) * 2.5f;
        backgrounds[9] = new Biplane(position);
        position.x = Screen.screen.getPixelDensity(0);
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[10] = new BeachBall(position);

        Vec.pool.recycle(position);

        weathered = true;

        musicId = R.raw.beach_vibes;
    }

    public void initDesertTheme(BackgroundTheme.Theme activeTheme)
    {
        Vec position = Vec.pool.get();
        backgrounds = ArrayPool.backgroundSceneryPool.get(12);
        initialSpawns = 1;
        intervals = ArrayPool.floatMaster.get(12);

        intervals[0] = 0;
        intervals[1] = 500;
        intervals[2] = 500;
        intervals[3] = 750;
        intervals[4] = 750;
        intervals[5] = 0;
        intervals[6] = 1000;
        intervals[7] = 1500;
        intervals[8] = 1500;
        intervals[9] = 1500;
        intervals[10] = 2000;
        intervals[11] = 2000;

        backgrounds[0] = new Sky(activeTheme);
        position.x = 0;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[1] = new SandDunes(position);
        position.x = 0;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[2] = new SandPiles(position);
        position.x = Screen.screen.getPixelDensity(0);
        position.y = Screen.screen.getPixelDensity(10);
        backgrounds[3] = new Pyramid(position);
        position.x = Screen.screen.getPixelDensity(125);
        position.y = (World.world.getWorldHeight() / 4);
        backgrounds[4] = new RedSun(position);
        position.x = (World.world.getWorldWidth() / 4) * 0.25f;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[5] = new Camel(position);
        position.x = Screen.screen.getPixelDensity(50);
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[6] = new SandstonePillar(position);
        position.x = (World.world.getWorldWidth() / 4) * 2;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[7] = new SandstonePillar(position);
        position.x = (Screen.screen.getWidth() / 4) * 2.5f;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[8] = new Sphinx(position);
        position.x = (Screen.screen.getWidth() / 4) * 2.75f;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[9] = new Obelisk(position);
        position.x = (World.world.getWorldWidth() / 4) * 1.5f;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[10] = new CatGenie(position);
        position.x = Screen.screen.getPixelDensity(-200);
        position.y = (World.world.getWorldHeight() / 4) * 3.5f;
        backgrounds[11] = new Ufo(position);

        Vec.pool.recycle(position);

        weathered = true;

        musicId = R.raw.beach_vibes;
    }

    public void initCanyonTheme(BackgroundTheme.Theme activeTheme)
    {
        Vec position = Vec.pool.get();
        backgrounds = ArrayPool.backgroundSceneryPool.get(12);
        initialSpawns = 1;
        intervals = ArrayPool.floatMaster.get(12);

        intervals[0] = 0;
        intervals[1] = 500;
        intervals[2] = 500;
        intervals[3] = 750;
        intervals[4] = 750;
        intervals[5] = 1000;
        intervals[6] = 1000;
        intervals[7] = 1000;
        intervals[8] = 1500;
        intervals[9] = 1500;
        intervals[10] = 1500;
        intervals[11] = 2000;

        backgrounds[0] = new Sky(activeTheme);
        position.x = 0;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[1] = new Cactuses(position);
        position.x = 0;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[2] = new CanyonCliffs(position);
        position.x = 0;
        position.y = World.world.getWorldHeight();
        backgrounds[3] = new Clouds(position);
        position.x = Screen.screen.getPixelDensity(70);
        position.y = Screen.screen.getPixelDensity(400);
        backgrounds[4] = new SmilingSun(position);
        position.x = Screen.screen.getPixelDensity(50);
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[5] = new Saloon(position);;
        position.x = Screen.screen.getPixelDensity(0);
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[6] = new Tumbleweed(position);
        position.x = Screen.screen.getPixelDensity(200);
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[7] = new Wagon(position);
        position.x = Screen.screen.getPixelDensity(200);
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[8] = new Teepee(position);
        position.x = Screen.screen.getPixelDensity(275);
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[9] = new Campfire(position);
        position.x = Screen.screen.getPixelDensity(35);
        position.y = Screen.screen.getPixelDensity(30);
        backgrounds[10] = new CowSkull(position);
        position.x = Screen.screen.getPixelDensity(40);
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[11] = new Rattlesnake(position);

        Vec.pool.recycle(position);

        weathered = true;

        musicId = R.raw.beach_vibes;
    }

    public void initForestTheme(BackgroundTheme.Theme activeTheme)
    {
        Vec position = Vec.pool.get();
        backgrounds = ArrayPool.backgroundSceneryPool.get(12);
        initialSpawns = 1;
        intervals = ArrayPool.floatMaster.get(12);

        intervals[0] = 0;
        intervals[1] = 500;
        intervals[2] = 500;
        intervals[3] = 500;
        intervals[4] = 500;
        intervals[5] = 750;
        intervals[6] = 750;
        intervals[7] = 1000;
        intervals[8] = 1000;
        intervals[9] = 1000;
        intervals[10] = 1500;
        intervals[11] = 2000;

        backgrounds[0] = new Sky(activeTheme);
        position.x = 0;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[1] = new Forest(position);
        position.x = 0;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[2] = new ConiferForest(position);
        position.x = 0;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[3] = new Grass(position);
        position.x = 0;
        position.y = World.world.getWorldHeight();
        backgrounds[4] = new Stars(position);
        position.x = 0;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[5] = new Mountains(position);
        position.x = (Screen.screen.getWidth() / 4) * 3;
        position.y = Screen.screen.getPixelDensity(350);
        backgrounds[6] = new Moon(position);
        position.x = (Screen.screen.getWidth() / 4);
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[7] = new FlowerBush(position);
        position.x = (Screen.screen.getWidth() / 4) * 2;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[8] = new Flower(position);
        position.x = Screen.screen.getPixelDensity(40);
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[9] = new Deer(position);
        position.x = Screen.screen.getPixelDensity(260);
        position.y = Screen.screen.getPixelDensity(15);
        backgrounds[10] = new Wolf(position);
        position.x = Screen.screen.getPixelDensity(135);
        position.y = Screen.screen.getPixelDensity(450);
        backgrounds[11] = new ShootingStar(position);

        Vec.pool.recycle(position);

        weatherSpawn = 12000;
        weather = new RainSpawner(width, height);

        musicId = R.raw.four_am_chilli;
    }

    public void initCityTheme(BackgroundTheme.Theme activeTheme)
    {
        Vec position = Vec.pool.get();
        backgrounds = ArrayPool.backgroundSceneryPool.get(12);
        initialSpawns = 1;
        intervals = ArrayPool.floatMaster.get(12);

        intervals[0] = 0;
        intervals[1] = 500;
        intervals[2] = 500;
        intervals[3] = 750;
        intervals[4] = 0;
        intervals[5] = 750;
        intervals[6] = 1000;
        intervals[7] = 1000;
        intervals[8] = 1500;
        intervals[9] = 0;
        intervals[10] = 2000;
        intervals[11] = 2000;

        backgrounds[0] = new Sky(activeTheme);
        position.x = 0;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[1] = new Buildings(position);
        position.x = 0;
        position.y = World.world.getWorldHeight();
        backgrounds[2] = new StormClouds(position);
        position.x = Screen.screen.getPixelDensity(0);
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[3] = new StreetLamps(position);
        position.x = (Screen.screen.getWidth() / 4) * 3;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[4] = new StreetLamps(position);
        position.x = (Screen.screen.getWidth() / 2);
        position.y = Screen.screen.getPixelDensity(350);
        backgrounds[5] = new Moon(position);
        position.x = Screen.screen.getPixelDensity(100);
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[6] = new GiantClock(position);
        position.x = Screen.screen.getPixelDensity(-200);
        position.y = Screen.screen.getPixelDensity(450);
        backgrounds[7] = new Biplane(position);
        position.x = Screen.screen.getPixelDensity(175);
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[8] = new BasketballHoop(position);
        position.x = Screen.screen.getPixelDensity(-30);
        position.y = Screen.screen.getPixelDensity(30);
        backgrounds[9] = new Trash(position);
        position.x = Screen.screen.getPixelDensity(-30);
        position.y = Screen.screen.getPixelDensity(25);
        backgrounds[10] = new Trash(position, 15);
        position.x = 0;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[11] = new Basketball(position);

        Vec.pool.recycle(position);

        weatherSpawn = 12000;
        weather = new TechnoSpawner(width, height);

        musicId = R.raw.four_am_chilli;
    }

    public void initCityParkTheme(BackgroundTheme.Theme activeTheme)
    {
        Vec position = Vec.pool.get();
        backgrounds = ArrayPool.backgroundSceneryPool.get(14);
        initialSpawns = 1;
        intervals = ArrayPool.floatMaster.get(14);

        intervals[0] = 0;
        intervals[1] = 500;
        intervals[2] = 500;
        intervals[3] = 750;
        intervals[4] = 750;
        intervals[5] = 1000;
        intervals[6] = 1500;
        intervals[7] = 1500;
        intervals[8] = 1500;
        intervals[9] = 0;
        intervals[10] = 0;
        intervals[11] = 0;
        intervals[12] = 2000;
        intervals[13] = 2000;

        backgrounds[0] = new Sky(activeTheme);
        position.x = 0;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[1] = new Buildings(position);
        position.x = 0;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[2] = new Forest(position);
        position.x = 0;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[3] = new Grass(position);
        position.x = 0;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[4] = new Mountains(position);
        position.x = 0;
        position.y = World.world.getWorldHeight();
        backgrounds[5] = new Stars(position);
        position.x = (Screen.screen.getWidth() / 4) * 2.5f;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[6] = new Flower(position);
        position.x = Screen.screen.getPixelDensity(30);
        position.y = Screen.screen.getPixelDensity(450);
        backgrounds[7] = new Moon(position);
        position.x = (Screen.screen.getWidth() / 4) * 0.5f;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[8] = new StreetLamps(position);
        position.x = (Screen.screen.getWidth() / 4) * 1.5f;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[9] = new StreetLamps(position);
        position.x = (Screen.screen.getWidth() / 4) * 2.5f;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[10] = new StreetLamps(position);
        position.x = (Screen.screen.getWidth() / 4) * 3.5f;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[11] = new StreetLamps(position);
        position.x = Screen.screen.getPixelDensity(225);
        position.y = Screen.screen.getPixelDensity(500);
        backgrounds[12] = new ShootingStar(position);
        position.x = Screen.screen.getPixelDensity(-200);
        position.y = Screen.screen.getPixelDensity(300);
        backgrounds[13] = new Ufo(position);

        Vec.pool.recycle(position);

        weathered = true;

        musicId = R.raw.four_am_chilli;
    }

    public void initUnderwaterTheme(BackgroundTheme.Theme activeTheme)
    {
        Vec position = Vec.pool.get();
        backgrounds = ArrayPool.backgroundSceneryPool.get(12);
        initialSpawns = 1;
        intervals = ArrayPool.floatMaster.get(12);

        intervals[0] = 0;
        intervals[1] = 1000;
        intervals[2] = 750;
        intervals[3] = 750;
        intervals[4] = 0;
        intervals[5] = 1000;
        intervals[6] = 1000;
        intervals[7] = 1000;
        intervals[8] = 1500;
        intervals[9] = 1500;
        intervals[10] = 1500;
        intervals[11] = 2000;

        backgrounds[0] = new Sky(activeTheme);
        position.x = 0;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[1] = new CoralReef(position);
        position.x = Screen.screen.getPixelDensity(75);
        position.y = Screen.screen.getPixelDensity(15);
        backgrounds[2] = new SunkenShip(position);
        position.x = Screen.screen.getPixelDensity(-50);
        position.y = Screen.screen.getPixelDensity(400);
        backgrounds[3] = new Fish(position);
        position.x = Screen.screen.getPixelDensity(400);
        position.y = Screen.screen.getPixelDensity(175);
        backgrounds[4] = new Fish(position, false);
        position.x = (Screen.screen.getWidth() / 4) * 0.5f;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[5] = new Seaweed(position);
        position.x = (Screen.screen.getWidth() / 4) * 1.5f;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[6] = new Seaweed(position);
        position.x = (Screen.screen.getWidth() / 4) * 2.5f;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[7] = new Eel(position);
        position.x = Screen.screen.getPixelDensity(-50);
        position.y = Screen.screen.getPixelDensity(350);
        backgrounds[8] = new AngelFish(position);
        position.x = (Screen.screen.getWidth() / 4) * 2.5f;
        position.y = Screen.screen.getPixelDensity(20);
        backgrounds[9] = new Seaweed(position, true);
        position.x = (Screen.screen.getWidth() / 4) * 3.25f;
        position.y = Screen.screen.getPixelDensity(200);
        backgrounds[10] = new Seahorse(position);
        position.x = Screen.screen.getPixelDensity(-200);
        position.y = Screen.screen.getPixelDensity(300);
        backgrounds[11] = new KoiFish(position);


        Vec.pool.recycle(position);

        weatherSpawn = 500;
        weather = new BubbleSpawner(width, height);

        musicId = R.raw.beach_vibes;
    }

    public void destruct()
    {
        super.destruct();

        /*for(int i = 0; i < backgrounds.length; i++)
        {
            if(backgrounds[i] instanceof Sky)
            {
                Sky.pool.recycle(backgrounds[i]);
            }
            else
            {
                WorldObject.pool.recycle(backgrounds[i]);
            }
        }*/
        ArrayPool.backgroundSceneryPool.recycle(backgrounds, backgrounds.length);
    }
}
