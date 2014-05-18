package net.joritan.jlime;

import net.joritan.jlime.stage.StageManager;
import net.joritan.jlime.stage.root.BlueScreen;
import net.joritan.jlime.stage.root.RootStage;
import net.joritan.jlime.util.Input;
import net.joritan.jlime.util.Timer;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

public class JLimeStart
{
    private static StageManager manager;
    public static final int FPS = 60;
    
    public static void setDisplayMode(int width, int height, boolean fullscreen)
    {
        if ((Display.getDisplayMode().getWidth() == width) && (Display.getDisplayMode().getHeight() == height) && (Display.isFullscreen() == fullscreen))
        {
            return;
        }
        
        try
        {
            DisplayMode targetDisplayMode = null;
            
            if (fullscreen)
            {
                DisplayMode[] modes = Display.getAvailableDisplayModes();
                int freq = 0;
                
                for (int i = 0; i < modes.length; i++)
                {
                    DisplayMode current = modes[i];
                    
                    if ((current.getWidth() == width) && (current.getHeight() == height))
                    {
                        if ((targetDisplayMode == null) || (current.getFrequency() >= freq))
                        {
                            if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel()))
                            {
                                targetDisplayMode = current;
                                freq = targetDisplayMode.getFrequency();
                            }
                        }
                        if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) && (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency()))
                        {
                            targetDisplayMode = current;
                            break;
                        }
                    }
                }
            }
            else
            {
                targetDisplayMode = new DisplayMode(width, height);
            }
            
            if (targetDisplayMode == null)
            {
                new BlueScreen(null, new Exception("error creating display"));
                return;
            }
            
            Display.setDisplayMode(targetDisplayMode);
            Display.setFullscreen(fullscreen);
        }
        catch (LWJGLException e)
        {
            new BlueScreen(null, new Exception("unable to setup display"));
            return;
        }
    }
    
    public static void create()
    {
        try
        {
            setDisplayMode(600, 600, false);
            Display.setTitle("Lime");
            Display.create();
            Keyboard.create();
            Mouse.create();
        }
        catch (LWJGLException e)
        {
            new BlueScreen(null, e, new String[]
            { "Failute to create Display contexts" });
        }
    }
    
    public static void run()
    {
        manager = new StageManager();
        BlueScreen.setDefaultManager(manager);
        manager.push(new RootStage(manager));
        
        Timer timer = new Timer();
        timer.reset();
        
        int frames = 0;
        long t1 = System.currentTimeMillis();
        while ((!Display.isCloseRequested()) && (!manager.hasStages()))
        {
            long t2 = System.currentTimeMillis();
            if ((t2 - t1) >= 1000)
            {
                System.out.println("FPS: " + frames);
                frames = 0;
                t1 = t2;
            }
            glClear(GL_COLOR_BUFFER_BIT);
            glLoadIdentity();
            
            float delta = timer.update();
            manager.update(delta);
            Input.update();
            manager.render();
            
            Display.update();
            Display.sync(FPS);
            frames++;
        }
    }
    
    public static void destroy()
    {
        Mouse.destroy();
        Keyboard.destroy();
        Display.destroy();
    }
    
    public static void main(String[] args)
    {
        create();
        run();
        destroy();
    }
}
