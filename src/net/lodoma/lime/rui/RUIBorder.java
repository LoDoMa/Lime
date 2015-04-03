package net.lodoma.lime.rui;

import org.lwjgl.opengl.GL11;

import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.resource.texture.Texture;
import net.lodoma.lime.util.Color;
import net.lodoma.lime.util.Vector2;

public class RUIBorder
{
    private static final int ARC_QUALITY = 16;

    protected final Color backgroundColor_c = new Color();
    protected final Color gradientColor_c = new Color();
    protected final Color borderColor_c = new Color();

    protected final Vector2 gradientSource_c = new Vector2();
    
    protected float borderWidth_c;
    protected final Vector2 borderRadiusTL_c = new Vector2();
    protected final Vector2 borderRadiusTR_c = new Vector2();
    protected final Vector2 borderRadiusBL_c = new Vector2();
    protected final Vector2 borderRadiusBR_c = new Vector2();
    
    private String oldState_m;
    private float stateTimeTotal_m;
    private float stateTime_m;
    private final Color deltaBackgroundColor_m = new Color();
    private final Color deltaGradientColor_m = new Color();
    private final Color deltaBorderColor_m = new Color();
    
    public void loadDefaultValues(RUIValueMap values, String prefix)
    {
        values.set("default", prefix + "background-color", RUIValue.COLOR_CLEAR);
        values.set("default", prefix + "gradient-color", RUIValue.COLOR_CLEAR);
        values.set("default", prefix + "gradient-source-x", new RUIValue(Float.NaN));
        values.set("default", prefix + "gradient-source-y", new RUIValue(Float.NaN));
        values.set("default", prefix + "border-color", RUIValue.COLOR_CLEAR);
        values.set("default", prefix + "border-width", RUIValue.SIZE_0);
        values.set("default", prefix + "border-radius-top-left", RUIValue.SIZE_0);
        values.set("default", prefix + "border-radius-top-right", RUIValue.SIZE_0);
        values.set("default", prefix + "border-radius-bottom-left", RUIValue.SIZE_0);
        values.set("default", prefix + "border-radius-bottom-right", RUIValue.SIZE_0);
    }
    
    public void loadData(RUIParserData data, RUIValueMap values, String prefix)
    {
        data.copy(prefix + "background-color", RUIValueType.COLOR, values);
        data.copy(prefix + "gradient-color", RUIValueType.COLOR, values);
        data.copy(prefix + "gradient-source-x", RUIValueType.SIZE, values);
        data.copy(prefix + "gradient-source-y", RUIValueType.SIZE, values);
        data.copy(prefix + "border-color", RUIValueType.COLOR, values);
        data.copy(prefix + "border-width", RUIValueType.SIZE, values);
        data.copy(prefix + "border-radius", prefix + "border-radius-top-left", RUIValueType.SIZE, values);
        data.copy(prefix + "border-radius", prefix + "border-radius-top-right", RUIValueType.SIZE, values);
        data.copy(prefix + "border-radius", prefix + "border-radius-bottom-left", RUIValueType.SIZE, values);
        data.copy(prefix + "border-radius", prefix + "border-radius-bottom-right", RUIValueType.SIZE, values);
        data.copy(prefix + "border-radius-top-left", RUIValueType.SIZE, values);
        data.copy(prefix + "border-radius-top-right", RUIValueType.SIZE, values);
        data.copy(prefix + "border-radius-bottom-left", RUIValueType.SIZE, values);
        data.copy(prefix + "border-radius-bottom-right", RUIValueType.SIZE, values);
    }
    
    public void update(double timeDelta, RUIElement element, String prefix)
    {
        Color backgroundColor_t = new Color(element.values.get(element.state, prefix + "background-color").toColor());
        Color gradientColor_t = new Color(element.values.get(element.state, prefix + "gradient-color").toColor());
        Color borderColor_t = new Color(element.values.get(element.state, prefix + "border-color").toColor());
        
        float gradientSourceX_t = element.values.get(element.state, prefix + "gradient-source-x").toSize();
        float gradientSourceY_t = element.values.get(element.state, prefix + "gradient-source-y").toSize();
        gradientSourceX_t *= (gradientSourceX_t < 0) ? (-1.0f / Window.viewportWidth) : element.dimensions_c.x;
        gradientSourceY_t *= (gradientSourceY_t < 0) ? (-1.0f / Window.viewportHeight) : element.dimensions_c.y;
        
        float borderWidth_t = element.values.get(element.state, prefix + "border-width").toSize();
        
        float borderRadiusTLx_t = element.values.get(element.state, prefix + "border-radius-top-left").toSize();
        float borderRadiusTRx_t = element.values.get(element.state, prefix + "border-radius-top-right").toSize();
        float borderRadiusBLx_t = element.values.get(element.state, prefix + "border-radius-bottom-left").toSize();
        float borderRadiusBRx_t = element.values.get(element.state, prefix + "border-radius-bottom-right").toSize();

        float borderRadiusTLy_t = borderRadiusTLx_t * ((borderRadiusTLx_t < 0) ? (-1.0f / Window.viewportHeight) : element.dimensions_c.y);
        float borderRadiusTRy_t = borderRadiusTRx_t * ((borderRadiusTRx_t < 0) ? (-1.0f / Window.viewportHeight) : element.dimensions_c.y);
        float borderRadiusBLy_t = borderRadiusBLx_t * ((borderRadiusBLx_t < 0) ? (-1.0f / Window.viewportHeight) : element.dimensions_c.y);
        float borderRadiusBRy_t = borderRadiusBRx_t * ((borderRadiusBRx_t < 0) ? (-1.0f / Window.viewportHeight) : element.dimensions_c.y);
        
        borderRadiusTLx_t *= (borderRadiusTLx_t < 0) ? (-1.0f / Window.viewportWidth) : element.dimensions_c.x;
        borderRadiusTRx_t *= (borderRadiusTRx_t < 0) ? (-1.0f / Window.viewportWidth) : element.dimensions_c.x;
        borderRadiusBLx_t *= (borderRadiusBLx_t < 0) ? (-1.0f / Window.viewportWidth) : element.dimensions_c.x;
        borderRadiusBRx_t *= (borderRadiusBRx_t < 0) ? (-1.0f / Window.viewportWidth) : element.dimensions_c.x;
        
        if (oldState_m != element.state)
        {
            if (oldState_m != null)
            {
                stateTimeTotal_m = element.values.get(element.state, "enter-state-time").toSize();
                if (stateTimeTotal_m < 0)
                    throw new IllegalStateException();
                stateTime_m = stateTimeTotal_m;
                
                deltaBackgroundColor_m.set(backgroundColor_t.r - backgroundColor_c.r,
                                           backgroundColor_t.g - backgroundColor_c.g,
                                           backgroundColor_t.b - backgroundColor_c.b,
                                           backgroundColor_t.a - backgroundColor_c.a);
                deltaGradientColor_m.set(gradientColor_t.r - gradientColor_c.r,
                                         gradientColor_t.g - gradientColor_c.g,
                                         gradientColor_t.b - gradientColor_c.b,
                                         gradientColor_t.a - gradientColor_c.a);
                deltaBorderColor_m.set(borderColor_t.r - borderColor_c.r,
                                       borderColor_t.g - borderColor_c.g,
                                       borderColor_t.b - borderColor_c.b,
                                       borderColor_t.a - borderColor_c.a);
            }
            oldState_m = element.state;
        }
        
        if (stateTime_m != 0)
        {
            stateTime_m -= timeDelta;

            if (stateTime_m < 0)
                stateTime_m = 0;
            else
            {
                float fract = 1.0f - stateTime_m / stateTimeTotal_m;
                backgroundColor_t.r = (backgroundColor_t.r - deltaBackgroundColor_m.r) + deltaBackgroundColor_m.r * fract;
                backgroundColor_t.g = (backgroundColor_t.g - deltaBackgroundColor_m.g) + deltaBackgroundColor_m.g * fract;
                backgroundColor_t.b = (backgroundColor_t.b - deltaBackgroundColor_m.b) + deltaBackgroundColor_m.b * fract;
                backgroundColor_t.a = (backgroundColor_t.a - deltaBackgroundColor_m.a) + deltaBackgroundColor_m.a * fract;
                gradientColor_t.r = (gradientColor_t.r - deltaGradientColor_m.r) + deltaGradientColor_m.r * fract;
                gradientColor_t.g = (gradientColor_t.g - deltaGradientColor_m.g) + deltaGradientColor_m.g * fract;
                gradientColor_t.b = (gradientColor_t.b - deltaGradientColor_m.b) + deltaGradientColor_m.b * fract;
                gradientColor_t.a = (gradientColor_t.a - deltaGradientColor_m.a) + deltaGradientColor_m.a * fract;
                borderColor_t.r = (borderColor_t.r - deltaBorderColor_m.r) + deltaBorderColor_m.r * fract;
                borderColor_t.g = (borderColor_t.g - deltaBorderColor_m.g) + deltaBorderColor_m.g * fract;
                borderColor_t.b = (borderColor_t.b - deltaBorderColor_m.b) + deltaBorderColor_m.b * fract;
                borderColor_t.a = (borderColor_t.a - deltaBorderColor_m.a) + deltaBorderColor_m.a * fract;
            }
        }
        
        backgroundColor_c.set(backgroundColor_t);
        gradientColor_c.set(gradientColor_t);
        borderColor_c.set(borderColor_t);
        
        gradientSource_c.set(gradientSourceX_t, gradientSourceY_t);
        
        borderWidth_c = borderWidth_t;
        borderRadiusTL_c.set(borderRadiusTLx_t, borderRadiusTLy_t);
        borderRadiusTR_c.set(borderRadiusTRx_t, borderRadiusTRy_t);
        borderRadiusBL_c.set(borderRadiusBLx_t, borderRadiusBLy_t);
        borderRadiusBR_c.set(borderRadiusBRx_t, borderRadiusBRy_t);
    }
    
    private float calcGradientAlpha(float x, float y, Vector2 dimensions)
    {
        float deltaX = Float.isNaN(gradientSource_c.x) ? Float.NaN : ((x - gradientSource_c.x) / dimensions.x);
        float deltaY = Float.isNaN(gradientSource_c.y) ? Float.NaN : ((y - gradientSource_c.y) / dimensions.y);
        
        float distance;
        if (Float.isNaN(deltaX) && Float.isNaN(deltaY))
            distance = Float.POSITIVE_INFINITY;
        else if (Float.isNaN(deltaX))
            distance = Math.abs(deltaY);
        else if (Float.isNaN(deltaY))
            distance = Math.abs(deltaX);
        else
            distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        
        if (distance > 1.0f)
            distance = 1.0f;
        
        return 1.0f - distance;
    }
    
    private void renderArc(Vector2 radius, Vector2 dimensions, boolean vara, boolean varx,
                           boolean vary, boolean dver, boolean efrs, boolean gradient)
    {
        for (int i = 0; i <= ARC_QUALITY; i++)
        {
            float angle;
            if (vara) angle = (float) Math.toRadians(90.0f - i * 90.0 / (float) ARC_QUALITY);
            else angle = (float) Math.toRadians(i * 90.0 / (float) ARC_QUALITY);
            
            float x;
            if (varx) x = radius.x + (float) Math.cos(angle) * -radius.x;
            else x = dimensions.x - radius.x + (float) Math.cos(angle) * radius.x;
            
            float y;
            if (vary) y = radius.y + (float) Math.sin(angle) * -radius.y;
            else y = dimensions.y - radius.y + (float) Math.sin(angle) * radius.y;
            
            if (x < 0) x = 0;
            if (y < 0) y = 0;
            if (x > dimensions.x) x = dimensions.x;
            if (y > dimensions.y) y = dimensions.y;
            
            if (gradient)
                gradientColor_c.setGL(calcGradientAlpha(x, y, dimensions));
            
            GL11.glVertex2f(x, y);
            if (dver && (!efrs || (efrs && i > 0)))
                GL11.glVertex2f(x, y);
        }
    }
    
    public void renderBorder(Vector2 dimensions)
    {
        Texture.NO_TEXTURE.bind(0);
        borderColor_c.setGL();
        
        GL11.glLineWidth(borderWidth_c);
        GL11.glBegin(GL11.GL_LINES);
        renderArc(borderRadiusBL_c, dimensions, true, true, true, true, true, false);
        renderArc(borderRadiusTL_c, dimensions, false, true, false, true, false, false);
        renderArc(borderRadiusTR_c, dimensions, true, false, false, true, false, false);
        renderArc(borderRadiusBR_c, dimensions, false, false, true, true, false, false);
        GL11.glVertex2f(borderRadiusBL_c.x, 0.0f);
        GL11.glEnd();
    }
    
    public void fillBackground(Vector2 dimensions)
    {
        Texture.NO_TEXTURE.bind(0);
        backgroundColor_c.setGL();
        
        GL11.glBegin(GL11.GL_POLYGON);
        renderArc(borderRadiusBL_c, dimensions, true, true, true, false, false, false);
        renderArc(borderRadiusTL_c, dimensions, false, true, false, false, false, false);
        renderArc(borderRadiusTR_c, dimensions, true, false, false, false, false, false);
        renderArc(borderRadiusBR_c, dimensions, false, false, true, false, false, false);
        GL11.glEnd();
    }
    
    public void fillGradient(Vector2 dimensions)
    {
        Texture.NO_TEXTURE.bind(0);
        
        GL11.glBegin(GL11.GL_POLYGON);
        renderArc(borderRadiusBL_c, dimensions, true, true, true, false, false, true);
        renderArc(borderRadiusTL_c, dimensions, false, true, false, false, false, true);
        renderArc(borderRadiusTR_c, dimensions, true, false, false, false, false, true);
        renderArc(borderRadiusBR_c, dimensions, false, false, true, false, false, true);
        GL11.glEnd();
    }
}
