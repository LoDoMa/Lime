package net.lodoma.lime.client.window;

import net.lodoma.lime.input.Input;
import net.lodoma.lime.util.Vector2;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.opengl.GLContext;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window
{
    public static Vector2 size = new Vector2(0.0f, 0.0f);
    public static Vector2 resolution = new Vector2(0.0f, 0.0f);
    public static boolean resizable = false;
    public static boolean fullscreen = false;
    public static String title = "-";
    public static boolean debugEnabled = false;
    public static boolean vsync = false;
    public static boolean closeRequested = false;

    public static long windowHandle;
    // Callbacks MUST be strongly referenced
    public static GLFWErrorCallback errorCallback;
    public static GLFWKeyCallback keyCallback;
    public static GLFWMouseButtonCallback mouseCallback;
    public static GLFWCursorPosCallback motionCallback;
    
    private static int vpx;
    private static int vpy;
    private static int vpwidth;
    private static int vpheight;
    
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
    
    public static void create() throws WindowException
    {
        errorCallback = errorCallbackPrint(System.err);
        glfwSetErrorCallback(errorCallback);
        
        if (glfwInit() != GL_TRUE)
            throw new WindowException("Failed to init GLFW");
        
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, resizable ? GL_TRUE : GL_FALSE);
        
        windowHandle = glfwCreateWindow((int) size.x, (int) size.y, title, NULL, NULL);
        if (windowHandle == NULL)
            throw new WindowException("Failed to create GLFW window");
        
        Input.init();
        
        keyCallback = new Input.KeyCallback();
        glfwSetKeyCallback(windowHandle, keyCallback);
        
        mouseCallback = new Input.MouseCallback();
        glfwSetMouseButtonCallback(windowHandle, mouseCallback);
        
        motionCallback = new Input.MotionCallback();
        glfwSetCursorPosCallback(windowHandle, motionCallback);
        
        glfwSetWindowPos(windowHandle, 0, 0);
        
        glfwMakeContextCurrent(windowHandle);
        glfwSwapInterval(1);
        
        glfwShowWindow(windowHandle);
        
        initGL();
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
    
    public static void updateViewport()
    {
        vpwidth = (int) (size.y * (resolution.x / resolution.y));
        
        if(vpwidth < size.x)
            vpheight = (int) size.y;
        else
        {
            vpwidth = (int) size.x;
            vpheight = (int) (size.x * (resolution.y / resolution.x));
        }
        
        vpx = ((int) size.x - vpwidth) / 2;
        vpy = ((int) size.y - vpheight) / 2;
        
        glViewport(vpx, vpy, vpwidth, vpheight);
    }
    
    public static void clear()
    {
        updateViewport();
        
        glClear(GL_COLOR_BUFFER_BIT);
        glLoadIdentity();
    }
    
    public static void bindFBO()
    {
        glViewport(vpx, vpy, vpwidth, vpheight);
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0); 
    }
    
    public static void update()
    {
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
        
        if (!closeRequested)
            closeRequested = glfwWindowShouldClose(windowHandle) == GL_TRUE;
    }
    
    public static void close()
    {
        try
        {
            keyCallback.release();
            mouseCallback.release();
            // motionCallback.release(); CRASH?
            glfwDestroyWindow(windowHandle);
        }
        finally
       {
            errorCallback.release();
            glfwTerminate();
        }
    }
}