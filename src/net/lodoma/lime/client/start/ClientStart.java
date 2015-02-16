package net.lodoma.lime.client.start;

import java.io.File;

import net.lodoma.lime.Lime;
import net.lodoma.lime.logger.LogLevel;
import net.lodoma.lime.util.OsHelper;

public class ClientStart
{
    public static void main(String[] args)
    {
        Lime.init();
        Lime.LOGGER.setMinimumLevel(LogLevel.INFO);
        for (String arg : args)
            if (arg.equals("/F"))
                Lime.LOGGER.setMinimumLevel(LogLevel.FINEST);
        
        String nativePath = OsHelper.JARPATH + "native/";
             if (OsHelper.isWindows()) nativePath += "windows/";
        else if (OsHelper.isMac())     nativePath += "macosx/";
        else if (OsHelper.isLinux())   nativePath += "linux/";

        if (OsHelper.is64bit()) nativePath += "x64";
        else                    nativePath += "x86";

        System.setProperty("org.lwjgl.librarypath", new File(nativePath).getAbsolutePath());
        
        VisualInstance visual = new VisualInstance();
        visual.run();
    }
}
