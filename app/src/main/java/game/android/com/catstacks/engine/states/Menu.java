package game.android.com.catstacks.engine.states;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.core.GameThread;
import game.android.com.catstacks.engine.core.RenderedState;
import game.android.com.catstacks.engine.core.SavedState;
import game.android.com.catstacks.engine.core.Screen;
import game.android.com.catstacks.engine.physics.Vec;
import game.android.com.catstacks.engine.sound.SoundManager;
import game.android.com.catstacks.game.objects.statics.backgrounds.MenuBackground;
import game.android.com.catstacks.game.objects.ui.CreditsButton;
import game.android.com.catstacks.game.objects.ui.MenuButton;
import game.android.com.catstacks.game.objects.ui.MuteButton;
import game.android.com.catstacks.game.objects.ui.SingingCat;
import game.android.com.catstacks.game.objects.ui.TextBox;

public class Menu extends RenderedState
{
    public static Menu menu;
    public final MenuSave save = new MenuSave();

    public MenuButton startButton;
    public ArrayList<TextBox> credits;

    private boolean changingState;
    private int selection, numOptions, trueSelection;

    public Menu(Context context)
    {
        super(context);

        changingState = false;

        selection = 1;
        numOptions = 5;
        trueSelection = 5;
    }

    public void initMenu()
    {
        synchronized(newSpawns)
        {
            newSpawns.add(new MenuBackground());

            Vec position = Vec.pool.get();

            position.x = Screen.screen.getWidth() / 2;
            position.y = Screen.screen.getHeight() / 2;
            startButton = new MenuButton(position);
            newSpawns.add(startButton);

            SingingCat cat1 = SingingCat.pool.get();
            position.x = 0;
            position.y = Screen.screen.getPixelDensity(100);
            cat1.init(position, R.raw.meow_kitten6);
            newSpawns.add(cat1);

            SingingCat cat2 = SingingCat.pool.get();
            position.x = Screen.screen.getPixelDensity(50);
            cat2.init(position, R.raw.high_meow);
            newSpawns.add(cat2);

            SingingCat cat3 = SingingCat.pool.get();
            position.x = Screen.screen.getPixelDensity(100);
            cat3.init(position, R.raw.squeak_meow_2);
            newSpawns.add(cat3);

            SingingCat cat4 = SingingCat.pool.get();
            position.x = Screen.screen.getPixelDensity(150);
            cat4.init(position, R.raw.squeak_meow);
            newSpawns.add(cat4);

            SingingCat cat5 = SingingCat.pool.get();
            position.x = Screen.screen.getPixelDensity(200);
            cat5.init(position, R.raw.quiet_meow);
            newSpawns.add(cat5);

            SingingCat cat6 = SingingCat.pool.get();
            position.x = Screen.screen.getPixelDensity(250);
            cat6.init(position, R.raw.full_meow);
            newSpawns.add(cat6);

            SingingCat cat7 = SingingCat.pool.get();
            position.x = Screen.screen.getPixelDensity(300);
            cat7.init(position, R.raw.steady_meow);
            newSpawns.add(cat7);

            position.x = Screen.screen.getPixelDensity(300);
            position.y = 10;
            newSpawns.add(new MuteButton(position));


            position.x = Screen.screen.getPixelDensity(0);
            position.y = 10;
            newSpawns.add(new CreditsButton(position));

            credits = new ArrayList<TextBox>();

            TextBox projectCredit = new TextBox("Project by Jordan Griffin", 0.35f, Screen.screen.getWidth() / 2, (Screen.screen.getHeight() / 1.5f - Screen.screen.getPixelDensity(16)) - Screen.screen.getPixelDensity(2000), 0, true);
            TextBox musicCredit = new TextBox("Music By Dylan Griffin", 0.35f, Screen.screen.getWidth() / 2, (Screen.screen.getHeight() / 1.5f - Screen.screen.getPixelDensity(32))  - Screen.screen.getPixelDensity(2000), 0, true);
            TextBox specialCredit = new TextBox("Testers:", 0.35f, Screen.screen.getWidth() / 2, (Screen.screen.getHeight() / 1.5f - Screen.screen.getPixelDensity(80))  - Screen.screen.getPixelDensity(2000), 0, true);
            TextBox tester1 = new TextBox("Brian Bartley", 0.35f, Screen.screen.getWidth() / 2, (Screen.screen.getHeight() / 1.5f - Screen.screen.getPixelDensity(96)) - Screen.screen.getPixelDensity(2000), 0, true);
            TextBox tester2 = new TextBox("Matus Drastich", 0.35f, Screen.screen.getWidth() / 2, (Screen.screen.getHeight() / 1.5f - Screen.screen.getPixelDensity(112))  - Screen.screen.getPixelDensity(2000), 0, true);
            //TextBox tester3 = new TextBox("Anthony Haze", 0.35f, Screen.screen.getWidth() / 2, (Screen.screen.getHeight() / 1.5f - Screen.screen.getPixelDensity(128))  - Screen.screen.getPixelDensity(2000), 0, true);
            //TextBox tester4 = new TextBox("Brendan O'Shea", 0.35f, Screen.screen.getWidth() / 2, (Screen.screen.getHeight() / 1.5f - Screen.screen.getPixelDensity(144))  - Screen.screen.getPixelDensity(2000), 0, true);
            credits.add(projectCredit);
            credits.add(musicCredit);
            credits.add(specialCredit);
            credits.add(tester1);
            credits.add(tester2);
            //credits.add(tester3);
            //credits.add(tester4);

            for(TextBox credit : credits)
            {
                newSpawns.add(credit);
            }

            Vec.pool.recycle(position);

            SoundManager.soundManager.stopMusic();
            SoundManager.soundManager.initMediaPlayer(context, R.raw.chill_tappin_riff);
            SoundManager.soundManager.playMusicLooped();
        }
    }

    @Override
    public void update()
    {
        super.update();

        if(changingState)
        {
            Menu.menu.destruct();
            System.gc();

            World.world = new World();
            World.world.initLevel();
            GameThread.nextState();
        }
    }

    public void despawnit()
    {
        startButton.move(0, Screen.screen.getPixelDensity(-2000));

        for(TextBox credit : credits)
        {
            credit.move(0, Screen.screen.getPixelDensity(2000));
        }
    }

    public void respawnit()
    {
        for(TextBox credit : credits)
        {
            credit.move(0, Screen.screen.getPixelDensity(-2000));
        }

        startButton.move(0, Screen.screen.getPixelDensity(2000));
    }

    public void queueStateChange()
    {
        changingState = true;
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

            save.changingState = changingState;
            save.selection = selection;
            save.numOptions = numOptions;
            save.trueSelection = trueSelection;

            save.isValid = true;
        }
    }

    public void restore()
    {
        synchronized(save)
        {
            MenuSave save = this.save;
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

            changingState = save.changingState;
            selection = save.selection;
            numOptions = save.numOptions;
            trueSelection = save.trueSelection;
        }
    }

    public void reset()
    {
        changingState = false;

        selection = 1;
        numOptions = 5;
        trueSelection = 5;
    }

    public static class MenuSave extends SavedState
    {
        public boolean changingState;
        public int selection, numOptions, trueSelection;
    }

    public void destruct()
    {
        SingingCat.pool.reset();
        MenuButton.pool.reset();
        MenuBackground.pool.reset();
    }
}
