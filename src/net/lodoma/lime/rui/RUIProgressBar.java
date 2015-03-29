package net.lodoma.lime.rui;

import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.util.Vector2;

public class RUIProgressBar extends RUIButton
{
    protected float progress_c;
    protected final Vector2 progressDimensions = new Vector2();
    protected RUIBorder progressBorder;
    
    private String oldState_m;
    private float stateTimeTotal_m;
    private float stateTime_m;
    private float oldProgress_m = Float.NaN;
    private float deltaProgressDimensionsX_m;
    
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
            float progress_t = values.get(state, "progress").toSize();
            if (show)
                values.set("default", "text", new RUIValue((int) (progress_t * 100) + "%"));
            progress_t *= (progress_t < 0) ? (-1.0f / Window.viewportWidth) : dimensions_c.x;
            progress_c = progress_t;
            
            if (!Float.isNaN(oldProgress_m) && oldProgress_m != progress_c)
            {
                if (oldState_m != null)
                {
                    stateTimeTotal_m = values.get(state, "enter-state-time").toSize();
                    if (stateTimeTotal_m < 0)
                        throw new IllegalStateException();
                    stateTime_m = stateTimeTotal_m;

                    deltaProgressDimensionsX_m = progress_c - oldProgress_m;
                }
                oldState_m = state;
            }
            
            oldProgress_m = progress_c;
            
            if (stateTime_m != 0)
            {
                stateTime_m -= timeDelta;

                if (stateTime_m < 0)
                    stateTime_m = 0;
                else
                {
                    float fract = 1.0f - stateTime_m / stateTimeTotal_m;
                    progress_t = (progress_t - deltaProgressDimensionsX_m) + deltaProgressDimensionsX_m * fract;
                }
            }
            
            progressDimensions.set(progress_t, dimensions_c.y);
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

