package net.joritan.jlime.stage.singleplayer.world;

import java.io.File;

public class FileEnvironmentManager implements EnvironmentManager
{
    @Override
    public void load(Environment environment, Object... args)
    {
        if(args.length != 1) throw new IllegalArgumentException();
        if(!(args[0] instanceof File)) throw new IllegalArgumentException();
        //File worldFile = (File) args[0];
    }

    @Override
    public void save(Environment environment, Object... args)
    {
        if(args.length != 1) throw new IllegalArgumentException();
        if(!(args[0] instanceof File)) throw new IllegalArgumentException();
        //File worldFile = (File) args[0];
    }
}
