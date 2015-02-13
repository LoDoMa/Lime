package net.lodoma.lime;

import net.lodoma.lime.logger.Logger;

public class Lime
{
    public static final Logger LOGGER = new Logger();
    
    public static void forceExit()
    {
        System.err.printf("Lime is about to forcefully exit.\n");
        
        // TODO: write error log to disk
        System.exit(1);
    }
}
