package net.lodoma.lime.util;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

import net.lodoma.lime.Lime;

public class FontHelper
{
    public static String registerFont(File fontFile)
    {
        try
        {
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            String name = font.getName();
            Lime.LOGGER.F("Registered font \"" + name + "\"");
            return name;
        }
        catch(FontFormatException | IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
