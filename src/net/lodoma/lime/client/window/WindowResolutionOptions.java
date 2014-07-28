package net.lodoma.lime.client.window;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class WindowResolutionOptions
{
    private static boolean initialized = false;
    
    private static GraphicsDevice defaultScreen;
    private static int width;
    private static int height;
    
    public static void initialize()
    {
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        defaultScreen = g.getDefaultScreenDevice();
        
        width = defaultScreen.getDisplayMode().getWidth();
        height = defaultScreen.getDisplayMode().getHeight();
        
        initialized = true;
    }
    
    public static void centerWindowPosition()
    {
        if(!initialized)
            initialize();
        
        Display.setLocation(width / 4, height / 4);
    }
    
    public static DisplayMode getDisplayMode(boolean fullscreen) throws WindowException
    {
        if(!initialized)
            initialize();
        
        if(fullscreen)
            try
            {
                int bitsPerPixel = defaultScreen.getDisplayMode().getBitDepth();
                int frequency = defaultScreen.getDisplayMode().getRefreshRate();
                
                DisplayMode target = null;
                
                DisplayMode[] modes = Display.getAvailableDisplayModes();
                int bestFrequency = 0;
                
                for (DisplayMode current : modes)
                    if ((current.getWidth() == width) && (current.getHeight() == height))
                    {
                        if((target == null) || (current.getFrequency() >= bestFrequency))
                            if((target == null) || (current.getBitsPerPixel() > target.getBitsPerPixel()))
                            {
                                target = current;
                                bestFrequency = target.getFrequency();
                            }
                        if ((current.getBitsPerPixel() == bitsPerPixel) && (current.getFrequency() == frequency))
                        {
                            target = current;
                            break;
                        }
                    }
                
                if(target == null) throw new WindowException("Failed to find correct window display mode");
                return target;
            }
            catch(LWJGLException e)
            {
                throw new WindowException("Failed to get available window display modes");
            }
        else
            return new DisplayMode(width / 2, height / 2);
    }
}
