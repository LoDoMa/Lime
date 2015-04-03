package net.lodoma.lime.resource.texture;

import java.io.FileInputStream;
import java.io.IOException;

import net.lodoma.lime.Lime;
import net.lodoma.lime.resource.Resource;
import net.lodoma.lime.util.OsHelper;

public class TextureResource extends Resource
{
    public Texture texture;
    
    @Override
    public void update(double timeDelta)
    {
        
    }
    
    @Override
    public void create()
    {
        try
        {
            texture = new Texture(new FileInputStream(OsHelper.JARPATH + "res/textures/" + name + ".png"));
        }
        catch(IOException e)
        {
            Lime.LOGGER.C("Failed to load texture " + name);
            Lime.LOGGER.log(e);
            Lime.forceExit(e);
        }
    }
    
    @Override
    public void destroy()
    {
        texture.delete();
    }
    
    @Override
    public Object get()
    {
        return texture;
    }
}
