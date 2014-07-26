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
    
    private static int ww;
    private static int wh;
    private static int lww;
    private static int lwh;
    private static int fps;

    private static boolean fullscreen = false;
    
    private static String title = "";
    
    private static int resw;
    private static int resh;
    
    private static int vpx;
    private static int vpy;
    private static int vpwidth;
    private static int vpheight;
    
    private static boolean closeRequested = false;
    
    private static boolean supportFBO;
    
    public static int getWindowWidth()
    {
        return ww;
    }

    public static int getWindowHeight()
    {
        return wh;
    }
    
    public static void setWindowSize(int width, int height)
    {
        ww = width;
        wh = height;
        lww = ww;
        lwh = wh;
    }
    
    public static int getFPS()
    {
        return fps;
    }
    
    public static void setFPS(int fps)
    {
        Window.fps = fps;
    }
    
    public static boolean isFullscreen()
    {
        return fullscreen;
    }
    
    public static void setFullscreen(boolean fs)
    {
        fullscreen = fs;
    }
    
    public static String getTitle()
    {
        return title;
    }
    
    public static void setTitle(String title)
    {
        Window.title = title;
    }
    
    public static int getResolutionWidth()
    {
        return resw;
    }
    
    public static int getResolutionHeight()
    {
        return resh;
    }
    
    public static void setResolution(int w, int h)
    {
        resw = w;
        resh = h;
    }
    
    public static int getViewportX()
    {
        return vpx;
    }
    
    public static int getViewportY()
    {
        return vpy;
    }
    
    public static int getViewportWidth()
    {
        return vpwidth;
    }
    
    public static int getViewportHeight()
    {
        return vpheight;
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
        
        setupGL();
    }
    
    private static void loadLimitations()
    {
        supportFBO = GLContext.getCapabilities().GL_EXT_framebuffer_object;
    }
    
    private static void setupGL()
    {
        loadLimitations();
        
        vpwidth = (int) (wh * (resw / (float) resh));
        if(vpwidth < ww)
            vpheight = wh;
        else
        {
            vpwidth = ww;
            vpheight = (int) (ww * (resh / (float) resw));
        }
        
        vpx = (ww - vpwidth) / 2;
        vpy = (wh - vpheight) / 2;
        
        GL11.glViewport(vpx, vpy, vpwidth, vpheight);
        
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0f, 1.0f, 0.0f, 1.0f, -1.0f, 1.0f);
        
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
        if(ww <= 0) throw new IllegalStateException();
        if(wh <= 0) throw new IllegalStateException();
        if(title == null) throw new NullPointerException();
        WindowHelper.setDisplayMode(ww, wh, fullscreen);
        Display.setTitle(title);
    }
    
    public static void clear()
    {
        ww = Display.getWidth();
        wh = Display.getHeight();
        if(ww != lww || wh != lwh)
            setupGL();
        lww = ww;
        lwh = wh;
        
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
