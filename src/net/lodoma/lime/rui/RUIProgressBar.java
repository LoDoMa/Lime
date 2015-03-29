package net.lodoma.lime.rui;

import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.util.Vector2;

public class RUIProgressBar extends RUIButton
{
    protected float progress_c;
    protected final Vector2 progressDimensions = new Vector2();
    protected RUIBorder progressBorder;
    
    public RUIProgressBar(RUIElement parent)
    {
        super(parent);
    }
    
    @Override
    protected void loadDefaultValues()
    {
        super.loadDefaultValues();
        
        values.set("default", "progress", RUIValue.SIZE_0);
        values.set("default", "progress-show", RUIValue.BOOLEAN_FALSE);
        
        progressBorder = new RUIBorder();
        progressBorder.loadDefaultValues(values, "progress-");
    }
    
    @Override
    public void loadData(RUIParserData data)
    {
        synchronized (treeLock)
        {
            super.loadData(data);

            data.copy("progress", RUIValueType.SIZE, values);
            data.copy("progress-show", RUIValueType.BOOLEAN, values);
            progressBorder.loadData(data, values, "progress-");
        }
    }
    
    @Override
    public void update(double timeDelta)
    {
        synchronized (treeLock)
        {
            super.update(timeDelta);

            progressBorder.update(timeDelta, this, "progress-");
            
            boolean show = values.get(state, "progress-show").toBoolean();
            progress_c = values.get(state, "progress").toSize();
            if (show)
                values.set("default", "text", new RUIValue((int) (progress_c * 100) + "%"));
            progress_c *= (progress_c < 0) ? (-1.0f / Window.viewportWidth) : dimensions_c.x;
            
            progressDimensions.set(progress_c, dimensions_c.y);
        }
    }
    
    @Override
    protected void renderBackground()
    {
        super.renderBackground();

        progressBorder.fillBackground(progressDimensions);
        progressBorder.fillGradient(progressDimensions);
        progressBorder.renderBorder(progressDimensions);
    }
}

