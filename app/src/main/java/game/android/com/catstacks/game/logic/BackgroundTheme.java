package game.android.com.catstacks.game.logic;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BackgroundTheme
{
    public enum Theme
    {
        ASIAN, SPOOKY, CHRISTMAS, BEACH, DESERT, CANYON, FOREST, CITY, CITYPARK, UNDERWATER
    }

    public static final List<Theme> VALUES = Collections.unmodifiableList(Arrays.asList(Theme.values()));
    public static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static Theme randomTheme()
    {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
