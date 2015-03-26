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
    public boolean visible;
    public final Vector2 position = new Vector2();
    public final Vector2 dimensions = new Vector2();
    public final RUIBorder border = new RUIBorder();
    public final Color fgColor = new Color();
    public final Color bgColor = new Color();

    protected final RUIElement parent;
    private  final Map<String, RUIElement> children;
    protected final Object treeLock;

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
        
        children = new HashMap<String, RUIElement>();
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
    
    public void removeChild(String name)
    {
        synchronized (treeLock)
        {
            if (!children.containsKey(name)) throw new IllegalStateException();
            children.remove(name);
        }
    }
    
    public void load(String filepath)
    {
        synchronized (treeLock)
        {
            RUIParser parser = new RUIParser(filepath);
            parser.load(this);
        }
    }
    
    public void loadDefinition(RUIParserDefinition definition)
    {
        synchronized (treeLock)
        {
            position.x = RUIParser.parseSize(definition.get("default", "position-x", "0px"));
            position.y = RUIParser.parseSize(definition.get("default", "position-y", "0px"));
            
            dimensions.x = RUIParser.parseSize(definition.get("default", "width", "100%"));
            dimensions.y = RUIParser.parseSize(definition.get("default", "height", "100%"));
    
            fgColor.set(RUIParser.parseColor(definition.get("default", "foreground-color", "00000000")));
            bgColor.set(RUIParser.parseColor(definition.get("default", "background-color", "00000000")));
            
            visible = RUIParser.parseBool(definition.get("default", "visible", "true"));

            border.width = RUIParser.parseSize(definition.get("default", "border-width", "0px"));
            border.radius = RUIParser.parseSize(definition.get("default", "border-radius", "0px"));
            border.color.set(RUIParser.parseColor(definition.get("default", "border-color", "00000000")));
        }
    }
    
    public void update(double timeDelta)
    {
        synchronized (treeLock)
        {
            Vector2 position_t = new Vector2(position);
            if (position_t.x < 0) position_t.x /= -Window.viewportWidth;
            else if (parent != null) position_t.x *= parent.dimensions_c.x;
            if (position_t.y < 0) position_t.y /= -Window.viewportHeight;
            else if (parent != null) position_t.y *= parent.dimensions_c.y;
            if (position_c == null) position_c = position_t;
            else position_c.set(position_t);
            
            Vector2 dimensions_t = new Vector2(dimensions);
            if (dimensions_t.x < 0) dimensions_t.x /= Window.viewportWidth;
            else if (parent != null) dimensions_t.x *= parent.dimensions_c.x;
            if (dimensions_t.y < 0) dimensions_t.y /= Window.viewportHeight;
            else if (parent != null) dimensions_t.y *= parent.dimensions_c.y;
            if (dimensions_c == null) dimensions_c = dimensions_t;
            else dimensions_c.set(dimensions_t);
            
            if (fgColor_c == null) fgColor_c = new Color(fgColor);
            else fgColor_c.set(fgColor);
            if (bgColor_c == null) bgColor_c = new Color(bgColor);
            else bgColor_c.set(bgColor);
            
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
            
            if (visible)
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
