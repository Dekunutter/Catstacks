package game.android.com.catstacks.engine.sound;

import game.android.com.catstacks.engine.pool.GenericObjectPool;

public class Sound
{
    private int soundId, streamId;
    private boolean isActive;
    private float volume;

    public static final GenericObjectPool<Sound> pool = new GenericObjectPool<Sound>(Sound.class);

    public Sound()
    {

    }

    public Sound(int resourceId)
    {
        soundId = SoundManager.soundManager.loadSound(resourceId);
        streamId = 0;
        isActive = false;
        volume = 1;
    }

    public Sound(int resourceId, float volume)
    {
        soundId = SoundManager.soundManager.loadSound(resourceId);
        streamId = 0;
        isActive = false;
        this.volume = volume;
    }

    public void init(int resourceId)
    {
        soundId = SoundManager.soundManager.loadSound(resourceId);
        streamId = 0;
        isActive = false;
        volume = 1;
    }

    public void init(int resourceId, float volume)
    {
        soundId = SoundManager.soundManager.loadSound(resourceId);
        streamId = 0;
        isActive = false;
        this.volume = volume;
    }

    public void playSound()
    {
        playSound(1);
    }

    public void playSound(float pitch)
    {
        streamId = SoundManager.soundManager.playSound(soundId, volume, pitch);
    }

    public void setRate(float rate)
    {
        SoundManager.soundManager.setRate(streamId, rate);
    }

    public void playSoundLooped()
    {
        if(!isActive)
        {
            streamId = SoundManager.soundManager.playSoundLooped(soundId, volume);
            isActive = true;
        }
    }

    public void stopSound()
    {
        SoundManager.soundManager.stopSound(streamId);
        isActive = false;
    }

    public boolean isPlaying()
    {
        return isActive;
    }
}
