package net.lodoma.lime.input;

import java.nio.ByteBuffer;

import net.lodoma.lime.util.Vector2;

public class InputData
{
    // Input state that is not taken in consideration yet
    public ByteBuffer liveKeyboard;
    public ByteBuffer liveKeyboardRepeated;
    public ByteBuffer liveMouse;
    public Vector2 liveMousePosition;
    
    // Most recent input state that is taken into consideration
    public ByteBuffer currentKeyboard;
    public ByteBuffer currentKeyboardRepeated;
    public ByteBuffer currentMouse;
    public Vector2 currentMousePosition;
    
    // Previous input state, used for comparison with the new one
    public ByteBuffer oldKeyboard;
    public ByteBuffer oldMouse;
    public Vector2 oldMousePosition;
    
    public InputData()
    {
        liveKeyboard            = ByteBuffer.allocate(Input.SIZE_KEYBOARD);
        liveKeyboardRepeated    = ByteBuffer.allocate(liveKeyboard.capacity());
        liveMouse               = ByteBuffer.allocate(Input.SIZE_MOUSE);
        liveMousePosition       = new Vector2(0.0f, 0.0f);
        
        currentKeyboard         = ByteBuffer.allocate(liveKeyboard.capacity());
        currentKeyboardRepeated = ByteBuffer.allocate(liveKeyboardRepeated.capacity());
        currentMouse            = ByteBuffer.allocate(liveMouse.capacity());
        currentMousePosition    = new Vector2(0.0f, 0.0f);
        
        oldKeyboard             = ByteBuffer.allocate(currentKeyboard.capacity());
        oldMouse                = ByteBuffer.allocate(currentMouse.capacity());
        oldMousePosition        = new Vector2(0.0f, 0.0f);
    }
    
    /**
     * Replaces the old input state with the current one,
     * then replaces the current one with the live one.
     */
    public void update()
    {
        oldKeyboard.position(0);
        oldKeyboard.put(currentKeyboard.array());
        oldMouse.position(0);
        oldMouse.put(currentMouse.array());
        oldMousePosition.set(currentMousePosition);
        
        currentKeyboard.position(0);
        currentKeyboard.put(liveKeyboard.array());
        currentKeyboardRepeated.position(0);
        currentKeyboardRepeated.put(liveKeyboardRepeated.array());
        currentMouse.position(0);
        currentMouse.put(liveMouse.array());
        currentMousePosition.set(liveMousePosition);
    }
    
    /**
     * Returns the current input state in a compressed format.
     * The state of each key or mouse button is stored in one bit.
     * 
     * @return current input state in a compressed format
     */
    public ByteBuffer getState()
    {
        ByteBuffer keyboardCompressed = compress(currentKeyboard);
        ByteBuffer repeatedCompressed = compress(currentKeyboardRepeated);
        ByteBuffer mouseCompressed = compress(currentMouse);
        
        keyboardCompressed.position(0);
        repeatedCompressed.position(0);
        mouseCompressed.position(0);
        
        ByteBuffer state = ByteBuffer.allocate(keyboardCompressed.capacity() +
                                               repeatedCompressed.capacity() +
                                               mouseCompressed.capacity() + 8);

        state.position(0);
        state.put(keyboardCompressed);
        state.put(repeatedCompressed);
        state.put(mouseCompressed);
        state.putFloat(currentMousePosition.x);
        state.putFloat(currentMousePosition.y);
        
        return state;
    }
    
    /**
     * Replaces the live input state with the given compressed input state.
     * 
     * @param state - input state in a compressed format that will replace the live input state
     */
    public void loadState(ByteBuffer state)
    {
        decompress(liveKeyboard, state, 0);
        decompress(liveKeyboardRepeated, state, liveKeyboard.capacity());
        decompress(liveMouse, state, liveKeyboard.capacity() + liveKeyboardRepeated.capacity());
    }
    
    private ByteBuffer compress(ByteBuffer source)
    {
        ByteBuffer compressed = ByteBuffer.allocate((int) Math.ceil(source.capacity() / 8.0));
        for (int i = 0; i < source.capacity(); i++)
            if (source.get(i) != 0)
                compressed.put(i / 8, (byte) (compressed.get(i / 8) | (1 << (i % 8))));
        return compressed;
    }
    
    private void decompress(ByteBuffer destination, ByteBuffer compressed, int offset)
    {
        offset = (int) Math.ceil(offset / 8.0);
        for (int i = 0; i < destination.capacity(); i++)
            destination.put(i, (byte) ((compressed.get(offset + i / 8) >>> (i % 8)) & 0x1));
    }
}
