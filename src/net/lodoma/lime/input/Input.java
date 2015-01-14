package net.lodoma.lime.input;

import java.nio.CharBuffer;

import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.util.Vector2;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import static org.lwjgl.glfw.GLFW.*;

public class Input
{
    public static class KeyCallback extends GLFWKeyCallback
    {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods)
        {
            boolean released = action == GLFW_RELEASE;
            boolean pressed = action == GLFW_PRESS;
            boolean repeated = action == GLFW_REPEAT;
            
            if (repeated)
                inputData.liveKeyboardRepeated.put(key, (byte) 1);
            else
            {
                inputData.liveKeyboard.put(key, (byte) (pressed ? 1 : 0));
                if (released)
                    inputData.liveKeyboardRepeated.put(key, (byte) 0);
            }
        }
    }
    
    public static class MouseCallback extends GLFWMouseButtonCallback
    {
        @Override
        public void invoke(long window, int button, int action, int mods)
        {
            // boolean released = action == GLFW_RELEASE;
            boolean pressed = action == GLFW_PRESS;
            
            inputData.liveMouse.put(button, (byte) (pressed ? 1 : 0));
        }
    }
    
    public static class MotionCallback extends GLFWCursorPosCallback
    {
        @Override
        public void invoke(long window, double xpos, double ypos)
        {
            inputData.liveMousePosition.set((float) ((xpos - Window.viewportX) / Window.viewportWidth),
                                            (float) (((Window.accsize.y - ypos) - Window.viewportY) / Window.viewportHeight));
        }
    }
    
    public static final int
        KEY_SPACE           = 0x20,
        KEY_APOSTROPHE      = 0x27,
        KEY_COMMA           = 0x2C,
        KEY_MINUS           = 0x2D,
        KEY_PERIOD          = 0x2E,
        KEY_SLASH           = 0x2F,
        KEY_0               = 0x30,
        KEY_1               = 0x31,
        KEY_2               = 0x32,
        KEY_3               = 0x33,
        KEY_4               = 0x34,
        KEY_5               = 0x35,
        KEY_6               = 0x36,
        KEY_7               = 0x37,
        KEY_8               = 0x38,
        KEY_9               = 0x39,
        KEY_SEMICOLON       = 0x3B,
        KEY_EQUAL           = 0x3D,
        KEY_A               = 0x41,
        KEY_B               = 0x42,
        KEY_C               = 0x43,
        KEY_D               = 0x44,
        KEY_E               = 0x45,
        KEY_F               = 0x46,
        KEY_G               = 0x47,
        KEY_H               = 0x48,
        KEY_I               = 0x49,
        KEY_J               = 0x4A,
        KEY_K               = 0x4B,
        KEY_L               = 0x4C,
        KEY_M               = 0x4D,
        KEY_N               = 0x4E,
        KEY_O               = 0x4F,
        KEY_P               = 0x50,
        KEY_Q               = 0x51,
        KEY_R               = 0x52,
        KEY_S               = 0x53,
        KEY_T               = 0x54,
        KEY_U               = 0x55,
        KEY_V               = 0x56,
        KEY_W               = 0x57,
        KEY_X               = 0x58,
        KEY_Y               = 0x59,
        KEY_Z               = 0x5A,
        KEY_LEFT_BRACKET    = 0x5B,
        KEY_BACKSLASH       = 0x5C,
        KEY_RIGHT_BRACKET   = 0x5D,
        KEY_GRAVE_ACCENT    = 0x60,
        KEY_WORLD_1         = 0xA1,
        KEY_WORLD_2         = 0xA2;

    public static final int
        KEY_ESCAPE          = 0x100,
        KEY_ENTER           = 0x101,
        KEY_TAB             = 0x102,
        KEY_BACKSPACE       = 0x103,
        KEY_INSERT          = 0x104,
        KEY_DELETE          = 0x105,
        KEY_RIGHT           = 0x106,
        KEY_LEFT            = 0x107,
        KEY_DOWN            = 0x108,
        KEY_UP              = 0x109,
        KEY_PAGE_UP         = 0x10A,
        KEY_PAGE_DOWN       = 0x10B,
        KEY_HOME            = 0x10C,
        KEY_END             = 0x10D,
        KEY_CAPS_LOCK       = 0x118,
        KEY_SCROLL_LOCK     = 0x119,
        KEY_NUM_LOCK        = 0x11A,
        KEY_PRINT_SCREEN    = 0x11B,
        KEY_PAUSE           = 0x11C,
        KEY_F1              = 0x122,
        KEY_F2              = 0x123,
        KEY_F3              = 0x124,
        KEY_F4              = 0x125,
        KEY_F5              = 0x126,
        KEY_F6              = 0x127,
        KEY_F7              = 0x128,
        KEY_F8              = 0x129,
        KEY_F9              = 0x12A,
        KEY_F10             = 0x12B,
        KEY_F11             = 0x12C,
        KEY_F12             = 0x12D,
        KEY_F13             = 0x12E,
        KEY_F14             = 0x12F,
        KEY_F15             = 0x130,
        KEY_F16             = 0x131,
        KEY_F17             = 0x132,
        KEY_F18             = 0x133,
        KEY_F19             = 0x134,
        KEY_F20             = 0x135,
        KEY_F21             = 0x136,
        KEY_F22             = 0x137,
        KEY_F23             = 0x138,
        KEY_F24             = 0x139,
        KEY_F25             = 0x13A,
        KEY_KP_0            = 0x140,
        KEY_KP_1            = 0x141,
        KEY_KP_2            = 0x142,
        KEY_KP_3            = 0x143,
        KEY_KP_4            = 0x144,
        KEY_KP_5            = 0x145,
        KEY_KP_6            = 0x146,
        KEY_KP_7            = 0x147,
        KEY_KP_8            = 0x148,
        KEY_KP_9            = 0x149,
        KEY_KP_DECIMAL      = 0x14A,
        KEY_KP_DIVIDE       = 0x14B,
        KEY_KP_MULTIPLY     = 0x14C,
        KEY_KP_SUBTRACT     = 0x14D,
        KEY_KP_ADD          = 0x14E,
        KEY_KP_ENTER        = 0x14F,
        KEY_KP_EQUAL        = 0x150,
        KEY_LEFT_SHIFT      = 0x154,
        KEY_LEFT_CONTROL    = 0x155,
        KEY_LEFT_ALT        = 0x156,
        KEY_LEFT_SUPER      = 0x157,
        KEY_RIGHT_SHIFT     = 0x158,
        KEY_RIGHT_CONTROL   = 0x159,
        KEY_RIGHT_ALT       = 0x15A,
        KEY_RIGHT_SUPER     = 0x15B,
        KEY_MENU            = 0x15C,
        SIZE_KEYBOARD       = KEY_MENU;

    public static final int
        MOD_SHIFT           = 0x1,
        MOD_CONTROL         = 0x2,
        MOD_ALT             = 0x4,
        MOD_SUPER           = 0x8;

    public static final int
        MOUSE_BUTTON_1      = 0x0,
        MOUSE_BUTTON_2      = 0x1,
        MOUSE_BUTTON_3      = 0x2,
        MOUSE_BUTTON_4      = 0x3,
        MOUSE_BUTTON_5      = 0x4,
        MOUSE_BUTTON_6      = 0x5,
        MOUSE_BUTTON_7      = 0x6,
        MOUSE_BUTTON_8      = 0x7,
        MOUSE_BUTTON_LEFT   = MOUSE_BUTTON_1,
        MOUSE_BUTTON_RIGHT  = MOUSE_BUTTON_2,
        MOUSE_BUTTON_MIDDLE = MOUSE_BUTTON_3,
        SIZE_MOUSE          = MOUSE_BUTTON_8;

    /** Joysticks. */
    public static final int
        JOYSTICK_1          = 0x0,
        JOYSTICK_2          = 0x1,
        JOYSTICK_3          = 0x2,
        JOYSTICK_4          = 0x3,
        JOYSTICK_5          = 0x4,
        JOYSTICK_6          = 0x5,
        JOYSTICK_7          = 0x6,
        JOYSTICK_8          = 0x7,
        JOYSTICK_9          = 0x8,
        JOYSTICK_10         = 0x9,
        JOYSTICK_11         = 0xA,
        JOYSTICK_12         = 0xB,
        JOYSTICK_13         = 0xC,
        JOYSTICK_14         = 0xD,
        JOYSTICK_15         = 0xE,
        JOYSTICK_16         = 0xF,
        SIZE_JOYSTICK       = JOYSTICK_16;
    
    public static final int STATE_SIZE          = (int) Math.ceil(SIZE_KEYBOARD / 8.0) * 2 + (int) Math.ceil(SIZE_MOUSE / 8.0);

    public static InputData inputData;
    public static CharBuffer chars;
    
    private static void loadChar(char c)
    {
        try
        {
            chars.put(Input.class.getField("KEY_" + Character.toUpperCase(c)).getInt(null), c);
        }
        catch(Exception e)
        {
            System.err.println("Error loading chars");
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private static void loadChars()
    {
        for(char i = 'a'; i < 'z'; i++) loadChar(i);
        for(char i = '0'; i < '9'; i++) loadChar(i);
        chars.put(KEY_SPACE, ' ');
        chars.put(KEY_PERIOD, '.');
        chars.put(KEY_COMMA, ',');
    }
    
    public static void init()
    {
        chars = CharBuffer.allocate(SIZE_KEYBOARD);
        loadChars();
    }
    
    public static void update()
    {
        inputData.update();
    }
    
    public static boolean getKey(int key)
    {
        return inputData.currentKeyboard.get(key) == 1;
    }
    
    public static boolean getKeyDown(int key)
    {
        return inputData.currentKeyboard.get(key) == 1 && inputData.oldKeyboard.get(key) == 0;
    }
    
    public static boolean getKeyUp(int key)
    {
        return inputData.currentKeyboard.get(key) == 0 && inputData.oldKeyboard.get(key) == 1;
    }
    
    public static boolean getKeyRepeated(int key)
    {
        return getKeyDown(key) || inputData.currentKeyboardRepeated.get(key) == 1;
    }
    
    public static boolean getMouse(int key)
    {
        return inputData.currentMouse.get(key) == 1;
    }
    
    public static boolean getMouseDown(int key)
    {
        return inputData.currentMouse.get(key) == 1 && inputData.oldMouse.get(key) == 0;
    }
    
    public static boolean getMouseUp(int key)
    {
        return inputData.currentMouse.get(key) == 0 && inputData.oldMouse.get(key) == 1;
    }
    
    public static Vector2 getMousePosition()
    {
        return new Vector2(inputData.currentMousePosition.x, inputData.currentMousePosition.y);
    }
    
    public static char getChar(int key)
    {
        return chars.get(key);
    }
}
