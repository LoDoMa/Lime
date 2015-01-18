package net.lodoma.lime.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class OsHelper
{
    public static final String JARPATH;
    
    static
    {
        String path = null;
        try
        {
            path = ClassLoader.getSystemClassLoader().getResource(".").toURI().getPath();
            path = path.substring(0, path.lastIndexOf("bin"));
            if (isWindows()) // Fix weird Windows bug
                while (!Character.isLetter(path.charAt(0)))
                    path = path.substring(1);
            path = new File(path).getCanonicalPath() + "/";
        }
        catch(IOException | URISyntaxException e)
        {
            e.printStackTrace();
        }
        
        JARPATH = path;
    }
    
    public static String getOsName()
    {
        if (osName == null)
            osName = System.getProperty("os.name", "unknown");
        return osName;
    }
    
    public static boolean isWindows()
    {
        return getOsName().toLowerCase().indexOf("windows") >= 0;
    }

    public static boolean isMac()
    {
        String os = getOsName().toLowerCase();
        return os.startsWith("mac") || os.startsWith("darwin");
    }
    
    public static boolean isLinux()
    {
        return getOsName().toLowerCase().indexOf("linux") >= 0;
    }
    
    public static boolean is64bit()
    {
        return System.getProperty("os.arch").indexOf("64") >= 0;
    }
    
    private static String osName = null;
    
    private OsHelper() {}
}
