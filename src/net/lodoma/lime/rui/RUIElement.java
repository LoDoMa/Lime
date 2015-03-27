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
    
    protected final RUIElement parent;
    private final Map<String, RUIElement> children;
    protected final Object treeLock;
    
    private final RUIBorder border = new RUIBorder();

    protected Vector2 position_c = null;
    protected Vector2 dimensions_c = null;
    protected Color fgColor_c = null;
    protected Color bgColor_c = null;
    
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
        values.set("default", "visible", RUIValue.BOOLEAN_TRUE);
        values.set("default", "position-x", RUIValue.SIZE_0);
        values.set("default", "position-y", RUIValue.SIZE_0);
        values.set("default", "width", RUIValue.SIZE_1);
        values.set("default", "height", RUIValue.SIZE_1);
        values.set("default", "foreground-color", RUIValue.COLOR_CLEAR);
        values.set("default", "background-color", RUIValue.COLOR_CLEAR);
        values.set("default", "border-width", RUIValue.SIZE_0);
        values.set("default", "border-radius", RUIValue.SIZE_0);
        values.set("default", "border-color", RUIValue.COLOR_CLEAR);
    }
    
    public void loadDefinition(RUIParserDefinition definition)
    {
        synchronized (treeLock)
        {
            definition.store("visible", RUIValueType.BOOLEAN, values);
            definition.store("position-x", RUIValueType.SIZE, values);
            definition.store("position-y", RUIValueType.SIZE, values);
            definition.store("width", RUIValueType.SIZE, values);
            definition.store("height", RUIValueType.SIZE, values);
            definition.store("foreground-color", RUIValueType.COLOR, values);
            definition.store("background-color", RUIValueType.COLOR, values);
            definition.store("border-width", RUIValueType.SIZE, values);
            definition.store("border-radius", RUIValueType.SIZE, values);
            definition.store("border-color", RUIValueType.COLOR, values);
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
            if (dimensions_x < 0) dimensions_x /= Window.viewportWidth;
            else if (parent != null) dimensions_x *= parent.dimensions_c.x;
            if (dimensions_y < 0) dimensions_y /= Window.viewportHeight;
            else if (parent != null) dimensions_y *= parent.dimensions_c.y;
            if (dimensions_c == null) dimensions_c = new Vector2(dimensions_x, dimensions_y);
            else dimensions_c.set(dimensions_x, dimensions_y);
            
            if (fgColor_c == null) fgColor_c = new Color(values.get(state, "foreground-color").toColor());
            else fgColor_c.set(values.get(state, "foreground-color").toColor());
            if (bgColor_c == null) bgColor_c = new Color(values.get(state, "background-color").toColor());
            else bgColor_c.set(values.get(state, "background-color").toColor());

            border.width = values.get(state, "border-width").toSize();
            border.radius = values.get(state, "border-radius").toSize();
            border.color.set(values.get(state, "border-color").toColor());
            
            Vector2 originalMousePosition = Input.inputData.currentMousePosition.clone();
            Input.inputData.currentMousePosition.subLocal(position_c);
            
            Collection<RUIElement> childrenSet = children.values();
            for (RUIElement child : childrenSet)
                child.update(timeDelta);
            
            Input.inputData.currentMousePosition.set(originalMousePosition);
        }
    }
    
    protected void renderBackground()
    {
        Texture.NO_TEXTURE.bind(0);
        bgColor_c.setGL();
        
        if (border == null)
        {
            glBegin(GL_QUADS);
            glVertex2f(0.0f, 0.0f);
            glVertex2f(dimensions_c.x, 0.0f);
            glVertex2f(dimensions_c.x, dimensions_c.y);
            glVertex2f(0.0f, dimensions_c.y);
            glEnd();
        }
        else
        {
            border.fillBackground(this);
            border.renderBorder(this);
        }
    }
    
    protected void renderForeground() {}
    
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
            
            Collection<RUIElement> childrenSet = children.values();
            for (RUIElement child : childrenSet)
                child.render();
            
            glPopMatrix();
        }
    }
}