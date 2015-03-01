package net.lodoma.lime.texture;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.lodoma.lime.Lime;
import net.lodoma.lime.util.OsHelper;

public class TexturePool
{
    private static Map<String, Texture> mappedTextures = new HashMap<String, Texture>();
    
    public static void add(String name)
    {
        try
        {
            mappedTextures.put(name, new Texture(new FileInputStream(OsHelper.JARPATH + "res/" + name + ".png")));
            Lime.LOGGER.I("Created texture " + name);
        }
        catch(IOException e)
        {
            Lime.LOGGER.C("Failed to load texture " + name);
            Lime.LOGGER.log(e);
            Lime.forceExit(e);
        }
    }
    
    public static Texture get(String name)
    {
        return mappedTextures.get(name);
    }
    
    public static void clear()
    {
        mappedTextures.forEach((String name, Texture texture) -> {
            texture.delete();
            Lime.LOGGER.I("Deleted texture " + name);
        });
    }
}
