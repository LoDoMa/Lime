package net.lodoma.lime.util;

public class SystemHelper
{
    public static long getTimeMillis()
    {
        return System.currentTimeMillis();
    }
    
    public static long getTimeNanos()
    {
        return System.nanoTime();
    }
}
