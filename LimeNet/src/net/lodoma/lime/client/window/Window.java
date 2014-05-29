package net.lodoma.lime.client.window;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class Window
{
    private static int width;
    private static int height;
    private static boolean fullscreen;
    private static String title;
    
    public static void setDimensions(int width, int height)
    {
        Window.width = width;
        Window.height = height;
    }
    
    public static void setFullscreen(boolean fullscreen)
    {
        Window.fullscreen = fullscreen;
    }
    
    public static void setTitle(String title)
    {
        Window.title = title;
    }
    
    public static void open()
    {
        apply();
        try
        {
            Display.create();
            Keyboard.create();
            Mouse.create();
        }
        catch (LWJGLException e)
        {
            e.printStackTrace();
        }
    }
    
    public static void apply()
    {
        try
        {
            if(width <= 0) throw new InvalidWindowPropertyException();
            if(height <= 0) throw new InvalidWindowPropertyException();
        }
        catch (InvalidWindowPropertyException e)
        {
            e.printStackTrace();
        }
        
        if(title == null) throw new NullPointerException();
        try
        {
            WindowHelper.setDisplayMode(width, height, fullscreen);
        }
        catch (DisplayModeSearchException e)
        {
            e.printStackTrace();
        }
        Display.setTitle(title);
    }
    
    public static void close()
    {
        Mouse.destroy();
        Keyboard.destroy();
        Display.destroy();
    }
}
