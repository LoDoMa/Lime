package net.lodoma.lime.client.window;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.PixelFormat;

public class Window
{
    private static int width = 0;
    private static int height = 0;
    private static boolean fullscreen = false;
    private static String title = null;
    private static int fps = 0;
    private static float orthoWidth = 0;
    private static float orthoHeight = 0;
    
    private static boolean closeRequested = false;
    
    private static boolean supportFBO;

    private static int initWidth;
    private static int initHeight;
    private static int lastWidth = -1;
    private static int lastHeight = -1;
    
    public static int getWidth()
    {
        return width;
    }

    public static int getHeight()
    {
        return height;
    }

    public static float getOrthoWidth()
    {
        return orthoWidth;
    }

    public static float getOrthoHeight()
    {
        return orthoHeight;
    }

    public static void setDimensions(int width, int height)
    {
        Window.width = width;
        Window.height = height;
        initWidth = width;
        initHeight = height;
    }
    
    public static void setFullscreen(boolean fullscreen)
    {
        Window.fullscreen = fullscreen;
    }
    
    public static void setTitle(String title)
    {
        Window.title = title;
    }
    
    public static void setFPS(int fps)
    {
        Window.fps = fps;
    }
    
    public static void setOrtho(float width, float height)
    {
        Window.orthoWidth = width;
        Window.orthoHeight = height;
    }
    
    public static void requestClose()
    {
        Window.closeRequested = true;
    }
    
    public static boolean isCloseRequested()
    {
        return Display.isCloseRequested() || closeRequested;
    }
    
    public static void open()
    {
        apply();
        try
        {
            PixelFormat pixelFormat = new PixelFormat();
            ContextAttribs contextAtrributes = new ContextAttribs(2, 0);
            // .withForwardCompatible(true);
            // .withProfileCore(true);        for 3.2+
        
            Display.setResizable(true);
            Display.create(pixelFormat, contextAtrributes);
            Keyboard.create();
            Mouse.create();
        }
        catch (LWJGLException e)
        {
            e.printStackTrace();
        }
    }
    
    private static void loadLimitations()
    {
        supportFBO = GLContext.getCapabilities().GL_EXT_framebuffer_object;
    }
    
    private static void setupGL()
    {
        loadLimitations();
        
        int dwidth = Display.getWidth();
        int dheight = Display.getHeight();
        
        int vpwidth = (int) (dheight * (initWidth / (float) initHeight));
        int vpheight;
        if(vpwidth < dwidth)
        {
            vpheight = dheight;
        }
        else
        {
            vpwidth = dwidth;
            vpheight = (int) (dwidth * (initHeight / (float) initWidth));
        }
        
        int vpx = (dwidth - vpwidth) / 2;
        int vpy = (dheight - vpheight) / 2;
        
        GL11.glViewport(vpx, vpy, vpwidth, vpheight);
        
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0f, orthoWidth, 0.0f, orthoHeight, -1.0f, 1.0f);
        
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }
    
    public static boolean supportsFBO()
    {
        return supportFBO;
    }
    
    public static void apply()
    {
        if(width <= 0) throw new IllegalStateException();
        if(height <= 0) throw new IllegalStateException();
        if(title == null) throw new NullPointerException();
        WindowHelper.setDisplayMode(width, height, fullscreen);
        Display.setTitle(title);
    }
    
    public static void clear()
    {
        width = Display.getWidth();
        height = Display.getHeight();
        if(width != lastWidth || height != lastHeight)
            setupGL();
        lastWidth = width;
        lastHeight = height;
        
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glLoadIdentity();
    }
    
    public static void update()
    {
        if(fps <= 0) throw new IllegalStateException();
        Display.update();
        Display.sync(fps);
    }
    
    public static void close()
    {
        Mouse.destroy();
        Keyboard.destroy();
        Display.destroy();
    }
}
