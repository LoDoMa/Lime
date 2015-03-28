package net.lodoma.lime.rui;

import org.lwjgl.opengl.GL11;

import net.lodoma.lime.client.window.Window;
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

    public float gradientSourceX_c;
    public float gradientSourceY_c;
    
    public void loadDefaultValues(RUIValueMap values)
    {
        values.set("default", "background-color", RUIValue.COLOR_CLEAR);
        values.set("default", "gradient-color", RUIValue.COLOR_CLEAR);
        values.set("default", "gradient-source", new RUIValue("top"));
        values.set("default", "border-color", RUIValue.COLOR_CLEAR);
        values.set("default", "border-width", RUIValue.SIZE_0);
        values.set("default", "border-radius-top-left", RUIValue.SIZE_0);
        values.set("default", "border-radius-top-right", RUIValue.SIZE_0);
        values.set("default", "border-radius-bottom-left", RUIValue.SIZE_0);
        values.set("default", "border-radius-bottom-right", RUIValue.SIZE_0);
    }
    
    public void loadData(RUIParserData data, RUIValueMap values)
    {
        data.copy("background-color", RUIValueType.COLOR, values);
        data.copy("gradient-color", RUIValueType.COLOR, values);
        data.copy("gradient-source", RUIValueType.STRING, values);
        data.copy("border-color", RUIValueType.COLOR, values);
        data.copy("border-width", RUIValueType.SIZE, values);
        data.copy("border-radius", "border-radius-top-left", RUIValueType.SIZE, values);
        data.copy("border-radius", "border-radius-top-right", RUIValueType.SIZE, values);
        data.copy("border-radius", "border-radius-bottom-left", RUIValueType.SIZE, values);
        data.copy("border-radius", "border-radius-bottom-right", RUIValueType.SIZE, values);
        data.copy("border-radius-top-left", RUIValueType.SIZE, values);
        data.copy("border-radius-top-right", RUIValueType.SIZE, values);
        data.copy("border-radius-bottom-left", RUIValueType.SIZE, values);
        data.copy("border-radius-bottom-right", RUIValueType.SIZE, values);
    }
    
    public void update(double timeDelta, RUIElement element)
    {
        backgroundColor_c.set(element.values.get(element.state, "background-color").toColor());
        gradientColor_c.set(element.values.get(element.state, "gradient-color").toColor());
        
        String gradientSource_t = element.values.get(element.state, "gradient-source").toString();
        switch (gradientSource_t)
        {
        case "left": gradientSourceX_c = element.dimensions_c.x * 0.0f; gradientSourceY_c = Float.NEGATIVE_INFINITY; break;
        case "right": gradientSourceX_c = element.dimensions_c.x * 1.0f; gradientSourceY_c = Float.NEGATIVE_INFINITY; break;
        case "top": gradientSourceX_c = Float.NEGATIVE_INFINITY; gradientSourceY_c = element.dimensions_c.y * 1.0f; break;
        case "bottom": gradientSourceX_c = Float.NEGATIVE_INFINITY; gradientSourceY_c = element.dimensions_c.y * 0.0f; break;
        case "top-left": gradientSourceX_c = element.dimensions_c.x * 0.0f; gradientSourceY_c = element.dimensions_c.y * 1.0f; break;
        case "top-right": gradientSourceX_c = element.dimensions_c.x * 1.0f; gradientSourceY_c = element.dimensions_c.y * 1.0f; break;
        case "bottom-left": gradientSourceX_c = element.dimensions_c.x * 0.0f; gradientSourceY_c = element.dimensions_c.y * 0.0f; break;
        case "bottom-right": gradientSourceX_c = element.dimensions_c.x * 1.0f; gradientSourceY_c = element.dimensions_c.y * 0.0f; break;
        default: throw new IllegalStateException();
        }
        
        borderColor_c.set(element.values.get(element.state, "border-color").toColor());
        width = element.values.get(element.state, "border-width").toSize();
        
        float borderRadiusTLx_c = element.values.get(element.state, "border-radius-top-left").toSize();
        float borderRadiusTRx_c = element.values.get(element.state, "border-radius-top-right").toSize();
        float borderRadiusBLx_c = element.values.get(element.state, "border-radius-bottom-left").toSize();
        float borderRadiusBRx_c = element.values.get(element.state, "border-radius-bottom-right").toSize();

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
        float distx = gradientSourceX_c == Float.NEGATIVE_INFINITY ? Float.NEGATIVE_INFINITY :
                                                                     Math.abs(gradientSourceX_c - x) / dimensions.x;
        float disty = gradientSourceY_c == Float.NEGATIVE_INFINITY ? Float.NEGATIVE_INFINITY :
                                                                     Math.abs(gradientSourceY_c - y) / dimensions.y;
        return Math.max(distx, disty);
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
            
            if (gradient)
                gradientColor_c.setGL(1.0f - calcGradientAlpha(x, y, dimensions));
            
            GL11.glVertex2f(x, y);
            if (dver && (!efrs || (efrs && i > 0)))
                GL11.glVertex2f(x, y);
        }
    }
    
    public void renderBorder(RUIElement element)
    {
        borderColor_c.setGL();
        
        GL11.glLineWidth(width);
        GL11.glBegin(GL11.GL_LINES);
        renderArc(radiusBL, element.dimensions_c, true, true, true, true, true, false);
        renderArc(radiusTL, element.dimensions_c, false, true, false, true, false, false);
        renderArc(radiusTR, element.dimensions_c, true, false, false, true, false, false);
        renderArc(radiusBR, element.dimensions_c, false, false, true, true, false, false);
        GL11.glVertex2f(radiusBL.x, 0.0f);
        GL11.glEnd();
    }
    
    public void fillBackground(RUIElement element)
    {
        backgroundColor_c.setGL();
        
        GL11.glBegin(GL11.GL_POLYGON);
        renderArc(radiusBL, element.dimensions_c, true, true, true, false, false, false);
        renderArc(radiusTL, element.dimensions_c, false, true, false, false, false, false);
        renderArc(radiusTR, element.dimensions_c, true, false, false, false, false, false);
        renderArc(radiusBR, element.dimensions_c, false, false, true, false, false, false);
        GL11.glEnd();
        
        GL11.glBegin(GL11.GL_POLYGON);
        renderArc(radiusBL, element.dimensions_c, true, true, true, false, false, true);
        renderArc(radiusTL, element.dimensions_c, false, true, false, false, false, true);
        renderArc(radiusTR, element.dimensions_c, true, false, false, false, false, true);
        renderArc(radiusBR, element.dimensions_c, false, false, true, false, false, true);
        GL11.glEnd();
    }
}
