package game.android.com.catstacks.engine.sound;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import game.android.com.catstacks.engine.core.ScoreSession;

public class SoundManager
{
    public static SoundManager soundManager;

    private Context context;
    private SoundPool soundPool;
    private MediaPlayer musicPlayer;
    private boolean soundPoolLoaded;
    private int leftVolume, rightVolume, musicTime;

    private List<Integer> streams;

    public SoundManager(Context context, SoundPool soundPool)
    {
        this.context = context;
        this.soundPool = soundPool;
        if(!ScoreSession.session.readMuteValue())
        {
            leftVolume = 1;
            rightVolume = 1;
        }
        else
        {
            leftVolume = 0;
            rightVolume = 0;
        }

        streams = new ArrayList<Integer>();
        musicTime = 0;

        initSoundPool();
    }

    public void initSoundPool()
    {
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener()
        {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status)
            {
                soundPoolLoaded = true;
            }
        });
    }

    public int loadSound(int id)
    {
        return loadSound(id, 1);
    }

    public int loadSound(int id, int priority)
    {
        return soundPool.load(context, id, priority);
    }

    public int playSound(int id, float soundVolume, float pitch)
    {
        if(soundPoolLoaded)
        {
            int streamId = soundPool.play(id, leftVolume * soundVolume, rightVolume * soundVolume, 1, 0, pitch);
            streams.add(streamId);
            return streamId;
        }
        return 0;
    }

    public int playSoundLooped(int id, float soundVolume)
    {
        if(soundPoolLoaded)
        {
            int streamId = soundPool.play(id, leftVolume * soundVolume, rightVolume * soundVolume, 1, -1, 1);
            streams.add(streamId);
            return streamId;
        }
        return 0;
    }

    public void setRate(int id, float rate)
    {
        if(soundPoolLoaded)
        {
            soundPool.setRate(id, rate);
        }
    }

    public void stopSound(int id)
    {
        if(soundPoolLoaded)
        {
            soundPool.stop(id);
        }
    }

    public void setStreamVolume()
    {
        for(int stream : streams)
        {
            soundPool.setVolume(stream, leftVolume, rightVolume);
        }
    }

    public void clearAllStreams()
    {
        for(int stream : streams)
        {
            soundPool.stop(stream);
            //soundPool.unload(stream);
        }
        streams.clear();
    }

    public void initMediaPlayer(Context context, int songId)
    {
        musicPlayer = MediaPlayer.create(context, songId);
    }

    public void playMusicLooped()
    {
        if(musicPlayer != null)
        {
            musicPlayer.setVolume(leftVolume, rightVolume);
            musicPlayer.setLooping(true);
            musicPlayer.start();
        }
    }

    public void playMusic()
    {
        if(musicPlayer != null)
        {
            musicPlayer.setVolume(leftVolume, rightVolume);
            musicPlayer.setLooping(false);
            musicPlayer.start();
        }
    }

    public void stopMusic()
    {
        if(musicPlayer != null)
        {
            musicPlayer.stop();
            musicPlayer.release();
            musicPlayer = null;
        }
    }

    public void setMusicVolume()
    {
        if(musicPlayer != null)
        {
            musicPlayer.setVolume(leftVolume, rightVolume);
        }
    }

    public void muteSound()
    {
        leftVolume = 0;
        rightVolume = 0;

        setStreamVolume();
        setMusicVolume();
    }

    public void unmuteSound()
    {
        leftVolume = 1;
        rightVolume = 1;

        setStreamVolume();
        setMusicVolume();
    }

    public void pause()
    {
        soundPool.autoPause();

        if(musicPlayer != null)
        {
            musicPlayer.pause();
            musicTime = musicPlayer.getCurrentPosition();
        }
    }

    public void resume()
    {
        soundPool.autoResume();

        if(musicPlayer != null)
        {
            musicPlayer.seekTo(musicTime);
            musicPlayer.start();
        }
    }
}
