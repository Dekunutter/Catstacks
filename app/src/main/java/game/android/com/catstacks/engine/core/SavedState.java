package game.android.com.catstacks.engine.core;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import game.android.com.catstacks.engine.objects.WorldObject;
import game.android.com.catstacks.engine.rendering.BufferObject;

public class SavedState
{
    public Context context;

    public ArrayList<WorldObject> objects;
    public ArrayList<WorldObject> newSpawns;
    public ArrayList<WorldObject> toRemove;
    public ArrayList<WorldObject> removeSpawns;
    public List<BufferObject> buffers;
    public ArrayList<BufferObject> removeBuffer;

    public Object bufferMutex = new Object();

    public boolean isAdReady;
    public int institialAdCounter;

    public boolean isValid = false;
}
