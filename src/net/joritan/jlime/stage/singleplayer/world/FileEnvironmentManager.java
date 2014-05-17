package net.joritan.jlime.stage.singleplayer.world;

import java.io.File;

import net.joritan.jlime.stage.root.BlueScreen;

public class FileEnvironmentManager implements EnvironmentManager
{
    @Override
    public void load(Environment environment, Object... args)
    {
        if(args.length != 1) new BlueScreen(null, new IllegalArgumentException(), "illegal argument");
        if(!(args[0] instanceof File)) new BlueScreen(null, new IllegalArgumentException(), "illegal argument");
        //File worldFile = (File) args[0];
    }

    @Override
    public void save(Environment environment, Object... args)
    {
        if(args.length != 1) new BlueScreen(null, new IllegalArgumentException(), "illegal argument");
        if(!(args[0] instanceof File)) new BlueScreen(null, new IllegalArgumentException(), "illegal argument");
        //File worldFile = (File) args[0];
    }
}
