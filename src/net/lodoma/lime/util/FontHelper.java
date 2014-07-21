package net.lodoma.lime.util;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

public class FontHelper
{
    public static String registerFont(File fontFile)
    {
        try
        {
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            return font.getName();
        }
        catch(FontFormatException | IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
