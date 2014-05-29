package net.lodoma.lime.client.window;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class Window
{
    private static int width = 0;
    private static int height = 0;
    private static boolean fullscreen = false;
    private static String title = null;
    
    private static boolean closeRequested = false;
    
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
    
    public static void requestClose()
    {
        Window.closeRequested = true;
    }
    
    public static boolean isCloseRequested()
    {
        return Display.isCloseRequested() || closeRequested;
    }
    
    public static void open() throws InvalidWindowPropertyException, DisplayModeSearchException
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
    
    public static void apply() throws InvalidWindowPropertyException, DisplayModeSearchException
    {
        if(width <= 0) throw new InvalidWindowPropertyException();
        if(height <= 0) throw new InvalidWindowPropertyException();
        if(title == null) throw new NullPointerException();
        WindowHelper.setDisplayMode(width, height, fullscreen);
        Display.setTitle(title);
    }
    
    public static void close()
    {
        Mouse.destroy();
        Keyboard.destroy();
        Display.destroy();
    }
}
