package net.lodoma.lime.input;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.util.Vector2;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import static org.lwjgl.glfw.GLFW.*;

public class Input
{
    public static final int LEFT_MOUSE_BUTTON   = GLFW_MOUSE_BUTTON_LEFT;
    public static final int RIGHT_MOUSE_BUTTON  = GLFW_MOUSE_BUTTON_RIGHT;
    public static final int MIDDLE_MOUSE_BUTTON = GLFW_MOUSE_BUTTON_MIDDLE;
    
    public static final int MOUSE_SIZE          = GLFW_MOUSE_BUTTON_LAST;
    
    public static final int KEY_NONE            = GLFW_KEY_UNKNOWN;
    public static final int KEY_ESCAPE          = GLFW_KEY_ESCAPE;
    public static final int KEY_1               = GLFW_KEY_1;
    public static final int KEY_2               = GLFW_KEY_2;
    public static final int KEY_3               = GLFW_KEY_3;
    public static final int KEY_4               = GLFW_KEY_4;
    public static final int KEY_5               = GLFW_KEY_5;
    public static final int KEY_6               = GLFW_KEY_6;
    public static final int KEY_7               = GLFW_KEY_7;
    public static final int KEY_8               = GLFW_KEY_8;
    public static final int KEY_9               = GLFW_KEY_9;
    public static final int KEY_0               = GLFW_KEY_0;
    public static final int KEY_MINUS           = GLFW_KEY_MINUS;
    public static final int KEY_EQUALS          = GLFW_KEY_EQUAL;
    public static final int KEY_BACK            = GLFW_KEY_BACKSPACE;
    public static final int KEY_TAB             = GLFW_KEY_TAB;
    public static final int KEY_Q               = GLFW_KEY_Q;
    public static final int KEY_W               = GLFW_KEY_W;
    public static final int KEY_E               = GLFW_KEY_E;
    public static final int KEY_R               = GLFW_KEY_R;
    public static final int KEY_T               = GLFW_KEY_T;
    public static final int KEY_Y               = GLFW_KEY_Y;
    public static final int KEY_U               = GLFW_KEY_U;
    public static final int KEY_I               = GLFW_KEY_I;
    public static final int KEY_O               = GLFW_KEY_O;
    public static final int KEY_P               = GLFW_KEY_P;
    public static final int KEY_LBRACKET        = GLFW_KEY_LEFT_BRACKET;
    public static final int KEY_RBRACKET        = GLFW_KEY_RIGHT_BRACKET;
    public static final int KEY_RETURN          = GLFW_KEY_ENTER;
    public static final int KEY_LCONTROL        = GLFW_KEY_LEFT_CONTROL;
    public static final int KEY_A               = GLFW_KEY_A;
    public static final int KEY_S               = GLFW_KEY_S;
    public static final int KEY_D               = GLFW_KEY_D;
    public static final int KEY_F               = GLFW_KEY_F;
    public static final int KEY_G               = GLFW_KEY_G;
    public static final int KEY_H               = GLFW_KEY_H;
    public static final int KEY_J               = GLFW_KEY_J;
    public static final int KEY_K               = GLFW_KEY_K;
    public static final int KEY_L               = GLFW_KEY_L;
    public static final int KEY_SEMICOLON       = GLFW_KEY_SEMICOLON;
    public static final int KEY_APOSTROPHE      = GLFW_KEY_APOSTROPHE;
    public static final int KEY_GRAVE           = GLFW_KEY_GRAVE_ACCENT;
    public static final int KEY_LSHIFT          = GLFW_KEY_LEFT_SHIFT;
    public static final int KEY_BACKSLASH       = GLFW_KEY_BACKSLASH;
    public static final int KEY_Z               = GLFW_KEY_Z;
    public static final int KEY_X               = GLFW_KEY_X;
    public static final int KEY_C               = GLFW_KEY_C;
    public static final int KEY_V               = GLFW_KEY_V;
    public static final int KEY_B               = GLFW_KEY_B;
    public static final int KEY_N               = GLFW_KEY_N;
    public static final int KEY_M               = GLFW_KEY_M;
    public static final int KEY_COMMA           = GLFW_KEY_COMMA;
    public static final int KEY_PERIOD          = GLFW_KEY_PERIOD;
    public static final int KEY_SLASH           = GLFW_KEY_SLASH;
    public static final int KEY_RSHIFT          = GLFW_KEY_RIGHT_SHIFT;
    public static final int KEY_MULTIPLY        = GLFW_KEY_KP_MULTIPLY;
    public static final int KEY_LMENU           = GLFW_KEY_LEFT_SUPER;
    public static final int KEY_LALT            = GLFW_KEY_LEFT_ALT;
    public static final int KEY_SPACE           = GLFW_KEY_SPACE;
    public static final int KEY_CAPITAL         = GLFW_KEY_CAPS_LOCK;
    public static final int KEY_F1              = GLFW_KEY_F1;
    public static final int KEY_F2              = GLFW_KEY_F2;
    public static final int KEY_F3              = GLFW_KEY_F3;
    public static final int KEY_F4              = GLFW_KEY_F4;
    public static final int KEY_F5              = GLFW_KEY_F5;
    public static final int KEY_F6              = GLFW_KEY_F6;
    public static final int KEY_F7              = GLFW_KEY_F7;
    public static final int KEY_F8              = GLFW_KEY_F8;
    public static final int KEY_F9              = GLFW_KEY_F9;
    public static final int KEY_F10             = GLFW_KEY_F10;
    public static final int KEY_NUMLOCK         = GLFW_KEY_NUM_LOCK;
    public static final int KEY_SCROLL          = GLFW_KEY_SCROLL_LOCK;
    public static final int KEY_NUMPAD7         = GLFW_KEY_KP_7;
    public static final int KEY_NUMPAD8         = GLFW_KEY_KP_8;
    public static final int KEY_NUMPAD9         = GLFW_KEY_KP_9;
    public static final int KEY_SUBTRACT        = GLFW_KEY_KP_SUBTRACT;
    public static final int KEY_NUMPAD4         = GLFW_KEY_KP_4;
    public static final int KEY_NUMPAD5         = GLFW_KEY_KP_5;
    public static final int KEY_NUMPAD6         = GLFW_KEY_KP_6;
    public static final int KEY_ADD             = GLFW_KEY_KP_ADD;
    public static final int KEY_NUMPAD1         = GLFW_KEY_KP_1;
    public static final int KEY_NUMPAD2         = GLFW_KEY_KP_2;
    public static final int KEY_NUMPAD3         = GLFW_KEY_KP_3;
    public static final int KEY_NUMPAD0         = GLFW_KEY_KP_0;
    public static final int KEY_DECIMAL         = GLFW_KEY_KP_DECIMAL;
    public static final int KEY_F11             = GLFW_KEY_F11;
    public static final int KEY_F12             = GLFW_KEY_F12;
    public static final int KEY_F13             = GLFW_KEY_F13;
    public static final int KEY_F14             = GLFW_KEY_F14;
    public static final int KEY_F15             = GLFW_KEY_F15;
    // public static final int KEY_KANA         = ;
    // public static final int KEY_CONVERT      = ;
    // public static final int KEY_NOCONVERT    = ;
    // public static final int KEY_YEN          = ;
    public static final int KEY_NUMPADEQUALS    = GLFW_KEY_KP_EQUAL;
    // public static final int KEY_CIRCUMFLEX   = ;
    // public static final int KEY_AT           = ;
    // public static final int KEY_COLON        = ;
    // public static final int KEY_UNDERLINE    = ;
    // public static final int KEY_KANJI        = ;
    // public static final int KEY_STOP         = ;
    // public static final int KEY_AX           = ;
    // public static final int KEY_UNLABELED    = ;
    public static final int KEY_NUMPADENTER     = GLFW_KEY_KP_ENTER;
    public static final int KEY_RCONTROL        = GLFW_KEY_RIGHT_CONTROL;
    public static final int KEY_NUMPADCOMMA     = GLFW_KEY_KP_DECIMAL;      // ?
    public static final int KEY_DIVIDE          = GLFW_KEY_KP_DIVIDE;
    // public static final int KEY_SYSRQ        = ;
    public static final int KEY_RMENU           = GLFW_KEY_RIGHT_SUPER;
    public static final int KEY_RALT            = GLFW_KEY_RIGHT_ALT;
    public static final int KEY_PAUSE           = GLFW_KEY_PAUSE;
    public static final int KEY_HOME            = GLFW_KEY_HOME;
    public static final int KEY_UP              = GLFW_KEY_UP;
    // public static final int KEY_PRIOR        = ;
    public static final int KEY_LEFT            = GLFW_KEY_LEFT;
    public static final int KEY_RIGHT           = GLFW_KEY_RIGHT;
    public static final int KEY_END             = GLFW_KEY_END;
    public static final int KEY_DOWN            = GLFW_KEY_DOWN;
    // public static final int KEY_NEXT         = ;
    public static final int KEY_INSERT          = GLFW_KEY_INSERT;
    public static final int KEY_DELETE          = GLFW_KEY_DELETE;
    // public static final int KEY_LMETA        = ;
    // public static final int KEY_LWIN         = ;
    // public static final int KEY_RMETA        = ;
    // public static final int KEY_RWIN         = ;
    // public static final int KEY_APPS         = ;
    // public static final int KEY_POWER        = ;
    // public static final int KEY_SLEEP        = ;
    
    public static final int KEYBOARD_SIZE       = GLFW_KEY_LAST;

    private static ByteBuffer liveKeyboard;
    private static ByteBuffer liveKeyboardRepeated;
    private static ByteBuffer currentKeyboard;
    private static ByteBuffer currentKeyboardRepeated;
    private static ByteBuffer oldKeyboard;

    private static ByteBuffer liveMouse;
    private static ByteBuffer currentMouse;
    private static ByteBuffer oldMouse;
    
    private static CharBuffer chars;

    private static float mouseX;
    private static float mouseY;
    
    public static class KeyCallback extends GLFWKeyCallback
    {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods)
        {
            boolean released = action == GLFW_RELEASE;
            boolean pressed = action == GLFW_PRESS;
            boolean repeated = action == GLFW_REPEAT;
            
            if (repeated)
                liveKeyboardRepeated.put(key, (byte) 1);
            else
            {
                liveKeyboard.put(key, (byte) (pressed ? 1 : 0));
                if (released)
                    liveKeyboardRepeated.put(key, (byte) 0);
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
            
            liveMouse.put(button, (byte) (pressed ? 1 : 0));
        }
    }
    
    public static class MotionCallback extends GLFWCursorPosCallback
    {
        @Override
        public void invoke(long window, double xpos, double ypos)
        {
            mouseX = (float) ((xpos - Window.viewportX) / Window.viewportWidth);
            mouseY = (float) (((Window.size.y - ypos) - Window.viewportY) / Window.viewportHeight);
        }
    }
    
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
        liveKeyboard            = ByteBuffer.allocate(KEYBOARD_SIZE);
        liveKeyboardRepeated    = ByteBuffer.allocate(liveKeyboard.capacity());
        currentKeyboard         = ByteBuffer.allocate(liveKeyboard.capacity());
        currentKeyboardRepeated = ByteBuffer.allocate(liveKeyboardRepeated.capacity());
        oldKeyboard             = ByteBuffer.allocate(currentKeyboard.capacity());

        liveMouse    = ByteBuffer.allocate(MOUSE_SIZE);
        currentMouse = ByteBuffer.allocate(liveMouse.capacity());
        oldMouse     = ByteBuffer.allocate(currentMouse.capacity());
        
        chars = CharBuffer.allocate(currentKeyboard.capacity());
        loadChars();
    }
    
    public static void update()
    {
        oldKeyboard.position(0);
        oldKeyboard.put(currentKeyboard.array());
        currentKeyboard.position(0);
        currentKeyboard.put(liveKeyboard.array());
        currentKeyboardRepeated.position(0);
        currentKeyboardRepeated.put(liveKeyboardRepeated.array());
        
        oldMouse.position(0);
        oldMouse.put(currentMouse.array());
        currentMouse.position(0);
        currentMouse.put(liveMouse.array());
    }
    
    public static boolean getKey(int key)
    {
        return currentKeyboard.get(key) == 1;
    }
    
    public static boolean getKeyDown(int key)
    {
        return currentKeyboard.get(key) == 1 && oldKeyboard.get(key) == 0;
    }
    
    public static boolean getKeyUp(int key)
    {
        return currentKeyboard.get(key) == 0 && oldKeyboard.get(key) == 1;
    }
    
    public static boolean getKeyRepeated(int key)
    {
        return getKeyDown(key) || currentKeyboardRepeated.get(key) == 1;
    }
    
    public static boolean getMouse(int key)
    {
        return currentMouse.get(key) == 1;
    }
    
    public static boolean getMouseDown(int key)
    {
        return currentMouse.get(key) == 1 && oldMouse.get(key) == 0;
    }
    
    public static boolean getMouseUp(int key)
    {
        return currentMouse.get(key) == 0 && oldMouse.get(key) == 1;
    }
    
    public static Vector2 getMousePosition()
    {
        return new Vector2(mouseX, mouseY);
    }
    
    public static char getChar(int key)
    {
        return chars.get(key);
    }
}
