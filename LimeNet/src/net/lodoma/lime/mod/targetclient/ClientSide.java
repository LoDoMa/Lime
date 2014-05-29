package net.lodoma.lime.mod.targetclient;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import net.lodoma.lime.mod.NetworkSide;

@NetworkSide
@Retention(RetentionPolicy.RUNTIME)
public @interface ClientSide
{
    
}
