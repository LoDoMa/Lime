package net.lodoma.lime.util;

public interface HashFunction<R, V>
{
    public R hash(V data);
}
