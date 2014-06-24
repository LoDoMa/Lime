package net.lodoma.lime.util;

public class BinaryHelper
{
    public static byte setOff(byte value, int mask)
    {
        return (byte) (value & (~mask));
    }
    
    public static byte setOn(byte value, int mask)
    {
        return (byte) (value | mask);
    }
}
