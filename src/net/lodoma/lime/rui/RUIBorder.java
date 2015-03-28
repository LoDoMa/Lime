package net.lodoma.lime.rui;

import org.lwjgl.opengl.GL11;

import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.util.Color;
import net.lodoma.lime.util.Vector2;

public class RUIBorder
{
    public float width;
    public final Vector2 radiusTL = new Vector2();
    public final Vector2 radiusTR = new Vector2();
    public final Vector2 radiusBL = new Vector2();
    public final Vector2 radiusBR = new Vector2();
    
    public final Color color = new Color();
    
    public void loadDefaultValues(RUIValueMap values)
    {
        values.set("default", "border-width", RUIValue.SIZE_0);
        values.set("default", "border-radius-top-left", RUIValue.SIZE_0);
        values.set("default", "border-radius-top-right", RUIValue.SIZE_0);
        values.set("default", "border-radius-bottom-left", RUIValue.SIZE_0);
        values.set("default", "border-radius-bottom-right", RUIValue.SIZE_0);
        values.set("default", "border-color", RUIValue.COLOR_CLEAR);
    }
    
    public void loadData(RUIParserData data, RUIValueMap values)
    {
        data.copy("border-width", RUIValueType.SIZE, values);
        data.copy("border-radius", "border-radius-top-left", RUIValueType.SIZE, values);
        data.copy("border-radius", "border-radius-top-right", RUIValueType.SIZE, values);
        data.copy("border-radius", "border-radius-bottom-left", RUIValueType.SIZE, values);
        data.copy("border-radius", "border-radius-bottom-right", RUIValueType.SIZE, values);
        data.copy("border-radius-top-left", RUIValueType.SIZE, values);
        data.copy("border-radius-top-right", RUIValueType.SIZE, values);
        data.copy("border-radius-bottom-left", RUIValueType.SIZE, values);
        data.copy("border-radius-bottom-right", RUIValueType.SIZE, values);
        data.copy("border-color", RUIValueType.COLOR, values);
    }
    
    public void update(double timeDelta, RUIElement element)
    {
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
        
        width = element.values.get(element.state, "border-width").toSize();
        color.set(element.values.get(element.state, "border-color").toColor());
    }
    
    public void renderBorder(RUIElement element)
    {
        GL11.glLineWidth(width);
        GL11.glBegin(GL11.GL_LINES);
        
        color.setGL();
        
        float firstx, firsty;
        firstx = firsty = 0;
        
        for (int i = 0; i <= 10; i++)
        {
            float angle = (float) Math.toRadians(90 - i * 90.0 / 10.0);
            float x = radiusBL.x + (float) Math.cos(angle) * -radiusBL.x;
            float y = radiusBL.y + (float) Math.sin(angle) * -radiusBL.y;
            if (i == 0)
            {
                firstx = x;
                firsty = y;
            }
            else
                GL11.glVertex2f(x, y);
            GL11.glVertex2f(x, y);
        }

        for (int i = 0; i <= 10; i++)
        {
            float angle = (float) Math.toRadians(i * 90.0 / 10.0);
            float x = radiusTL.x + (float) Math.cos(angle) * -radiusTL.x;
            float y = element.dimensions_c.y - radiusTL.y + (float) Math.sin(angle) * radiusTL.y;
            GL11.glVertex2f(x, y);
            GL11.glVertex2f(x, y);
        }

        for (int i = 0; i <= 10; i++)
        {
            float angle = (float) Math.toRadians(90 - i * 90.0 / 10.0);
            float x = element.dimensions_c.x - radiusTR.x + (float) Math.cos(angle) * radiusTR.x;
            float y = element.dimensions_c.y - radiusTR.y + (float) Math.sin(angle) * radiusTR.y;
            GL11.glVertex2f(x, y);
            GL11.glVertex2f(x, y);
        }

        for (int i = 0; i <= 10; i++)
        {
            float angle = (float) Math.toRadians(i * 90.0 / 10.0);
            float x = element.dimensions_c.x - radiusBR.x + (float) Math.cos(angle) * radiusBR.x;
            float y = radiusBR.y + (float) Math.sin(angle) * -radiusBR.y;
            GL11.glVertex2f(x, y);
            GL11.glVertex2f(x, y);
        }

        GL11.glVertex2f(firstx, firsty);
        
        GL11.glEnd();
    }
    
    public void fillBackground(RUIElement element)
    {
        GL11.glBegin(GL11.GL_POLYGON);
        
        for (int i = 0; i <= 10; i++)
        {
            float angle = (float) Math.toRadians(90 - i * 90.0 / 10.0);
            float x = radiusBL.x + (float) Math.cos(angle) * -radiusBL.x;
            float y = radiusBL.y + (float) Math.sin(angle) * -radiusBL.y;
            GL11.glVertex2f(x, y);
        }

        for (int i = 0; i <= 10; i++)
        {
            float angle = (float) Math.toRadians(i * 90.0 / 10.0);
            float x = radiusTL.x + (float) Math.cos(angle) * -radiusTL.x;
            float y = element.dimensions_c.y - radiusTL.y + (float) Math.sin(angle) * radiusTL.y;
            GL11.glVertex2f(x, y);
        }

        for (int i = 0; i <= 10; i++)
        {
            float angle = (float) Math.toRadians(90 - i * 90.0 / 10.0);
            float x = element.dimensions_c.x - radiusTR.x + (float) Math.cos(angle) * radiusTR.x;
            float y = element.dimensions_c.y - radiusTR.y + (float) Math.sin(angle) * radiusTR.y;
            GL11.glVertex2f(x, y);
        }

        for (int i = 0; i <= 10; i++)
        {
            float angle = (float) Math.toRadians(i * 90.0 / 10.0);
            float x = element.dimensions_c.x - radiusBR.x + (float) Math.cos(angle) * radiusBR.x;
            float y = radiusBR.y + (float) Math.sin(angle) * -radiusBR.y;
            GL11.glVertex2f(x, y);
        }
        
        GL11.glEnd();
    }
}
