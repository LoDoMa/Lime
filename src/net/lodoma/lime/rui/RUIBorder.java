package net.lodoma.lime.rui;

import org.lwjgl.opengl.GL11;

import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.texture.Texture;
import net.lodoma.lime.util.Color;
import net.lodoma.lime.util.Vector2;

public class RUIBorder
{
    private static final int ARC_QUALITY = 16;
    
    public float width;
    public final Vector2 radiusTL = new Vector2();
    public final Vector2 radiusTR = new Vector2();
    public final Vector2 radiusBL = new Vector2();
    public final Vector2 radiusBR = new Vector2();

    public final Color backgroundColor_c = new Color();
    public final Color gradientColor_c = new Color();
    public final Color borderColor_c = new Color();

    public final Vector2 gradientSource_c = new Vector2();
    
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
        backgroundColor_c.set(element.values.get(element.state, prefix + "background-color").toColor());
        gradientColor_c.set(element.values.get(element.state, prefix + "gradient-color").toColor());
        
        float gradientSourceX_t = element.values.get(element.state, prefix + "gradient-source-x").toSize();
        float gradientSourceY_t = element.values.get(element.state, prefix + "gradient-source-y").toSize();
        gradientSourceX_t *= (gradientSourceX_t < 0) ? (-1.0f / Window.viewportWidth) : element.dimensions_c.x;
        gradientSourceY_t *= (gradientSourceY_t < 0) ? (-1.0f / Window.viewportHeight) : element.dimensions_c.y;
        gradientSource_c.set(gradientSourceX_t, gradientSourceY_t);
        
        borderColor_c.set(element.values.get(element.state, prefix + "border-color").toColor());
        width = element.values.get(element.state, prefix + "border-width").toSize();
        
        float borderRadiusTLx_c = element.values.get(element.state, prefix + "border-radius-top-left").toSize();
        float borderRadiusTRx_c = element.values.get(element.state, prefix + "border-radius-top-right").toSize();
        float borderRadiusBLx_c = element.values.get(element.state, prefix + "border-radius-bottom-left").toSize();
        float borderRadiusBRx_c = element.values.get(element.state, prefix + "border-radius-bottom-right").toSize();

        float borderRadiusTLy_c = borderRadiusTLx_c * ((borderRadiusTLx_c < 0) ? (-1.0f / Window.viewportHeight) : element.dimensions_c.y);
        float borderRadiusTRy_c = borderRadiusTRx_c * ((borderRadiusTRx_c < 0) ? (-1.0f / Window.viewportHeight) : element.dimensions_c.y);
        float borderRadiusBLy_c = borderRadiusBLx_c * ((borderRadiusBLx_c < 0) ? (-1.0f / Window.viewportHeight) : element.dimensions_c.y);
        float borderRadiusBRy_c = borderRadiusBRx_c * ((borderRadiusBRx_c < 0) ? (-1.0f / Window.viewportHeight) : element.dimensions_c.y);
        
        borderRadiusTLx_c *= (borderRadiusTLx_c < 0) ? (-1.0f / Window.viewportWidth) : element.dimensions_c.x;
        borderRadiusTRx_c *= (borderRadiusTRx_c < 0) ? (-1.0f / Window.viewportWidth) : element.dimensions_c.x;
        borderRadiusBLx_c *= (borderRadiusBLx_c < 0) ? (-1.0f / Window.viewportWidth) : element.dimensions_c.x;
        borderRadiusBRx_c *= (borderRadiusBRx_c < 0) ? (-1.0f / Window.viewportWidth) : element.dimensions_c.x;
        
        radiusTL.set(borderRadiusTLx_c, borderRadiusTLy_c);
        radiusTR.set(borderRadiusTRx_c, borderRadiusTRy_c);
        radiusBL.set(borderRadiusBLx_c, borderRadiusBLy_c);
        radiusBR.set(borderRadiusBRx_c, borderRadiusBRy_c);
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
        
        GL11.glLineWidth(width);
        GL11.glBegin(GL11.GL_LINES);
        renderArc(radiusBL, dimensions, true, true, true, true, true, false);
        renderArc(radiusTL, dimensions, false, true, false, true, false, false);
        renderArc(radiusTR, dimensions, true, false, false, true, false, false);
        renderArc(radiusBR, dimensions, false, false, true, true, false, false);
        GL11.glVertex2f(radiusBL.x, 0.0f);
        GL11.glEnd();
    }
    
    public void fillBackground(Vector2 dimensions)
    {
        Texture.NO_TEXTURE.bind(0);
        backgroundColor_c.setGL();
        
        GL11.glBegin(GL11.GL_POLYGON);
        renderArc(radiusBL, dimensions, true, true, true, false, false, false);
        renderArc(radiusTL, dimensions, false, true, false, false, false, false);
        renderArc(radiusTR, dimensions, true, false, false, false, false, false);
        renderArc(radiusBR, dimensions, false, false, true, false, false, false);
        GL11.glEnd();
    }
    
    public void fillGradient(Vector2 dimensions)
    {
        Texture.NO_TEXTURE.bind(0);
        
        GL11.glBegin(GL11.GL_POLYGON);
        renderArc(radiusBL, dimensions, true, true, true, false, false, true);
        renderArc(radiusTL, dimensions, false, true, false, false, false, true);
        renderArc(radiusTR, dimensions, true, false, false, false, false, true);
        renderArc(radiusBR, dimensions, false, false, true, false, false, true);
        GL11.glEnd();
    }
}
