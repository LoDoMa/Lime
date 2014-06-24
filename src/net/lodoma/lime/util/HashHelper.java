package net.lodoma.lime.util;

public class HashHelper
{
    public static int hash32(String string)
    {
        return string.hashCode();
    }
    
    public static long hash64(String string)
    {
        long h = 1125899906842597L;
        int len = string.length();
        for (int i = 0; i < len; i++)
            h = 31 * h + string.charAt(i);
        return h;
    }
}
