package net.lodoma.lime.rui;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.texture.Texture;
import net.lodoma.lime.util.Color;
import net.lodoma.lime.util.Vector2;
import static org.lwjgl.opengl.GL11.*;

public class RUIElement
{
    public final RUIValueMap values;
    public String state;
    public RUIEventListener eventListener;
    public RUIGroup group;
    
    protected final RUIElement parent;
    private final Map<String, RUIElement> children;
    protected final Object treeLock;
    
    private final RUIBorder border = new RUIBorder();

    protected Vector2 position_c = null;
    protected Vector2 dimensions_c = null;
    protected Color fgColor_c = null;
    
    public RUIElement(RUIElement parent)
    {
        this.parent = parent;
        if (parent != null)
            treeLock = parent.treeLock;
        else
            treeLock = new Object();
        
        values = new RUIValueMap();
        state = "default";
        
        children = new HashMap<String, RUIElement>();
        
        loadDefaultValues();
    }
    
    protected void loadDefaultValues()
    {
        values.set("default", "index", new RUIValue(-1));
        values.set("default", "visible", RUIValue.BOOLEAN_TRUE);
        values.set("default", "position-x", RUIValue.SIZE_0);
        values.set("default", "position-y", RUIValue.SIZE_0);
        values.set("default", "width", RUIValue.SIZE_1);
        values.set("default", "height", RUIValue.SIZE_1);
        values.set("default", "foreground-color", RUIValue.COLOR_CLEAR);
        border.loadDefaultValues(values);
        
        if (parent == null)
        {
            // By default, root is not visible
            values.set("default", "visible", RUIValue.BOOLEAN_FALSE);
        }
    }
    
    public void loadData(RUIParserData data)
    {
        synchronized (treeLock)
        {
            data.copy("index", RUIValueType.INTEGER, values);
            data.copy("visible", RUIValueType.BOOLEAN, values);
            data.copy("position-x", RUIValueType.SIZE, values);
            data.copy("position-y", RUIValueType.SIZE, values);
            data.copy("width", RUIValueType.SIZE, values);
            data.copy("height", RUIValueType.SIZE, values);
            data.copy("foreground-color", RUIValueType.COLOR, values);
            border.loadData(data, values);
        }
    }
    
    public void addChild(String name, RUIElement child)
    {
        synchronized (treeLock)
        {
            if (children.containsKey(name)) throw new IllegalStateException();
            children.put(name, child);
        }
    }
    
    public RUIElement getChild(String name)
    {
        synchronized (treeLock)
        {
            if (!children.containsKey(name)) throw new IllegalStateException();        
            return children.get(name);
        }
    }
    
    public RUIElement getChildRecursive(String path)
    {
        String[] segms = path.split("\\.");
        RUIElement current = this;
        for (String segm : segms)
            current = current.getChild(segm);
        return current;
    }
    
    public void removeChild(String name)
    {
        synchronized (treeLock)
        {
            if (!children.containsKey(name)) throw new IllegalStateException();
            children.remove(name);
        }
    }
    
    public void removeChildRecursive(String path)
    {
        String[] segms = path.split("\\.");
        RUIElement current = this;
        for (int i = 0; i < segms.length - 1; i++)
            current = current.getChild(segms[i]);
        current.removeChild(segms[segms.length - 1]);
    }
    
    public void load(String filepath)
    {
        synchronized (treeLock)
        {
            RUIParser parser = new RUIParser(filepath);
            parser.load(this);
        }
    }
    
    protected void updateChildren(double timeDelta)
    {
        Collection<RUIElement> childrenSet = children.values();
        for (RUIElement child : childrenSet)
            child.update(timeDelta);
    }
    
    public void update(double timeDelta)
    {
        synchronized (treeLock)
        {
            float position_x = values.get(state, "position-x").toSize();
            float position_y = values.get(state, "position-y").toSize();
            if (position_x < 0) position_x /= -Window.viewportWidth;
            else if (parent != null) position_x *= parent.dimensions_c.x;
            if (position_y < 0) position_y /= -Window.viewportHeight;
            else if (parent != null) position_y *= parent.dimensions_c.y;
            if (position_c == null) position_c = new Vector2(position_x, position_y);
            else position_c.set(position_x, position_y);

            float dimensions_x = values.get(state, "width").toSize();
            float dimensions_y = values.get(state, "height").toSize();
            if (dimensions_x < 0) dimensions_x /= -Window.viewportWidth;
            else if (parent != null) dimensions_x *= parent.dimensions_c.x;
            if (dimensions_y < 0) dimensions_y /= -Window.viewportHeight;
            else if (parent != null) dimensions_y *= parent.dimensions_c.y;
            if (dimensions_c == null) dimensions_c = new Vector2(dimensions_x, dimensions_y);
            else dimensions_c.set(dimensions_x, dimensions_y);
            
            if (fgColor_c == null) fgColor_c = new Color(values.get(state, "foreground-color").toColor());
            else fgColor_c.set(values.get(state, "foreground-color").toColor());

            border.update(timeDelta, this);
            
            Vector2 originalMousePosition = Input.inputData.currentMousePosition.clone();
            Input.inputData.currentMousePosition.subLocal(position_c);
            
            updateChildren(timeDelta);
            
            Input.inputData.currentMousePosition.set(originalMousePosition);
        }
    }
    
    protected void renderBackground()
    {
        Texture.NO_TEXTURE.bind(0);
        
        border.fillBackground(this);
        border.renderBorder(this);
    }
    
    protected void renderForeground() {}
    
    protected void renderChildren()
    {
        Collection<RUIElement> childrenSet = children.values();
        for (RUIElement child : childrenSet)
            child.render();
    }
    
    public void render()
    {
        synchronized (treeLock)
        {
            glPushMatrix();
            glTranslatef(position_c.x, position_c.y, 0.0f);
            
            if (values.get(state, "visible").toBoolean())
            {
                renderBackground();
                renderForeground();
            }
            
            renderChildren();
            
            glPopMatrix();
        }
    }
}
