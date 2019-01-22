package game.android.com.catstacks.engine.pool;

import android.util.Log;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public final class ReusableStack<Type>
{
    private Deque<Type> stack;
    private List<Type> list;

    private Provider<Type> objectProvider;

    public ReusableStack(Provider<Type> objectProvider)
    {
        stack = new LinkedList<>();
        list = new ArrayList<>();
        this.objectProvider = objectProvider;
    }

    public Type pop()
    {
        if(stack.size() == 0)
        {
            try
            {
                Type object = objectProvider.provide();

                list.add(object);
                stack.push(object);
            }
            catch(Exception e)
            {
                Log.println(Log.ERROR, "exception", "oops");
            }
        }
        return stack.pop();
    }

    public void push(Type value)
    {
        stack.push(value);
    }

    public List<Type> getAsList()
    {
        return list;
    }
}
