package game.android.com.catstacks.engine.pool;


import java.lang.reflect.Type;

public interface Poolable
{
    public void setPoolID(final int id);
    public int getPoolID();
    public Type get();
    public void clean();
}
