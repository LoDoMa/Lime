package net.lodoma.lime.gui.editor;

import net.lodoma.lime.gui.UICallback;
import net.lodoma.lime.gui.UIObject;
import net.lodoma.lime.gui.clean.CleanText;
import net.lodoma.lime.texture.Texture;
import net.lodoma.lime.util.Color;
import net.lodoma.lime.util.TrueTypeFont;
import net.lodoma.lime.util.Vector2;
import static org.lwjgl.opengl.GL11.*;

public class EditorWindow extends UIObject
{
    public EditorPanel panel;
    public EditorContentPane contentPane;
    
    private Vector2 minSize;
    private EditorWindowHandle moveHandle;
    private EditorWindowHandle scaleHandle;
    private boolean shade;
    
    public EditorWindow(Vector2 position, Vector2 dimensions, EditorWindowPopulator populator)
    {
        super();
        getLocalPosition().set(position);
        getLocalDimensions().set(dimensions.x, dimensions.y + 0.03f);

        this.minSize = dimensions.div(2.0f);
        
        panel = new EditorPanel(new Vector2(0.0f, dimensions.y), new Vector2(dimensions.x, 0.03f));
        contentPane = new EditorContentPane(new Vector2(0), dimensions);
        moveHandle = new EditorWindowHandle(new Vector2(0.0f, dimensions.y), new Vector2(dimensions.x, 0.03f), false, () -> {
            getLocalPosition().addLocal(moveHandle.currentPress.sub(moveHandle.lastPress));
        });
        scaleHandle = new EditorWindowHandle(new Vector2(dimensions.x - 0.015f * (9.0f / 16.0f), 0.0f), new Vector2(0.015f * (9.0f / 16.0f), 0.015f), true, new UICallback()
        {
            @Override
            public void call()
            {
                Vector2 diff = scaleHandle.currentPress.sub(scaleHandle.lastPress);
                Vector2 diffabs = diff.abs();
                
                Vector2 position = getLocalPosition();
                Vector2 dimensions = getLocalDimensions();

                if (diffabs.x > diffabs.y)
                {
                    float addy = (dimensions.y / dimensions.x) * diff.x;
                    position.y -= addy;
                    dimensions.x += diff.x;
                    dimensions.y += addy;
                }
                else
                {
                    float addx = (dimensions.x / dimensions.y) * -diff.y;
                    position.y += diff.y;
                    dimensions.x += addx;
                    dimensions.y -= diff.y;
                }
            }
        });
        
        final EditorWindow thisWindow = this;
        panel.addChild(new EditorIcon(new Vector2(), new Vector2(9.0f / 16.0f, 1.0f), "editor/window_close", () -> {
            UIObject parent = getParent();
            if (parent != null)
                parent.removeChild(thisWindow);
        }));
        panel.addChild(new EditorIcon(new Vector2(), new Vector2(9.0f / 16.0f, 1.0f), "editor/window_shade", () -> shade = !shade));
        
        panel.addChild(new CleanText(new Vector2(), 0.025f, populator.getTitle(), new Color(1.0f, 1.0f, 1.0f, 1.0f), TrueTypeFont.ALIGN_LEFT));
        
        addChild(panel);
        addChild(contentPane);
        addChild(moveHandle);
        addChild(scaleHandle);
        
        populator.apply(this);
    }
    
    @Override
    public void update(double timeDelta)
    {
        // We update children manually.
        // super.update(timeDelta);
        if (!shade)
        {
            contentPane.update(timeDelta);
            scaleHandle.update(timeDelta);
        }
        moveHandle.update(timeDelta);
        panel.update(timeDelta);
        
        Vector2 position = getLocalPosition();
        Vector2 dimensions = getLocalDimensions();
        
        if (dimensions.x < minSize.x || dimensions.x > 1.0f)
        {
            float ratio = dimensions.y / dimensions.x;
            dimensions.x = (dimensions.x < minSize.x) ? minSize.x : 1.0f;
            dimensions.y = dimensions.x * ratio;
        }
        if (dimensions.y < minSize.y || dimensions.y > 1.0f)
        {
            float ratio = dimensions.x / dimensions.y;
            dimensions.y = (dimensions.y < minSize.y) ? minSize.y : 1.0f;
            dimensions.x = dimensions.y * ratio;
        }
        
        if (position.x < 0.0f) position.x = 0.0f;
        if (position.x + dimensions.x > 1.0f) position.x = 1.0f - dimensions.x;
        if (shade && (position.y + dimensions.y - 0.03f) < 0.0f) position.y = -dimensions.y + 0.03f;
        if (!shade && position.y < 0.0f) position.y = 0.0f;
        if ((position.y + dimensions.y) > 1.0f) position.y = 1.0f - dimensions.y;
        
        panel.getLocalPosition().set(0.0f, dimensions.y - 0.03f);
        panel.getLocalDimensions().set(dimensions.x, 0.03f);
        
        contentPane.getLocalDimensions().set(dimensions.subY(0.03f));

        moveHandle.getLocalPosition().set(0.0f, dimensions.y - 0.03f);
        moveHandle.getLocalDimensions().set(dimensions.x, 0.03f);
        
        scaleHandle.getLocalPosition().set(dimensions.x - 0.015f * (9.0f / 16.0f), 0.0f);
        scaleHandle.getLocalDimensions().set(0.015f * (9.0f / 16.0f), 0.015f);
    }
    
    @Override
    public void render()
    {
        // We render children manually.
        // super.render();
        if (!shade)
        {
            contentPane.render();
            scaleHandle.render();
        }
        panel.render();
        
        glPushMatrix();
        
        Vector2 position = getPosition();
        glTranslatef(position.x, position.y, 0.0f);
        
        Vector2 dimensions = getDimensions();
        
        Texture.NO_TEXTURE.bind(0);
        EditorUI.BACKGROUND_FOCUS.setGL();
        
        if (shade)
        {
            glBegin(GL_LINES);
            {
                glVertex2f(0.0f, dimensions.y - 0.03f);
                glVertex2f(dimensions.x, dimensions.y - 0.03f);
                glVertex2f(dimensions.x, dimensions.y - 0.03f);
                glVertex2f(dimensions.x, dimensions.y);
                glVertex2f(dimensions.x, dimensions.y);
                glVertex2f(0.0f, dimensions.y);
                glVertex2f(0.0f, dimensions.y);
                glVertex2f(0.0f, dimensions.y - 0.03f);
            }
            glEnd();
        }
        else
        {
            glBegin(GL_LINES);
            {
                glVertex2f(0.0f, 0.0f);
                glVertex2f(dimensions.x, 0.0f);
                glVertex2f(dimensions.x, 0.0f);
                glVertex2f(dimensions.x, dimensions.y);
                glVertex2f(dimensions.x, dimensions.y);
                glVertex2f(0.0f, dimensions.y);
                glVertex2f(0.0f, dimensions.y);
                glVertex2f(0.0f, 0.0f);
            }
            glEnd();
        }
        
        glPopMatrix();
    }
}
