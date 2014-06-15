package net.lodoma.lime.client.window;

import java.io.File;

import net.lodoma.lime.shader.Program;
import net.lodoma.lime.shader.Shader;
import net.lodoma.lime.shader.ShaderPool;
import net.lodoma.lime.shader.ShaderType;
import net.lodoma.lime.shader.UniformType;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class Window
{
    private static int width = 0;
    private static int height = 0;
    private static boolean fullscreen = false;
    private static String title = null;
    private static int fps = 0;
    
    private static boolean closeRequested = false;
    
    private static Program program;
    
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
    
    public static void setFPS(int fps)
    {
        Window.fps = fps;
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

        Shader vertexShader = new Shader(new File("./shader/testshader.vsx"), ShaderType.VERTEX);
        Shader fragmentShader = new Shader(new File("./shader/testshader.fsx"), ShaderType.FRAGMENT);
        ShaderPool shaderPool = new ShaderPool();
        shaderPool.addShader("vs", vertexShader);
        shaderPool.addShader("fs", fragmentShader);
        program = new Program(shaderPool, "vs", "fs");
        program.setUniform("texture", UniformType.INT1, 0);
        
        setupGL();
    }
    
    private static void setupGL()
    {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0f, 10.0f, 0.0f, 10.0f, -1.0f, 1.0f);
        
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        program.useProgram();
    }
    
    public static void apply() throws InvalidWindowPropertyException, DisplayModeSearchException
    {
        if(width <= 0) throw new InvalidWindowPropertyException();
        if(height <= 0) throw new InvalidWindowPropertyException();
        if(title == null) throw new NullPointerException();
        WindowHelper.setDisplayMode(width, height, fullscreen);
        Display.setTitle(title);
    }
    
    public static void clear()
    {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glLoadIdentity();
    }
    
    public static void update() throws InvalidWindowPropertyException
    {
        if(fps <= 0) throw new InvalidWindowPropertyException();
        Display.update();
        Display.sync(fps);
    }
    
    public static void close()
    {
        program.deleteProgram();
        Mouse.destroy();
        Keyboard.destroy();
        Display.destroy();
    }
}
