package net.lodoma.lime.mod.targetserver;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import net.lodoma.lime.mod.NetworkSide;

@NetworkSide
@Retention(RetentionPolicy.RUNTIME)
public @interface ServerSide
{
    
}
