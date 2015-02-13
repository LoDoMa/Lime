package net.lodoma.lime.client.window;

import java.nio.ByteBuffer;

import net.lodoma.lime.Lime;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.input.InputData;
import net.lodoma.lime.shader.Program;
import net.lodoma.lime.util.Vector2;

import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.ContextCapabilities;
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
    public static GLFWCharCallback charCallback;
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
        {
            Lime.LOGGER.C("Init failed; glfwInit() != GL_TRUE");
            throw new WindowException("Failed to init GLFW");
        }
        Lime.LOGGER.F("GLFW initialized");
        
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VERSION_MAJOR, 2);
        glfwWindowHint(GLFW_VERSION_MINOR, 0);
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, resizable ? GL_TRUE : GL_FALSE);
        
        windowHandle = glfwCreateWindow((int) size.x, (int) size.y, title, fullscreen ? glfwGetPrimaryMonitor() : NULL, NULL);
        if (windowHandle == NULL)
        {
            Lime.LOGGER.C("Failed to craete window; windowHandle == null");
            throw new WindowException("Failed to create GLFW window");
        }
        Lime.LOGGER.F("Created the window");
        
        Input.inputData = new InputData();
        
        setCallbacks();
        
        ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        fullsize.setX(GLFWvidmode.width(vidmode));
        fullsize.setY(GLFWvidmode.height(vidmode));
        int winposx = (int) ((fullsize.x - size.x) / 2.0);
        int winposy = (int) ((fullsize.y - size.y) / 2.0);
        glfwSetWindowPos(windowHandle, winposx, winposy);
        Lime.LOGGER.F("Set window position to " + winposx + ":" + winposy);
        
        glfwMakeContextCurrent(windowHandle);
        updateSyncInterval();
        glfwShowWindow(windowHandle);
        
        initGL();

        Program.createAll();
    }
    
    public static void initGL()
    {
        Lime.LOGGER.F("Checking GL context capabilities");
        GLContext context = GLContext.createFromCurrent();
        ContextCapabilities capabilities = context.getCapabilities();

        if (!capabilities.GL_EXT_framebuffer_object)
        {
            Lime.LOGGER.C("GL_XBT_framebuffer_object not supported");
            throw new IllegalStateException("GL context not capable: GL_XBT_framebuffer_object");
        }

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0.0f, 1.0f, 0.0f, 1.0f, -1.0f, 1.0f);
        
        glMatrixMode(GL_MODELVIEW);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        
        glEnable(GL_TEXTURE_2D);
        
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        updateViewport();
        
        Lime.LOGGER.F("GL initialized");
    }
    
    public static void recreate() throws WindowException
    {
        Lime.LOGGER.F("Recreating window");
        
        glfwMakeContextCurrent(windowHandle);
        
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VERSION_MAJOR, 2);
        glfwWindowHint(GLFW_VERSION_MINOR, 0);
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, resizable ? GL_TRUE : GL_FALSE);

        accsize.set(fullscreen ? fullsize : size);
        
        long newWindowHandle = glfwCreateWindow((int) accsize.x, (int) accsize.y, title, fullscreen ? glfwGetPrimaryMonitor() : NULL, windowHandle);glfwSetWindowPos(windowHandle, 0, 0);
        Lime.LOGGER.F("New window created");
        
        releaseCallbacks();
        glfwDestroyWindow(windowHandle);
        Lime.LOGGER.F("Old window destroyed");
        
        windowHandle = newWindowHandle;
        setCallbacks();

        int winposx = (int) ((fullsize.x - size.x) / 2.0);
        int winposy = (int) ((fullsize.y - size.y) / 2.0);
        glfwSetWindowPos(windowHandle, winposx, winposy);
        Lime.LOGGER.F("Set window position to " + winposx + ":" + winposy);
        
        glfwMakeContextCurrent(windowHandle);
        updateSyncInterval();
        glfwShowWindow(windowHandle);
        
        initGL();
    }
    
    public static void updateSyncInterval()
    {
        glfwSwapInterval(vsync ? 1 : 0);
        Lime.LOGGER.F("Updated sync interval; vsync = " + vsync + ", swap interval = " + (vsync ? 1 : 0));
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
            Lime.LOGGER.F("Window destroyed");
        }
        finally
        {
            glfwTerminate();
            Lime.LOGGER.F("GLFW terminated");
        }
    }
    
    public static void setCallbacks()
    {
        keyCallback = new Input.KeyCallback();
        charCallback = new Input.CharCallback();
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

        Lime.LOGGER.F("Setting callbacks");
        glfwSetKeyCallback(windowHandle, keyCallback);
        glfwSetCharCallback(windowHandle, charCallback);
        glfwSetMouseButtonCallback(windowHandle, mouseCallback);
        glfwSetCursorPosCallback(windowHandle, motionCallback);
        glfwSetWindowSizeCallback(windowHandle, resizeCallback);
        Lime.LOGGER.F("Callbacks set");
    }
    
    public static void releaseCallbacks()
    {
        Lime.LOGGER.F("Releasing callbacks");
        keyCallback.release();
        charCallback.release();
        mouseCallback.release();
        motionCallback.release();
        resizeCallback.release();
        Lime.LOGGER.F("Callbacks released");
    }
}