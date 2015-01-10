package net.lodoma.lime.client.window;

import java.nio.ByteBuffer;

import net.lodoma.lime.input.Input;
import net.lodoma.lime.shader.Program;
import net.lodoma.lime.util.Vector2;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GLContext;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window
{
    public static Vector2 size = new Vector2(0.0f, 0.0f);
    public static Vector2 accsize = new Vector2(0.0f, 0.0f);
    public static Vector2 fullsize = new Vector2(0.0f, 0.0f);
    public static Vector2 resolution = new Vector2(0.0f, 0.0f);
    public static boolean resizable = false;
    public static boolean fullscreen = false;
    public static String title = "-";
    public static boolean debugEnabled = false;
    public static boolean vsync = false;
    public static boolean closeRequested = false;

    public static long windowHandle;
    // Callbacks MUST be strongly referenced
    public static GLFWKeyCallback keyCallback;
    public static GLFWMouseButtonCallback mouseCallback;
    public static GLFWCursorPosCallback motionCallback;
    public static GLFWWindowSizeCallback resizeCallback;
    
    public static int viewportX;
    public static int viewportY;
    public static int viewportWidth;
    public static int viewportHeight;
    
    public static void create() throws WindowException
    {
        if (glfwInit() != GL_TRUE)
            throw new WindowException("Failed to init GLFW");
        
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, resizable ? GL_TRUE : GL_FALSE);
        
        windowHandle = glfwCreateWindow((int) size.x, (int) size.y, title, fullscreen ? glfwGetPrimaryMonitor() : NULL, NULL);
        if (windowHandle == NULL)
            throw new WindowException("Failed to create GLFW window");
        
        Input.init();
        
        setCallbacks();
        
        ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        fullsize.setX(GLFWvidmode.width(vidmode));
        fullsize.setY(GLFWvidmode.height(vidmode));
        glfwSetWindowPos(windowHandle, (int) ((fullsize.x - size.x) / 2.0), (int) ((fullsize.y - size.y) / 2.0));
        
        glfwMakeContextCurrent(windowHandle);
        glfwSwapInterval(1);
        
        glfwShowWindow(windowHandle);
        
        initGL();
        
        Program.createAll();
    }
    
    public static void initGL()
    {
        GLContext.createFromCurrent();
        
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0.0f, 1.0f, 0.0f, 1.0f, -1.0f, 1.0f);
        
        glMatrixMode(GL_MODELVIEW);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        
        glEnable(GL_TEXTURE_2D);
        
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        updateViewport();
    }
    
    public static void recreate() throws WindowException
    {
        glfwMakeContextCurrent(windowHandle);
        
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, resizable ? GL_TRUE : GL_FALSE);

        accsize.set(fullscreen ? fullsize : size);
        
        long newWindowHandle = glfwCreateWindow((int) accsize.x, (int) accsize.y, title, fullscreen ? glfwGetPrimaryMonitor() : NULL, windowHandle);glfwSetWindowPos(windowHandle, 0, 0);
        releaseCallbacks();
        glfwDestroyWindow(windowHandle);
        windowHandle = newWindowHandle;
        setCallbacks();
        
        glfwSetWindowPos(windowHandle, (int) ((fullsize.x - size.x) / 2.0), (int) ((fullsize.y - size.y) / 2.0));
        
        glfwMakeContextCurrent(windowHandle);
        glfwSwapInterval(1);
        glfwShowWindow(windowHandle);
        
        initGL();
    }
    
    public static void updateViewport()
    {
        accsize.set(fullscreen ? fullsize : size);
        
        float width = accsize.x;
        float height = accsize.y;
        
        viewportWidth = (int) (height * (resolution.x / resolution.y));
        
        if(viewportWidth < width)
            viewportHeight = (int) height;
        else
        {
            viewportWidth = (int) width;
            viewportHeight = (int) (width * (resolution.y / resolution.x));
        }
        
        viewportX = ((int) width - viewportWidth) / 2;
        viewportY = ((int) height - viewportHeight) / 2;
        
        glViewport(viewportX, viewportY, viewportWidth, viewportHeight);
    }
    
    public static void clear()
    {
        updateViewport();
        
        glClear(GL_COLOR_BUFFER_BIT);
        glLoadIdentity();
    }
    
    public static void bindFBO()
    {
        glViewport(viewportX, viewportY, viewportWidth, viewportHeight);
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0); 
    }
    
    public static void update()
    {
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
        
        try
        {
            Thread.sleep(10);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
        
        if (!closeRequested)
            closeRequested = glfwWindowShouldClose(windowHandle) == GL_TRUE;
    }
    
    public static void close()
    {
        Program.destroyAll();
        
        try
        {
            releaseCallbacks();
            glfwDestroyWindow(windowHandle);
        }
        finally
        {
            glfwTerminate();
        }
    }
    
    public static void setCallbacks()
    {
        keyCallback = new Input.KeyCallback();
        mouseCallback = new Input.MouseCallback();
        motionCallback = new Input.MotionCallback();
        resizeCallback = new GLFWWindowSizeCallback()
        {
            @Override
            public void invoke(long window, int width, int height)
            {
                if (!fullscreen)
                {
                    size.x = width;
                    size.y = height;
                }
            }
        };
        
        glfwSetKeyCallback(windowHandle, keyCallback);
        glfwSetMouseButtonCallback(windowHandle, mouseCallback);
        glfwSetCursorPosCallback(windowHandle, motionCallback);
        glfwSetWindowSizeCallback(windowHandle, resizeCallback);
    }
    
    public static void releaseCallbacks()
    {
        keyCallback.release();
        mouseCallback.release();
        motionCallback.release();
        resizeCallback.release();
    }
}