package game.android.com.catstacks.engine.core;

import android.content.Context;
import android.content.SharedPreferences;

import game.android.com.catstacks.R;

public class ScoreSession
{
    public static ScoreSession session;
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private final static int PRIVATE_MODE = 0;

    public ScoreSession(Context context)
    {
        this.context = context;
        this.preferences = context.getSharedPreferences(context.getString(R.string.preferences_key), PRIVATE_MODE);
        editor = preferences.edit();
    }

    public void saveScore(long newScore)
    {
        long foundScore = readScore();
        if(foundScore < newScore)
        {
            editor.putLong(context.getString(R.string.saved_high_score), newScore);
            editor.commit();
        }
    }

    public long readScore()
    {
        return preferences.getLong(context.getString(R.string.saved_high_score), 0);
    }

    public void saveMuteValue(boolean isMuted)
    {
        editor.putBoolean(context.getString(R.string.saved_mute_value), isMuted);
        editor.commit();
    }

    public boolean readMuteValue()
    {
        return preferences.getBoolean(context.getString(R.string.saved_mute_value), false);
    }
}
