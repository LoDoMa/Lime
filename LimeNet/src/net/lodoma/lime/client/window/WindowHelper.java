package net.lodoma.lime.client.window;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class WindowHelper
{
    public static void setDisplayMode(int width, int height, boolean fullscreen) throws DisplayModeSearchException
    {
        if ((Display.getDisplayMode().getWidth() == width) && (Display.getDisplayMode().getHeight() == height)
                && (Display.isFullscreen() == fullscreen))
            return;
        
        try
        {
            DisplayMode targetDisplayMode = null;
            
            if (fullscreen)
            {
                DisplayMode[] modes = Display.getAvailableDisplayModes();
                int freq = 0;
                
                for (DisplayMode current : modes)
                    if ((current.getWidth() == width) && (current.getHeight() == height))
                    {
                        if ((targetDisplayMode == null) || (current.getFrequency() >= freq))
                            if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel()))
                            {
                                targetDisplayMode = current;
                                freq = targetDisplayMode.getFrequency();
                            }
                        if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel())
                                && (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency()))
                        {
                            targetDisplayMode = current;
                            break;
                        }
                    }
            }
            else
                targetDisplayMode = new DisplayMode(width, height);
            
            if (targetDisplayMode == null)
                throw new DisplayModeSearchException();
            
            Display.setDisplayMode(targetDisplayMode);
            Display.setFullscreen(fullscreen);
            
        }
        catch (LWJGLException e)
        {
            throw new DisplayModeSearchException();
        }
    }
}
