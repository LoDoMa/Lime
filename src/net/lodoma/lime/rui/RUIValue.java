package net.lodoma.lime.rui;

import net.lodoma.lime.util.Color;

public class RUIValue
{
    public static final RUIValue BOOLEAN_FALSE = new RUIValue(false);
    public static final RUIValue BOOLEAN_TRUE = new RUIValue(true);
    public static final RUIValue SIZE_0 = new RUIValue(0.0f);
    public static final RUIValue SIZE_1 = new RUIValue(1.0f);
    public static final RUIValue COLOR_CLEAR = new RUIValue(new Color(0.0f, 0.0f, 0.0f, 0.0f));
    
    private final RUIValueType type;
    private final Object value;
    
    private RUIValue(RUIValueType type, Object value)
    {
        this.type = type;
        this.value = value;
    }
    
    public RUIValue(boolean bool) { this(RUIValueType.BOOLEAN, bool); }
    public RUIValue(int integer) { this(RUIValueType.INTEGER, integer); }
    public RUIValue(float size) { this(RUIValueType.SIZE, size); }
    public RUIValue(Color color) { this(RUIValueType.COLOR, color); }
    public RUIValue(String string) { this(RUIValueType.STRING, string); }
    public RUIValue(Object userdata) { this(RUIValueType.USERDATA, userdata); }
    
    public boolean toBoolean() { return (Boolean) checkType(RUIValueType.BOOLEAN); }
    public int toInteger() { return (Integer) checkType(RUIValueType.INTEGER); }
    public float toSize() { return (Float) checkType(RUIValueType.SIZE); }
    public Color toColor() { return (Color) checkType(RUIValueType.COLOR); }
    public String toString() { return (String) checkType(RUIValueType.STRING); }
    public Object toUserdata() { return checkType(RUIValueType.USERDATA); }
    
    private Object checkType(RUIValueType wtype)
    {
        if (type != wtype)
            throw new IllegalStateException();
        return value;
    }
}
