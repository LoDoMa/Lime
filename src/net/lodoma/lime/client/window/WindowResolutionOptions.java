package net.lodoma.lime.client.window;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class WindowResolutionOptions
{
    public static DisplayMode getDisplayMode(boolean fullscreen)
    {
        if(fullscreen)
            try
            {
                DisplayMode target = null;
                int width = Display.getDesktopDisplayMode().getWidth();
                int height = Display.getDesktopDisplayMode().getHeight();
                
                DisplayMode[] modes = Display.getAvailableDisplayModes();
                int freq = 0;
                for (DisplayMode current : modes)
                    if ((current.getWidth() == width) && (current.getHeight() == height))
                    {
                        if((target == null) || (current.getFrequency() >= freq))
                            if((target == null) || (current.getBitsPerPixel() > target.getBitsPerPixel()))
                            {
                                target = current;
                                freq = target.getFrequency();
                            }
                        if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel())
                                && (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency()))
                        {
                            target = current;
                            break;
                        }
                    }
                return target;
            }
            catch(LWJGLException e)
            {
                e.printStackTrace();
            }
        else
            return new DisplayMode(Display.getDesktopDisplayMode().getWidth() / 2, Display.getDesktopDisplayMode().getHeight() / 2);
        return null;
    }
}
