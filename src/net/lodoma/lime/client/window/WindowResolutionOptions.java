package net.lodoma.lime.client.window;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class WindowResolutionOptions
{
    public static Map<Integer, DisplayMode> displayModes;
    
    public static void load()
    {
        try
        {
            displayModes = new HashMap<Integer, DisplayMode>();
            
            DisplayMode[] modes = Display.getAvailableDisplayModes();
            
            for (DisplayMode current : modes)
            {
                int width = current.getWidth();
                int height = current.getHeight();
    
                int shw = width;
                int shh = height;
                
                for(int i = (int) Math.min(width, height); i > 1; i--)
                    if(width % i == 0 && height % i == 0)
                    {
                        shw /= i;
                        shh /= i;
                        break;
                    }
    
                short sw = (short) shw;
                short sh = (short) shh;
                int dmc = ((int) sw) << 16 | ((int) sh);
                
                DisplayMode best = displayModes.get(dmc);
                if(best == null)
                    displayModes.put(dmc, current);
                else if(current.getWidth() > best.getWidth() && current.getHeight() > best.getHeight())
                    displayModes.put(dmc, current);
                else if(current.getFrequency() >= best.getFrequency() && current.getBitsPerPixel() > best.getBitsPerPixel())
                    displayModes.put(dmc, current);
            }
        }
        catch(LWJGLException e)
        {
            e.printStackTrace();
        }
    }
}
